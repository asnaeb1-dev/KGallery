package com.abhigyan.user.galleryapp.Utility;

public class Image {
    private String imageName;
    private String uniqueName;
    private String imageSize;
    private String imageData;
    private String imageDate;

    public Image(String imageName, String imageSize, String imageData, String imageDate, String uniqueName) {
        this.imageName = imageName;
        this.imageSize = imageSize;
        this.imageData = imageData;
        this.imageDate = imageDate;
        this.uniqueName = uniqueName;
    }

    public String getImageName() {
        return imageName;
    }

    public String getImageSize() {
        return imageSize;
    }

    public String getImageData() {
        return imageData;
    }

    public String getImageDate() {
        return imageDate;
    }

    public String getUniqueName(){
        return uniqueName;
    }
}
