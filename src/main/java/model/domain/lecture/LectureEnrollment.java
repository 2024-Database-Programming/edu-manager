package model.domain.lecture;

import model.domain.member.Student;

public class LectureEnrollment {
    private long enrollmentId;
    private long lectureId;
    private String memberId;

    public long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public long getLectureId() {
        return lectureId;
    }

    public void setLectureId(long lectureId) {
        this.lectureId = lectureId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "LectureEnrollment [enrollmentId=" + enrollmentId + ", lectureId=" + lectureId + ", memberId=" + memberId
                + "]";
    }
}
