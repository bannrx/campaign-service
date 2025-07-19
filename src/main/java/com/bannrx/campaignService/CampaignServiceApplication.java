package com.bannrx.campaignService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
		"com.bannrx.common.service",
		"com.bannrx.common.securityfilters",
		"com.bannrx.campaignService",
		"rklab.utility"
})
@EntityScan(basePackages = "com.bannrx.common.persistence.entities")
@EnableJpaRepositories(basePackages = "com.bannrx.common.repository")
@EnableAspectJAutoProxy
public class CampaignServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CampaignServiceApplication.class, args);
	}

}
