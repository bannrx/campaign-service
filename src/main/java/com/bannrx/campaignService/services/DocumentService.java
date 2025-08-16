package com.bannrx.campaignService.services;

import com.bannrx.campaignService.dto.DeleteDocumentResponse;
import com.bannrx.campaignService.repository.specifications.DocumentSpecification;
import com.bannrx.campaignService.searchCriteria.DocumentSearchCriteria;
import com.bannrx.common.dtos.DocumentDto;
import com.bannrx.campaignService.dto.configuration.AwsProperties;
import com.bannrx.common.dtos.requests.DocumentUploadRequest;
import com.bannrx.common.dtos.responses.PageableResponse;
import com.bannrx.common.mappers.DocumentMapper;
import com.bannrx.common.persistence.entities.Document;
import com.bannrx.common.persistence.entities.UserProfile;
import com.bannrx.common.repository.DocumentRepository;
import com.bannrx.common.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rklab.utility.annotations.Loggable;
import rklab.utility.dto.AwsPreSignedUrlRequest;
import rklab.utility.dto.AwsRequest;
import rklab.utility.dto.AwsUploadRequest;
import rklab.utility.expectations.InvalidInputException;
import rklab.utility.expectations.ServerException;
import rklab.utility.services.AwsService;
import rklab.utility.utilities.PageableUtils;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static com.bannrx.common.constants.SystemConfigKeys.TIME_TO_LIVE_PRESIGNED_URL_SECONDS;
import static com.bannrx.common.enums.DocumentStatus.DELETED;
import static com.bannrx.common.enums.DocumentStatus.UPLOADED;
import static rklab.utility.constants.GlobalConstants.Symbols.BACK_SLASH;

@Service
@Loggable
@RequiredArgsConstructor
public class DocumentService {

    private final AwsProperties properties;
    private final AwsService awsService;
    private final DocumentRepository repository;
    private final SystemConfigService systemConfigService;

    /**
     * File path pattern: user-id/type/fileName.extension
     */
    private static final String FILE_PATH_PATTERN="%s/%s/%s";

    /**
     * URL: endpoint/key
     */
    private static final String ENDPOINT_KEY ="%s/%s";

    /**
     * Upload document
     *
     * @param request DocumentUploadRequest
     * @param profile UserProfile
     * @return DocumentDto
     * @throws IOException Exception while getting input stream
     * @throws ServerException Exception while uploading to the object db
     */
    public DocumentDto create(DocumentUploadRequest request, UserProfile profile)
            throws IOException, ServerException {
        var loggedInUser = profile.getUser();
        var key = getKey(
                loggedInUser.getId(),
                request.getType().name().toLowerCase(),
                request.getFile().getOriginalFilename()
        );
        var documentDto = DocumentDto.of(request);
        documentDto.setUrl(getUrl(key));
        var eTag = upload(request.getFile(), key, documentDto);
        documentDto.setStatus(UPLOADED);
        var document = DocumentMapper.INSTANCE.toEntity(documentDto, eTag, properties.getBucket());
        profile.appendDocument(document);
        document = repository.save(document);
        return DocumentMapper.INSTANCE.toDto(document);
    }


    /**
     * Utility Method
     */

    private String upload(
            MultipartFile file,
            String key,
            DocumentDto documentDto
    ) throws IOException, ServerException {
        var uploadRequest = AwsUploadRequest.awsUploadRequestBuilder()
                .config(properties)
                .key(key)
                .content(file.getBytes())
                .contentType(documentDto.getMimeType())
                .build();
        return awsService.upload(uploadRequest);
    }

    private String getUrl(String key){
        return String.format(ENDPOINT_KEY, properties.getEndpoint(), key);
    }

    private String getKey(
            final String userId,
            final String type,
            final String fileName
    ){
        return String.format(
                FILE_PATH_PATTERN,
                userId,
                type,
                fileName
        );
    }

