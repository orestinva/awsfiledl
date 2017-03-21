package com.inva.ui.controller;

import com.inva.aws.AWSDriver;
import com.inva.ui.controller.IButtonEventHandler;
import com.inva.ui.events.DeleteEvent;
import com.inva.ui.events.IEvent;

/**
 * Created by inva on 11/14/2016.
 */
public class DeleteButtonHandler implements IButtonEventHandler {

    private AWSDriver driver;

    public DeleteButtonHandler(AWSDriver driver) {
        this.driver = driver;
    }

    public void handleEvent(IEvent event) {
        DeleteEvent deleteEvent = (DeleteEvent) event;
        driver.deleteSelectedObject(deleteEvent.getBucketName(), deleteEvent.getObjectName());
    }
}
