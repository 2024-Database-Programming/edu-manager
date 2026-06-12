package model.domain.lecture;

import java.io.File;
import java.sql.Date;

public class LectureAssignment {
    private Date dueDate;
    private String title;
    private String description;
    private Date createAt;
    private File textFile;
    private Lecture lectureId;

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public File getTextFile() {
        return textFile;
    }

    public void setTextFile(File textFile) {
        this.textFile = textFile;
    }

    public Lecture getLectureId() {
        return lectureId;
    }

    public void setLectureId(Lecture lectureId) {
        this.lectureId = lectureId;
    }

    @Override
    public String toString() {
        return "LectureAssignment [dueDate=" + dueDate + ", title=" + title + ", description=" + description
                + ", createAt=" + createAt + ", textFile=" + textFile + ", lectureId=" + lectureId + "]";
    }

}
