package model.domain.studyGroup;

import java.util.Date;

public class StudyGroupReview {

    private Long studyGroupReviewId; 
    private String reviewText; 
    private Date createAt; 
    private Long studyGroupId; 
    private String stuId;
    private String memberName;

    // 기본 생성자
    public StudyGroupReview() {
    }

    // 생성자
    public StudyGroupReview(Long studyGroupReviewId, String reviewText,Long studyGroupId, String stuId, String memberName) {
        this.studyGroupReviewId = studyGroupReviewId;
        this.reviewText = reviewText;
        this.studyGroupId = studyGroupId;
        this.stuId = stuId;
        this.memberName = memberName;
    }

    // Getter 및 Setter
    public Long getStudyGroupReviewId() {
        return studyGroupReviewId;
    }

    public void setStudyGroupReviewId(Long studyGroupReviewId) {
        this.studyGroupReviewId = studyGroupReviewId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Long getStudyGroupId() {
        return studyGroupId;
    }

    public void setStudyGroupId(Long studyGroupId) {
        this.studyGroupId = studyGroupId;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }
    
    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
   

    @Override
    public String toString() {
        return "StudyGroupReview{" +
                "studyGroupReviewId=" + studyGroupReviewId +
                ", reviewText='" + reviewText + '\'' +
                ", createAt=" + createAt +
                ", studyGroupId=" + studyGroupId +
                ", stuId='" + stuId + '\'' +
                '}';
    }
}
