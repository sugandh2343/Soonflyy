package soonflyy.learning.hub.Teacher_Pannel.Model;

public class AssignTutors {
    String uid;
    String name;
    String mobile;
    String image;

    public AssignTutors() {
    }

    public AssignTutors(String uid , String name , String mobile , String image) {
        this.uid = uid;
        this.name = name;
        this.mobile = mobile;
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
