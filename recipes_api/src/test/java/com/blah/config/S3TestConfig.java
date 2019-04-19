package com.blah.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.findify.s3mock.S3Mock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class S3TestConfig {

    private static final int PORT = 8001;

    private S3Mock s3Mock;

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public S3Mock s3Mock() {
        return new S3Mock.Builder().withPort(PORT).withInMemoryBackend().build();
    }

    @Bean
    public AmazonS3 amazonS3(S3Mock s3Mock) {
        var endpoint = new AwsClientBuilder.EndpointConfiguration(
                "http://localhost:" + PORT, "us-west-2");

        return AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(endpoint)
                .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
                .build();
    }
}
