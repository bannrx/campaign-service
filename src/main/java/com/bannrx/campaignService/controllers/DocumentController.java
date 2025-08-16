package com.bannrx.campaignService.controllers;

import com.bannrx.campaignService.apis.DeleteDocumentApi;
import com.bannrx.campaignService.apis.ListDocumentApi;
import com.bannrx.campaignService.apis.PreSignedUrlApi;
import com.bannrx.campaignService.apis.UploadDocumentApi;
import com.bannrx.campaignService.searchCriteria.DocumentSearchCriteria;
import com.bannrx.common.dtos.requests.DocumentUploadRequest;
import com.bannrx.common.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import rklab.utility.annotations.Loggable;
import rklab.utility.dto.ApiOutput;
import rklab.utility.expectations.InvalidInputException;
import rklab.utility.expectations.ServerException;

import java.io.IOException;
import java.util.Set;

@Loggable
@RestController
@RequestMapping("/v1/document")
@RequiredArgsConstructor
public class DocumentController {

    private final UserService userService;
    private final UploadDocumentApi uploadDocumentApi;
    private final PreSignedUrlApi preSignedUrlApi;
    private final DeleteDocumentApi deleteDocumentApi;
    private final ListDocumentApi listDocumentApi;

    @PostMapping("/")
    public ApiOutput<?> upload(@ModelAttribute DocumentUploadRequest request)
            throws InvalidInputException, ServerException, IOException {
        var loggedInUser = userService.fetchLoggedInUser();
        return uploadDocumentApi.process(request, loggedInUser);
    }

    @GetMapping("/pre-signed")
    public ApiOutput<?> preSignedUrl(@RequestParam("doc_id") String docId)
            throws InvalidInputException, ServerException {
        return preSignedUrlApi.process(docId);
    }

    @DeleteMapping("/")
    public ApiOutput<?> delete(@RequestParam("doc_ids") Set<String> docIds)
            throws InvalidInputException {
        return deleteDocumentApi.process(docIds, false);
    }

    @DeleteMapping("/hard")
    public ApiOutput<?> hardDelete(@RequestParam("doc_ids") Set<String> docIds)
            throws InvalidInputException {
        return deleteDocumentApi.process(docIds, true);
    }

    @GetMapping("/")
    public ApiOutput<?> list(DocumentSearchCriteria searchCriteria){
        return listDocumentApi.process(searchCriteria);
    }

}
