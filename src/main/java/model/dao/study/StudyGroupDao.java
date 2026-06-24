package model.dao.study;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.swing.JOptionPane;

import model.dao.JDBCUtil;
import model.domain.lecture.Lecture;
import model.domain.lecture.LectureReview;
import model.domain.member.Member;
import model.domain.study.StudyGroup;
import model.domain.study.StudyGroupApplication;
import model.domain.study.StudyGroupReview;

public class StudyGroupDao {
   private JDBCUtil jdbcUtil = null;

   public StudyGroupDao() {
      jdbcUtil = new JDBCUtil(); // JDBCUtil 객체 생성
   }

   public StudyGroup create(StudyGroup group) throws SQLException {
      try {
         String sql = "INSERT INTO StudyGroup (studyGroupId, name, img, description, capacity, category, createAt, leaderId, place) "
               + "VALUES (SEQ_STUDY_GROUP_ID.nextval, ?, ?, ?, ?, ?, SYSDATE, ?, ?)";
         Object[] param = new Object[] { group.getName(), group.getImg(), group.getDescription(),
               group.getCapacity(), group.getCategory(), group.getLeaderId(), group.getPlace() };

         jdbcUtil.setSqlAndParameters(sql, param);

         // StudyGroup에 id setting
         String key[]={"studyGroupId"};  // PK 컬럼(들)의 이름 배열       
         int result = jdbcUtil.executeUpdate(key);
            if (result > 0) {
               ResultSet rs = jdbcUtil.getGeneratedKeys();
                if(rs.next()) {
                   long generatedKey = rs.getLong(1);   // 생성된 PK 값
                   group.setStudyGroupId(generatedKey);    // id필드에 저장  
                }
             return group;
            } 
      } catch (Exception ex) {
         jdbcUtil.rollback();
         ex.printStackTrace();
      } finally {
         jdbcUtil.commit();
         jdbcUtil.close();
      }
      return null;
   }

   // 스터디 그룹 정보 수정
   public int update(StudyGroup group) throws SQLException {
      String sql = "UPDATE StudyGroup " + "SET name=?, img=?, description=?, capacity=?, category=? "
            + "WHERE studyGroupId=? ";
      Object[] param = new Object[] { group.getName(), group.getImg(), group.getDescription(), group.getCapacity(),
            group.getCategory(), group.getStudyGroupId() };
      jdbcUtil.setSqlAndParameters(sql, param);
      try {
         int result = jdbcUtil.executeUpdate(); // update 문 실행
         return result;
      } catch (Exception ex) {
         jdbcUtil.rollback();
         ex.printStackTrace();
      } finally {
         jdbcUtil.commit();
         jdbcUtil.close(); // resource 반환
      }
      return 0;
   }

// 스터디 그룹 정보 불러오기
public StudyGroup findGroupInfo(long groupId) {
    try {
        StudyGroup group = new StudyGroup();
        String query = "SELECT sg.studyGroupId, sg.name, sg.img, sg.description, sg.capacity, sg.category, sg.place, sg.leaderId, ss.dayOfWeek, " +
                       "m.name AS leaderName " +
                       "FROM StudyGroup sg " +
                       "JOIN Member m ON sg.leaderId = m.id " +
                       "LEFT JOIN Studyschedule ss ON sg.studyGroupId = ss.studyGroupId " +
                       "WHERE sg.studyGroupId = ?";
        jdbcUtil.setSqlAndParameters(query, new Object[] { groupId });

        ResultSet rs = jdbcUtil.executeQuery();

        if (rs.next()) {
            group.setStudyGroupId(groupId);
            group.setName(rs.getString("name"));
            group.setImg(rs.getString("img"));
            group.setDescription(rs.getString("description"));
            group.setCapacity(rs.getInt("capacity"));
            group.setCategory(rs.getString("category"));
            group.setPlace(rs.getString("place"));
            group.setLeaderId(rs.getString("leaderId"));
            group.setLeaderName(rs.getString("leaderName")); // 리더 이름 설정
            group.setDayOfWeek(rs.getString("dayOfWeek"));
            
        } else {
        }
        return group;
    } catch (SQLException ex) {
        ex.printStackTrace();
    } finally {
        jdbcUtil.close();
    }

    return null;
}


