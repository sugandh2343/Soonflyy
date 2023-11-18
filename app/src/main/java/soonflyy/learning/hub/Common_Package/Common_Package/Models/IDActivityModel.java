package soonflyy.learning.hub.Common_Package.Common_Package.Models;

public class IDActivityModel {

    String activity_id;
    String activity_name;
    public IDActivityModel() {
    }

    public IDActivityModel(String activity_id, String activity_name) {
        this.activity_id = activity_id;
        this.activity_name = activity_name;
    }

    public String getActivity_id() {
        return activity_id;
    }


    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }
}
