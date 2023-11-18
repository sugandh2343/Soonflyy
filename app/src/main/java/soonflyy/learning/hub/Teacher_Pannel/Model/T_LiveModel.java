package soonflyy.learning.hub.Teacher_Pannel.Model;

public class T_LiveModel {
    String live_id;//for school teacher
    String id;
    String user_id;
    String course_id;
    String chapter_id;
    String date;
    String title;
    String description;
    String start_time;
    String end_time;
    String is_live;
    String thumbnail;
    String cover_image;
    String is_demo;
    String slug;

    public String getLive_id() {
        return live_id;
    }

    public String getSlug() {
        return slug;
    }

    public String getId() {
        return id;
    }

    public void setIs_live(String is_live) {
        this.is_live = is_live;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public String getChapter_id() {
        return chapter_id;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getIs_live() {
        return is_live;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getCover_image() {
        return cover_image;
    }

    public String getIs_demo() {
        return is_demo;
    }
}
