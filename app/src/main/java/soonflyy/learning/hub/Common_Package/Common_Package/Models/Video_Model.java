package soonflyy.learning.hub.Common_Package.Common_Package.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Video_Model implements Parcelable {
    String id;
    String title;
    String file;
    String view_status;
    String description;
    String video_thumbnail;

    protected Video_Model(Parcel in) {
        id = in.readString();
        title = in.readString();
        file = in.readString();
        view_status = in.readString();
        description = in.readString();
        video_thumbnail=in.readString();
    }

    public static final Creator<Video_Model> CREATOR = new Creator<Video_Model>() {
        @Override
        public Video_Model createFromParcel(Parcel in) {
            return new Video_Model(in);
        }

        @Override
        public Video_Model[] newArray(int size) {
            return new Video_Model[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getVideo_thumbnail() {
        return video_thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getFile() {
        return file;
    }

    public String getView_status() {
        return view_status;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(file);
        dest.writeString(view_status);
        dest.writeString(description);
        dest.writeString(video_thumbnail);
    }
}
