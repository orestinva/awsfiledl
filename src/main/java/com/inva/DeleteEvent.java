package com.inva;

/**
 * Created by inva on 11/14/2016.
 */
public class DeleteEvent implements IEvent {

    private String bucketName;
    private String objectName;

    public DeleteEvent(String bucketName, String objectName) {
        this.bucketName = bucketName;
        this.objectName = objectName;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getObjectName() {
        return objectName;
    }
}
