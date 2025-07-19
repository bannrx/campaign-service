package com.bannrx.campaignService.apis;

import com.bannrx.common.service.CampaignService;
import com.bannrx.common.utilities.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import rklab.utility.annotations.Loggable;
import rklab.utility.dto.ApiOutput;
import rklab.utility.expectations.InvalidInputException;



@Loggable
@Service
@RequiredArgsConstructor
public class DeleteCampaignApi {

    private final CampaignService campaignService;

    private final static String MESSAGE = "Campaign deleted successfully";

    public ApiOutput<?> process(String campaignId) throws InvalidInputException {
        validate(campaignId);
        return new ApiOutput<>(HttpStatus.OK.value(), MESSAGE, campaignService.delete(campaignId));
    }

    private void validate(String id) throws InvalidInputException {

        if(!StringUtil.isNotBlank(id)){
            throw new InvalidInputException("Campaign id is required");
        }

        if(!campaignService.isExistById(id)){
            throw new InvalidInputException(String.format("Campaign id is incorrect %s", id));
        }
    }
}
