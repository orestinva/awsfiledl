package com.inva.ui.view;

import com.inva.aws.AWSDriver;
import com.inva.aws.AWSFileDescription;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

/**
 * Created by inva on 11/3/2016.
 */
public class TableRefresher extends SwingWorker<DefaultTableModel, Void> {
    private AWSDriver driver;
    private DefaultTableModel tableModel;
    private String activeBucket;
    private GUI gui;

    public TableRefresher(AWSDriver driver, String activeBucket, DefaultTableModel tableModel, GUI gui){
        this.driver = driver;
        this.tableModel = tableModel;
        this.activeBucket = activeBucket;
        this.gui = gui;
    }

    protected DefaultTableModel doInBackground() throws Exception {
        // remove rows
        int rows = gui.getTableModel().getRowCount();
        for (int i = rows - 1; i >= 0; i--) {
            gui.getTableModel().removeRow(i);
        }
        //get new rows
        try {
            gui.disableButtons();
            ArrayList<AWSFileDescription> descriptions = (ArrayList<AWSFileDescription>) driver.getFileDescriptions(gui.getActiveBucket());
            for(AWSFileDescription d : descriptions){
                String[] data = new String[3];
                data[0] = d.getObjectName();
                data[1] = d.getSizeToStr();
                data[2] = isFolderToStr(d.getIsFolder());
                tableModel.addRow(data);
            }
        } catch (Exception e){
            //todo = handle this exc
        }
        return tableModel;
    }

    public String isFolderToStr(boolean isFolder) {
        String strIsFolder;
        if (!isFolder){
            strIsFolder = "File";
        } else {
            strIsFolder = "Folder";
        }
        return strIsFolder;
    }

    public void done(){
        gui.enableButtons();
    }
}
