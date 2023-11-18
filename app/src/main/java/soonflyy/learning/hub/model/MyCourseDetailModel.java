package soonflyy.learning.hub.model;

import android.os.Parcel;
import android.os.Parcelable;

import soonflyy.learning.hub.Teacher_Pannel.Model.AssignProfile;

public class MyCourseDetailModel implements Parcelable {


  boolean isSelected=false;
        public String id;
        public String title;

        public String decription;

        public String user_id;
        public String course_thumbnail;

        public String is_free_course;
        public String validity;

        public String is_subscriptions;
        public String status;//pending or active

        public String category_name;
        public String category_id;
        public String created_at;
        public String amount;
        public String discount_amount;
        public  String assigned_value ="-1";
        public  String course_id ="";
        public AssignProfile assign_to;
        public  AssignProfile assign_by;
        public String assign_to_id;
        public String assign_by_id;
        public String creatorId;
        public String assign_to_name;
        public String assign_to_image;
        public Boolean assigned;



   // }

    public MyCourseDetailModel() {
    }

    public MyCourseDetailModel(boolean isSelected ,
                               String id ,
                               String title ,
                               String decription ,
                               String user_id ,
                               String course_thumbnail ,
                               String is_free_course ,
                               String validity ,
                               String is_subscriptions ,
                               String status ,
                               String category_name ,
                               String category_id ,
                               String created_at ,
                               String amount ,
                               String discount_amount ,
                               String assigned_value ,
                               String course_id ,
                               AssignProfile assign_to ,
                               AssignProfile assign_by ,
                               String assign_to_id ,
                               String assign_by_id ,
                               String creatorId ,
                               String assign_to_name ,
                               String assign_to_image ,
                               Boolean assigned) {
        this.isSelected = isSelected;
        this.id = id;
        this.title = title;
        this.decription = decription;
        this.user_id = user_id;
        this.course_thumbnail = course_thumbnail;
        this.is_free_course = is_free_course;
        this.validity = validity;
        this.is_subscriptions = is_subscriptions;
        this.status = status;
        this.category_name = category_name;
        this.category_id = category_id;
        this.created_at = created_at;
        this.amount = amount;
        this.discount_amount = discount_amount;
        this.assigned_value = assigned_value;
        this.course_id = course_id;
        this.assign_to = assign_to;
        this.assign_by = assign_by;
        this.assign_to_id = assign_to_id;
        this.assign_by_id = assign_by_id;
        this.creatorId = creatorId;
        this.assign_to_name = assign_to_name;
        this.assign_to_image = assign_to_image;
        this.assigned = assigned;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public void setIs_free_course(String is_free_course) {
        this.is_free_course = is_free_course;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public void setIs_subscriptions(String is_subscriptions) {
        this.is_subscriptions = is_subscriptions;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setDiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }

    public void setAssign_to(AssignProfile assign_to) {
        this.assign_to = assign_to;
    }

    public void setAssign_by(AssignProfile assign_by) {
        this.assign_by = assign_by;
    }

    public MyCourseDetailModel(String title) {
        this.title = title;
    }

    protected MyCourseDetailModel(Parcel in) {
        id = in.readString();
        title = in.readString();
        //price = in.readString();
        decription = in.readString();
        //discount_flag = in.readString();
        user_id = in.readString();
        course_thumbnail = in.readString();
       // is_top_course = in.readString();
        is_free_course = in.readString();
        status = in.readString();
        validity = in.readString();
        is_subscriptions = in.readString();
        //subcategory_id = in.readString();
        //subcategory_name = in.readString();
        category_id = in.readString();
        category_id = in.readString();
        assigned_value =in.readString();
    }

    public static final Creator<MyCourseDetailModel> CREATOR = new Creator<MyCourseDetailModel>() {
        @Override
        public MyCourseDetailModel createFromParcel(Parcel in) {
            return new MyCourseDetailModel(in);
        }

        @Override
        public MyCourseDetailModel[] newArray(int size) {
            return new MyCourseDetailModel[size];
        }
    };

    @Override
    public String toString(){
        return title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public AssignProfile getAssign_to() {
        return assign_to;
    }

    public AssignProfile getAssign_by() {
        return assign_by;
    }

    //    public String getPrice() {
//        return price;
//    }

    public String getDecription() {
        return decription;
    }

//    public String getDiscount_flag() {
//        return discount_flag;
//    }

    public String getUser_id() {
        return user_id;
    }


//    public String getIs_top_course() {
//        return is_top_course;
//    }

    public String getIs_free_course() {
        return is_free_course;
    }

    public String getValidity() {
        return validity;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }
    // public List<Video> getDemo_video() {
     //   return demo_video;
    //}


    public String getCreated_at() {
        return created_at;
    }

    public String getAmount() {
        return amount;
    }

    public String getDiscount_amount() {
        return discount_amount;
    }

    public String getIs_subscriptions() {
        return is_subscriptions;
    }

//    public List<Chapter> getChapter() {
//        return chapter;
//    }
//
//    public String getSubcategory_name() {
//        return subcategory_name;
//    }
//
//    public String getSubcategory_id() {
//        return subcategory_id;
//    }

    public String getCategory_name() {
        return category_name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getStatus() {
        return status;
    }

    public String getAssigned_value() {
        return assigned_value;
    }

    public void setAssigned_value(String assigned_value) {
        this.assigned_value = assigned_value;
    }

    public static Creator<MyCourseDetailModel> getCREATOR() {
        return CREATOR;
    }

    public String getCourse_thumbnail() {
        return course_thumbnail;
    }

    public void setCourse_thumbnail(String course_thumbnail) {
        this.course_thumbnail = course_thumbnail;
    }

    public String getAssign_to_id() {
        return assign_to_id;
    }

    public void setAssign_to_id(String assign_to_id) {
        this.assign_to_id = assign_to_id;
    }

    public String getAssign_by_id() {
        return assign_by_id;
    }

    public void setAssign_by_id(String assign_by_id) {
        this.assign_by_id = assign_by_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public Boolean getAssigned() {
        return assigned;
    }

    public void setAssigned(Boolean assigned) {
        this.assigned = assigned;
    }

    public String getAssign_to_name() {
        return assign_to_name;
    }

    public void setAssign_to_name(String assign_to_name) {
        this.assign_to_name = assign_to_name;
    }

    public String getAssign_to_image() {
        return assign_to_image;
    }

    public void setAssign_to_image(String assign_to_image) {
        this.assign_to_image = assign_to_image;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        //dest.writeString(price);
        dest.writeString(decription);
        //dest.writeString(discount_flag);
        dest.writeString(user_id);
        dest.writeString(course_thumbnail);
        //dest.writeString(is_top_course);
        dest.writeString(is_free_course);
        dest.writeString(status);
        dest.writeString(validity);
        dest.writeString(is_subscriptions);

        //dest.writeString(subcategory_id);
        //dest.writeString(subcategory_name);
        dest.writeString(category_id);
        dest.writeString(category_name);
        dest.writeString(assigned_value);
    }
}

