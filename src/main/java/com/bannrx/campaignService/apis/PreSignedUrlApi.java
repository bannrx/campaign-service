package com.bannrx.campaignService.apis;

import com.bannrx.campaignService.services.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import rklab.utility.annotations.Loggable;
import rklab.utility.dto.ApiOutput;
import rklab.utility.expectations.InvalidInputException;
import rklab.utility.expectations.ServerException;

@Service
@Loggable
@RequiredArgsConstructor
public class PreSignedUrlApi {

    private final DocumentService documentService;

    public ApiOutput<String> process(final String documentId)
            throws InvalidInputException, ServerException {
        return new ApiOutput<>(
                HttpStatus.CREATED.value(),
                "Pre-Signed Url",
                documentService.fetchPreSignedUrl(documentId));
    }

}
