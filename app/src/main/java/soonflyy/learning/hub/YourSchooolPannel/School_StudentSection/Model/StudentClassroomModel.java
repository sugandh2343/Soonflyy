package soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Model;

public class StudentClassroomModel {

    String title;
    int image;

    public StudentClassroomModel() {
    }

    public StudentClassroomModel(String title, int image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
