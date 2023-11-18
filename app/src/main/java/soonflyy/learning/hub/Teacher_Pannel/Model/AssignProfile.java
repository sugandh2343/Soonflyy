package soonflyy.learning.hub.Teacher_Pannel.Model;

import java.io.Serializable;

public class AssignProfile implements Serializable {
    public String id;
    public String name;
    public String mobile;
    public String image;
    String about="";

    public AssignProfile(String id, String name, String mobile, String image) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.image = image;
    }

    public String getAbout() {
        return about;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getImage() {
        return image;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
