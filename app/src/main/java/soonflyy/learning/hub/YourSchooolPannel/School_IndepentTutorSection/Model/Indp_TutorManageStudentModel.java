package soonflyy.learning.hub.YourSchooolPannel.School_IndepentTutorSection.Model;

public class Indp_TutorManageStudentModel {
    int image;
    String Adm_No ;
    String student_id;
    String name ;
    String mobile ;
    String email ;
    String address ;
    String percantage ;
    String class_name;
    String section;

    String is_block;
    String father_name;



    public Indp_TutorManageStudentModel() {
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPercantage() {
        return percantage;
    }

    public String getStudent_id() {
        return student_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public String getSection() {
        return section;
    }

    public String getIs_block() {
        return is_block;
    }

    public String getFather_name() {
        return father_name;
    }
}
