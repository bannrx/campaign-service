package com.bannrx.campaignService.services;

import com.bannrx.common.dtos.DocumentDto;
import com.bannrx.common.dtos.SecurityUserDto;
import com.bannrx.campaignService.dto.configuration.AwsProperties;
import com.bannrx.common.dtos.requests.DocumentUploadRequest;
import com.bannrx.common.mappers.DocumentMapper;
import com.bannrx.common.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rklab.utility.annotations.Loggable;
import rklab.utility.dto.AwsUploadRequest;
import rklab.utility.expectations.ServerException;
import rklab.utility.services.AwsService;

import java.io.IOException;

import static com.bannrx.common.enums.DocumentStatus.UPLOADED;

@Service
@Loggable
@RequiredArgsConstructor
public class DocumentService {

    private final AwsProperties properties;
    private final AwsService awsService;
    private final DocumentRepository repository;

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
     * @param loggedInUser SecurityUserDto
     * @return DocumentDto
     * @throws IOException Exception while getting input stream
     * @throws ServerException Exception while uploading to the object db
     */
    public DocumentDto create(DocumentUploadRequest request, SecurityUserDto loggedInUser)
            throws IOException, ServerException {
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

}
