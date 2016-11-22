package com.inva.ui.view;

import com.amazonaws.services.s3.AmazonS3;
import com.inva.aws.AWSDriver;
import com.inva.ui.controller.DeleteButtonHandler;
import com.inva.ui.controller.DownloadButtonHandler;
import com.inva.ui.controller.UploadButtonHandler;
import com.inva.ui.events.DeleteEvent;
import com.inva.ui.events.DownloadEvent;
import com.inva.ui.events.UploadEvent;

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

    private JPanel objPanel = new JPanel(new BorderLayout());
    private JPanel taskPanel = new JPanel(new FlowLayout());
    private JPanel buttonPanel = new JPanel(new FlowLayout());
    private JButton downloadButton = new JButton("Download");
    private JButton deleteButton = new JButton("Delete");
    private JButton uploadButton = new JButton("Upload..");
    private JComboBox buckets = new JComboBox();
    private JTable objectsTable = new JTable();
    private JTable taskTable = new JTable();
    private JPanel bucketsPanel = new JPanel(new FlowLayout());
    private JLabel bucketLabel = new JLabel("Select bucket:");
    private String activeBucket;
    private JTabbedPane tabbedPane = new JTabbedPane();
    private TaskTableModel taskTableModel;
    private JScrollPane scrollPane;
    private JScrollPane taskScrollPane;
    private String path;
    private final DefaultTableModel tableModel = (DefaultTableModel) objectsTable.getModel();
    private JMenuBar menuBar = new JMenuBar();
    private DownloadButtonHandler dlHandler;
    private AWSDriver driver;
    private DownloadEvent dlEvent;
    private UploadButtonHandler uplHandler;
    private UploadEvent uplEvent;
    private DeleteEvent delEvent;
    private DeleteButtonHandler delHandler;
    private SettingsDialog settingsDialog;

    public GUI(AmazonS3 s3Client){
        super("AWS File Downloader");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //check if properties exist, if no - set default
        settingsDialog.checkIfPropertiesExist();

        //Adding file menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem menuExit = new JMenuItem("Exit", KeyEvent.VK_X);
        menuExit.addActionListener(new ActionListener() {
            //Exit when this menu chosen
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(menuExit);
        menuBar.add(fileMenu);

        //Adding view menu
        JMenu viewMenu = new JMenu("View");
        JMenuItem menuSettings = new JMenuItem("Settings", KeyEvent.VK_S);

        //Adding a listener to open settings dialog when menu is pressed
        menuSettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                settingsDialog = new SettingsDialog();
            }
        });
        viewMenu.add(menuSettings);
        menuBar.add(viewMenu);

        //adding tasktablemodel
        taskTableModel = new TaskTableModel();
        taskTable.setModel(taskTableModel);
        taskTable.getColumn("Status").setCellRenderer(new TaskTableProgressCellRenderer());

        //Configuring and enabling menu bar
        menuBar.setOpaque(true);
        menuBar.setPreferredSize(new Dimension(200, 20));


        //Adding driver and handlers
        this.driver = new AWSDriver(s3Client, taskTableModel);
        this.dlHandler = new DownloadButtonHandler(driver, this);
        this.uplHandler = new UploadButtonHandler(driver, this);
        this.delHandler = new DeleteButtonHandler(driver);

        //Adding header to table
        String[] columns = {"Name", "Size", "Type"};
        tableModel.setColumnIdentifiers(columns);

        //Configuring table
        objectsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        objectsTable.setModel(tableModel);
        objectsTable.getTableHeader().setReorderingAllowed(false);
        objectsTable.getTableHeader().setResizingAllowed(true);
        scrollPane = new JScrollPane(objectsTable);
        objPanel.add(scrollPane, BorderLayout.CENTER);

        //Adding drop-down list with buckets
        buckets.setPreferredSize(new Dimension(200, 20));
        ArrayList<String> bucketsList = driver.getBucketList();
        for (String b : bucketsList){
            buckets.addItem(b);
        }
        buckets.setSelectedIndex(-1);

        //Adding a listener to update table with objects when bucket is chosen from drop-down list
        buckets.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeBucket = buckets.getSelectedItem().toString();

                //drawing table
                refreshTable();

                //enabling buttons
                enableButtons();
            }
        });
        bucketsPanel.add(bucketLabel);
        bucketsPanel.add(buckets);

        //Adding drop-down and its label to root objPanel
        objPanel.add(bucketsPanel, BorderLayout.PAGE_START);

        // All buttons are disabled until some bucket is chosen (in listener)
        disableButtons();

        //Adding a listener to download a file when downloadButton is pressed
        downloadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            objectsTable.getSelectedRow();
            String objectName = (String)objectsTable.getValueAt(objectsTable.getSelectedRow(), 0);
                //Getting path to download to
                path = settingsDialog.getSaveDir();
            File file = new File(path+File.separator+objectName);
            dlEvent = new DownloadEvent(activeBucket, objectName, file);
                if(dlHandler != null){
                    dlHandler.handleEvent(dlEvent);
                }

            }
        });
        buttonPanel.add(downloadButton);

        //Adding a listener to delete an object when deleteButton is pressed
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                objectsTable.getSelectedRow();
                String objectName = (String)objectsTable.getValueAt(objectsTable.getSelectedRow(), 0);

                delEvent = new DeleteEvent(activeBucket, objectName);
                if(delHandler != null){
                    delHandler.handleEvent(delEvent);
                }

            }
        });
        buttonPanel.add(deleteButton);

        //Adding a listener to upload a file when uploadButton is pressed and file chosen
        uploadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Making filechooser dialog
                JFileChooser fileChooser = new JFileChooser();
                int ret = fileChooser.showDialog(GUI.this, "Choose File");
                if (ret == JFileChooser.APPROVE_OPTION){
                    //uploading selected file
                    File file = fileChooser.getSelectedFile();
                    uplEvent = new UploadEvent(activeBucket, file.getName(), file);
                    if(uplHandler != null){
                        uplHandler.handleEvent(uplEvent);
                    }
                }
            }
        });
        buttonPanel.add(uploadButton);
        objPanel.add(scrollPane, BorderLayout.CENTER);

        objPanel.add(buttonPanel, BorderLayout.PAGE_END);

        //Configuring root objPanel
        objPanel.setPreferredSize(new Dimension(640, 480));
        tabbedPane.add(objPanel);
        tabbedPane.setTitleAt(0, "Objects");
        tabbedPane.add(taskPanel);

        taskScrollPane = new JScrollPane(taskTable);
        taskPanel.add(taskScrollPane);
        tabbedPane.add(taskPanel);
        tabbedPane.setTitleAt(1, "Tasks");
        tabbedPane.setVisible(true);
        setContentPane(tabbedPane);
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
        (new TableRefresher(driver, activeBucket, tableModel, this)).execute();
        tableModel.fireTableDataChanged();
        objectsTable.setModel(tableModel);
    }
    public void refreshTaskTable(File file){
        (new TaskTableRefresher(taskTableModel, file)).execute();
        taskTableModel.fireTableDataChanged();
        taskTable.setModel(taskTableModel);
    }

    public void enableButtons(){
        downloadButton.setEnabled(true);
        uploadButton.setEnabled(true);
        deleteButton.setEnabled(true);
    }
    public void disableButtons(){
        downloadButton.setEnabled(false);
        uploadButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }
}
