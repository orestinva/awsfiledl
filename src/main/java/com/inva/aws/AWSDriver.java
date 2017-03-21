package com.inva.aws;

import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.inva.ui.controller.GUIController;
import com.inva.ui.view.TaskTableModel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by inva on 10/26/2016.
 */
public class AWSDriver {
    private AmazonS3 s3;
    private TransferManager transferManager;
    private TaskTableModel taskTableModel;
    private GUIController guiController;

    public AWSDriver(AmazonS3 s3, TaskTableModel taskTableModel){
        this.s3 = s3;
        this.taskTableModel = taskTableModel;
        transferManager = new TransferManager(s3);
    }

    public ArrayList<String> getBucketList(){
        List<Bucket> bucketList = s3.listBuckets();
        ArrayList<String> bucketsStringList = new ArrayList<String>();
        for (Bucket b : bucketList){
            bucketsStringList.add(b.getName());
        }
        return bucketsStringList;
    }

    public List<AWSFileDescription> getFileDescriptions (String bucketName){
        ObjectListing listing = s3.listObjects(bucketName);
        List<S3ObjectSummary> summaries = listing.getObjectSummaries();
        List<AWSFileDescription> descriptions = new ArrayList<AWSFileDescription>();
        for(S3ObjectSummary o : summaries){
            boolean isFolder;
            if (o.getKey().endsWith("/")){
                isFolder = true;
            } else {
                isFolder = false;
            }
            AWSFileDescription description = new AWSFileDescription(o.getKey(), isFolder, o.getSize());
            descriptions.add(description);
        }
        return descriptions;
    }

    public void copyTo(String bucket, String key, final File file){
        try {
            final Download myDownload = transferManager.download(bucket, key, file);
            myDownload.addProgressListener(new ProgressListener() {
                public void progressChanged(ProgressEvent progressEvent) {
                    taskTableModel.updateStatus(file, myDownload.getProgress().getPercentTransferred());

                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void uploadTo(String bucket, String key, final File file){
        try {
            final Upload myUpload = transferManager.upload(bucket, key, file);
            myUpload.addProgressListener(new ProgressListener() {
                public void progressChanged(ProgressEvent progressEvent) {
                    taskTableModel.updateStatus(file, myUpload.getProgress().getPercentTransferred());
                    //if(myUpload.getProgress().getPercentTransferred() == 100.00){
                     //   AWSDriver.this.guiController.refreshTable();
                    //}
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void deleteSelectedObject(String bucketName, String keyName){
        try {
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, keyName);
            s3.deleteObject(deleteObjectRequest);
            Thread.sleep(70);
            guiController.refreshTable();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void createFolder (String bucketName, String folderName){
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);
        InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
        try {
            final Upload myUpload = transferManager.upload(bucketName, folderName, emptyContent, metadata);
            Thread.sleep(70);
            guiController.refreshTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setGuiController(GUIController guiController) {
        this.guiController = guiController;
    }

}
