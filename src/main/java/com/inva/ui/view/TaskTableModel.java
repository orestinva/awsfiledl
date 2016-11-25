package com.inva.ui.view;

import com.inva.aws.AWSFileDescription;

import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.util.*;

/**
 * Created by inva on 11/19/2016.
 */
public class TaskTableModel extends AbstractTableModel {
    private List<TaskTableRowData> dataList;
    private Map<String, TaskTableRowData> mapLookup;

    public TaskTableModel(){
        dataList = new ArrayList<TaskTableRowData>(25);
        mapLookup = new HashMap<String, TaskTableRowData>(25);
    }

    public int getRowCount() {
        return dataList.size();
    }

    public int getColumnCount() {
        return 4;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        TaskTableRowData rowData = dataList.get(rowIndex);
        Object value = null;
        switch (columnIndex) {
            case 0:
                value = rowData.getFileName();
                break;
            case 1:
                value = rowData.getFolderToStr(rowData.getFolder());
                break;
            case 2:
                value = rowData.getLength();
                break;
            case 3:
                value = rowData.getStatus();
                break;
        }
        return value;
    }

    public String getColumnName(int column){
        String name = "??";
        switch (column) {
            case 0:
                name = "File";
                break;
            case 1:
                name = "File Type";
                break;
            case 2:
                name = "Size";
                break;
            case 3:
                name = "Status";
                break;
        }
        return name;
    }
    public void setValueAt(float value, int rowIndex, int columnIndex) {
        TaskTableRowData rowData = dataList.get(rowIndex);
        switch (columnIndex) {
            case 3:
                rowData.setStatus(value);
        }
    }

    public void addFile(String fileName, long size, boolean isFolder) {
        TaskTableRowData rowData = new TaskTableRowData(fileName, size, isFolder);
        mapLookup.put(fileName, rowData);
        dataList.add(rowData);
        fireTableRowsInserted(dataList.size() - 1, dataList.size() - 1);
    }

    public void updateStatus(File file, double progress) {
        TaskTableRowData rowData = mapLookup.get(file.getName());
        if (rowData != null) {
            int row = dataList.indexOf(rowData);
            float p = (float) progress / 100f;
            setValueAt(p, row, 3);
            fireTableCellUpdated(row, 3);
        }
    }
}
