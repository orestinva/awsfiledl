package com.inva.ui.view;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by inva on 11/19/2016.
 */
public class TaskTableProgressCellRenderer extends JProgressBar implements TableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        int progress = 0;
        if (value instanceof Float) {
            progress = Math.round(((Float) value) * 100f);
        } else if (value instanceof Integer) {
            progress = (Integer)value;
        }
        setValue(progress);
        return this;
    }
}
