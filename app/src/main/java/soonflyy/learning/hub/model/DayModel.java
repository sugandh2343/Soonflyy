package soonflyy.learning.hub.model;

public class DayModel {
    String id;
    String name;
    boolean isSelected=false;


    public DayModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public DayModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
