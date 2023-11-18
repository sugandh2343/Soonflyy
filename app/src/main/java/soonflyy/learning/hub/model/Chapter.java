package soonflyy.learning.hub.model;

public class Chapter  {

    String id;
    String title;
    String cover_image;


    public Chapter() {
    }

    public Chapter( String chapter_title) {

        this.title = chapter_title;
    }

    @Override
    public String toString(){
        return title;
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

    public void setId(String id) {
        this.id = id;
    }

    public String getCover_image() {
        return cover_image;
    }
}
