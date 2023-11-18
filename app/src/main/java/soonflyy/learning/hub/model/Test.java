package soonflyy.learning.hub.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Test implements Parcelable {

    String test_id;
    String title;
    String duration;
    String start_time;
    String end_time;
    String date;
    String is_paid;
    String amount;
    String user_id;
    String test_description;
    String test_icon;
    String attempted;
    String status;
    List<Question> questions;
    String passing_mark;
    String total_mark;
    String total_selected_question;

    public Test() {
    }


    protected Test(Parcel in) {
        test_id = in.readString();
        title = in.readString();
        duration = in.readString();
        start_time = in.readString();
        end_time = in.readString();
        date = in.readString();
        is_paid = in.readString();
        amount = in.readString();
        user_id = in.readString();
        test_description = in.readString();
        test_icon = in.readString();
        attempted=in.readString();
        status = in.readString();
        passing_mark=in.readString();
        total_mark=in.readString();
        total_selected_question=in.readString();
        questions = in.createTypedArrayList(Question.CREATOR);
    }

    public static final Creator<Test> CREATOR = new Creator<Test>() {
        @Override
        public Test createFromParcel(Parcel in) {
            return new Test(in);
        }

        @Override
        public Test[] newArray(int size) {
            return new Test[size];
        }
    };

    public String getTest_id() {
        return test_id;
    }

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return duration;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getDate() {
        return date;
    }

    public String getIs_paid() {
        return is_paid;
    }

    public String getAmount() {
        return amount;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getTest_description() {
        return test_description;
    }

    public String getTest_icon() {
        return test_icon;
    }

    public String getStatus() {
        return status;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public String getPassing_mark() {
        return passing_mark;
    }

    public String getTotal_mark() {
        return total_mark;
    }

    public String getTotal_selected_question() {
        return total_selected_question;
    }

    public String getAttempted() {
        return attempted;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(test_id);
        dest.writeString(title);
        dest.writeString(duration);
        dest.writeString(start_time);
        dest.writeString(end_time);
        dest.writeString(date);
        dest.writeString(is_paid);
        dest.writeString(amount);
        dest.writeString(user_id);
        dest.writeString(test_description);
        dest.writeString(test_icon);
        dest.writeString(attempted);
        dest.writeString(status);
        dest.writeString(passing_mark);
        dest.writeString(total_mark);
        dest.writeString(total_selected_question);
        dest.writeTypedList(questions);
    }
}
