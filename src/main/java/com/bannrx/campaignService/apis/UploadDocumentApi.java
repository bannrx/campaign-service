package com.bannrx.campaignService.apis;

import com.bannrx.common.dtos.DocumentDto;
import com.bannrx.common.dtos.SecurityUserDto;
import com.bannrx.common.dtos.requests.DocumentUploadRequest;
import com.bannrx.common.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import rklab.utility.annotations.Loggable;
import rklab.utility.dto.ApiOutput;
import rklab.utility.expectations.ServerException;
import rklab.utility.utilities.ValidationUtils;

import java.io.IOException;

@Service
@Loggable
@RequiredArgsConstructor
public class UploadDocumentApi {

    private final ValidationUtils validationUtils;
    private final DocumentService documentService;

    public ApiOutput<DocumentDto> process(DocumentUploadRequest request, SecurityUserDto loggedInUser)
            throws ServerException, IOException {
        validationUtils.validate(request);
        var response = documentService.create(request, loggedInUser);
        return new ApiOutput<>(HttpStatus.CREATED.value(), "Document Uploaded Successfully", response);
    }

}
