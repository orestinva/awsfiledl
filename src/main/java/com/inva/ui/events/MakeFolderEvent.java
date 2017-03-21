package com.inva.ui.events;

import java.io.File;

/**
 * Created by inva on 11/25/2016.
 */
public class MakeFolderEvent implements IEvent {

    private String bucketName;
    private String fileName;

    public MakeFolderEvent(String bucketName, String fileName) {
        this.bucketName = bucketName;
        this.fileName = fileName;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getFileName() {
        return fileName;
    }


}
