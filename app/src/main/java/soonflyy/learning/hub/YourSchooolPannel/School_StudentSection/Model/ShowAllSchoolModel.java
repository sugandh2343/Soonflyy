package soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Model;

import java.util.ArrayList;

public class ShowAllSchoolModel {
    String id;
    String name;
    String image;
    String section_id;
    String class_id;
    String block_status="0";
    String  student_id;
    String student_name;
    ArrayList<OtherStudent>student_data;


    public String getBlock_status() {
        return block_status;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getSection_id() {
        return section_id;
    }

    public String getClass_id() {
        return class_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public ArrayList<OtherStudent> getStudent_data() {
        return student_data;
    }

    //    String school_name;
//
//    public ShowAllSchoolModel(int image, String school_name) {
//        this.image = image;
//        this.school_name = school_name;
//    }
//
//    public ShowAllSchoolModel() {
//    }
//
//    public int getImage() {
//        return image;
//    }
//
//    public void setImage(int image) {
//        this.image = image;
//    }
//
//    public String getSchool_name() {
//        return school_name;
//    }
//
//    public void setSchool_name(String school_name) {
//        this.school_name = school_name;
//    }
}
