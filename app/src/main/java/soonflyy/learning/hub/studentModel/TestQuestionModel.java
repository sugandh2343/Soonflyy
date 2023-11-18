package soonflyy.learning.hub.studentModel;

import android.os.Parcel;
import android.os.Parcelable;

public class TestQuestionModel implements Parcelable {
    String id;
    String title;
    String []options;
    String answer;
    String marks;
    String details;
    String is_selected;
    String selected_option;

    public TestQuestionModel() {
    }



    protected TestQuestionModel(Parcel in) {
        id = in.readString();
        title = in.readString();
        options = in.createStringArray();
        answer = in.readString();
        marks = in.readString();
        details = in.readString();
        is_selected = in.readString();
        selected_option = in.readString();
    }

    public static final Creator<TestQuestionModel> CREATOR = new Creator<TestQuestionModel>() {
        @Override
        public TestQuestionModel createFromParcel(Parcel in) {
            return new TestQuestionModel(in);
        }

        @Override
        public TestQuestionModel[] newArray(int size) {
            return new TestQuestionModel[size];
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

    public String getIs_selected() {
        return is_selected;
    }

    public String getSelected_option() {
        return selected_option;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSelected_option(String selected_option) {
        this.selected_option = selected_option;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public void setDetails(String details) {
        this.details = details;
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
        dest.writeString(is_selected);
        dest.writeString(selected_option);
    }
}
