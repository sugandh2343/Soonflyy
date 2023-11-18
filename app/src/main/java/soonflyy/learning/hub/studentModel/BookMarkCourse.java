package soonflyy.learning.hub.studentModel;

import android.os.Parcel;
import android.os.Parcelable;

import soonflyy.learning.hub.model.Chapter;
import soonflyy.learning.hub.model.Video;

import java.util.List;

public class BookMarkCourse implements Parcelable {

    String course_id;
    String title;
    String decription;
    String price;
    String teacher_id;
    String teacher_name;
    List<Video> demo_video;
    String is_free_course;
    String thumbnail;
    String status;
    String validity;
    List<Chapter>chapter;
    String total_paidvideos;
    String total_demovideos;
    String total_pdf;
    String total_testseries;
    String expiry_date;
    String is_expired;
    String is_subscribed;


    public BookMarkCourse() {
    }

    protected BookMarkCourse(Parcel in) {
        course_id = in.readString();
        title = in.readString();
        decription = in.readString();
        price = in.readString();
        teacher_id = in.readString();
        teacher_name = in.readString();
        is_free_course = in.readString();
        thumbnail = in.readString();
        status = in.readString();
        validity = in.readString();
        total_paidvideos = in.readString();
        total_demovideos = in.readString();
        total_pdf = in.readString();
        total_testseries = in.readString();
        expiry_date=in.readString();
        is_expired=in.readString();
        is_subscribed=in.readString();

    }

    public static final Creator<BookMarkCourse> CREATOR = new Creator<BookMarkCourse>() {
        @Override
        public BookMarkCourse createFromParcel(Parcel in) {
            return new BookMarkCourse(in);
        }

        @Override
        public BookMarkCourse[] newArray(int size) {
            return new BookMarkCourse[size];
        }
    };

    public String getCourse_id() {
        return course_id;
    }

    public String getTitle() {
        return title;
    }

    public String getDecription() {
        return decription;
    }

    public String getPrice() {
        return price;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public List<Video> getDemo_video() {
        return demo_video;
    }

    public String getIs_free_course() {
        return is_free_course;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getStatus() {
        return status;
    }

    public String getValidity() {
        return validity;
    }

    public List<Chapter> getChapter() {
        return chapter;
    }

    public String getTotal_paidvideos() {
        return total_paidvideos;
    }

    public String getTotal_demovideos() {
        return total_demovideos;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public String getIs_expired() {
        return is_expired;
    }

    public String getIs_subscribed() {
        return is_subscribed;
    }

    public String getTotal_pdf() {
        return total_pdf;
    }

    public String getTotal_testseries() {
        return total_testseries;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(course_id);
        dest.writeString(title);
        dest.writeString(decription);
        dest.writeString(price);
        dest.writeString(teacher_id);
        dest.writeString(teacher_name);
        dest.writeString(is_free_course);
        dest.writeString(thumbnail);
        dest.writeString(status);
        dest.writeString(validity);
        dest.writeString(total_paidvideos);
        dest.writeString(total_demovideos);
        dest.writeString(total_pdf);
        dest.writeString(total_testseries);
        dest.writeString(expiry_date);
        dest.writeString(is_expired);
        dest.writeString(is_subscribed);
    }
}
