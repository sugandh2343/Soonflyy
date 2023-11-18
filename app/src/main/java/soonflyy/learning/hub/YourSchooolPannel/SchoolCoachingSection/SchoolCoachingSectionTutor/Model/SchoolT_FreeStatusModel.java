package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model;

public class SchoolT_FreeStatusModel {
    String student_id;
    String student_name;
    String fees_status;
    boolean paidOrunpaid=false;

    public String getStudent_id() {
        return student_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public String getFees_status() {
        return fees_status;
    }

    public boolean isPaidOrunpaid() {
        return paidOrunpaid;
    }

    public void setPaidOrunpaid(boolean paidOrunpaid) {
        this.paidOrunpaid = paidOrunpaid;
    }

    public void setFees_status(String fees_status) {
        this.fees_status = fees_status;
    }
}
