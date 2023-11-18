package soonflyy.learning.hub.model;

public class StudentAttendenceModel {

    String student_id;
    String course_id;
    String teacher_id;
    String image;
    String first_name;
    boolean attendanceStatus=true;
    String attenadnce_value="";
    //-----used in assigned to case----//
    String totalStudent="";
    String present="";
    String absent="";
    //-------------------//

    public String getStudent_id() {
        return student_id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public boolean isAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(boolean attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public String getAttenadnce_value() {
        return attenadnce_value;
    }

    public String getTotalStudent() {
        return totalStudent;
    }

    public void setTotalStudent(String totalStudent) {
        this.totalStudent = totalStudent;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    public String getAbsent() {
        return absent;
    }

    public void setAbsent(String absent) {
        this.absent = absent;
    }

    public void setAttenadnce_value(String attenadnce_value) {
        this.attenadnce_value = attenadnce_value;
    }
}