	// 특정 스터디그룹에 해당하는 정보 삭제
	public int remove(String groupId) throws SQLException {
		String sql = "DELETE FROM StudyGroup WHERE studyGroupId=?";
		jdbcUtil.setSqlAndParameters(sql, new Object[] { groupId }); // JDBCUtil에 delete문과 매개 변수 설정

		try {
			int result = jdbcUtil.executeUpdate(); // delete 문 실행
			return result;
		} catch (Exception ex) {
			jdbcUtil.rollback();
			ex.printStackTrace();
		} finally {
			jdbcUtil.commit();
			jdbcUtil.close(); // resource 반환
		}
		return 0;
	}


   // 스터디그룹 전체 보여주기
   public List<StudyGroup> getStudyGroupList() throws SQLException {
      List<StudyGroup> groupList = new ArrayList<>();
      String sql = "SELECT studyGroupId, name, img, description, capacity, category FROM StudyGroup";

      jdbcUtil.setSqlAndParameters(sql, new Object[] {});
      ResultSet rs = jdbcUtil.executeQuery();

      while (rs.next()) {
         StudyGroup group = new StudyGroup();
         group.setStudyGroupId(rs.getInt("studyGroupId"));
         group.setName(rs.getString("name"));
         group.setImg(rs.getString("img"));
         group.setDescription(rs.getString("description"));
         group.setCapacity(rs.getInt("capacity"));
         group.setCategory(rs.getString("category"));
         groupList.add(group);
      }

      jdbcUtil.close();
      return groupList;
   }



   public void toggleStudyGroupLike(String memberId, long studyGroupId) throws SQLException {
      try {

         String checkSql = "SELECT COUNT(*) FROM StudyGroupLike WHERE studyGroupId = ? AND memberId = ?";
         jdbcUtil.setSqlAndParameters(checkSql, new Object[] { studyGroupId, memberId });

         ResultSet rs = jdbcUtil.executeQuery();
         if (rs.next()) {
            int count = rs.getInt(1);

            if (count > 0) {

               String deleteSql = "DELETE FROM StudyGroupLike WHERE studyGroupId = ? AND memberId = ?";
               jdbcUtil.setSqlAndParameters(deleteSql, new Object[] { studyGroupId, memberId });
               jdbcUtil.executeUpdate();
            } else {
               // 좋아요가 없다면 추가
               String insertSql = "INSERT INTO StudyGroupLike (studyLikeId, createAt, studyGroupId, memberId) "
                     + "VALUES (SEQ_STUDY_GROUP_LIKE_ID.nextval, SYSDATE, ?, ?)";
               jdbcUtil.setSqlAndParameters(insertSql, new Object[] { studyGroupId, memberId });
               jdbcUtil.executeUpdate();
            }
         }
      } catch (Exception ex) {
         jdbcUtil.rollback();
         ex.printStackTrace();
      } finally {
         jdbcUtil.commit();
         jdbcUtil.close();
      }
   }    
    
   public List<StudyGroup> getStudyGroupsExcludingStudent(String stuId) {
     
       StringBuffer query = new StringBuffer();
       query.append("SELECT S.studyGroupId, S.name, S.img, S.category, S.description, S.capacity, S.createAt, S.place, ");
       query.append("ic.name AS categoryName, ic.color ");
       query.append("FROM StudyGroup S ");
       query.append("JOIN InterestCategory ic ON S.category = ic.Id ");
       query.append("WHERE S.leaderId != ? ");
       query.append("ORDER BY ");
       query.append("    CASE ");
       query.append("        WHEN S.category IN (");
       query.append("            SELECT INTERESTID ");
       query.append("            FROM studentInterestCategory ");
       query.append("            WHERE stuId = ? ");
       query.append("        ) THEN 1 ");
       query.append("        ELSE 2 ");
       query.append("    END");
       jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { stuId, stuId });
       List<StudyGroup> studyGroupList = new ArrayList<>(); // 변수 선언
       
