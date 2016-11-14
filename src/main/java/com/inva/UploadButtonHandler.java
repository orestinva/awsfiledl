package com.inva;

/**
 * Created by inva on 11/14/2016.
 */
public class UploadButtonHandler implements IButtonEventHandler {

    private AWSDriver driver;

    public UploadButtonHandler(AWSDriver driver) {
        this.driver = driver;
    }


    public void handleEvent(IEvent event) {
        UploadEvent uploadEvent = (UploadEvent) event;
        driver.uploadTo(uploadEvent.getBucketName(), uploadEvent.getFileName(), uploadEvent.getFile());
    }
}
