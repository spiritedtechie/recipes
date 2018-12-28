package com.blah.recipes.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;

import java.util.Objects;
import java.util.Optional;

@DynamoDBDocument
public class Step {

    private String description;

    private Optional<String> imageUri = Optional.empty();

    private Optional<String> videoUri = Optional.empty();

    public Step() {
    }

    public Step(String description, Optional<String> imageUri, Optional<String> videoUri) {
        this.description = description;
        this.imageUri = imageUri;
        this.videoUri = videoUri;
    }

    public Step(String description) {
        this(description, Optional.empty(), Optional.empty());
    }

    @DynamoDBAttribute
    public String getDescription() {
        return description;
    }

    @DynamoDBAttribute
    public String getImageUri() {
        return imageUri.orElse(null);
    }

    @DynamoDBIgnore
    public Optional<String> getImageUriSafe() {
        return imageUri;
    }

    @DynamoDBAttribute
    private String getVideoUri() {
        return videoUri.orElse(null);
    }

    @DynamoDBIgnore
    public Optional<String> getVideoUriSafe() {
        return videoUri;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUri(String imageUri) {
        this.setImageUri(Optional.ofNullable(imageUri));
    }

    public void setImageUri(Optional<String> imageUri) {
        this.imageUri = imageUri;
    }

    public void setVideoUri(String videoUri) {
        this.setImageUri(Optional.ofNullable(videoUri));
    }

    public void setVideoUri(Optional<String> videoUri) {
        this.videoUri = videoUri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Step step = (Step) o;
        return Objects.equals(description, step.description) &&
                Objects.equals(imageUri, step.imageUri) &&
                Objects.equals(videoUri, step.videoUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, imageUri, videoUri);
    }

    @Override
    public String toString() {
        return "Step{" +
                "description='" + description + '\'' +
                ", imageUri=" + imageUri +
                ", videoUri=" + videoUri +
                '}';
    }
}
