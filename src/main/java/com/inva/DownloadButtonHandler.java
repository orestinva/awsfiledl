package com.inva;

import java.io.File;

/**
 * Created by inva on 11/13/2016.
 */
public class DownloadButtonHandler implements IButtonEventHandler {

    private AWSDriver driver;
    public DownloadButtonHandler(AWSDriver driver){
        this.driver = driver;
    }

    public void handleEvent(IEvent event) {
        DownloadEvent dlEvent = (DownloadEvent) event;
        driver.copyTo(dlEvent.getBucketName(), dlEvent.getObjectName(), dlEvent.getFile());
    }
}
