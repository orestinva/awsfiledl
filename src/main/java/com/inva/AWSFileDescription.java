package com.inva;

/**
 * Created by inva on 10/26/2016.
 */
public class AWSFileDescription {


    private String objectName;
    private boolean isFolder;
    private long size;

    public AWSFileDescription(String objectName, boolean isFolder, long size){
        this.objectName = objectName;
        this.isFolder = isFolder;
        this.size = size;
    }

    public String getObjectName() {
        return objectName;
    }

    public String isFolderToStr() {
        String strIsFolder;
        if (isFolder == false){
            strIsFolder = "File";
        } else {
            strIsFolder = "Folder";
        }
        return strIsFolder;
    }

    public String getSizeToStr() {
        String strSize = String.valueOf(size);
        if (size == 0){
            strSize = "";
        }
        return strSize;
    }


}
