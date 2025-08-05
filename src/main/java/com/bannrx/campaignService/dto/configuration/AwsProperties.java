package com.bannrx.campaignService.dto.configuration;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rklab.utility.services.AwsConfiguration;

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AwsProperties implements AwsConfiguration {

    private String endPoint;
    private String region;
    private String bucket;
    private String accessKey;
    private String secretKey;
    private boolean forcePathStyleUrl;

    @Override
    public String getEndpoint() {
        return this.endPoint;
    }

    @Override
    public String getRegion() {
        return this.region;
    }

    @Override
    public String getBucket() {
        return this.bucket;
    }

    @Override
    public String getAccessKey() {
        return this.accessKey;
    }

    @Override
    public String getSecretKey() {
        return this.secretKey;
    }

    @Override
    public boolean forcePathStyleUrl() {
        return this.forcePathStyleUrl;
    }
}
