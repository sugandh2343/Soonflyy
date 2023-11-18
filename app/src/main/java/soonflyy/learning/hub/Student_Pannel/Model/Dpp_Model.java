package soonflyy.learning.hub.Student_Pannel.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Dpp_Model implements Parcelable  {
    String id;
    String topic;
    String paper;
    String date;
    String section_id;
    String course_id;
    String lesson_id;
    String status;

    protected Dpp_Model(Parcel in) {
        id = in.readString();
        topic = in.readString();
        paper = in.readString();
        date = in.readString();
        section_id = in.readString();
        course_id = in.readString();
        lesson_id = in.readString();
        status=in.readString();
    }

    public static final Creator<Dpp_Model> CREATOR = new Creator<Dpp_Model>() {
        @Override
        public Dpp_Model createFromParcel(Parcel in) {
            return new Dpp_Model(in);
        }

        @Override
        public Dpp_Model[] newArray(int size) {
            return new Dpp_Model[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public String getPaper() {
        return paper;
    }

    public String getDate() {
        return date;
    }

    public String getSection_id() {
        return section_id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public String getLesson_id() {
        return lesson_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(topic);
        dest.writeString(paper);
        dest.writeString(date);
        dest.writeString(section_id);
        dest.writeString(course_id);
        dest.writeString(lesson_id);
        dest.writeString(status);
    }
}
