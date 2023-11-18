package soonflyy.learning.hub.Common_Package.Models;

public class ShowMessageModel {

    String id;
    String to_id;
    String from_id;
    String message;
    String date;
    String time;
    String is_seen;
    String photo;
    String name;
    String unseen_count;
    String user_id;
    String last_msg;
    String  block_status;


    //---------
    String to_type;
    String from_type;
    String type;
    String blocked_by;
    String blocked_to;



    public ShowMessageModel() {
    }

    public String getUser_id() {
        return user_id;
    }

    public String getId() {
        return id;
    }

    public String getTo_id() {
        return to_id;
    }

    public String getFrom_id() {
        return from_id;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getIs_seen() {
        return is_seen;
    }

    public String getPhoto() {
        return photo;
    }

    public String getName() {
        return name;
    }

    public String getUnseen_count() {
        return unseen_count;
    }

    public String getLast_msg() {
        return last_msg;
    }

    public String getBlock_status() {
        return block_status;
    }

    public String getTo_type() {
        return to_type;
    }

    public String getFrom_type() {
        return from_type;
    }

    public String getType() {
        return type;
    }

    public String getBlocked_by() {
        return blocked_by;
    }

    public String getBlocked_to() {
        return blocked_to;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTo_id(String to_id) {
        this.to_id = to_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setIs_seen(String is_seen) {
        this.is_seen = is_seen;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnseen_count(String unseen_count) {
        this.unseen_count = unseen_count;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setLast_msg(String last_msg) {
        this.last_msg = last_msg;
    }

    public void setBlock_status(String block_status) {
        this.block_status = block_status;
    }

    public void setTo_type(String to_type) {
        this.to_type = to_type;
    }

    public void setFrom_type(String from_type) {
        this.from_type = from_type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBlocked_by(String blocked_by) {
        this.blocked_by = blocked_by;
    }

    public void setBlocked_to(String blocked_to) {
        this.blocked_to = blocked_to;
    }
}
