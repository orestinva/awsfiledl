package com.inva;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.transfer.TransferManager;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        BasicAWSCredentials credentials = new BasicAWSCredentials("AKIAIZ5YHQJJCKLBR2WQ", "OBaYw8MtRRKyybUzPf36b9CHvIcxJdO47V+5l6yO");
        AmazonS3 s3Client = new AmazonS3Client(credentials);
        StartGUI startGUI = new StartGUI(s3Client);

    }
}
