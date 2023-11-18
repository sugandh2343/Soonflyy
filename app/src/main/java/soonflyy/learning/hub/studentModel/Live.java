package soonflyy.learning.hub.studentModel;

import java.io.Serializable;

public class Live implements Serializable {
    String id;
    String course_id;
    String user_id;
    String chapter_id;
    String title;
    String topic_name;
    String date;
    String start_time;
    String end_time;
    String is_live;
    String thumbnail;

    String teacher_id;
    String teacher_name;
    String course_name;
    String chapter_name;
    String topic_id;
    String slug;
    String cover_image;
    String token;
    String meeting_id;
    String description;

    public String getCover_image() {
        return cover_image;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getTitle() {
        return title;
    }

    public String getSlug() {
        return slug;
    }

    public String getId() {
        return id;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public String getCourse_id() {
        return course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public String getChapter_id() {
        return chapter_id;
    }

    public String getChapter_name() {
        return chapter_name;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public String getTopic_name() {
        return topic_name;
    }

    public String getDate() {
        return date;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setIs_live(String is_live) {
        this.is_live = is_live;
    }

    public String getIs_live() {
        return is_live;
    }

    public String getToken() {
        return token;
    }

    public String getMeeting_id() {
        return meeting_id;
    }

    public String getDescription() {
        return description;
    }
}
