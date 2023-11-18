package soonflyy.learning.hub.model;

import soonflyy.learning.hub.Teacher_Pannel.Model.AssignProfile;

public class TeacherSubject_Model {

    String id;
    String title;
    String course_id;
    String course_name;
    String cover_image;
    String status;
    String assign_by_id,assigm_to_id,assign_to_name;

    String assigned_value ="-1";
    AssignProfile assigned_to;
    AssignProfile assigned_by;


    public  TeacherSubject_Model(){

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

    public String getCover_image() {
        return cover_image;
    }

    public String getAssigned_value() {
        return assigned_value;
    }

    public void setAssigned_value(String assigned_value) {
        this.assigned_value = assigned_value;
    }
    //------------just for testing purpose only-----------//

    public void setId(String id) {
        this.id = id;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public AssignProfile getAssigned_to() {
        return assigned_to;
    }

    public AssignProfile getAssigned_by() {
        return assigned_by;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAssigned_to(AssignProfile assigned_to) {
        this.assigned_to = assigned_to;
    }

    public void setAssigned_by(AssignProfile assigned_by) {
        this.assigned_by = assigned_by;
    }

    public String getAssign_by_id() {
        return assign_by_id;
    }

    public void setAssign_by_id(String assign_by_id) {
        this.assign_by_id = assign_by_id;
    }

    public String getAssigm_to_id() {
        return assigm_to_id;
    }

    public void setAssigm_to_id(String assigm_to_id) {
        this.assigm_to_id = assigm_to_id;
    }

    public String getAssign_to_name() {
        return assign_to_name;
    }

    public void setAssign_to_name(String assign_to_name) {
        this.assign_to_name = assign_to_name;
    }
    //-------------------------------------------------//
}
