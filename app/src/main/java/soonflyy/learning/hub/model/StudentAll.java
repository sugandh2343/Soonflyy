package soonflyy.learning.hub.model;

import android.os.Parcel;
import android.os.Parcelable;

public class StudentAll implements Parcelable {
    String course_id;
    String title;
    String user_id;
    String first_name;
    String expiry_date;
    String is_expired;
    String user_image;

    public StudentAll() {
    }

    protected StudentAll(Parcel in) {
        course_id = in.readString();
        title = in.readString();
        user_id = in.readString();
        first_name = in.readString();
        expiry_date = in.readString();
        is_expired = in.readString();
        user_image = in.readString();
    }

    public static final Creator<StudentAll> CREATOR = new Creator<StudentAll>() {
        @Override
        public StudentAll createFromParcel(Parcel in) {
            return new StudentAll(in);
        }

        @Override
        public StudentAll[] newArray(int size) {
            return new StudentAll[size];
        }
    };

    public String getCourse_id() {
        return course_id;
    }

    public String getTitle() {
        return title;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public String getIs_expired() {
        return is_expired;
    }

    public String getUser_image() {
        return user_image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(course_id);
        dest.writeString(title);
        dest.writeString(user_id);
        dest.writeString(first_name);
        dest.writeString(expiry_date);
        dest.writeString(is_expired);
        dest.writeString(user_image);
    }
}
