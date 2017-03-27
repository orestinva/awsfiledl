package com.inva.ui.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.inva.aws.AWSDriver;
import com.inva.ui.events.*;
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
    private MakeFolderEvent mfEvent;
    private MakeFolderButtonHandler mfHandler;

    public GUIController (AmazonS3 s3client, GUI gui){
        this.gui = gui;
        this.driver = new AWSDriver(s3client, gui.getTaskTableModel());
        this.dlHandler = new DownloadButtonHandler(driver);
        this.uplHandler = new UploadButtonHandler(driver);
        this.delHandler = new DeleteButtonHandler(driver);
        this.mfHandler = new MakeFolderButtonHandler(driver);
        initBuckets();
        driver.setGuiController(this);
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
        if (event.getEventID().equals("MkFolder")&& event.getType().equals(Event.Type.BUTTON) ){
            onMakeFolderClicked();
        }

    }

    private void onMakeFolderClicked() {
        try {
            String folderName = gui.getOptionPane().showInputDialog(gui, "Please enter folder name (non-alphanumeric characters will be ignored)");
            String fName = folderName.replaceAll("[^A-Za-z0-9]", "");
            mfEvent = new MakeFolderEvent(gui.getActiveBucket(), fName + "/");
            if(mfHandler != null){
                mfHandler.handleEvent(mfEvent);
            }
        } catch (Exception e){

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
            refreshTaskTable(file.getName(), file.length(), false, "Upload");
        }
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
        refreshTaskTable(objectName, size, isFolder, "Download");
    }

    public void refreshTable(){
            (new TableRefresher(driver, gui.getActiveBucket(), gui.getTableModel(), gui)).execute();
    }

    public void refreshTaskTable(String fileName, long size, boolean isFolder, String type){
        (new TaskTableRefresher(gui.getTaskTableModel(), fileName, size, isFolder, type)).execute();
        gui.getTaskTableModel().fireTableDataChanged();
        gui.getTaskTable().setModel(gui.getTaskTableModel());
    }

    public void initBuckets(){
        ArrayList<String> bucketsList = driver.getBucketList();
        for (String b : bucketsList){
            gui.getBucketsDropDown().addItem(b);
        }
    }

}
