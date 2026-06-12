package model.dao.lecture;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import model.dao.JDBCUtil;
import model.domain.Notice;
import model.domain.lecture.Lecture;
import model.domain.lecture.LectureEnrollment;
import model.domain.studyGroup.StudyGroupApplication;

public class LectureDao {
    private JDBCUtil jdbcUtil = null; // JDBCUtil 필드 선언

    public LectureDao() { // 생성자
        jdbcUtil = new JDBCUtil(); // JDBCUtil 객체 생성
    }

    public Lecture createLecture(Lecture lecture) { // 강의 추가
        StringBuffer query = new StringBuffer();
        int result = 0;
        query.append("INSERT INTO Lecture (lectureId, name, img, category, capacity, lectureLevel, createdAt, teacherId, lectureRoom, description) ");
        query.append("VALUES (SEQ_LECTURE_ID.nextval, ?, '/images/eduLogo.png', ?, ?, ?, SYSDATE, ?, ?, ?) ");

        Object[] param = new Object[] {lecture.getName(), 
                lecture.getCategory(), lecture.getCapacity(), lecture.getLevel(), lecture.getTeacherId(), lecture.getLectureRoom(), lecture.getDescription()};

        jdbcUtil.setSqlAndParameters(query.toString(), param); // JDBCUtil에 질의문과 파라미터 설정

        String key[]={"lectureId"};  // PK 컬럼(들)의 이름 배열       
        try {
            result = jdbcUtil.executeUpdate(key);
            if (result > 0) {
               ResultSet rs = jdbcUtil.getGeneratedKeys();
                if(rs.next()) {
                   long generatedKey = rs.getLong(1);   // 생성된 PK 값
                   lecture.setLectureId(generatedKey);    // id필드에 저장  
                }
                return lecture;
            }
            
            
        } catch (Exception ex) {
            jdbcUtil.rollback();
            ex.printStackTrace();
        } finally {
            jdbcUtil.commit();
            jdbcUtil.close(); // ResultSet, PreparedStatement, Connection 등 해제
        }
        return null;
    }

   
    
    // 강의 삭제 메서드
    public int deleteLecture(long lectureId) {
        StringBuffer query = new StringBuffer();
        int result = 0;
        query.append("DELETE FROM Lecture WHERE lectureId = ?");

        jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { lectureId }); // JDBCUtil에 질의문과 파라미터 설정