    private String getKey(final String url)
            throws InvalidInputException, ServerException {
        if (Objects.nonNull(url)){
            var parts = url.split(BACK_SLASH);
            var len = parts.length;
            if (3 < len){
                return Arrays.stream(parts, 3, len)
                        .collect(Collectors.joining(BACK_SLASH));
            }
            throw new ServerException("Key not available in url");
        }
        throw new InvalidInputException("Url not found");
    }

    public String fetchPreSignedUrl(final String documentId)
            throws InvalidInputException, ServerException {
        var document = fetchById(documentId);
        var request = AwsPreSignedUrlRequest.awsPreSignedUrlRequestBuilder()
                .key(getKey(document.getUrl()))
                .config(properties)
                .duration(getDefaultDuration())
                .build();
        var response = awsService.fetchPreSignedUrl(request);
        return response.toString();
    }

    private Duration getDefaultDuration() throws InvalidInputException {
        var configuration = systemConfigService.getSystemConfig(TIME_TO_LIVE_PRESIGNED_URL_SECONDS).getValue();
        return Duration.ofSeconds(Long.parseLong(configuration));
    }

    public DeleteDocumentResponse delete(Set<String> docIds, Boolean force)
            throws InvalidInputException {
        if (!force){
            return delete(docIds);
        }
        return hardDelete(docIds);
    }

    private DeleteDocumentResponse delete(Set<String> docIds){
        var retVal = new DeleteDocumentResponse();
        var documentUpdated = new HashSet<Document>();
        for (String id : docIds){
            try{
                var document = fetchById(id);
                document.setStatus(DELETED);
                retVal.appendSuccess(id);
                documentUpdated.add(document);
            } catch (InvalidInputException e){
                retVal.appendFailed(id, e.getMessage());
            }
        }
        repository.saveAll(documentUpdated);
        return retVal;
    }

    private DeleteDocumentResponse hardDelete(Set<String> docIds)
            throws InvalidInputException {
        var retVal = new DeleteDocumentResponse();
        var documents = fetchAllById(docIds);
        for (var document : documents){
            try {
                deleteObject(document);
                repository.deleteById(document.getId());
                retVal.appendSuccess(document.getId());
            } catch (InvalidInputException | ServerException e) {
                retVal.appendFailed(document.getId(), e.getMessage());
            }
            docIds.remove(document.getId());
            if (CollectionUtils.isNotEmpty(docIds)){
                for (var id : docIds){
                    retVal.appendFailed(id, "Document not found.");
                }
            }
        }
        return retVal;
    }

    private void deleteObject(Document document)
            throws InvalidInputException, ServerException {
        var key = getKey(document.getUrl());
        var request = AwsRequest.builder()
                .config(properties)
                .key(key)
                .build();
        awsService.delete(request);
    }

    public PageableResponse<DocumentDto> fetch(DocumentSearchCriteria searchCriteria){
        var pageable = PageableUtils.createPageable(searchCriteria);
        var documentsPage = repository.findAll(
                DocumentSpecification.buildSearchCriteria(searchCriteria),
                pageable
        );
        var documentList = documentsPage.stream()
                .map(DocumentMapper.INSTANCE::toDto)
                .toList();
        return new PageableResponse<>(documentList, searchCriteria);
    }


    /**
     * Fetches document by document id
     *
     * @param documentId document id
     * @return Document
     * @throws InvalidInputException In case the document not found by id given;
     */
    public Document fetchById(final String documentId) throws InvalidInputException {
        return repository.findById(documentId)
                .orElseThrow(() -> new InvalidInputException("Document not found with id "+documentId));
    }

    public List<Document> fetchAllById(final Set<String> docIds)
            throws InvalidInputException {
        var retVal = repository.findAllById(docIds);
        if (CollectionUtils.isNotEmpty(retVal)){
            return retVal;
        }
        throw new InvalidInputException("Documents not founds");
    }

}
