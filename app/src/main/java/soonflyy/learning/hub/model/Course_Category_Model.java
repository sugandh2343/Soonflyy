package soonflyy.learning.hub.model;

public class Course_Category_Model {
    String id;
    String code;
    String name;
    String thumbnail;
    String number_of_courses;

    public Course_Category_Model() {
    }

    public Course_Category_Model(String id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public String toString(){
        return name;
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

   public String getNumber_of_courses() {
        return number_of_courses;
   }
}
