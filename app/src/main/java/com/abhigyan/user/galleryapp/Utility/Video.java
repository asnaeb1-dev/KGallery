package com.abhigyan.user.galleryapp.Utility;

public class Video {

    private String videoName;
    private String videoID;
    private String videoData;
    private String videoSize;
    private String videoDate;
    private String videoRes;
    private String videoUniqueName;

    public Video(String videoName, String videoID, String videoData, String videoSize, String videoDate, String videoRes, String videoUniqueName) {
        this.videoName = videoName;
        this.videoID = videoID;
        this.videoData = videoData;
        this.videoSize = videoSize;
        this.videoDate = videoDate;
        this.videoRes = videoRes;
        this.videoUniqueName = videoUniqueName;
    }

    public String getVideoName() {
        return videoName;
    }

    public String getVideoID() {
        return videoID;
    }

    public String getVideoData() {
        return videoData;
    }

    public String getVideoSize() {
        return videoSize;
    }

    public String getVideoDate() {
        return videoDate;
    }

    public String getVideoRes() {
        return videoRes;
    }

    public String getVideoUniqueName() {
        return videoUniqueName;
    }
}
