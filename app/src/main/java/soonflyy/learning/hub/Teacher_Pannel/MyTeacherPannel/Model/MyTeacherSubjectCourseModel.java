package soonflyy.learning.hub.Teacher_Pannel.MyTeacherPannel.Model;

import java.util.ArrayList;

public class MyTeacherSubjectCourseModel {
    String id;
    String name;
    String mobile;
    String image;
    String course_name;
    ArrayList<String>assignCourses;
    ArrayList<String>assignSubjects;

    ArrayList<AssignCourseSubject>course;
    ArrayList<AssignCourseSubject>subject;

    public MyTeacherSubjectCourseModel() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getImage() {
        return image;
    }

    public MyTeacherSubjectCourseModel(String id , String name , String mobile , String image , String course_name) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.image = image;
        this.course_name = course_name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<String> getAssignCourses() {
        assignCourses=new ArrayList<>();
        for ( int i=0;i<course.size();i++){
            assignCourses.add(course.get(i).course_name);
        }
        return assignCourses;
    }

    public ArrayList<String> getAssignSubjects() {
        assignSubjects=new ArrayList<>();
        for ( int i=0;i<subject.size();i++){
            assignSubjects.add(subject.get(i).subject_name);
        }
        return assignSubjects;
    }

    public ArrayList<AssignCourseSubject> getSubject() {
        return subject;
    }

    public ArrayList<AssignCourseSubject> getCourse() {
        return course;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public void setAssignCourses(ArrayList<String> assignCourses) {
        this.assignCourses = assignCourses;
    }

    public void setAssignSubjects(ArrayList<String> assignSubjects) {
        this.assignSubjects = assignSubjects;
    }

    public void setCourse(ArrayList<AssignCourseSubject> course) {
        this.course = course;
    }

    public void setSubject(ArrayList<AssignCourseSubject> subject) {
        this.subject = subject;
    }

    public  class AssignCourseSubject{
        String course_id;
        String course_name;
        String subject_id;
        String subject_name;

    }
}
