package com.inva.ui.controller;

import com.inva.aws.AWSDriver;
import com.inva.ui.controller.IButtonEventHandler;
import com.inva.ui.events.DownloadEvent;
import com.inva.ui.events.IEvent;
import com.inva.ui.view.GUI;
import com.inva.ui.view.TaskTableModel;
import com.inva.ui.view.TaskTableRowData;

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
