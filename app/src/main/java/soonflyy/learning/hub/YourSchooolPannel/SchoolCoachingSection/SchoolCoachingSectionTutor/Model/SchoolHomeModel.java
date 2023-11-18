package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model;

public class SchoolHomeModel {
    String image;
    String school_name;
    String school_id;
    String block_status="0";

    public SchoolHomeModel(String image, String school_name) {
        this.image = image;
        this.school_name = school_name;
    }

    public SchoolHomeModel() {
    }

    public String getBlock_status() {
        return block_status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public void setBlock_status(String block_status) {
        this.block_status = block_status;
    }
}