package com.bannrx.campaignService.controllers;

import com.bannrx.common.dtos.campaign.CampaignDto;
import com.bannrx.common.searchCriteria.CampaignSearchCriteria;
import com.bannrx.campaignService.apis.AddCampaignApi;
import com.bannrx.campaignService.apis.DeleteCampaignApi;
import com.bannrx.campaignService.apis.FetchCampaignApi;
import com.bannrx.campaignService.apis.UpdateCampaignApi;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import rklab.utility.annotations.Loggable;
import rklab.utility.dto.ApiOutput;
import rklab.utility.expectations.InvalidInputException;
import rklab.utility.expectations.ServerException;


@Loggable
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/campaign")
public class CampaignController {
    private final AddCampaignApi addCampaignApi;
    private final UpdateCampaignApi updateCampaignApi;
    private final DeleteCampaignApi deleteCampaignApi;
    private final FetchCampaignApi fetchCampaignApi;

    @PostMapping("/register")
    public ApiOutput<?> register(@RequestBody CampaignDto dto) throws InvalidInputException {
        return addCampaignApi.process(dto);
    }

    @PutMapping("/update")
    public ApiOutput<?> update(@RequestBody CampaignDto dto) throws InvalidInputException, ServerException {
        return updateCampaignApi.update(dto);
    }

    @DeleteMapping("/delete")
    public ApiOutput<?> delete(@RequestParam String campaignId) throws InvalidInputException {
        return deleteCampaignApi.process(campaignId);
    }

    @GetMapping("/all")
    public ApiOutput<?> fetchCampaignList(CampaignSearchCriteria searchCriteria){
        return fetchCampaignApi.process(searchCriteria);
    }
}
