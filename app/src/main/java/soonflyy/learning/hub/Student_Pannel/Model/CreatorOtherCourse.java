package soonflyy.learning.hub.Student_Pannel.Model;

import soonflyy.learning.hub.Common.BaseUrl;

public class CreatorOtherCourse {
    String id;
    String title;
    String image;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return BaseUrl.BASE_URL_MEDIA+image;
    }
}
