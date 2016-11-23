package com.inva.ui.view;

import javax.swing.*;
import java.io.File;

/**
 * Created by inva on 11/19/2016.
 */
public class TaskTableRefresher extends SwingWorker {

    private TaskTableModel taskTableModel;
    private File file;
    
    public TaskTableRefresher(TaskTableModel taskTableModel, File file){
        this.taskTableModel = taskTableModel;
        this.file = file;
    }

    protected TaskTableModel doInBackground() throws Exception {
        taskTableModel.addFile(file);
        return taskTableModel;
    }
}
