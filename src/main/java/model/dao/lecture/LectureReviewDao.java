package model.dao.lecture;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.dao.JDBCUtil;
import model.domain.lecture.LectureReview;

public class LectureReviewDao {

    private JDBCUtil jdbcUtil = new JDBCUtil();

    
    public boolean isEnrolledInLecture(String memberId, long lectureId) throws SQLException {
        String sql = "SELECT COUNT(*) " +
                     "FROM LectureEnrollment le " +
                     "WHERE le.lectureId = ? AND le.stuId = ?";

        jdbcUtil.setSqlAndParameters(sql, new Object[]{lectureId, memberId});

        try {
            ResultSet rs = jdbcUtil.executeQuery();

            if (rs != null && rs.next()) {
                int count = rs.getInt(1);
                System.out.println("Is enrolled in lecture: " + count);  // 로그 출력

                return count > 0;
            }

            System.out.println("No matching enrollment records found.");  // 로그 출력
            return false;
        } finally {
            jdbcUtil.close();
        }
    }
    
    // 강의 리뷰 등록
    public LectureReview insertReview(LectureReview review) {
        
        StringBuffer query = new StringBuffer();
        query.append(" INSERT INTO LectureReview (lectureReviewId, reviewText, lectureId, stuId) ") ;
        query.append(" VALUES (SEQ_LECTURE_REVIEW_ID.nextval, ?, ?, ?) ") ;
         

        // 명시적으로 데이터 타입 설정
        try {
            
            Object[] param = new Object[] { review.getReviewText(),    // reviewText는 String 타입
                    review.getLectureId(),    // lectureId는 Integer 또는 Long 타입
                    review.getMemberId()  };


            jdbcUtil.setSqlAndParameters(query.toString(), param); // JDBCUtil에 질의문과 파라미터 설정

            // 삽입 실행
            int result = jdbcUtil.executeUpdate();

            if (result > 0) {
                jdbcUtil.commit();
                return review; // 삽입 성공 시 입력된 review 객체 반환
            } else {
                throw new RuntimeException("Failed to insert LectureReview.");
            }
        } catch (Exception ex) {
            jdbcUtil.rollback();
            ex.printStackTrace();
            throw new RuntimeException("Database error occurred while inserting LectureReview.", ex);
        } finally {
            jdbcUtil.close();
        }
    }

    // 강의 리뷰 삭제
    public int deleteReview(long lectureReviewId) {
        StringBuffer query = new StringBuffer();
        query.append("DELETE FROM LectureReview WHERE lectureReviewId = ?");

        jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { lectureReviewId });

        int result = 0;
        try {
            result = jdbcUtil.executeUpdate();
        } catch (Exception ex) {
            jdbcUtil.rollback();
            ex.printStackTrace();
        } finally {
            jdbcUtil.commit();
            jdbcUtil.close();
        }
        return result;
    }

    // 강의 리뷰 수정
    public int updateReview(LectureReview review) {
        StringBuffer query = new StringBuffer();
        query.append("UPDATE LectureReview ");
        query.append("SET reviewText = ?, lectureId = ?, memberId = ? ");
        query.append("WHERE lectureReviewId = ?");

        Object[] params = new Object[] { review.getReviewText(), review.getLectureId(), // Lecture 객체에서
                                                                                                       // ID 추출
                review.getLectureReviewId() };

        jdbcUtil.setSqlAndParameters(query.toString(), params);

        int result = 0;
        try {
            result = jdbcUtil.executeUpdate();
        } catch (Exception ex) {
            jdbcUtil.rollback();
            ex.printStackTrace();
        } finally {
            jdbcUtil.commit();
            jdbcUtil.close();
        }
        return result;
    }

    // 강의 리뷰 조회 (특정 강의 ID 기준)
    public List<LectureReview> getReviewsByLectureId(long lectureId) {
        StringBuffer query = new StringBuffer();
        query.append("""
                     SELECT
                        lr.lectureReviewId,
                        lr.reviewText,
                        lr.lectureId,
                        lr.stuId,
                        m.name
                    FROM
                        LectureReview lr
                    JOIN
                        Member m
                    ON
                        lr.stuId = m.Id
                    WHERE
                        lr.lectureId =  ?
                """);

        jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { lectureId });

        List<LectureReview> reviews = new ArrayList<>();
        try {
            ResultSet rs = jdbcUtil.executeQuery();
            while (rs.next()) {
                LectureReview review = new LectureReview();
                review.setLectureReviewId(rs.getLong("lectureReviewId"));
                review.setReviewText(rs.getString("reviewText"));
                review.setMemberName(rs.getString("name"));
                // Lecture 및 StudentDTO 로드는 필요 시 추가 가능
                reviews.add(review);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            jdbcUtil.close();
        }
        return reviews;
    }
}
