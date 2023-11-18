package soonflyy.learning.hub.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class VideoUploadUri implements Parcelable  {
    ArrayList<Uri>videoList;

    public VideoUploadUri(ArrayList<Uri> videoList) {
        this.videoList = videoList;
    }

    protected VideoUploadUri(Parcel in) {
        videoList = in.createTypedArrayList(Uri.CREATOR);
    }

    public static final Creator<VideoUploadUri> CREATOR = new Creator<VideoUploadUri>() {
        @Override
        public VideoUploadUri createFromParcel(Parcel in) {
            return new VideoUploadUri(in);
        }

        @Override
        public VideoUploadUri[] newArray(int size) {
            return new VideoUploadUri[size];
        }
    };

    public ArrayList<Uri> getVideoList() {
        return videoList;
    }

    public void setVideoList(ArrayList<Uri> videoList) {
        this.videoList = videoList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(videoList);
    }
}
