package soonflyy.learning.hub.model;

public class StudentModel {
    String user_id;
    String course_id;
    String title;
    String first_name;
    String  user_image;


    public StudentModel() {
    }

    public String getUser_id() {
        return user_id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public String getTitle() {
        return title;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getUser_image() {
        return user_image;
    }
}

/*
response
 {
          "course_id": "17",
          "title": "Physics",
          "user_id": "4",
          "first_name": "Abhishek"
        }
 */
