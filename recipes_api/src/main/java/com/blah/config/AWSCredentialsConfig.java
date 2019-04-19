package com.blah.config;

import com.amazonaws.auth.*;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AWSCredentialsConfig {

    @Value("${amazon.credentials.profile}")
    private String awsCredentialsProfile;

    @Bean
    public AWSCredentialsProvider awsCredentialsProvider() {
        return new AWSCredentialsProviderChain(
                new EnvironmentVariableCredentialsProvider(),
                new SystemPropertiesCredentialsProvider(),
                new ProfileCredentialsProvider(awsCredentialsProfile),
                new EC2ContainerCredentialsProviderWrapper()
        );
    }
}
