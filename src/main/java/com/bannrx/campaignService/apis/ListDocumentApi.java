package com.bannrx.campaignService.apis;

import com.bannrx.campaignService.searchCriteria.DocumentSearchCriteria;
import com.bannrx.campaignService.services.DocumentService;
import com.bannrx.common.dtos.DocumentDto;
import com.bannrx.common.dtos.responses.PageableResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import rklab.utility.annotations.Loggable;
import rklab.utility.dto.ApiOutput;

@Service
@Loggable
@RequiredArgsConstructor
public class ListDocumentApi {

    private final DocumentService service;

    public ApiOutput<PageableResponse<DocumentDto>> process(DocumentSearchCriteria searchCriteria){
        var retVal = service.fetch(searchCriteria);
        return new ApiOutput<>(HttpStatus.OK.value(), "Documents list", retVal);
    }

}
