package com.inva.aws;

import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.inva.ui.view.TaskTableModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by inva on 10/26/2016.
 */
public class AWSDriver {
    private AmazonS3 s3;
    private TransferManager transferManager;
    private TaskTableModel taskTableModel;

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
            boolean isFolder = false;
            if (o.getKey().endsWith("/")){
                isFolder = true;
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
                    System.out.println("aaa");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void deleteSelectedObject(String bucketName, String keyName){
        try {
            s3.deleteObject(new DeleteObjectRequest(bucketName, keyName));
        } catch (Exception e){
            e.printStackTrace();
        }

    }

}
