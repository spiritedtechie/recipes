package com.blah;

public abstract class NotFoundException extends RuntimeException {

    private final String detailedMessage;

    public NotFoundException(String resourceId) {
        this.detailedMessage = getResourceName() + " not found: " + resourceId;
    }

    protected abstract String getResourceName();

    @Override
    public String getMessage() {
        return this.detailedMessage;
    }
}



