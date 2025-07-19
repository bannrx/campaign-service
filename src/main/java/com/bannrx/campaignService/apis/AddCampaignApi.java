package com.bannrx.campaignService.apis;

import com.bannrx.common.dtos.campaign.CampaignDto;
import com.bannrx.common.service.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import rklab.utility.annotations.Loggable;
import rklab.utility.dto.ApiOutput;
import rklab.utility.expectations.InvalidInputException;
import rklab.utility.expectations.ServerException;
import rklab.utility.utilities.ValidationUtils;
import java.time.LocalDate;


@RequiredArgsConstructor
@Loggable
@Service
public class AddCampaignApi {
    private final CampaignService campaignService;
    private final ValidationUtils validationUtils;

    private final static String SUCCESS_MESSAGE = "Campaign Register successfully";

    public ApiOutput<?> process(CampaignDto dto) throws InvalidInputException {
        validate(dto);
        return new ApiOutput<>(HttpStatus.OK.value(), SUCCESS_MESSAGE, campaignService.register(dto));
    }

    private void validate(CampaignDto dto) throws InvalidInputException {
        validationUtils.validate(dto);
        campaignService.validateCampaignDates(dto);
    }
}
