package soonflyy.learning.hub.studentModel;

import android.os.Parcel;
import android.os.Parcelable;

public class SubscribedCourse implements Parcelable {

    String enrol_id;
    String course_id;
    String title;
    String decription;
    String price;
    String teacher_id;
    String teacher_name;
    String teacher_image;
    String is_free_course;
    String thumbnail;
    String status;
    String validity;
    String start_date;
    String expiry_date;
    String screenshot_enable;
    String rating_value="0";
    //List<Chapter>chapter;
    //List<Video>demo_video;


    public SubscribedCourse() {
    }

    public SubscribedCourse(String title) {
        this.title = title;
    }

    protected SubscribedCourse(Parcel in) {
        enrol_id = in.readString();
        course_id = in.readString();
        title = in.readString();
        decription = in.readString();
        price = in.readString();
        teacher_id = in.readString();
        teacher_name = in.readString();
        teacher_image=in.readString();
        is_free_course = in.readString();
        thumbnail = in.readString();
        status = in.readString();
        validity = in.readString();
        start_date = in.readString();
        expiry_date = in.readString();
        screenshot_enable = in.readString();
        rating_value=in.readString();
    }

    public static final Creator<SubscribedCourse> CREATOR = new Creator<SubscribedCourse>() {
        @Override
        public SubscribedCourse createFromParcel(Parcel in) {
            return new SubscribedCourse(in);
        }

        @Override
        public SubscribedCourse[] newArray(int size) {
            return new SubscribedCourse[size];
        }
    };

    public String getRating_value() {
        return rating_value;
    }

    public void setRating_value(String rating_value) {
        this.rating_value = rating_value;
    }

    public String getScreenshot_enable() {
        return screenshot_enable;
    }

    public String getEnrol_id() {
        return enrol_id;
    }

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

    public String getStart_date() {
        return start_date;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

//  //  public List<Chapter> getChapter() {
//        return chapter;
//    }
//
//    public List<Video> getDemo_video() {
//        return demo_video;
//    }

    public String getTeacher_image() {
        return teacher_image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(enrol_id);
        dest.writeString(course_id);
        dest.writeString(title);
        dest.writeString(decription);
        dest.writeString(price);
        dest.writeString(teacher_id);
        dest.writeString(teacher_name);
        dest.writeString(teacher_image);
        dest.writeString(is_free_course);
        dest.writeString(thumbnail);
        dest.writeString(status);
        dest.writeString(validity);
        dest.writeString(start_date);
        dest.writeString(expiry_date);
        dest.writeString(screenshot_enable);
        dest.writeString(rating_value);
    }
    public String toString(){
        return title;
    }

}
