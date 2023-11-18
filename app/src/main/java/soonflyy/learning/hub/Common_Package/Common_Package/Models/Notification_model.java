package soonflyy.learning.hub.Common_Package.Common_Package.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Notification_model implements Parcelable {
    String id;
    String title;

    protected Notification_model(Parcel in) {
        id = in.readString();
        title = in.readString();
    }

    public static final Creator<Notification_model> CREATOR = new Creator<Notification_model>() {
        @Override
        public Notification_model createFromParcel(Parcel in) {
            return new Notification_model(in);
        }

        @Override
        public Notification_model[] newArray(int size) {
            return new Notification_model[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
    }
}