       try {
           ResultSet rs = jdbcUtil.executeQuery(); // 쿼리 실행
           
           while (rs.next()) {
               StudyGroup studyGroup = new StudyGroup();
               studyGroup.setStudyGroupId(rs.getLong("studyGroupId"));
               studyGroup.setName(rs.getString("name"));
               studyGroup.setCategory(rs.getString("category"));
               studyGroup.setCapacity(rs.getInt("capacity"));
               studyGroup.setImg(rs.getString("img"));
               studyGroup.setDescription(rs.getString("description"));
               studyGroup.setCreateAt(rs.getDate("createAt"));
               studyGroup.setPlace(rs.getString("place"));
               studyGroup.setCategoryColor(rs.getString("color"));
               studyGroup.setCategoryName(rs.getString("categoryName"));

               // 디버깅 출력

               studyGroupList.add(studyGroup); // 리스트에 추가
           }
       } catch (Exception ex) {
           ex.printStackTrace();
       } finally {
           jdbcUtil.close();
       }

       return studyGroupList;
   }
    
    public List<StudyGroup> getStudyGroupsSearch(String stuid, String searchParam) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT S.studyGroupId, S.name, S.img, S.category, S.description, S.capacity, S.createAt, S.place, ");
        query.append("ic.name AS categoryName, ic.color ");
        query.append("FROM StudyGroup S ");
        query.append("JOIN InterestCategory ic ON S.category = ic.Id ");
        query.append("WHERE S.leaderId != ? ");
       
        if (searchParam != null && !searchParam.trim().isEmpty()) {
            query.append("AND (S.name LIKE ? OR ic.name LIKE ?) ");
        }

        List<Object> params = new ArrayList<>();
        params.add(stuid); // 리더 ID (예: ? 자리에 들어갈 값)

        if (searchParam != null && !searchParam.trim().isEmpty()) {
            params.add("%" + searchParam + "%"); // S.name LIKE '%검색어%'
            params.add("%" + searchParam + "%"); // ic.name LIKE '%검색어%'
        }

        jdbcUtil.setSqlAndParameters(query.toString(), params.toArray()); // stuid와 searchName 파라미터 전달
        List<StudyGroup> studyGroupList = new ArrayList<>();

        try {
            ResultSet rs = jdbcUtil.executeQuery(); // 쿼리 실행

            while (rs.next()) {
                // 각 열의 값을 StudyGroup 객체에 매핑
                StudyGroup studyGroup = new StudyGroup();
                studyGroup.setStudyGroupId(rs.getLong("studyGroupId"));
                studyGroup.setName(rs.getString("name"));
                studyGroup.setCategory(rs.getString("category"));
                studyGroup.setCapacity(rs.getInt("capacity"));
                studyGroup.setImg(rs.getString("img"));
                studyGroup.setDescription(rs.getString("description"));
                studyGroup.setCreateAt(rs.getDate("createAt"));
                studyGroup.setPlace(rs.getString("place"));
                studyGroup.setCategoryColor(rs.getString("color"));
                studyGroup.setCategoryName(rs.getString("categoryName"));

                studyGroupList.add(studyGroup);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            jdbcUtil.close(); // 리소스 해제
        }

        return studyGroupList; // 결과 반환
    }
    
    public StudyGroupApplication createApplication(String memberId, long studyGroupId) throws SQLException { 
        // 새로운 StudyGroupApplication 객체 생성
        StudyGroupApplication application = new StudyGroupApplication();
        application.setStatus("진행중");
        application.setMemberId(memberId);
        application.setStudyGroupId(studyGroupId);
        
        // 스터디 그룹 요청 생성 SQL
        String sql = "INSERT INTO StudyGroupApplication (studyGroupApplicationId, status, createAt, stuId, studyGroupId) "
                   + "VALUES (SEQ_STUDY_GROUP_APPLICATION_ID.nextval, ?, SYSDATE, ?, ?)";
        
        Object[] param = new Object[] {
            application.getStatus(),   
            memberId,                 
            studyGroupId               
        };         
        
        jdbcUtil.setSqlAndParameters(sql, param); // SQL과 매개변수 설정
                            
        String key[] = {"studyGroupApplicationId"}; // 자동 생성된 키 반환 설정
        try {    
            jdbcUtil.executeUpdate(key);  
            ResultSet rs = jdbcUtil.getGeneratedKeys();
            if (rs.next()) {
                int generatedKey = rs.getInt(1);  // 자동 생성된 ID 가져오기
                application.setStudyGroupApplicationId(generatedKey);  
            }
            return application;  // 생성된 객체 반환
        } catch (Exception ex) {
            jdbcUtil.rollback();
            ex.printStackTrace();
        } finally {     
            jdbcUtil.commit();
            jdbcUtil.close();   
        }       
        return null;            
    }
    
    
    //스터디 그룹 요청 상태 가져오기
    public String getStatusByMemberIdAndGroupId(String memberId, long studyGroupId) throws SQLException {
        String sql = "SELECT status " +
                     "FROM StudyGroupApplication " +
                     "WHERE stuId = ? AND studyGroupId = ?";
        
        jdbcUtil.setSqlAndParameters(sql, new Object[]{memberId, studyGroupId}); // SQL과 매개변수 설정
        
        try {
            ResultSet rs = jdbcUtil.executeQuery(); // 쿼리 실행
            if (rs.next()) {
                return rs.getString("status"); // status 값 반환
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            jdbcUtil.close(); // 리소스 해제
        }
        return null; // 조회된 값이 없으면 null 반환
    }
    
    public List<StudyGroupApplication> getStudyRequestList(long groupId) throws SQLException {
        List<StudyGroupApplication> groupList = new ArrayList<>();
       
        String sql = "SELECT sga.studygroupapplicationId, sga.status, sga.createat, sga.stuid, sga.studygroupid, m.name AS student_name " +
                "FROM studygroupapplication sga " +
                "JOIN member m ON sga.stuId = m.id " +
                "WHERE sga.status = '진행중' AND sga.studygroupid = ?";

        
        jdbcUtil.setSqlAndParameters(sql, new Object[]{groupId}); // memberId를 두 번 파라미터로 설정
        ResultSet rs = jdbcUtil.executeQuery();
        
        while (rs.next()) {
            StudyGroupApplication group = new StudyGroupApplication();
            group.setStudyGroupApplicationId(rs.getLong("studygroupapplicationId")); 
            group.setStatus(rs.getString("status")); 
            group.setCreateAt(rs.getDate("createat"));
            group.setMemberId(rs.getString("stuId")); 
            group.setStudyGroupId(rs.getLong("studygroupid")); 
            group.setMemberName(rs.getString("student_name")); // 학생 이름

            groupList.add(group);
        }
        
        jdbcUtil.close();
        return groupList;
    }
    
    public StudyGroupApplication findById(Long applicationId) throws SQLException {
        StudyGroupApplication application = null;
        
        String sql = "SELECT sga.studygroupapplicationId, sga.status, sga.createat, sga.stuId, "
                + "sga.studygroupid, m.name AS student_name "
                + "FROM studygroupapplication sga "
                + "JOIN member m ON sga.stuId = m.id "
                + "WHERE sga.studygroupapplicationId = ?";
        
        
        jdbcUtil.setSqlAndParameters(sql, new Object[]{applicationId});
        try {
            ResultSet rs = jdbcUtil.executeQuery();

            if (rs.next()) {
                application = new StudyGroupApplication();
                application.setStudyGroupApplicationId(rs.getLong("studygroupapplicationId"));
                application.setStatus(rs.getString("status"));
                application.setCreateAt(rs.getDate("createat"));
                application.setMemberId(rs.getString("stuId"));
                application.setStudyGroupId(rs.getLong("studygroupid"));
                application.setMemberName(rs.getString("student_name"));
            }
        } finally {
            jdbcUtil.close();
        }

        return application;
    }
    
    
 // 요청 상태를 "수락"으로 변경
    public void acceptApplication(long applicationId) throws SQLException {
        
        String updateStatusSql = "UPDATE StudyGroupApplication SET status = '수락' WHERE studyGroupApplicationId = ?";
        
        try {
            // 상태 변경 실행
            jdbcUtil.setSqlAndParameters(updateStatusSql, new Object[]{applicationId});
            jdbcUtil.executeUpdate();
        } catch (Exception ex) {
            jdbcUtil.rollback();
            ex.printStackTrace();
        } finally {
            jdbcUtil.commit();
            jdbcUtil.close(); // resource 반환
        }
    }
    
  public void deleteApplication(long applicationId) throws SQLException {
        
        String updateStatusSql = "DELETE FROM StudyGroupApplication WHERE studyGroupApplicationId = ?";
        
        try {
            // 상태 변경 실행
            jdbcUtil.setSqlAndParameters(updateStatusSql, new Object[]{applicationId});
            jdbcUtil.executeUpdate();
        } catch (Exception ex) {
            jdbcUtil.rollback();
            ex.printStackTrace();
        } finally {
            jdbcUtil.commit();
            jdbcUtil.close(); // resource 반환
        }
    }
    
    //팀원으로 가입된 스터디 그룹 가져오기 
    public List<StudyGroup> getStudyGroupListByMember(String memberId) throws SQLException {
        List<StudyGroup> groupList = new ArrayList<>();
       
        String sql = "SELECT sg.studyGroupId, sg.name, sg.img, sg.description, sg.capacity, sg.category, ic.name AS categoryName, ic.color " +
                     "FROM StudyGroup sg " +
                     "JOIN StudyGroupApplication sga ON sg.studyGroupId = sga.studyGroupId " +
                     "JOIN InterestCategory ic ON sg.category = ic.Id "+
                     "WHERE sga.stuId = ? AND sga.status = '수락' AND sg.leaderId != ?";

        jdbcUtil.setSqlAndParameters(sql, new Object[]{memberId, memberId}); // memberId를 두 번 파라미터로 설정
        ResultSet rs = jdbcUtil.executeQuery();
        
        while (rs.next()) {
            StudyGroup group = new StudyGroup();
            group.setStudyGroupId(rs.getInt("studyGroupId"));
            group.setName(rs.getString("name"));
            group.setImg(rs.getString("img"));
            group.setDescription(rs.getString("description"));
            group.setCapacity(rs.getInt("capacity"));
            group.setCategory(rs.getString("category"));
            group.setCategoryColor(rs.getString("color"));
            group.setCategoryName(rs.getString("categoryName"));
            groupList.add(group);
        }
        
        jdbcUtil.close();
        return groupList;
    }
    
    //팀장으로 가입된 스터디 가져오기
    public List<StudyGroup> getStudyGroupListByLeader(String memberId) throws SQLException {
        List<StudyGroup> groupList = new ArrayList<>();
        
        
        String sql = "SELECT sg.studyGroupId, sg.name, sg.img, sg.description, sg.capacity, sg.category, ic.name AS categoryName, ic.color "+ 
                     "FROM StudyGroup sg " +
                     "JOIN InterestCategory ic ON sg.category = ic.Id "+
                     "WHERE sg.leaderId = ?";

        jdbcUtil.setSqlAndParameters(sql, new Object[]{memberId}); 
        ResultSet rs = jdbcUtil.executeQuery();
        
        while (rs.next()) {
            StudyGroup group = new StudyGroup();
            group.setStudyGroupId(rs.getInt("studyGroupId"));
            group.setName(rs.getString("name"));
            group.setImg(rs.getString("img"));
            group.setDescription(rs.getString("description"));
            group.setCapacity(rs.getInt("capacity"));
            group.setCategory(rs.getString("category"));
            group.setCategoryColor(rs.getString("color"));
            group.setCategoryName(rs.getString("categoryName"));
            groupList.add(group);
        }
        
        jdbcUtil.close();
        return groupList;
    }
    
    
    public boolean isLikedByUser(String memberId, long studyGroupId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM StudyGroupLike WHERE stuId = ? AND studyGroupId = ?";
        jdbcUtil.setSqlAndParameters(sql, new Object[]{memberId, studyGroupId});

        ResultSet rs = jdbcUtil.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0; // 좋아요가 있으면 true, 없으면 false
        }
        return false;
    }

    // 좋아요 추가
    public void addLike(String memberId, long studyGroupId) throws SQLException {
        String sql = "INSERT INTO StudyGroupLike (studyLikeId, createAt, studyGroupId, stuId) "
                     + "VALUES (SEQ_STUDY_GROUP_LIKE_ID.nextval, SYSDATE, ?, ?)";
        jdbcUtil.setSqlAndParameters(sql, new Object[]{studyGroupId, memberId});
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
    public void removeLike(String memberId, long studyGroupId) throws SQLException {
        String sql = "DELETE FROM StudyGroupLike WHERE studyGroupId = ? AND stuId = ?";
        jdbcUtil.setSqlAndParameters(sql, new Object[]{studyGroupId, memberId});
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

    //좋아요 누른 스터디 그룹들 가지고 오기
    public List<StudyGroup> getLikedStudyGroups(String memberId) throws SQLException {
        List<StudyGroup> groupList = new ArrayList<>();
        
        String sql = "SELECT sg.studyGroupId, sg.name, sg.img, sg.category, ic.name AS categoryName, ic.color " +
                     "FROM StudyGroupLike sgLike " +
                     "JOIN StudyGroup sg ON sgLike.studyGroupId = sg.studyGroupId " +
                     "JOIN InterestCategory ic ON sg.category = ic.Id "+
                     "WHERE sgLike.stuId = ?";
                     
        jdbcUtil.setSqlAndParameters(sql, new Object[]{memberId});
        ResultSet rs = jdbcUtil.executeQuery();
        
        
        while (rs.next()) {
            StudyGroup group = new StudyGroup();
            group.setStudyGroupId(rs.getInt("studyGroupId"));
            group.setName(rs.getString("name"));
            group.setImg(rs.getString("img"));
            group.setCategory(rs.getString("category"));
            group.setCategoryColor(rs.getString("color"));
            group.setCategoryName(rs.getString("categoryName"));
            
            groupList.add(group);
        }
        
        jdbcUtil.close();
        return groupList;
    }
    
    //리뷰 작성을 위한 스터디 그룹 소속인지 아닌지 확인
    public boolean isMemberOfStudyGroup(String memberId, long studyGroupId) throws SQLException {
        String sql = "SELECT COUNT(*) " +
                     "FROM StudyGroup sg " +
                     "FULL OUTER JOIN StudyGroupApplication sga ON sga.studyGroupId = sg.studyGroupId " +
                     "WHERE (sga.studyGroupId = ? AND sga.stuId = ? AND sga.status = '수락') " +
                     "OR (sg.studyGroupId = ? AND sg.leaderId = ?)";

        jdbcUtil.setSqlAndParameters(sql, new Object[]{studyGroupId, memberId, studyGroupId, memberId});
        
        try {
            ResultSet rs = jdbcUtil.executeQuery();

            if (rs != null && rs.next()) {
                int count = rs.getInt(1);  // COUNT(*) 결과

                return count > 0;  // count가 0보다 크면 멤버
            }

            return false;  // 결과가 없으면 false
        } finally {
            jdbcUtil.close();
        }
    }
    
    // 스터디후기 생성
    public StudyGroupReview insertReview(StudyGroupReview review) throws SQLException {
        try {
            
                StringBuffer query = new StringBuffer();
                query.append(" INSERT INTO StudyGroupReview (studyGroupReviewId, reviewText, studyGroupId, stuId) ");
                query.append(" VALUES (SEQ_STUDY_GROUP_REVIEW_ID.nextval, ?, ?, ?) ");

                // memberId를 review의 stuId로 설정
                Object[] param = new Object[] { review.getReviewText(),    // reviewText는 String 타입
                        review.getStudyGroupId(),    // studyGroupId는 Long 타입
                        review.getStuId()  };      // memberId는 String 타입

                jdbcUtil.setSqlAndParameters(query.toString(), param); // JDBCUtil에 질의문과 파라미터 설정

                // 삽입 실행
                int result = jdbcUtil.executeUpdate();

                if (result > 0) {
                    jdbcUtil.commit();
                    return review; // 삽입 성공 시 입력된 review 객체 반환
                } else {
                    throw new RuntimeException("Failed to insert StudyGroupReview.");
                }

           
        } catch (Exception ex) {
            jdbcUtil.rollback();
            ex.printStackTrace();
            throw new SQLException("오류가 발생했습니다.", ex);
        } finally {
            jdbcUtil.close();  // 자원 해제
        }
    }
  
  //스터디 그룹 리뷰 
    public List<StudyGroupReview> getReviewsByGroupId(long groupId) {
        StringBuffer query = new StringBuffer();
        query.append("""
                SELECT
                    sgr.studyGroupReviewId,
                    sgr.reviewText,
                    sgr.studyGroupId,
                    sgr.stuId,
                    m.name
                FROM
                    StudyGroupReview sgr
                JOIN
                    Member m
                ON
                    sgr.stuId = m.Id
                WHERE
                    sgr.studyGroupId = ?
            """);

        jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { groupId });

        List<StudyGroupReview> reviews = new ArrayList<>();
        try {
            ResultSet rs = jdbcUtil.executeQuery();
            while (rs.next()) {
                StudyGroupReview review = new StudyGroupReview();
                review.setStudyGroupReviewId(rs.getLong("studyGroupReviewId"));
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


  //팀원으로 가입된 스터디 그룹 가져오기 
    public List<String> findStudyMembers(int studyGruopId) throws SQLException {
    	List<String> members = new ArrayList<>();
       
        String sql = "SELECT s.name " +
                     "FROM Student s " +
                     "JOIN StudyGroupApplication sga ON s.id = sga.stuId " +
                     "WHERE sga.studyGroupId = ? AND sga.status = '수락'";

        jdbcUtil.setSqlAndParameters(sql, new Object[]{studyGruopId}); // memberId를 두 번 파라미터로 설정
        ResultSet rs = jdbcUtil.executeQuery();
        
        while (rs.next()) {
        	members.add(rs.getString("name"));
        }
        
        jdbcUtil.close();
        return members;
    }

    public List<Member> findStudyMemberDetails(int studyGroupId) throws SQLException {
    	List<Member> members = new ArrayList<>();

        String sql = "SELECT s.id, s.name, s.email, s.phone, m.img " +
                     "FROM Student s " +
                     "JOIN Member m ON s.id = m.id " +
                     "JOIN StudyGroupApplication sga ON s.id = sga.stuId " +
                     "WHERE sga.studyGroupId = ? AND sga.status = '수락' " +
                     "ORDER BY s.name";

        jdbcUtil.setSqlAndParameters(sql, new Object[]{studyGroupId});
        ResultSet rs = jdbcUtil.executeQuery();

        while (rs.next()) {
        	members.add(new Member(
        			rs.getString("id"),
        			null,
        			rs.getString("name"),
        			rs.getString("email"),
        			rs.getString("phone"),
        			rs.getString("img")));
        }

        jdbcUtil.close();
        return members;
    }

    public List<LocalDate> findMonthSchedule(int studyGroupId, int month, int year) {
        List<LocalDate> eventDates = new ArrayList<>();

        String sql =
                "SELECT DISTINCT TRUNC(startDate) AS eventDate " +
                "FROM studySchedule " +
                "WHERE (type <> 'regular' OR type IS NULL) " +
                "AND EXTRACT(MONTH FROM startDate) = ? " +
                "AND EXTRACT(YEAR FROM startDate) = ? " +
                "AND studyGroupId = ? " +

                "UNION " +

                "SELECT DISTINCT TRUNC(createAt) AS eventDate " +
                "FROM studyNotice " +
                "WHERE EXTRACT(MONTH FROM createAt) = ? " +
                "AND EXTRACT(YEAR FROM createAt) = ? " +
                "AND studyGroupId = ? " +

                "UNION " +

                "SELECT DISTINCT TRUNC(dueDate) AS eventDate " +
                "FROM studyAssignment " +
                "WHERE EXTRACT(MONTH FROM dueDate) = ? " +
                "AND EXTRACT(YEAR FROM dueDate) = ? " +
                "AND studyGroupId = ? " +

                "UNION " +

                "SELECT DISTINCT TRUNC(createAt) AS eventDate " +
                "FROM studyAssignment " +
                "WHERE createAt IS NOT NULL " +
                "AND EXTRACT(MONTH FROM createAt) = ? " +
                "AND EXTRACT(YEAR FROM createAt) = ? " +
                "AND studyGroupId = ? " +

                "ORDER BY eventDate";

        jdbcUtil.setSqlAndParameters(sql,
                new Object[]{month, year, studyGroupId, month, year, studyGroupId, month, year, studyGroupId, month, year, studyGroupId});

        try {
            ResultSet rs = jdbcUtil.executeQuery();

            while (rs.next()) {
                Date eventDate = rs.getDate("eventDate");
                if (eventDate != null) {
                    eventDates.add(eventDate.toLocalDate());
                }
            }
            return eventDates;
        } catch (Exception ex) {
            jdbcUtil.rollback();
            ex.printStackTrace();
        } finally {
            jdbcUtil.close();
        }

        return eventDates;
    }



  //스터디 생성 중복확인
    public boolean isStudyConflict(String stuId, String newDayOfWeek) throws SQLException {
    	 // SQL 쿼리
        String sql = """
            SELECT 
                CASE 
                    WHEN EXISTS (
                        SELECT 1
                        FROM STUDYGROUPAPPLICATION sga
                        JOIN STUDYSCHEDULE ss ON sga.STUDYGROUPID = ss.STUDYGROUPID
                        WHERE sga.STUID = ?
                          AND sga.STATUS = '수락'
                          AND ss.DAYOFWEEK = ?
                          AND ss.TYPE = 'regular'
                    ) THEN 'true'
                    ELSE 'false'
                END AS is_overlap
            FROM DUAL
        """;

        Object[] params = new Object[] { stuId, newDayOfWeek};
        jdbcUtil.setSqlAndParameters(sql, params);

        try {
            ResultSet rs = jdbcUtil.executeQuery();
            if (rs.next()) {
                String isOverlap = rs.getString("is_overlap");
                return "true".equals(isOverlap); // 'true'와 비교하여 boolean 값으로 반환
            }
            return false; // 결과가 없으면 false 반환
        } finally {
            jdbcUtil.close(); // 리소스 해제
        }
    }
}
