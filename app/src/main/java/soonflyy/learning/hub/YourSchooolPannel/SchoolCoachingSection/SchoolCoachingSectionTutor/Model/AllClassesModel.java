package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model;

public class AllClassesModel {
    String class_id;
    String name;
    String section_id;

    public AllClassesModel() {
    }

    public AllClassesModel(String class_id , String name , String section_id) {
        this.class_id = class_id;
        this.name = name;
        this.section_id = section_id;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }
}
