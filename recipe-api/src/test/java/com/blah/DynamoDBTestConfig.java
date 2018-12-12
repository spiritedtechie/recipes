package com.blah;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
@EnableDynamoDBRepositories(basePackages = "com.blah")
public class DynamoDBTestConfig {

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        System.setProperty("sqlite4java.library.path", "native-libs");
        return DynamoDBEmbedded.create().amazonDynamoDB();
    }
}
