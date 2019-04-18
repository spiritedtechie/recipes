package com.blah;

public abstract class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = -1734945070338778479L;
    
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



