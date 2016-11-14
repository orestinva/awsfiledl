package com.inva.ui.events;

import java.io.File;

/**
 * Created by inva on 11/13/2016.
 */
public class DownloadEvent implements IEvent {

    private String bucketName;
    private String objectName;
    private File file;

    public DownloadEvent(String bucketName, String objectName, File file) {
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.file = file;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getObjectName() {
        return objectName;
    }

    public File getFile() {
        return file;
    }
}
