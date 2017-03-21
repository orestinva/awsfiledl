package com.inva.ui.view;

import com.inva.aws.AWSFileDescription;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by inva on 11/26/2016.
 */
public class ObjectsTableModel extends AbstractTableModel {

    private List<AWSFileDescription> fileDescriptionList;
    private Map<String, List<AWSFileDescription>> fileDescriptionMap;

    public ObjectsTableModel(){
        fileDescriptionList = new ArrayList<AWSFileDescription>();
        fileDescriptionMap = new HashMap<String, List<AWSFileDescription>>();
    }

    public void addFiles(List<AWSFileDescription> fileDescriptions){
        for (AWSFileDescription fd : fileDescriptions){

        }
    }

    public int getRowCount() {
        return 0;
    }

    public int getColumnCount() {
        return 0;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }
}
