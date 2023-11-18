package soonflyy.learning.hub.Common_Package.Common_Package.Models;

public class Chat_Message {
    String id;
    String to_id;
    String from_id;
    String is_seen;
    String message;
    String date;
    String time;

    public Chat_Message() {
    }

    public Chat_Message(String to_id, String from_id,
                        String message, String date) {
        this.id = id;
        this.to_id = to_id;
        this.from_id = from_id;
        this.is_seen = is_seen;
        this.message = message;
        this.date = date;
        this.time = time;
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

    public String getIs_seen() {
        return is_seen;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setTo_id(String to_id) {
        this.to_id = to_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public void setIs_seen(String is_seen) {
        this.is_seen = is_seen;
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
}
