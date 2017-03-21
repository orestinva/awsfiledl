package com.inva.ui.controller;

import com.inva.aws.AWSDriver;
import com.inva.ui.events.IEvent;
import com.inva.ui.events.MakeFolderEvent;

/**
 * Created by inva on 11/25/2016.
 */
public class MakeFolderButtonHandler implements IButtonEventHandler {

    private AWSDriver driver;

    public MakeFolderButtonHandler(AWSDriver driver) {
        this.driver = driver;
    }


    public void handleEvent(IEvent event) {
        MakeFolderEvent makeFolderEvent = (MakeFolderEvent) event;
        driver.createFolder(makeFolderEvent.getBucketName(), makeFolderEvent.getFileName());
    }
}
