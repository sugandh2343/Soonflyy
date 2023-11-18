package soonflyy.learning.hub.model;

public class TestCourseofferModel {
    String title;
    String id;
    String thumbnail;
    boolean isSelected=false;

    public TestCourseofferModel() {

    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
