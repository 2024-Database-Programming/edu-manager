package model.domain.lecture;

public class LectureReview {
    private long lectureReviewId;
    private String reviewText;
    private long lectureId;
    private String memberId;
    private String memberName;
    
    public LectureReview() {}

    public LectureReview(Long lectureReviewId, String reviewText, long lectureId, String memberId, String memberName) {
        this.lectureReviewId = lectureReviewId;
        this.reviewText = reviewText;
        this.lectureId = lectureId;
        this.memberId = memberId;
        this.memberName = memberName;
    }

    public long getLectureReviewId() {
        return lectureReviewId;
    }

    public void setLectureReviewId(long lectureReviewId) {
        this.lectureReviewId = lectureReviewId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
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
    
    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
   

    @Override
    public String toString() {
        return "LectureReview [lectureReviewId=" + lectureReviewId + ", reviewTest=" + reviewText + ", lectureId="
                + lectureId + ", memberId=" + memberId + "]";
    }

}
