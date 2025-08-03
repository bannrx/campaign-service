package com.bannrx.campaignService.controllers;

import com.bannrx.campaignService.apis.UploadDocumentApi;
import com.bannrx.common.dtos.requests.DocumentUploadRequest;
import com.bannrx.common.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import rklab.utility.annotations.Loggable;
import rklab.utility.dto.ApiOutput;
import rklab.utility.expectations.InvalidInputException;
import rklab.utility.expectations.ServerException;

import java.io.IOException;

@Loggable
@RestController
@RequestMapping("/v1/document")
@RequiredArgsConstructor
public class DocumentController {

    private final UserService userService;
    private final UploadDocumentApi uploadDocumentApi;

    @PostMapping("/")
    public ApiOutput<?> upload(@ModelAttribute DocumentUploadRequest request)
            throws InvalidInputException, ServerException, IOException {
        var loggedInUser = userService.fetchLoggedInUser();
        return uploadDocumentApi.process(request, loggedInUser);
    }

}