        try {
            result = jdbcUtil.executeUpdate();
        } catch (Exception ex) {
            jdbcUtil.rollback();
            ex.printStackTrace();
        } finally {
            jdbcUtil.commit();
            jdbcUtil.close(); // ResultSet, PreparedStatement, Connection 등 해제
        }
        return result;
    }

    // 강의 수정 메서드
    public int updateLecture(Lecture lecture) {
        StringBuffer query = new StringBuffer();
        int result = 0;
        query.append("UPDATE Lecture ");
        query.append("SET name = ?, img = ?, category = ?, capacity = ?, lectureLevel = ?, lectureRoom = ?, description = ? ");
        query.append("WHERE lectureId = ?");

        Object[] param = new Object[] { lecture.getName(), lecture.getImg(), lecture.getCategory(),
                lecture.getCapacity(), lecture.getLevel(), lecture.getLectureRoom(), lecture.getDescription(), lecture.getLectureId() };

        jdbcUtil.setSqlAndParameters(query.toString(), param); // JDBCUtil에 질의문과 파라미터 설정

        try {
            result = jdbcUtil.executeUpdate();
        } catch (Exception ex) {
            jdbcUtil.rollback();
            ex.printStackTrace();
        } finally {
            jdbcUtil.commit();
            jdbcUtil.close(); // ResultSet, PreparedStatement, Connection 등 해제
        }
        return result;
    }
    

    //강의 상세 조회
    public Lecture findLectureById(long lectureId) {
        StringBuffer query = new StringBuffer();
        query.append("SELECT l.name AS lectureName, l.description, l.img, l.category, l.capacity, ");
        query.append("l.lectureLevel, l.teacherId, l.createdAt, l.lectureroom, ");
        query.append("t.name AS teacherName, t.phone AS teacherPhone ");
        query.append("FROM lecture l ");
        query.append("JOIN teacher t ON l.teacherId = t.id "); // TEACHER 테이블과 JOIN
        query.append("WHERE l.lectureId = ?");

        jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { lectureId });

        try {
            Lecture lecture = null;
            ResultSet rs = jdbcUtil.executeQuery(); // 질의 실행
            if (rs.next()) {
                lecture = new Lecture();
                lecture.setLectureId(lectureId);
                lecture.setName(rs.getString("lectureName"));
                lecture.setDescription(rs.getString("description"));
                lecture.setImg(rs.getString("img"));
                lecture.setCategory(rs.getString("category"));
                lecture.setCapacity(rs.getLong("capacity"));
                lecture.setLevel(rs.getInt("lectureLevel"));
                lecture.setTeacherId(rs.getString("teacherId"));
                lecture.setLectureRoom(rs.getInt("lectureroom"));
                
                String teacherName = rs.getString("teacherName");
                String teacherPhone = rs.getString("teacherPhone");
                lecture.setTeacherName(teacherName); 
                lecture.setPhone(teacherPhone); 

               
            }
            return lecture;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            jdbcUtil.close(); // ResultSet, PreparedStatement, Connection 등 해제
        }
        return null;
    }
    
    public List<Lecture> getAllLectures() {
        StringBuffer query = new StringBuffer();
        query.append("SELECT lectureId, name, img, category, capacity, lecturelevel, teacherId ");
        query.append("FROM Lecture");

        jdbcUtil.setSqlAndParameters(query.toString(), null); // 파라미터 없음
        List<Lecture> lectureList = new ArrayList<>(); // 결과를 담을 리스트

        try {
            ResultSet rs = jdbcUtil.executeQuery(); // 쿼리 실행
            
            while (rs.next()) {
                // 각 열의 값을 Lecture 객체에 매핑
                Lecture lecture = new Lecture();
                lecture.setLectureId(rs.getLong("lectureId"));
                lecture.setName(rs.getString("name"));
                lecture.setImg(rs.getString("img"));
                lecture.setCategory(rs.getString("category"));
                lecture.setCapacity(rs.getInt("capacity"));
                lecture.setLevel(rs.getInt("lecturelevel"));

                // 로그 찍기: Lecture 객체의 각 필드 값 출력
                System.out.println("Lecture ID: " + lecture.getLectureId());
                System.out.println("Lecture Name: " + lecture.getName());
                System.out.println("Lecture Img: " + lecture.getImg());
                System.out.println("Lecture Category: " + lecture.getCategory());
                System.out.println("Lecture Capacity: " + lecture.getCapacity());
                System.out.println("Lecture Level: " + lecture.getLevel());

                lectureList.add(lecture); // 리스트에 추가
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            jdbcUtil.close(); // 리소스 해제
        }
        return lectureList; // 결과 반환
    }
    
    public Lecture getLectureById(long lectureId) {
        StringBuffer query = new StringBuffer();
        query.append("SELECT lectureId, name, img, category, capacity, lecturelevel, teacherId ");
        query.append("FROM Lecture WHERE lectureId = ?"); // lectureId로 조건 추가
       
        List<Object> params = new ArrayList<>();
        params.add(lectureId);

        Object[] paramsArray = params.toArray();

        jdbcUtil.setSqlAndParameters(query.toString(), paramsArray); 
        Lecture lecture = null;

        try {
            ResultSet rs = jdbcUtil.executeQuery(); // 쿼리 실행

            if (rs.next()) {
                // 강의 데이터가 있으면 Lecture 객체에 매핑
                lecture = new Lecture();

                String name = rs.getString("name");
                String img = rs.getString("img");
                String category = rs.getString("category");
                int capacity = rs.getInt("capacity");
                int level = rs.getInt("lecturelevel");
                String teacherId = rs.getString("teacherId");

                lecture.setLectureId(lectureId); // lectureId는 메서드 파라미터로 전달받음
                lecture.setName(name);
                lecture.setImg(img);
                lecture.setCategory(category);
                lecture.setCapacity(capacity);
                lecture.setLevel(level);
                lecture.setTeacherId(teacherId);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            jdbcUtil.close(); // 리소스 해제
        }

        return lecture; // 결과 반환
    }

    //수강하지 않고 있는 강의 목록 보여주기(수강 신청용)
    public List<Lecture> getLecturesExcludingStudent(String stuid) {
        StringBuffer query = new StringBuffer();
        query.append("SELECT L.lectureId, L.name, L.img, L.category, L.capacity, L.lecturelevel, ");
        query.append("T.name AS teacherName, ic.name AS categoryName, ic.color ");
        query.append("FROM Lecture L ");
        query.append("JOIN Teacher T ON L.teacherId = T.Id "); // Teacher 테이블과 조인
        query.append("JOIN InterestCategory ic ON L.category = ic.Id "); 
        query.append("WHERE NOT EXISTS (");
        query.append("    SELECT 1 ");
        query.append("    FROM LectureEnrollment ");
        query.append("    WHERE LectureEnrollment.lectureId = L.lectureId ");
        query.append("    AND LectureEnrollment.stuid = ? ");
        query.append(") ");
        query.append("ORDER BY ");
        query.append("    CASE ");
        query.append("        WHEN L.category IN (");
        query.append("            SELECT INTERESTID ");
        query.append("            FROM studentInterestCategory ");
        query.append("            WHERE stuid = ? ");
        query.append("        ) THEN 1 ");
        query.append("        ELSE 2 ");
        query.append("    END");

        jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { stuid, stuid }); // stuid 파라미터 전달
        List<Lecture> lectureList = new ArrayList<>(); // 결과를 담을 리스트

        try {
            ResultSet rs = jdbcUtil.executeQuery(); // 쿼리 실행

            while (rs.next()) {
                // 각 열의 값을 Lecture 객체에 매핑
                Lecture lecture = new Lecture();
                lecture.setLectureId(rs.getLong("lectureId"));
                lecture.setName(rs.getString("name"));
                lecture.setImg(rs.getString("img"));
                lecture.setCategory(rs.getString("category"));
                lecture.setCapacity(rs.getInt("capacity"));
                lecture.setLevel(rs.getInt("lecturelevel"));
                lecture.setTeacherName(rs.getString("teacherName"));
                lecture.setCategoryColor(rs.getString("color"));
                lecture.setCategoryName(rs.getString("categoryName"));
                
                // 로그 찍기: Lecture 객체의 각 필드 값 출력
                System.out.println("Lecture ID: " + lecture.getLectureId());
                System.out.println("Lecture Name: " + lecture.getName());
                System.out.println("Lecture Img: " + lecture.getImg());
                System.out.println("Lecture Category: " + lecture.getCategory());
                System.out.println("Lecture Capacity: " + lecture.getCapacity());
                System.out.println("Lecture Level: " + lecture.getLevel());
                System.out.println("Lecture teacherName: " + lecture.getTeacherName());
                System.out.println("Lecture CategoryColor: " + lecture.getCategoryColor());
                System.out.println("Lecture CategoryName: " + lecture.getCategoryName());
                

                lectureList.add(lecture); // 리스트에 추가
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            jdbcUtil.close(); // 리소스 해제
        }

        return lectureList; // 결과 반환
    }
    
    public List<Lecture> getLecturesSearch(String stuid, String searchParam) {
        StringBuffer query = new StringBuffer();
        query.append("SELECT L.lectureId, L.name, L.img, L.category, L.capacity, L.lecturelevel, ");
        query.append("T.name AS teacherName, ic.name AS categoryName, ic.color ");
        query.append("FROM Lecture L ");
        query.append("JOIN Teacher T ON L.teacherId = T.Id "); // Teacher 테이블과 조인
        query.append("JOIN InterestCategory ic ON L.category = ic.Id "); 
        query.append("WHERE NOT EXISTS (");
        query.append("    SELECT 1 ");
        query.append("    FROM LectureEnrollment ");
        query.append("    WHERE LectureEnrollment.lectureId = L.lectureId ");
        query.append("    AND LectureEnrollment.stuid = ? ");
        query.append(")");


        
        if (searchParam != null && !searchParam.trim().isEmpty()) {
            query.append("AND (L.name LIKE ? OR ic.name LIKE ? OR T.name LIKE ?) ");
        }

        List<Object> params = new ArrayList<>();
        params.add(stuid);
        
        if (searchParam != null && !searchParam.trim().isEmpty()) {
            params.add("%" + searchParam + "%"); // S.name LIKE '%검색어%'
            params.add("%" + searchParam + "%"); // ic.name LIKE '%검색어%'
            params.add("%" + searchParam + "%"); // ic.name LIKE '%검색어%'
        }


        jdbcUtil.setSqlAndParameters(query.toString(), params.toArray()); // stuid와 searchName 파라미터 전달
        List<Lecture> lectureList = new ArrayList<>(); // 결과를 담을 리스트

        try {
            ResultSet rs = jdbcUtil.executeQuery(); // 쿼리 실행

            while (rs.next()) {
                // 각 열의 값을 Lecture 객체에 매핑
                Lecture lecture = new Lecture();
                lecture.setLectureId(rs.getLong("lectureId"));
                lecture.setName(rs.getString("name"));
                lecture.setImg(rs.getString("img"));
                lecture.setCategory(rs.getString("category"));
                lecture.setCapacity(rs.getInt("capacity"));
                lecture.setLevel(rs.getInt("lecturelevel"));
                lecture.setTeacherName(rs.getString("teacherName"));
                lecture.setCategoryColor(rs.getString("color"));
                lecture.setCategoryName(rs.getString("categoryName"));
                
                

                lectureList.add(lecture); // 리스트에 추가
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            jdbcUtil.close(); // 리소스 해제
        }

        return lectureList; // 결과 반환
    }

    public List<Lecture> getMyLectureList(String stuid) {
        StringBuffer query = new StringBuffer();
        query.append("SELECT l.lectureId, l.name, l.img, l.category, t.name AS teacherName,  ic.name AS categoryName, ic.color ");
        query.append("FROM Lecture l ");
        query.append("JOIN LectureEnrollment le ON l.lectureId = le.lectureId ");
        query.append("JOIN Teacher t ON l.teacherId = t.Id "); 
        query.append("JOIN InterestCategory ic ON l.category = ic.Id "); 
        query.append("WHERE le.stuId = ?");
        
        jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { stuid }); // stuid 파라미터 전달
        List<Lecture> lectureList = new ArrayList<>(); // 결과를 담을 리스트

        try {
            ResultSet rs = jdbcUtil.executeQuery(); // 쿼리 실행

            while (rs.next()) {
                // 각 열의 값을 Lecture 객체에 매핑
                Lecture lecture = new Lecture();
                lecture.setLectureId(rs.getLong("lectureId"));
                lecture.setName(rs.getString("name"));
                lecture.setImg(rs.getString("img"));
                lecture.setCategory(rs.getString("category"));
                lecture.setTeacherName(rs.getString("teacherName"));
                lecture.setCategoryColor(rs.getString("color"));
                lecture.setCategoryName(rs.getString("categoryName"));
               
 
                lectureList.add(lecture); // 리스트에 추가
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            jdbcUtil.close(); // 리소스 해제
        }

        return lectureList; // 결과 반환
    }

    
    public List<Lecture> getMyLectureListByTeacher(String teacherId) {
        StringBuffer query = new StringBuffer();
        query.append("SELECT l.lectureId, l.name, l.img, l.category, t.name AS teacherName, ic.name AS categoryName, ic.color ");
        query.append("FROM Lecture l ");
        query.append("JOIN Teacher t ON l.teacherId = t.Id "); 
        query.append("JOIN InterestCategory ic ON l.category = ic.Id "); 
        query.append("WHERE l.teacherId = ?");
        
        jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { teacherId }); // stuid 파라미터 전달
        List<Lecture> lectureList = new ArrayList<>(); // 결과를 담을 리스트

        try {
            ResultSet rs = jdbcUtil.executeQuery(); // 쿼리 실행

            while (rs.next()) {
                // 각 열의 값을 Lecture 객체에 매핑
                Lecture lecture = new Lecture();
                lecture.setLectureId(rs.getLong("lectureId"));
                lecture.setName(rs.getString("name"));
                lecture.setImg(rs.getString("img"));
                lecture.setCategory(rs.getString("category"));
                lecture.setTeacherName(rs.getString("teacherName"));
                lecture.setCategoryColor(rs.getString("color"));
                lecture.setCategoryName(rs.getString("categoryName"));
                
 
                lectureList.add(lecture); // 리스트에 추가
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            jdbcUtil.close(); // 리소스 해제
        }

        return lectureList; // 결과 반환
    }

    
    //강의 수강신청 
    public LectureEnrollment createLectureEnrollment(String memberId, long lectureId) throws SQLException { 
        // 새로운 LectureEnrollment 객체 생성
        LectureEnrollment enrollment = new LectureEnrollment();
        enrollment.setMemberId(memberId);
        enrollment.setLectureId(lectureId);
        
        String insertSql = "INSERT INTO LectureEnrollment (enrollmentId, LECTUREID, STUID) VALUES (SEQ_LECTURE_ENROLLMENT_ID.nextval, ?, ?)";
        Object[] insertParams = new Object[] { lectureId, memberId };
        String[] key = {"enrollmentId"}; // 자동 생성된 키 반환 설정
        jdbcUtil.setSqlAndParameters(insertSql, insertParams); // 삽입 SQL 설정

        try {
            jdbcUtil.executeUpdate(key);
            ResultSet rs = jdbcUtil.getGeneratedKeys();
            if (rs.next()) {
                int generatedKey = rs.getInt(1); // 자동 생성된 ID 가져오기
                enrollment.setEnrollmentId(generatedKey);  // 객체에 ID 설정
            }
            return enrollment; 
        } catch (Exception ex) {
            jdbcUtil.rollback(); 
            ex.printStackTrace(); // 로그를 남기기 위해 로깅 도입 필요
        } finally {
           jdbcUtil.commit();       
           jdbcUtil.close(); 
        }

        return null; 
    }
    
    //강의 스케줄 중복확인
    public boolean isLectureConflict(String memberId, long lectureId) throws SQLException {
        String sql = "WITH TargetLecture AS ( " +
                     "  SELECT DAYOFWEEK, STARTTIME, ENDTIME " +
                     "  FROM LectureSchedule " +
                     "  WHERE LECTUREID = ? " +
                     "    AND TYPE = 'regular' " +
                     ") " +
                     "SELECT 1 " +
                     "FROM LectureEnrollment le " +
                     "JOIN LectureSchedule ls ON le.LECTUREID = ls.LECTUREID " +
                     "JOIN TargetLecture tl ON ls.DAYOFWEEK = tl.DAYOFWEEK " +
                     "WHERE le.STUID = ? " +
                     "  AND ls.TYPE = 'regular' " +
                     "  AND ( " +
                     "        tl.STARTTIME BETWEEN ls.STARTTIME AND ls.ENDTIME OR " +
                     "        tl.ENDTIME BETWEEN ls.STARTTIME AND ls.ENDTIME OR " +
                     "        ls.STARTTIME BETWEEN tl.STARTTIME AND tl.ENDTIME OR " +
                     "        ls.ENDTIME BETWEEN tl.STARTTIME AND tl.ENDTIME " +
                     "      )";

        Object[] params = new Object[] { lectureId, memberId };
        jdbcUtil.setSqlAndParameters(sql, params);

        try {
            ResultSet rs = jdbcUtil.executeQuery();
            return rs.next(); // 결과가 있으면 중복 발생
        } finally {
            jdbcUtil.close(); // 리소스 해제
        }
    }
    
    //강사 강의 중복확인
    public boolean isLectureConflict(String teacherId, String newDayOfWeek, LocalTime newStartTime, LocalTime newEndTime) throws SQLException {
        String sql = "WITH TeacherLectures AS ( " +
                     "  SELECT LECTUREID " +
                     "  FROM LECTURE " +
                     "  WHERE TEACHERID = ? " +  // 강사의 lectureId를 가져오는 부분
                     ") " +
                     "SELECT 1 " +
                     "FROM LECTURESCHEDULE ls " +
                     "JOIN TeacherLectures tl ON ls.LECTUREID = tl.LECTUREID " +
                     "WHERE ls.TYPE = 'regular' " +  // 정기 일정만 확인
                     "  AND ls.DAYOFWEEK = ? " +  // 새로 추가하려는 강의의 요일
                     "  AND ( " +
                     "        (? BETWEEN ls.STARTTIME AND ls.ENDTIME) OR " +  // 새 강의 시작 시간이 기존 강의 시간대에 포함되는지 확인
                     "        (? BETWEEN ls.STARTTIME AND ls.ENDTIME) OR " +  // 새 강의 종료 시간이 기존 강의 시간대에 포함되는지 확인
                     "        (ls.STARTTIME BETWEEN ? AND ?) OR " +  // 기존 강의 시작 시간이 새 강의 시간대에 포함되는지 확인
                     "        (ls.ENDTIME BETWEEN ? AND ?) " +  // 기존 강의 종료 시간이 새 강의 시간대에 포함되는지 확인
                     "      )";

        Object[] params = new Object[] { teacherId, newDayOfWeek, newStartTime, newEndTime, newStartTime, newEndTime, newStartTime, newEndTime };
        jdbcUtil.setSqlAndParameters(sql, params);

        try {
            ResultSet rs = jdbcUtil.executeQuery();
            return rs.next(); // 결과가 있으면 일정이 겹친다는 의미
        } finally {
            jdbcUtil.close(); // 리소스 해제
        }
    }

    
   // stuId와 lectureId에 해당하는 레코드가 존재하는지 확인 
    public boolean isEnrollmentExists(String memberId, long lectureId) throws SQLException {
        
        String sql = "SELECT COUNT(*) FROM LectureEnrollment WHERE STUID = ? AND LECTUREID = ?";
        Object[] params = new Object[] { memberId, lectureId };

        // 결과 확인
        jdbcUtil.setSqlAndParameters(sql, params); // SQL과 파라미터 설정

        try {
            // 쿼리 실행
            ResultSet rs = jdbcUtil.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1); // 레코드 수를 가져옴
                return count > 0; // 레코드가 있으면 true, 없으면 false
            }
        } catch (Exception ex) {
            jdbcUtil.rollback(); // 오류 발생 시 롤백
            ex.printStackTrace();
        } finally {
            jdbcUtil.close(); // 리소스 해제
        }

        return false; // 예외 발생 시 false 반환
    }
    
  //수강생 조회
    public List<String> findLectureMembers(int lectureId) throws SQLException {
       List<String> members = new ArrayList<>();
       
        String sql = "SELECT s.name " +
                     "FROM Student s " +
                     "JOIN LectureEnrollment le ON s.id = le.stuId " +
                     "WHERE le.lectureId = ? ";

        jdbcUtil.setSqlAndParameters(sql, new Object[]{lectureId}); // memberId를 두 번 파라미터로 설정
        ResultSet rs = jdbcUtil.executeQuery();
        
        while (rs.next()) {
           members.add(rs.getString("name"));
        }
        
        jdbcUtil.close();
        return members;
    }

   public List<LocalDate> findMonthSchedule(int lectureId, int month, int year) {
        List<LocalDate> eventDates = new ArrayList<>();
        
        String sql = 
                "SELECT DISTINCT TRUNC(startDate) AS eventDate " +
                "FROM lectureSchedule " +
                "WHERE type = 'special' " +
                "AND EXTRACT(MONTH FROM startDate) = ? " +
                "AND EXTRACT(YEAR FROM startDate) = ? " +
                "AND lectureId = ? " +

                "UNION " +

                "SELECT DISTINCT TRUNC(createAt) AS eventDate " +
                "FROM lectureNotice " +
                "WHERE EXTRACT(MONTH FROM createAt) = ? " +
                "AND EXTRACT(YEAR FROM createAt) = ? " +
                "AND lectureId = ? " +

                "UNION " +

                "SELECT DISTINCT TRUNC(dueDate) AS eventDate " +
                "FROM lectureAssignment " +
                "WHERE EXTRACT(MONTH FROM dueDate) = ? " +
                "AND EXTRACT(YEAR FROM dueDate) = ? " +
                "AND lectureId = ? " +

                "ORDER BY eventDate";
        
        jdbcUtil.setSqlAndParameters(sql, new Object[]{month, year, lectureId,month, year, lectureId,month, year, lectureId}); // memberId를 두 번 파라미터로 설정
        
        try {
           ResultSet rs = jdbcUtil.executeQuery();
            
            while (rs.next()) {
                Date eventDate = rs.getDate("eventDate");
                 // java.sql.Date를 LocalDate로 변환
                 if (eventDate != null) {
                     eventDates.add(eventDate.toLocalDate());
                 }
            }
            return eventDates;
        } catch (Exception ex) {
            jdbcUtil.rollback(); // 오류 발생 시 롤백
            ex.printStackTrace();
        } finally {
            jdbcUtil.close(); // 리소스 해제
        }
        
        
      return null;
   }
    
}

