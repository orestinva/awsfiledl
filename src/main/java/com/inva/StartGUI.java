package com.inva;

import com.amazonaws.services.s3.AmazonS3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by inva on 10/26/2016.
 */
public class StartGUI extends JFrame {

    private JPanel panel = new JPanel(new BorderLayout());
    private JPanel buttonPanel = new JPanel(new FlowLayout());
    private JButton downloadButton = new JButton("Download");
    private JButton deleteButton = new JButton("Delete");
    private JButton uploadButton = new JButton("Upload..");
    private JComboBox buckets = new JComboBox();
    private JTable objectsTable = new JTable();
    private JPanel bucketsPanel = new JPanel(new FlowLayout());
    private JLabel bucketLabel = new JLabel("Select bucket:");
    private AWSDriver driver;
    private String activeBucket;
    private JScrollPane scrollPane;
    private String path = "C:" + File.separator +"temp"+File.separator;
    private final DefaultTableModel tableModel = (DefaultTableModel) objectsTable.getModel();
    private JMenuBar menuBar = new JMenuBar();

    public StartGUI(AmazonS3 s3Client){
        super("AWS File Downloader");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JMenu menu = new JMenu("File");
        JMenuItem menuExit = new JMenuItem("Exit", KeyEvent.VK_X);

        menu.add(menuExit);
        menuBar.add(menu);
        menuBar.setOpaque(true);
        menuBar.setPreferredSize(new Dimension(200, 20));

        driver = new AWSDriver(s3Client);

        String[] columns = {"Name", "Size", "Type"};
        tableModel.setColumnIdentifiers(columns);
        objectsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        objectsTable.setModel(tableModel);
        objectsTable.getTableHeader().setReorderingAllowed(false);
        objectsTable.getTableHeader().setResizingAllowed(true);
        scrollPane = new JScrollPane(objectsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        buckets.setPreferredSize(new Dimension(200, 20));
        ArrayList<String> bucketsList = driver.getBucketList();
        for (String b : bucketsList){
            buckets.addItem(b);
        }
        buckets.setSelectedIndex(-1);
        buckets.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeBucket = buckets.getSelectedItem().toString();

                refreshTable();

                downloadButton.setEnabled(true);
                uploadButton.setEnabled(true);
                deleteButton.setEnabled(true);
            }
        });

        bucketsPanel.add(bucketLabel);
        bucketsPanel.add(buckets);

        panel.add(bucketsPanel, BorderLayout.PAGE_START);

        downloadButton.setEnabled(false);
        uploadButton.setEnabled(false);
        deleteButton.setEnabled(false);

        downloadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            objectsTable.getSelectedRow();
            String objectName = (String)objectsTable.getValueAt(objectsTable.getSelectedRow(), 0);
            File file = new File(path+objectName);
            driver.copyTo(activeBucket, objectName, file);
            }
        });
        buttonPanel.add(downloadButton);

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                objectsTable.getSelectedRow();
                String objectName = (String)objectsTable.getValueAt(objectsTable.getSelectedRow(), 0);
                driver.deleteSelectedObject(activeBucket, objectName);
            }
        });
        buttonPanel.add(deleteButton);

        uploadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int ret = fileChooser.showDialog(StartGUI.this, "Choose File");
                if (ret == JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();
                    driver.uploadTo(activeBucket, file.getName(), file);
                }
            }
        });
        buttonPanel.add(uploadButton);
        panel.add(scrollPane, BorderLayout.CENTER);

        panel.add(buttonPanel, BorderLayout.PAGE_END);

        panel.setPreferredSize(new Dimension(640, 480));
        setContentPane(panel);
        setJMenuBar(menuBar);
        pack();
        setVisible(true);
    }

    public void refreshTable(){
        int rows = tableModel.getRowCount();
        for (int i = rows - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }
        (new TableRefresher(driver, activeBucket, tableModel)).execute();
        tableModel.fireTableDataChanged();
        objectsTable.setModel(tableModel);
    }

}
