package soonflyy.learning.hub.Teacher_Pannel.Model;

import android.net.Uri;

public class SubjectModel {
    String title;
    String section_thumbnail;
    Uri imgUri;

    public SubjectModel() {
    }

    public SubjectModel(String title , String section_thumbnail , Uri imgUri) {
        this.title = title;
        this.section_thumbnail = section_thumbnail;
        this.imgUri = imgUri;
    }


    public SubjectModel(String title) {
        this.title = title;
    }

    public Uri getImgUri() {
        return imgUri;
    }

    public void setImgUri(Uri imgUri) {
        this.imgUri = imgUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSection_thumbnail() {
        return section_thumbnail;
    }

    public void setSection_thumbnail(String section_thumbnail) {
        this.section_thumbnail = section_thumbnail;
    }
}
