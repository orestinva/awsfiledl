package com.inva;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        CredentialsInputWindow window = new CredentialsInputWindow();
        //BasicAWSCredentials credentials = new BasicAWSCredentials("one", "two");
        //AmazonS3 s3Client = new AmazonS3Client(credentials);
        //GUI gui = new GUI(s3Client);

    }
}
