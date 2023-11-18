package soonflyy.learning.hub.model;

public class CreateTestModel {
    String title;
    String marks;
    String options[];
    String answer;
    String details;

    public CreateTestModel() {
    }

    public CreateTestModel(String title, String marks, String[] options, String answer, String details) {
        this.title = title;
        this.marks = marks;
        this.options = options;
        this.answer = answer;
        this.details = details;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
