package com.inva.ui.view;

import javax.swing.*;
import java.io.File;

/**
 * Created by inva on 11/19/2016.
 */
public class TaskTableRefresher extends SwingWorker {

    private TaskTableModel taskTableModel;
    private String fileName;
    private long size;
    private boolean isFolder;
    private String type;

    public TaskTableRefresher(TaskTableModel taskTableModel, String fileName, long size, boolean isFolder, String type){
        this.taskTableModel = taskTableModel;
        this.fileName = fileName;
        this.size = size;
        this.isFolder = isFolder;
        this.type = type;
    }

    protected TaskTableModel doInBackground() throws Exception {
        taskTableModel.addFile(fileName, size, isFolder, type);
        return taskTableModel;
    }
}
