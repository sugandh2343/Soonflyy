package soonflyy.learning.hub.Teacher_Pannel.Model;

public class T_ChapterModel {

    String id;
    String title;
    String course_id;
    String section_id;

    public T_ChapterModel() {
    }

    public T_ChapterModel(String id , String title , String course_id , String section_id) {
        this.id = id;
        this.title = title;
        this.course_id = course_id;
        this.section_id = section_id;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCourse_id() {
        return course_id;
    }

    public String getSection_id() {
        return section_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }
}
