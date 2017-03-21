package com.inva.ui.view;

import com.amazonaws.services.s3.AmazonS3;
import com.inva.ui.controller.GUIController;
import com.inva.ui.events.Event;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

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
    private JButton createFolderButton = new JButton("Create Folder..");
    private JComboBox bucketsDropDown = new JComboBox();
    private JTable objectsTable = new JTable();
    private JTable taskTable = new JTable();
    private JPanel bucketsPanel = new JPanel(new FlowLayout());
    private JLabel bucketLabel = new JLabel("Select bucket:");
    private String activeBucket;
    private JTabbedPane tabbedPane = new JTabbedPane();
    private TaskTableModel taskTableModel = new TaskTableModel();
    private JScrollPane scrollPane;
    private JScrollPane taskScrollPane;
    private final DefaultTableModel tableModel = (DefaultTableModel) objectsTable.getModel();
    private JMenuBar menuBar = new JMenuBar();

    private JOptionPane optionPane = new JOptionPane();

    private SettingsDialog settingsDialog;
    private GUIController guiController;

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

        optionPane.setVisible(false);
        objPanel.add(optionPane);

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

        //Configuring and enabling menu bar
        menuBar.setOpaque(true);
        menuBar.setPreferredSize(new Dimension(200, 20));

        configureTable();

        //Adding drop-down list with bucketsDropDown
        bucketsDropDown.setPreferredSize(new Dimension(200, 20));

        //Adding GUIcontroller
        guiController = new GUIController(s3Client, this);

        bucketsDropDown.setSelectedIndex(-1);
        //Adding a listener to update table with objects when bucket is chosen from drop-down list
        bucketsDropDown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeBucket = bucketsDropDown.getSelectedItem().toString();

                //drawing table
                guiController.refreshTable();

            }
        });
        bucketsPanel.add(bucketLabel);
        bucketsPanel.add(bucketsDropDown);

        //Adding drop-down and its label to root objPanel
        objPanel.add(bucketsPanel, BorderLayout.PAGE_START);

        // All buttons are disabled until some bucket is chosen (in listener)
        disableButtons();

        //Adding a listener to download a file when downloadButton is pressed
        downloadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Event downloadEvent = new Event("Download", Event.Type.BUTTON);
                    handleEvent(downloadEvent);
                } catch (Exception e1){
                    JOptionPane.showMessageDialog(objPanel, "Please select an object in table to download");
                }
            }
        });
        buttonPanel.add(downloadButton);

        //Adding a listener to delete an object when deleteButton is pressed
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Event deleteEvent = new Event("Delete", Event.Type.BUTTON);
                    handleEvent(deleteEvent);
                } catch (Exception e1){
                    JOptionPane.showMessageDialog(objPanel, "Please select an object in table to delete");
                }

            }
        });
        buttonPanel.add(deleteButton);

        //Adding a listener to upload a file when uploadButton is pressed and file chosen
        uploadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Event uploadEvent = new Event("Upload", Event.Type.BUTTON);
                handleEvent(uploadEvent);
            }
        });
        buttonPanel.add(uploadButton);

        createFolderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Event createFolderEvent = new Event("MkFolder", Event.Type.BUTTON);
                handleEvent(createFolderEvent);
            }
        });

        buttonPanel.add(createFolderButton);
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
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
    }

    public void enableButtons(){
        downloadButton.setEnabled(true);
        uploadButton.setEnabled(true);
        deleteButton.setEnabled(true);
        createFolderButton.setEnabled(true);
    }
    public void disableButtons(){
        downloadButton.setEnabled(false);
        uploadButton.setEnabled(false);
        deleteButton.setEnabled(false);
        createFolderButton.setEnabled(false);
    }

    public void handleEvent(Event event){
        guiController.handleEvent(event);
    }

    public void configureTable(){
        taskTable.setModel(taskTableModel);
        taskTable.getColumn("Status").setCellRenderer(new TaskTableProgressCellRenderer());

        //Adding header to table
        String[] columns = {"Name", "Size", "Type"};
        tableModel.setColumnIdentifiers(columns);

        // Configuring table
        objectsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        objectsTable.setModel(tableModel);
        objectsTable.getTableHeader().setReorderingAllowed(false);
        objectsTable.getTableHeader().setResizingAllowed(true);
        objectsTable.setDefaultEditor(Object.class, null);
        scrollPane = new JScrollPane(objectsTable);
        objPanel.add(scrollPane, BorderLayout.CENTER);
    }

    public JTable getTaskTable() {
        return taskTable;
    }

    public JComboBox getBucketsDropDown() {
        return bucketsDropDown;
    }

    public String getActiveBucket() {
        return activeBucket;
    }

    public JTable getObjectsTable() {
        return objectsTable;
    }

    public SettingsDialog getSettingsDialog() {
        return settingsDialog;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public TaskTableModel getTaskTableModel() {
        return taskTableModel;
    }

    public JOptionPane getOptionPane() {
        return optionPane;
    }

}
