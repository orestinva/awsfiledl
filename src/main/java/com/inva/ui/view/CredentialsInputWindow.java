package com.inva.ui.view;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import java.awt.*;
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
    private JOptionPane optionPane = new JOptionPane();

    public CredentialsInputWindow (){
        super("Please provide user credentials");
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
        accessKeyField.setPreferredSize(new Dimension(200, 20));
        secretKeyField.setPreferredSize(new Dimension(270, 20));
        accessKeyPanel.add(accessKeyLabel);
        accessKeyPanel.add(accessKeyField);
        secretKeyPanel.add(secretKeyLabel);
        secretKeyPanel.add(secretKeyField);
        optionPane.setVisible(false);
        optionPane.add(panel);

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setCredentials(accessKeyField.getText(), secretKeyField.getText());
                try {
                    GUI gui = new GUI(new AmazonS3Client(new BasicAWSCredentials(getAccessKey(), getSecretKey())));
                    dispose();
                } catch (Exception e1){
                    optionPane.showMessageDialog(panel, "Credentials you provided do not exist");
                }


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
