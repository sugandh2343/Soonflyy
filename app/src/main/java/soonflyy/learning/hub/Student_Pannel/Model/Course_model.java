package soonflyy.learning.hub.Student_Pannel.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Course_model implements Parcelable {
    String id;
    String title;
    String thumbnail;
    String instituteName;
    String name;
    String rating;
    String is_purchased;

    protected Course_model(Parcel in) {
        id = in.readString();
        title = in.readString();
        thumbnail = in.readString();
        instituteName = in.readString();
        name = in.readString();
        is_purchased=in.readString();
    }

    public static final Creator<Course_model> CREATOR = new Creator<Course_model>() {
        @Override
        public Course_model createFromParcel(Parcel in) {
            return new Course_model(in);
        }

        @Override
        public Course_model[] newArray(int size) {
            return new Course_model[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(thumbnail);
        dest.writeString(instituteName);
        dest.writeString(name);
        dest.writeString(is_purchased);
    }
    public String getRating() {
        return rating;
    }

    public String getIs_purchased() {
        return is_purchased;
    }
}
