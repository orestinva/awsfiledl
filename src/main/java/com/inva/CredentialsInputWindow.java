package com.inva;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by inva on 11/10/2016.
 */
public class CredentialsInputWindow  extends JFrame{

    private JPanel panel = new JPanel();
    private JTextField accessKeyField = new JTextField();
    private JTextField secretKeyField = new JTextField();
    private JPanel accessKeyPanel = new JPanel(new FlowLayout());
    private JPanel secretKeyPanel = new JPanel(new FlowLayout());
    private JLabel accessKeyLabel = new JLabel("Access Key ID:");
    private JLabel secretKeyLabel = new JLabel("Secret Key:");
    private JButton okButton = new JButton("Go");
    private String secretKey;
    private String accessKey;

    public CredentialsInputWindow (){
        super("Please provide user credentials");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        accessKeyField.setPreferredSize(new Dimension(200, 25));
        secretKeyField.setPreferredSize(new Dimension(270, 25));
        accessKeyPanel.add(accessKeyLabel);
        accessKeyPanel.add(accessKeyField);
        secretKeyPanel.add(secretKeyLabel);
        secretKeyPanel.add(secretKeyField);

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setCredentials(accessKeyField.getText(), secretKeyField.getText());
                dispose();
                GUI gui = new GUI(new AmazonS3Client(new BasicAWSCredentials(getAccessKey(), getSecretKey())));
            }
        });

        panel.add(accessKeyPanel);
        panel.add(secretKeyPanel);
        panel.add(okButton);

        panel.setLayout(new FlowLayout());
        panel.setPreferredSize(new Dimension(420, 85));
        setContentPane(panel);
        pack();
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
    }
    public void setCredentials(String accessKey, String secretKey){
        // + remove whitespaces
        this.secretKey = secretKey.replaceAll("\\s+","");
        this.accessKey = accessKey.replaceAll("\\s+","");
    }

    public String getAccessKey(){
        return accessKey;
    }

    public String getSecretKey(){
        return secretKey;
    }

}
