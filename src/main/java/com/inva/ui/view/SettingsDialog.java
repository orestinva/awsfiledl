package com.inva.ui.view;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Properties;

/**
 * Created by inva on 11/14/2016.
 */
public class SettingsDialog extends JDialog {
    private JLabel saveDirLabel = new JLabel("Save to:");
    private JFileChooser saveDirChooser = new JFileChooser("Choose a directory..");
    private JButton chooseDirButton;
    private JPanel panel = new JPanel();
    private JTextField saveDir = new JTextField();
    private JPanel saveDirPanel = new JPanel();
    public static String settingsPath = new File(".").getAbsolutePath();
    //creating file for settings storage
    public static File settings = new File(settingsPath+"config.properties");;

    public SettingsDialog() {
        setTitle("Settings");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        //get props
        final Properties props = getProperties();

        //adding listener for window closing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                dispose();
            }
        });
        saveDir.setPreferredSize(new Dimension(175, 20));
        saveDir.setText(props.getProperty("saveDirectory"));

        //adding button for saveDir chooser
        chooseDirButton = new JButton("...");
        chooseDirButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveDirChooser = new JFileChooser();
                saveDirChooser.setCurrentDirectory(new java.io.File("."));
                saveDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int ret = saveDirChooser.showDialog(SettingsDialog.this, "Choose File");
                if (ret == JFileChooser.APPROVE_OPTION){
                    String newSaveDir = saveDirChooser.getSelectedFile().getAbsolutePath();
                    //setting text to text field
                    saveDir.setText(newSaveDir);
                    //saving settings to settings file
                    props.setProperty("saveDirectory", newSaveDir);
                    System.out.println(props.toString());
                    saveProperties(props);
                }
            }
        });
        saveDirPanel.setLayout(new FlowLayout());
        saveDirPanel.add(saveDirLabel);
        saveDirPanel.add(saveDir);
        saveDirPanel.add(chooseDirButton);

        panel.setLayout(new BorderLayout());
        panel.add(saveDirPanel, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(400, 300));
        setContentPane(panel);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public static String getSaveDir() {
        Properties properties = getProperties();
        return properties.getProperty("saveDirectory");
    }

    public static void initDefaultProperties(){
        try {
            Properties properties = new Properties();
            properties.setProperty("saveDirectory", "C:"+File.separator+"temp"+File.separator);
            FileWriter writer = new FileWriter(settings);
            properties.store(writer, "savedir settings");
            writer.close();
        } catch (FileNotFoundException ex) {
            // file does not exist

        } catch (IOException ex) {
            // I/O error

        }
    }

    public static Properties getProperties(){
        Properties properties = new Properties();
        try {

            FileReader fileReader = new FileReader(settings);
            properties.load(fileReader);
        } catch (FileNotFoundException ex){
            //file doesnt exist
            createPropertiesFile();
        } catch (IOException ex){
            //IO error
        }
        return properties;
    }

    public static File createPropertiesFile(){
        File propertiesFile = new File(settingsPath+"config.properties");
        return propertiesFile;
    }

    public static void saveProperties(Properties properties){
        try {
            FileWriter writer = new FileWriter(settings);
            properties.store(writer, "saved settings");
            writer.close();
        } catch (FileNotFoundException ex) {
            // file does not exist

        } catch (IOException ex) {
            // I/O error

        }
    }
    public static void checkIfPropertiesExist(){
        if(!settings.exists() && !settings.isDirectory()){
            settings = createPropertiesFile();
            initDefaultProperties();
        }
    }

}
