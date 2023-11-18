package soonflyy.learning.hub.studentModel;

import android.os.Parcel;
import android.os.Parcelable;

import soonflyy.learning.hub.model.Chapter;
import soonflyy.learning.hub.model.Video;

import java.util.List;

public class CoursesModel implements Parcelable {
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
    String total_paidvideos;
    String total_demovideos;
    String total_pdf;
    String total_testseries;
    List<Chapter> chapter;
    String is_bookmark;
    String bookmark_id;
    String is_subscribed;


    public CoursesModel() {
    }

    protected CoursesModel(Parcel in) {
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
        is_bookmark = in.readString();
        bookmark_id = in.readString();
        is_subscribed = in.readString();
    }

    public static final Creator<CoursesModel> CREATOR = new Creator<CoursesModel>() {
        @Override
        public CoursesModel createFromParcel(Parcel in) {
            return new CoursesModel(in);
        }

        @Override
        public CoursesModel[] newArray(int size) {
            return new CoursesModel[size];
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

    public String getTotal_paidvideos() {
        return total_paidvideos;
    }

    public String getTotal_demovideos() {
        return total_demovideos;
    }

    public String getTotal_pdf() {
        return total_pdf;
    }

    public String getTotal_testseries() {
        return total_testseries;
    }

    public List<Chapter> getChapter() {
        return chapter;
    }

    public String getIs_bookmark() {
        return is_bookmark;
    }

    public void setIs_bookmark(String is_bookmark) {
        this.is_bookmark = is_bookmark;
    }

    public String getBookmark_id() {
        return bookmark_id;
    }

    public void setBookmark_id(String bookmark_id) {
        this.bookmark_id = bookmark_id;
    }

    public String getIs_subscribed() {
        return is_subscribed;
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
        dest.writeString(is_bookmark);
        dest.writeString(bookmark_id);
        dest.writeString(is_subscribed);
    }
}
