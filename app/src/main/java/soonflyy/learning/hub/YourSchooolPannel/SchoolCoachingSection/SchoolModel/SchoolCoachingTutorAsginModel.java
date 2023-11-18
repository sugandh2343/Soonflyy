package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolModel;

import java.util.ArrayList;

public class SchoolCoachingTutorAsginModel {

    String id;
    String name;
    String mobile;
    String percantage;
    String block_status="0";
    ArrayList<SchoolCoachingTutorAsginClassModel>assign_class;
    public SchoolCoachingTutorAsginModel() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPercantage() {
        return percantage;
    }

    public String getBlock_status() {
        return block_status;
    }

    public ArrayList<SchoolCoachingTutorAsginClassModel> getAssign_class() {
        return assign_class;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPercantage(String percantage) {
        this.percantage = percantage;
    }

    public void setBlock_status(String block_status) {
        this.block_status = block_status;
    }

    public void setAssign_class(ArrayList<SchoolCoachingTutorAsginClassModel> assign_class) {
        this.assign_class = assign_class;
    }
    /////
//    public  class AssignClass{
//        String class_id;
//        String class_name;
//        String section_id;
//        String section_name;
//        String subject_id;
//        String subject_name;
//        String peroid_id;
//        String peroid;
//        String date;
//        String start_time;
//        String end_time;
//
//        public String getClass_id() {
//            return class_id;
//        }
//
//        public String getClass_name() {
//            return class_name;
//        }
//
//        public String getSection_id() {
//            return section_id;
//        }
//
//        public String getSection_name() {
//            return section_name;
//        }
//
//        public String getSubject_id() {
//            return subject_id;
//        }
//
//        public String getSubject_name() {
//            return subject_name;
//        }
//
//        public String getPeroid_id() {
//            return peroid_id;
//        }
//
//        public String getPeroid() {
//            return peroid;
//        }
//
//        public String getDate() {
//            return date;
//        }
//
//        public String getStart_time() {
//            return start_time;
//        }
//
//        public String getEnd_time() {
//            return end_time;
//        }
//    }
}
