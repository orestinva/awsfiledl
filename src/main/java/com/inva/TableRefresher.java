package com.inva;

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
    public TableRefresher(AWSDriver driver, String activeBucket, DefaultTableModel tableModel){
        this.driver = driver;
        this.tableModel = tableModel;
        this.activeBucket = activeBucket;
    }

    protected DefaultTableModel doInBackground() throws Exception {

        //get new rows
        ArrayList<AWSFileDescription> descriptions = (ArrayList<AWSFileDescription>) driver.getFileDescriptions(activeBucket);
        for(AWSFileDescription d : descriptions){
            String[] data = new String[3];
            data[0] = d.getObjectName();
            data[1] = d.getSizeToStr();
            data[2] = isFolderToStr(d.getIsFolder());
            tableModel.addRow(data);
        }
        return tableModel;
    }

    public String isFolderToStr(boolean isFolder) {
        String strIsFolder;
        if (isFolder == false){
            strIsFolder = "File";
        } else {
            strIsFolder = "Folder";
        }
        return strIsFolder;
    }

    public void done(){

    }
}
