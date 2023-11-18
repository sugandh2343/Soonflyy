package soonflyy.learning.hub.Download;

import android.os.Parcel;
import android.os.Parcelable;

public class DownloadVideo implements Parcelable {
    String id;
    String title;
    String displayName;
    String size;
    String duration;
    String path;
    String dateAdded;

    public DownloadVideo(String id, String title, String displayName, String size, String duration,
                         String path, String dateAdded) {
        this.id = id;
        this.title = title;
        this.displayName = displayName;
        this.size = size;
        this.duration = duration;
        this.path = path;
        this.dateAdded = dateAdded;
    }

    public DownloadVideo(String title, String path) {
        this.title = title;
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSize() {
        return size;
    }

    public String getDuration() {
        return duration;
    }

    public String getPath() {
        return path;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    protected DownloadVideo(Parcel in) {
        id = in.readString();
        title = in.readString();
        displayName = in.readString();
        size = in.readString();
        duration = in.readString();
        path = in.readString();
        dateAdded = in.readString();
    }

    public static final Creator<DownloadVideo> CREATOR = new Creator<DownloadVideo>() {
        @Override
        public DownloadVideo createFromParcel(Parcel in) {
            return new DownloadVideo(in);
        }

        @Override
        public DownloadVideo[] newArray(int size) {
            return new DownloadVideo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(displayName);
        parcel.writeString(size);
        parcel.writeString(duration);
        parcel.writeString(path);
        parcel.writeString(dateAdded);
    }
}
