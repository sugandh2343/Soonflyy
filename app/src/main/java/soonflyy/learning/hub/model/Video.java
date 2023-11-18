package soonflyy.learning.hub.model;

public class Video {
    String video_id;
    String video_file;
    String video_thumbnail;
    String is_bookmark;
    String bookmark_id;

    public Video() {
    }

    public String getVideo_id() {
        return video_id;
    }

    public String getVideo_file() {
        return video_file;
    }

    public String getVideo_thumbnail() {
        return video_thumbnail;
    }

    public String getIs_bookmark() {
        return is_bookmark;
    }

    public String getBookmark_id() {
        return bookmark_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public void setVideo_file(String video_file) {
        this.video_file = video_file;
    }

    public void setVideo_thumbnail(String video_thumbnail) {
        this.video_thumbnail = video_thumbnail;
    }

    public void setIs_bookmark(String is_bookmark) {
        this.is_bookmark = is_bookmark;
    }

    public void setBookmark_id(String bookmark_id) {
        this.bookmark_id = bookmark_id;
    }
}
