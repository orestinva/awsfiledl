package com.inva.ui.view;

import java.io.File;

/**
 * Created by inva on 11/19/2016.
 */
public class TaskTableRowData {
    private File file;
    private boolean isFolder;
    private long length;
    private float status;

    public TaskTableRowData(File file, boolean isFolder) {
        this.file = file;
        this.isFolder = isFolder;
        this.length = file.length();
        this.status = 0f;
    }

    public File getFile() {
        return file;
    }

    public long getLength() {
        return length;
    }

    public float getStatus() {
        return status;
    }

    public boolean getFolder() {
        return isFolder;
    }

    public String getFolderToStr(boolean isFolder){
        String strIsFolder;
        if (isFolder == false){
            strIsFolder = "File";
        } else {
            strIsFolder = "Folder";
        }
        return strIsFolder;
    }

    public void setStatus(float status) {
        this.status = status;
    }
}
