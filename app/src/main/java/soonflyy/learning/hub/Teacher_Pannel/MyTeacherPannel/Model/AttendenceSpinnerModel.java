package soonflyy.learning.hub.Teacher_Pannel.MyTeacherPannel.Model;

public class AttendenceSpinnerModel {
    String title;
    boolean checked=false;

    public AttendenceSpinnerModel(String title, boolean checked) {
        this.title = title;
        this.checked = checked;
    }

    public AttendenceSpinnerModel(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
