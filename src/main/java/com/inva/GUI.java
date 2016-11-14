package com.inva;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.transfer.Upload;

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
public class GUI extends JFrame {

    private JPanel panel = new JPanel(new BorderLayout());
    private JPanel buttonPanel = new JPanel(new FlowLayout());
    private JButton downloadButton = new JButton("Download");
    private JButton deleteButton = new JButton("Delete");
    private JButton uploadButton = new JButton("Upload..");
    private JComboBox buckets = new JComboBox();
    private JTable objectsTable = new JTable();
    private JPanel bucketsPanel = new JPanel(new FlowLayout());
    private JLabel bucketLabel = new JLabel("Select bucket:");
    private String activeBucket;
    private JScrollPane scrollPane;
    private String path = "C:" + File.separator +"temp"+File.separator;
    private final DefaultTableModel tableModel = (DefaultTableModel) objectsTable.getModel();
    private JMenuBar menuBar = new JMenuBar();

    private DownloadButtonHandler dlHandler;
    private AWSDriver driver;
    private DownloadEvent dlEvent;
    private UploadButtonHandler uplHandler;
    private UploadEvent uplEvent;
    private DeleteEvent delEvent;
    private DeleteEventHandler delHandler;

    public GUI(AmazonS3 s3Client){
        super("AWS File Downloader");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JMenu menu = new JMenu("File");
        JMenuItem menuExit = new JMenuItem("Exit", KeyEvent.VK_X);
        menuExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menu.add(menuExit);
        menuBar.add(menu);
        menuBar.setOpaque(true);
        menuBar.setPreferredSize(new Dimension(200, 20));

        this.driver = new AWSDriver(s3Client);
        this.dlHandler = new DownloadButtonHandler(driver);
        this.uplHandler = new UploadButtonHandler(driver);
        this.delHandler = new DeleteEventHandler(driver);

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
            dlEvent = new DownloadEvent(activeBucket, objectName, file);
                if(dlHandler != null){
                    dlHandler.handleEvent(dlEvent);
                }
            }
        });
        buttonPanel.add(downloadButton);

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                objectsTable.getSelectedRow();
                String objectName = (String)objectsTable.getValueAt(objectsTable.getSelectedRow(), 0);

                //driver.deleteSelectedObject(activeBucket, objectName);
                delEvent = new DeleteEvent(activeBucket, objectName);
                if(delHandler != null){
                    delHandler.handleEvent(delEvent);
                }
            }
        });
        buttonPanel.add(deleteButton);

        uploadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int ret = fileChooser.showDialog(GUI.this, "Choose File");
                if (ret == JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();
                    uplEvent = new UploadEvent(activeBucket, file.getName(), file);
                    if(uplHandler != null){
                        uplHandler.handleEvent(uplEvent);
                    }
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
        setLocationRelativeTo(null);
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
    public void setDlEventHandler(DownloadButtonHandler dlHandler){
        this.dlHandler = dlHandler;
    }

}
