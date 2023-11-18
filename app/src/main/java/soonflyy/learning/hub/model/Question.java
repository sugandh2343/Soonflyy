package soonflyy.learning.hub.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {
    String id;
    String title;
    String []options;
    String answer;
    String marks;
    String details;
   boolean is_selected;

    public Question() {
    }


    protected Question(Parcel in) {
        id = in.readString();
        title = in.readString();
        options = in.createStringArray();
        answer = in.readString();
        marks = in.readString();
        details = in.readString();
        is_selected = in.readByte() != 0;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String[] getOptions() {
        return options;
    }

    public String getAnswer() {
        return answer;
    }

    public String getMarks() {
        return marks;
    }

    public String getDetails() {
        return details;
    }

    public boolean isIs_selected() {
        return is_selected;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeStringArray(options);
        dest.writeString(answer);
        dest.writeString(marks);
        dest.writeString(details);
        dest.writeByte((byte) (is_selected ? 1 : 0));
    }
}
