package model.domain.lecture;

import java.sql.Date;

import model.domain.member.Student;

public class LectureLike {
    private long lectureLikeId;
    private Date createAt;
    private Lecture lectureId;
    private Student memberId;

    public long getLectureLikeId() {
        return lectureLikeId;
    }

    public void setLectureLikeId(long lectureLikeId) {
        this.lectureLikeId = lectureLikeId;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Lecture getLectureId() {
        return lectureId;
    }

    public void setLectureId(Lecture lectureId) {
        this.lectureId = lectureId;
    }

    public Student getMemberId() {
        return memberId;
    }

    public void setMemberId(Student memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "LectureLike [lectureLikeId=" + lectureLikeId + ", createAt=" + createAt + ", lectureId=" + lectureId
                + ", memberId=" + memberId + "]";
    }

}
