package soonflyy.learning.hub.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TopicsModel implements Parcelable {
    private String topic_id;
    private String topic_title;
    private String summary;
    private String topic_order;
   
    public TopicsModel() {

    }

    public TopicsModel( String topic_title) {
        this.topic_title = topic_title;
    }

    protected TopicsModel(Parcel in) {
        topic_id = in.readString();
        topic_title = in.readString();
        summary = in.readString();
        topic_order = in.readString();
    }

    public static final Creator<TopicsModel> CREATOR = new Creator<TopicsModel>() {
        @Override
        public TopicsModel createFromParcel(Parcel in) {
            return new TopicsModel(in);
        }

        @Override
        public TopicsModel[] newArray(int size) {
            return new TopicsModel[size];
        }
    };

    @Override
    public String toString(){
        return topic_title;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public String getTopic_title() {
        return topic_title;
    }

    public String getSummary() {
        return summary;
    }

    public String getTopic_order() {
        return topic_order;
    }

  

    public void setTopic_title(String topic_title) {
        this.topic_title = topic_title;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(topic_id);
        dest.writeString(topic_title);
        dest.writeString(summary);
        dest.writeString(topic_order);
    }
}
