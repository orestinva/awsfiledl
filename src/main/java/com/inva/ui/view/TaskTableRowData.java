package com.inva.ui.view;

import java.io.File;

/**
 * Created by inva on 11/19/2016.
 */
public class TaskTableRowData {
    private String fileName;
    private boolean isFolder;
    private long size;
    private float status;
    private String type;

    public TaskTableRowData(String fileName, long size, boolean isFolder, String type) {
        this.fileName = fileName;
        this.isFolder = isFolder;
        this.size = size;
        this.status = 0f;
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public long getLength() {
        return size;
    }

    public float getStatus() {
        return status;
    }

    public boolean getFolder() {
        return isFolder;
    }

    public String getFolderToStr(boolean isFolder){
        String strIsFolder;
        if (!isFolder){
            strIsFolder = "File";
        } else {
            strIsFolder = "Folder";
        }
        return strIsFolder;
    }

    public void setStatus(float status) {
        this.status = status;
    }
    public String getType() {
        return type;
    }

}
