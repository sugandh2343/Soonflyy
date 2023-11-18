package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model;

public class SchoolLiveClassModel {
    int image;
    String livechaptername;
    String liveduration;

    public SchoolLiveClassModel(int image, String livechaptername, String liveduration) {
        this.image = image;
        this.livechaptername = livechaptername;
        this.liveduration = liveduration;
    }

    public SchoolLiveClassModel() {
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getLivechaptername() {
        return livechaptername;
    }

    public void setLivechaptername(String livechaptername) {
        this.livechaptername = livechaptername;
    }

    public String getLiveduration() {
        return liveduration;
    }

    public void setLiveduration(String liveduration) {
        this.liveduration = liveduration;
    }
}
