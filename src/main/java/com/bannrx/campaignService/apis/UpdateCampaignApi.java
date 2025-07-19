package com.bannrx.campaignService.apis;

import com.bannrx.common.dtos.campaign.CampaignDto;
import com.bannrx.common.service.CampaignService;
import com.bannrx.common.validationGroups.UpdateValidationGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import rklab.utility.annotations.Loggable;
import rklab.utility.dto.ApiOutput;
import rklab.utility.expectations.InvalidInputException;
import rklab.utility.expectations.ServerException;
import rklab.utility.utilities.ValidationUtils;
import java.util.Objects;



@Loggable
@Service
@RequiredArgsConstructor
public class UpdateCampaignApi {
    private final CampaignService campaignService;
    private final ValidationUtils validationUtils;

    private static final String MESSAGE = "Campaign update successfully";

    public ApiOutput<?> update(CampaignDto dto) throws InvalidInputException, ServerException {
        validate(dto);
        return new ApiOutput<>(HttpStatus.OK.value(), MESSAGE, campaignService.update(dto));
    }

    public void validate(CampaignDto dto) throws InvalidInputException {
        validationUtils.validate(dto, UpdateValidationGroup.class);

        if(Objects.nonNull(dto.getStartDate())){
            throw new InvalidInputException("Start date cannot be changed");
        }

        if(Objects.nonNull(dto.getEndDate()) && campaignService.canEndDateExtend(dto)){
            campaignService.checkEndDateAfterStart(dto);
        }
    }
}
