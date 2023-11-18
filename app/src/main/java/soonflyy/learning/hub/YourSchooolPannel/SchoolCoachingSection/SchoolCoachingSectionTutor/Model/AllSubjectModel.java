package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model;

public class AllSubjectModel {
    String subject_id;
    String subject_name;

    String chapter_id;
    String chapter_name;



    public AllSubjectModel() {
    }

    public AllSubjectModel(String subject_id , String subject_name , String chapter_id , String chapter_name) {
        this.subject_id = subject_id;
        this.subject_name = subject_name;
        this.chapter_id = chapter_id;
        this.chapter_name = chapter_name;
    }

    public AllSubjectModel(String subject_id , String subject_name) {
        this.subject_id = subject_id;
        this.subject_name = subject_name;
    }

    @Override
    public String toString(){
        return subject_name;
    }

    public String getChapter_id() {
        return chapter_id;
    }

    public String getChapter_name() {
        return chapter_name;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public void setChapter_id(String chapter_id) {
        this.chapter_id = chapter_id;
    }

    public void setChapter_name(String chapter_name) {
        this.chapter_name = chapter_name;
    }
}

