package model.domain.lecture;

import java.sql.Date;

public class LectureNotice {
    private long lectureNoticeId;
    private Date createAt;
    private String title;
    private String description;

    public long getLectureNoticeId() {
        return lectureNoticeId;
    }

    public void setLectureNoticeId(long lectureNoticeId) {
        this.lectureNoticeId = lectureNoticeId;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
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

    @Override
    public String toString() {
        return "LectureNotice [lectureNoticeId=" + lectureNoticeId + ", createAt=" + createAt + ", title=" + title
                + ", description=" + description + "]";
    }
}
