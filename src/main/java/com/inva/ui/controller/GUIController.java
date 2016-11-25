package com.inva.ui.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.inva.aws.AWSDriver;
import com.inva.ui.events.DeleteEvent;
import com.inva.ui.events.DownloadEvent;
import com.inva.ui.events.Event;
import com.inva.ui.events.UploadEvent;
import com.inva.ui.view.*;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by inva on 11/23/2016.
 */
public class GUIController {
    private GUI gui;
    private AWSDriver driver;
    private DownloadButtonHandler dlHandler;
    private DownloadEvent dlEvent;
    private UploadButtonHandler uplHandler;
    private UploadEvent uplEvent;
    private DeleteEvent delEvent;
    private DeleteButtonHandler delHandler;


    public GUIController (AmazonS3 s3client, GUI gui){
        this.gui = gui;
        driver = new AWSDriver(s3client, gui.getTaskTableModel());
        this.dlHandler = new DownloadButtonHandler(driver);
        this.uplHandler = new UploadButtonHandler(driver);
        this.delHandler = new DeleteButtonHandler(driver);

        ArrayList<String> bucketsList = driver.getBucketList();
        for (String b : bucketsList){
            gui.getBucketsDropDown().addItem(b);
        }

    }

    public void handleEvent(Event event){
        if (event.getEventID().equals("Download") && event.getType().equals(Event.Type.BUTTON)){
            onDownloadClicked();
        }
        if (event.getEventID().equals("Delete") && event.getType().equals(Event.Type.BUTTON)){
            onDeleteClicked();
        }
        if (event.getEventID().equals("Upload")&& event.getType().equals(Event.Type.BUTTON)){
            onUploadClicked();
        }

    }

    private void onUploadClicked() {
        //Making filechooser dialog
        JFileChooser fileChooser = new JFileChooser();
        int ret = fileChooser.showDialog(gui, "Choose File");
        if (ret == JFileChooser.APPROVE_OPTION){
            //uploading selected file
            File file = fileChooser.getSelectedFile();
            uplEvent = new UploadEvent(gui.getActiveBucket(), file.getName(), file);
            if(uplHandler != null){
                uplHandler.handleEvent(uplEvent);
            }
            refreshTaskTable(file.getName(), file.length(), false);
        }
        refreshTable();
    }

    private void onDeleteClicked() {
        String objectName = (String)gui.getObjectsTable().getValueAt(gui.getObjectsTable().getSelectedRow(), 0);
        delEvent = new DeleteEvent(gui.getActiveBucket(), objectName);
        if(delHandler != null){
            delHandler.handleEvent(delEvent);
        }
    }

    private void onDownloadClicked() {
        String objectName = (String)gui.getObjectsTable().getValueAt(gui.getObjectsTable().getSelectedRow(), 0);
        String sizeStr = (String)gui.getObjectsTable().getValueAt(gui.getObjectsTable().getSelectedRow(), 1);
        long size = Long.parseLong(sizeStr);
        String isFolderStr = (String) gui.getObjectsTable().getValueAt(gui.getObjectsTable().getSelectedRow(), 2);
        boolean isFolder = true;
        if (isFolderStr.equals("File")){
            isFolder = false;
        }
        String path = gui.getSettingsDialog().getSaveDir();
        File file = new File(path+File.separator+objectName);
        dlEvent = new DownloadEvent(gui.getActiveBucket(), objectName, file);
        if(dlHandler != null){
            dlHandler.handleEvent(dlEvent);
        }
        refreshTaskTable(objectName, size, isFolder);
    }

    public void refreshTable(){
        int rows = gui.getTableModel().getRowCount();
        for (int i = rows - 1; i >= 0; i--) {
            gui.getTableModel().removeRow(i);
        }
        (new TableRefresher(driver, gui.getActiveBucket(), gui.getTableModel(), gui)).execute();
        gui.getTableModel().fireTableDataChanged();
        gui.getObjectsTable().setModel(gui.getTableModel());
    }
    public void refreshTaskTable(String fileName, long size, boolean isFolder){
        (new TaskTableRefresher(gui.getTaskTableModel(), fileName, size, isFolder)).execute();
        gui.getTaskTableModel().fireTableDataChanged();
        gui.getTaskTable().setModel(gui.getTaskTableModel());
    }

}
