package soonflyy.learning.hub.studentModel;

public class Category_student_model {
    String id;
    String code;
    String name;
    String thumbnail;

    boolean isSelected=false;

    public Category_student_model(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getThumbnail() {
        return thumbnail;
    }


}


