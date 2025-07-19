package com.bannrx.campaignService.configuration;

import com.bannrx.common.dtos.configuration.JWTConfigProperties;
import com.bannrx.common.service.SystemConfigService;
import com.bannrx.common.service.SystemVariableService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rklab.utility.expectations.InvalidInputException;
import rklab.utility.utilities.DateUtils;

import java.util.Date;

import static com.bannrx.common.constants.SystemConfigKeys.*;

@Slf4j
@Configuration
public class JWTConfiguration {

    @Autowired private SystemVariableService variableService;
    @Autowired private SystemConfigService systemConfigService;

    private JWTConfigProperties properties;

    @PostConstruct
    void setup() throws InvalidInputException {
        log.info("Setting up JWT token configurations");
        var secret = systemConfigService.getSystemConfig(JWT_TOKEN_SECRET).getValue();
        var expiryDate = getExpiryDate();
        properties = new JWTConfigProperties(secret, expiryDate);
        log.info("JWT configuration completed successfully");
    }

    private Date getExpiryDate() throws InvalidInputException {
        var profile = variableService.getActiveProfile();
        if (StringUtils.equalsAnyIgnoreCase("local", profile)){
            var expiryAfterDays = Integer.parseInt(systemConfigService.getSystemConfig(JWT_EXPIRY_AFTER_DAYS).getValue());
            return DateUtils.getDateAfterNDays(expiryAfterDays);
        } else {
            var expiryAfterMinutes = Integer.parseInt(systemConfigService.getSystemConfig(JWT_EXPIRY_AFTER_MINUTES).getValue());
            return DateUtils.getDateAfterNMinutes(expiryAfterMinutes);
        }
    }

    @Bean
    public JWTConfigProperties jwtConfigProperties(){
        return properties;
    }

}
