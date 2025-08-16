package com.bannrx.campaignService.apis;

import com.bannrx.campaignService.dto.DeleteDocumentResponse;
import com.bannrx.campaignService.services.DocumentService;
import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import rklab.utility.annotations.Loggable;
import rklab.utility.dto.ApiOutput;
import rklab.utility.expectations.InvalidInputException;
import rklab.utility.expectations.ServerException;

import java.util.Set;

@Service
@Loggable
@RequiredArgsConstructor
public class DeleteDocumentApi {

    private final DocumentService documentService;

    public ApiOutput<DeleteDocumentResponse> process(Set<String> docIds, Boolean force)
            throws InvalidInputException {
        if (Collections.isEmpty(docIds)){
            throw new InvalidInputException("Please give document ids to delete.");
        }
        var retVal = documentService.delete(docIds, force);
        return new ApiOutput<>(HttpStatus.OK.value(), "Successfully processed the request.", retVal);
    }

}
