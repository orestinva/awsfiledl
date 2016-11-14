package com.inva;

import java.io.File;

/**
 * Created by inva on 11/14/2016.
 */
public class UploadEvent implements IEvent {

    private String bucketName;
    private String fileName;
    private File file;

    public UploadEvent(String bucketName, String fileName, File file) {
        this.bucketName = bucketName;
        this.fileName = fileName;
        this.file = file;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getFileName() {
        return fileName;
    }

    public File getFile() {
        return file;
    }


}
