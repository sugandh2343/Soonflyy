package soonflyy.learning.hub.Common_Package.Common_Package.Models;

public class IDAchievementModel {
    String id;
    String achievement_name;
    public IDAchievementModel() {
    }

    public IDAchievementModel(String id, String achievement_name) {
        this.id = id;
        this.achievement_name = achievement_name;
    }

    public String getId() {
        return id;
    }

    public String getAchievement_name() {
        return achievement_name;
    }

    public void setAchievement_name(String achievement_name) {
        this.achievement_name = achievement_name;
    }
}
