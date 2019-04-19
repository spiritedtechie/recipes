package com.blah.recipes.repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.blah.imageutils.Base64Image;
import com.blah.recipes.model.Recipe;
import org.socialsignin.spring.data.dynamodb.core.DynamoDBTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class RecipeSaveImpl implements RecipeSave {

    private AmazonS3 amazonS3;

    private String s3Bucket;

    private DynamoDBTemplate dynamoDBTemplate;

    private Supplier<UUID> uuidSupplier;

    @Autowired
    public RecipeSaveImpl(AmazonS3 amazonS3,
                          @Value("${s3.bucket.images}") String s3Bucket,
                          DynamoDBTemplate dynamoDBTemplate,
                          Supplier<UUID> uuidSupplier) {
        this.amazonS3 = amazonS3;
        this.s3Bucket = s3Bucket;
        this.dynamoDBTemplate = dynamoDBTemplate;
        this.uuidSupplier = uuidSupplier;
    }

    @Override
    public Recipe saveRecipe(Recipe recipe) {
        if (recipe.getRawImageBase64().isPresent()) {
            var image = new Base64Image(recipe.getRawImageBase64().get());
            var imageUrl = storeImageInS3(image);
            recipe.setImageUrl(imageUrl);
            recipe.setRawImageBase64(Optional.empty());
        }

        return dynamoDBTemplate.save(recipe);
    }

    private String storeImageInS3(Base64Image base64Image) {
        var fileExtension = base64Image.getFileType().name().toLowerCase();
        var filename = uuidSupplier.get().toString() + "." + fileExtension;
        var is = new ByteArrayInputStream(base64Image.getBytes());

        var metadata = new ObjectMetadata();
        metadata.setContentLength(base64Image.getBytes().length);
        metadata.setContentType("image/" + fileExtension);

        var request = new PutObjectRequest(s3Bucket, filename, is, metadata);
        request.setMetadata(metadata);
        request.setCannedAcl(CannedAccessControlList.PublicRead);

        amazonS3.putObject(request);

        return "https://" + s3Bucket + ".s3.amazonaws.com/" + filename;
    }
}