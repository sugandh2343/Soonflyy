package soonflyy.learning.hub.Student_Pannel.Model;

import android.os.Parcel;
import android.os.Parcelable;

import soonflyy.learning.hub.Common_Package.Models.Video_Model;

import java.util.ArrayList;

public class Course_DetailsMOdel implements Parcelable {
    String id;
    String title;
    String description;
    String price;
    String discounted_price;
    String thumbnail;
    String validity;
    String is_free_course;
    String user_id;
    String rating;
    String subscription;
    String shareable_link;
    String instructor_name;
    String is_purchased;
    String is_bookmark;
    String  course_provider;
    String amount;
    String live_demo_count;
    String offer_course_price;
    String course_offers;
    ArrayList<Video_Model> live_demo_video;

    public String getLive_demo_count() {
        return live_demo_count;
    }

    public String getOffer_course_price() {
        return offer_course_price;
    }

    public String getCourse_offers() {
        return course_offers;
    }

    public String getAmount() {
        return amount;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getDiscounted_price() {
        return discounted_price;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getValidity() {
        return validity;
    }

    public String getIs_free_course() {
        return is_free_course;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getRating() {
        return rating;
    }

    public String getSubscription() {
        return subscription;
    }

    public String getShareable_link() {
        return shareable_link;
    }

    public void setIs_bookmark(String is_bookmark) {
        this.is_bookmark = is_bookmark;
    }

    public String getInstructor_name() {
        return instructor_name;
    }

    public String getIs_purchased() {
        return is_purchased;
    }

    public String getIs_bookmark() {
        return is_bookmark;
    }

    public String getCourse_provider() {
        return course_provider;
    }

    public ArrayList<Video_Model> getLive_demo_video() {
        return live_demo_video;
    }

    public static Creator<Course_DetailsMOdel> getCREATOR() {
        return CREATOR;
    }

    protected Course_DetailsMOdel(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        price = in.readString();
        discounted_price = in.readString();
        thumbnail = in.readString();
        validity = in.readString();
        is_free_course = in.readString();
        user_id = in.readString();
        rating = in.readString();
        subscription = in.readString();
        shareable_link = in.readString();
        instructor_name = in.readString();
        is_purchased = in.readString();
        is_bookmark = in.readString();
       // course_provider = in.createStringArray();
        course_provider = in.readString();
        amount = in.readString();
        live_demo_count = in.readString();
        course_offers = in.readString();
        offer_course_price = in.readString();

    }

    public static final Creator<Course_DetailsMOdel> CREATOR = new Creator<Course_DetailsMOdel>() {
        @Override
        public Course_DetailsMOdel createFromParcel(Parcel in) {
            return new Course_DetailsMOdel(in);
        }

        @Override
        public Course_DetailsMOdel[] newArray(int size) {
            return new Course_DetailsMOdel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(price);
        dest.writeString(discounted_price);
        dest.writeString(thumbnail);
        dest.writeString(validity);
        dest.writeString(is_free_course);
        dest.writeString(user_id);
        dest.writeString(rating);
        dest.writeString(subscription);
        dest.writeString(shareable_link);
        dest.writeString(instructor_name);
        dest.writeString(is_purchased);
        dest.writeString(is_bookmark);
        //dest.writeStringArray(course_provider);
        dest.writeString(course_provider);
        dest.writeString(amount);

        dest.writeString(live_demo_count);
        dest.writeString(offer_course_price);
        dest.writeString(course_offers);

    }
}
