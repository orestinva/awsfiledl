package com.inva.ui.controller;

import com.inva.ui.events.IEvent;
import com.inva.ui.events.UploadEvent;
import com.inva.aws.AWSDriver;
import com.inva.ui.view.GUI;
import com.inva.ui.view.TaskTableModel;

/**
 * Created by inva on 11/14/2016.
 */
public class UploadButtonHandler implements IButtonEventHandler {

    private AWSDriver driver;
    private GUI gui;

    public UploadButtonHandler(AWSDriver driver, GUI gui) {
        this.driver = driver;
        this.gui = gui;
    }


    public void handleEvent(IEvent event) {
        UploadEvent uploadEvent = (UploadEvent) event;
        driver.uploadTo(uploadEvent.getBucketName(), uploadEvent.getFileName(), uploadEvent.getFile());
        gui.refreshTaskTable(uploadEvent.getFile());
    }
}
