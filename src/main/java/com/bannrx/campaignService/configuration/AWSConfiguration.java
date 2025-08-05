package com.bannrx.campaignService.configuration;

import com.bannrx.campaignService.dto.configuration.AwsProperties;
import com.bannrx.common.service.SystemConfigService;
import com.fasterxml.jackson.core.JsonParseException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rklab.utility.expectations.InvalidInputException;
import rklab.utility.utilities.JsonUtils;

import static com.bannrx.common.constants.SystemConfigKeys.AWS_S3_CONFIGURATION;

@Slf4j
@Configuration
public class AWSConfiguration {

    @Autowired private SystemConfigService systemConfigService;

    private AwsProperties properties;

    @PostConstruct
    void setUp() throws InvalidInputException, JsonParseException {
        log.info("Setting up AWS Configurations");
        var secret = systemConfigService.getSystemConfig(AWS_S3_CONFIGURATION).getValue();
        properties = JsonUtils.readObjectFromJson(secret, AwsProperties.class);
        log.info("AWS configuration completed successfully.");
    }

    @Bean
    public AwsProperties awsProperties(){
        return properties;
    }

}
