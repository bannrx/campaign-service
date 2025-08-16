package com.bannrx.campaignService.apis;

import com.bannrx.common.dtos.DocumentDto;
import com.bannrx.common.dtos.SecurityUserDto;
import com.bannrx.common.dtos.requests.DocumentUploadRequest;
import com.bannrx.campaignService.services.DocumentService;
import com.bannrx.common.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import rklab.utility.annotations.Loggable;
import rklab.utility.dto.ApiOutput;
import rklab.utility.expectations.InvalidInputException;
import rklab.utility.expectations.ServerException;
import rklab.utility.utilities.ValidationUtils;

import java.io.IOException;

@Service
@Loggable
@RequiredArgsConstructor
public class UploadDocumentApi {

    private final ValidationUtils validationUtils;
    private final DocumentService documentService;
    private final UserProfileService userProfileService;

    public ApiOutput<DocumentDto> process(DocumentUploadRequest request, SecurityUserDto loggedInUser)
            throws ServerException, IOException, InvalidInputException {
        validationUtils.validate(request);
        var profile = userProfileService.fetchByUserId(loggedInUser.getId());
        var response = documentService.create(request, profile);
        return new ApiOutput<>(HttpStatus.CREATED.value(), "Document Uploaded Successfully", response);
    }

}
