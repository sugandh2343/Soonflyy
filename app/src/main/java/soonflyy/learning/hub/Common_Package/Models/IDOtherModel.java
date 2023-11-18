package soonflyy.learning.hub.Common_Package.Models;

public class IDOtherModel {
    String id;
    String title;
    String link;



    public IDOtherModel() {
    }

    public IDOtherModel(String id, String title, String link) {
        this.id = id;
        this.title = title;
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
