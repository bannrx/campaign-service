package com.bannrx.campaignService.apis;

import com.bannrx.common.searchCriteria.CampaignSearchCriteria;
import com.bannrx.common.service.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import rklab.utility.annotations.Loggable;
import rklab.utility.dto.ApiOutput;


@Loggable
@Service
@RequiredArgsConstructor
public class FetchCampaignApi {
    private final CampaignService campaignService;

    private static final String MESSAGE = "All campaign is below";

    public ApiOutput<?> process(CampaignSearchCriteria searchCriteria){
        return new ApiOutput<>(HttpStatus.OK.value(), MESSAGE, campaignService.fetch(searchCriteria));
    }
}
