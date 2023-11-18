package soonflyy.learning.hub.studentModel;

import java.io.Serializable;

public class Teacher implements Serializable {

    String id;
    String first_name;

    String last_name;
    String email;
    String mobile;
    String role_id;
    String image;
    String status;
    String is_instructor;
    String country;
    String gender;
    String dob;
    String state;
    String city;
    String pincode;
    String category;
    String social_links;
    String biography;

    public Teacher() {
    }

    public String getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getRole_id() {
        return role_id;
    }

    public String getImage() {
        return image;
    }

    public String getStatus() {
        return status;
    }

    public String getIs_instructor() {
        return is_instructor;
    }

    public String getCountry() {
        return country;
    }

    public String getGender() {
        return gender;
    }

    public String getDob() {
        return dob;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getPincode() {
        return pincode;
    }

    public String getCategory() {
        return category;
    }

    public String getSocial_links() {
        return social_links;
    }

    public String getBiography() {
        return biography;
    }
}
