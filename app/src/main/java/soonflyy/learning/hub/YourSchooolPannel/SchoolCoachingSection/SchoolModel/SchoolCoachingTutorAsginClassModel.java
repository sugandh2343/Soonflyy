package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolModel;

public class SchoolCoachingTutorAsginClassModel {
    String class_id;
    String class_name;
    String section_id;
    String section_name;
    String subject_id;
    String subject_name;
    String peroid_id;
    String peroid;
    String date;
    String start_time;
    String end_time;
    int parentPosition=-1;
//    ArrayList<String>days;
    String days;

    public int getParentPosition() {
        return parentPosition;
    }

    public void setParentPosition(int parentPosition) {
        this.parentPosition = parentPosition;
    }

    public String getClass_id() {
        return class_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public String getSection_id() {
        return section_id;
    }

    public String getSection_name() {
        return section_name;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public String getPeroid_id() {
        return peroid_id;
    }

    public String getPeroid() {
        return peroid;
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

    public String getDays() {
        return days;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public void setPeroid_id(String peroid_id) {
        this.peroid_id = peroid_id;
    }

    public void setPeroid(String peroid) {
        this.peroid = peroid;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setDays(String days) {
        this.days = days;
    }
}
