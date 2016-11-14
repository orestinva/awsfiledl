package com.inva.ui.view;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Paths;

/**
 * Created by inva on 11/14/2016.
 */
public class SettingsDialog extends JDialog {
    private JLabel saveDirLabel;
    private JFileChooser saveDirChooser = new JFileChooser("Choose a directory..");
    private JButton chooseDirButton;
    private JPanel panel = new JPanel();
    private JButton applyChangesButton;
    private JTextField saveDir = new JTextField("Choose a directory");
    private JPanel saveDirPanel = new JPanel();

    public SettingsDialog() {
        setTitle("Settings");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                dispose();
            }
        });
        saveDirLabel = new JLabel("Save to:");
        saveDir.setPreferredSize(new Dimension(175, 20));

        chooseDirButton = new JButton("...");
        chooseDirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDirChooser = new JFileChooser();
                saveDirChooser.setCurrentDirectory(new java.io.File("."));
                saveDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int ret = saveDirChooser.showDialog(SettingsDialog.this, "Choose File");
                if (ret == JFileChooser.APPROVE_OPTION){
                    File file = saveDirChooser.getSelectedFile();
                    saveDir.setText(file.getPath());
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

    public String getSaveDir() {
        return saveDir.getText();
    }

}
