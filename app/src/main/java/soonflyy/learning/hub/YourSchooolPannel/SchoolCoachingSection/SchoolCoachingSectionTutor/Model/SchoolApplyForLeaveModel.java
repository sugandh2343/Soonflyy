package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model;

public class SchoolApplyForLeaveModel {
    String id;
    String name;
    String reason_message;

    String date ;
    String leave_status;

    public SchoolApplyForLeaveModel() {
    }

    public SchoolApplyForLeaveModel(String date, String leave_status) {
        this.date = date;
        this.leave_status = leave_status;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getReason_message() {
        return reason_message;
    }

    public String getDate() {
        return date;
    }

    public void setDate_time(String date_time) {
        this.date = date;
    }

    public String getLeave_status() {
        return leave_status;
    }

    public void setLeave_status(String leave_status) {
        this.leave_status = leave_status;
    }
}
