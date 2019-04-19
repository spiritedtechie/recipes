package com.blah.imageutils;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class Base64Image {

    private static final String ERROR_MESSAGE = "Image base64 data in invalid. "
            + "Expecting data in format 'data:image/<file_type>;base64,<base_64_encoded_image>'";

    private Pattern METADATA_PATTERN = Pattern.compile("data:image/(jpeg|jpg|png|gif);base64");

    public enum FileType {
        PNG("png"), JPEG("jpeg", "jpg"), GIF("gif");

        private List<String> supportedExtensions;

        FileType(String... supportedExtensions) {
            this.supportedExtensions = Arrays.asList(supportedExtensions);
        }

        public static Optional<FileType> parse(String fileExtension) {
            return Arrays.stream(values()).filter(
                    type -> type.supportedExtensions.contains(fileExtension)
            ).findFirst();
        }
    }

    private byte[] imageBytes;

    private FileType fileType;

    public Base64Image(String data) {
        var base64Components = data.split(",");
        if (base64Components.length != 2) {
            throw new RuntimeException(ERROR_MESSAGE);
        }

        this.fileType = this.extractFileType(base64Components[0]);
        this.imageBytes = Base64.getDecoder().decode(base64Components[1]);
    }

    private FileType extractFileType(String metaData) {
        var m = METADATA_PATTERN.matcher(metaData);
        if (!m.matches()) {
            throw new RuntimeException(ERROR_MESSAGE);
        }

        var type = m.group(1);
        var fileType = FileType.parse(type);
        if (fileType.isEmpty()) {
            throw new RuntimeException("Unsupported file type: " + fileType + ". Only jpeg/png/gif are supported.");
        }

        return fileType.get();
    }

    public byte[] getBytes() {
        return imageBytes;
    }

    public FileType getFileType() {
        return fileType;
    }
}