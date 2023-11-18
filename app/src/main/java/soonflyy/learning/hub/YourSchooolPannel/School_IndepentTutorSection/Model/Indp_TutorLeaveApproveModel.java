package soonflyy.learning.hub.YourSchooolPannel.School_IndepentTutorSection.Model;

import androidx.annotation.NonNull;

public class Indp_TutorLeaveApproveModel {
    String StudentName;
    String StudentClass;
    String Section;
    String reason;
    String Date_Time;
    String Action;

    public Indp_TutorLeaveApproveModel() {
    }

    public Indp_TutorLeaveApproveModel(String studentName, String StudentClass, String section, String reason, String date_Time, String action) {
        StudentName = studentName;
        StudentClass = StudentClass;
        Section = section;
        this.reason = reason;
        Date_Time = date_Time;
        Action = action;
    }

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String studentName) {
        StudentName = studentName;
    }

    @NonNull

    public String getStudentClass() {
        return StudentClass;
    }

    public void setStudentClass(String StudentClass) {
        StudentClass = StudentClass;
    }

    public String getSection() {
        return Section;
    }

    public void setSection(String section) {
        Section = section;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDate_Time() {
        return Date_Time;
    }

    public void setDate_Time(String date_Time) {
        Date_Time = date_Time;
    }

    public String getAction() {
        return Action;
    }

    public void setAction(String action) {
        Action = action;
    }
}
