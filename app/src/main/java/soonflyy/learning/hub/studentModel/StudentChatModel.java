package soonflyy.learning.hub.studentModel;

import org.json.JSONObject;

public class StudentChatModel {
    String user_id;
    String first_name;
    String user_image;
   JSONObject chats;



      public JSONObject getChats() {
        return chats;
    }

    public void setChats(JSONObject chats) {
        this.chats = chats;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUnread_count() {
        return unread_count;
    }

    public void setUnread_count(String unread_count) {
        this.unread_count = unread_count;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }

    String unread_count;
    String message_text;
    String is_read;


    public String getUser_id() {
        return user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getUser_image() {
        return user_image;
    }
}
