package model.dao.lecture;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.dao.JDBCUtil;
import model.domain.lecture.Lecture;
import model.domain.studyGroup.StudyGroup;

public class LectureLikeDao {
    private JDBCUtil jdbcUtil = new JDBCUtil();
    
    public boolean isLikedByUser(String memberId, long lectureId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM LectureLike WHERE stuId = ? AND lectureId = ?";
        jdbcUtil.setSqlAndParameters(sql, new Object[]{memberId, lectureId});

        ResultSet rs = jdbcUtil.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0; // 좋아요가 있으면 true, 없으면 false
        }
        return false;
    }

    // 좋아요 추가
    public void addLike(String memberId, long lectureId) throws SQLException {
        String sql = "INSERT INTO LectureLike (lectureLikeId, createAt, lectureId, stuId) "
                     + "VALUES (SEQ_STUDY_GROUP_LIKE_ID.nextval, SYSDATE, ?, ?)";
        jdbcUtil.setSqlAndParameters(sql, new Object[]{lectureId, memberId});
        try {
            jdbcUtil.executeUpdate();
        } catch (Exception ex) {
            jdbcUtil.rollback();
            ex.printStackTrace();
        } finally {
            jdbcUtil.commit();
            jdbcUtil.close(); // resource 반환
        }
    }

    // 좋아요 삭제
    public void removeLike(String memberId, long lectureId) throws SQLException {
        String sql = "DELETE FROM LectureLike WHERE lectureId = ? AND stuId = ?";
        jdbcUtil.setSqlAndParameters(sql, new Object[]{lectureId, memberId});
        try {
            jdbcUtil.executeUpdate();
        } catch (Exception ex) {
            jdbcUtil.rollback();
            ex.printStackTrace();
        } finally {
            jdbcUtil.commit();
            jdbcUtil.close(); // resource 반환
        }
    }
    
    //좋아요 누른 lecture들 가지고 오기
public List<Lecture> getLikedLectures(String memberId) throws SQLException {
    List<Lecture> lectureList = new ArrayList<>();
    
    // lectureLike 테이블과 lecture 테이블을 조인하여 필요한 정보를 가져오는 쿼리
    String sql = "SELECT l.lectureId, l.name, l.category, l.img, t.name AS teacherName,  ic.name AS categoryName, ic.color " +
                 "FROM lectureLike ll " +
                 "JOIN lecture l ON ll.lectureId = l.lectureId " +
                 "JOIN Teacher t ON l.teacherId = t.Id  " +
                 "JOIN InterestCategory ic ON l.category = ic.Id  " +
                 "WHERE ll.stuId = ?";
                 
    jdbcUtil.setSqlAndParameters(sql, new Object[]{memberId});
    ResultSet rs = jdbcUtil.executeQuery();
    
    // 결과에서 정보를 추출하여 Lecture 객체에 설정
    while (rs.next()) {
        Lecture lecture = new Lecture();
        lecture.setLectureId(rs.getInt("lectureId"));
        lecture.setName(rs.getString("name"));
        lecture.setCategory(rs.getString("category"));
        lecture.setImg(rs.getString("img"));
        lecture.setTeacherName(rs.getString("teacherName"));
        lecture.setCategoryColor(rs.getString("color"));
        lecture.setCategoryName(rs.getString("categoryName"));
        
        lectureList.add(lecture);
    }
    
    jdbcUtil.close();
    return lectureList;
}
}
