package com.inva;

/**
 * Created by inva on 11/14/2016.
 */
public class DeleteEventHandler implements IButtonEventHandler {

    private AWSDriver driver;

    public DeleteEventHandler(AWSDriver driver) {
        this.driver = driver;
    }


    public void handleEvent(IEvent event) {
        DeleteEvent deleteEvent = (DeleteEvent) event;
        driver.deleteSelectedObject(deleteEvent.getBucketName(), deleteEvent.getObjectName());
    }
}
