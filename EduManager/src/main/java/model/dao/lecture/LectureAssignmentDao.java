package model.dao.lecture;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.dao.JDBCUtil;
import model.domain.Assignment;

import java.time.LocalDate;

public class LectureAssignmentDao {
	private JDBCUtil jdbcUtil = null; // JDBCUtil 필드 선언

	public LectureAssignmentDao() { // 생성자
		jdbcUtil = new JDBCUtil(); // JDBCUtil 객체 생성
	}

	public List<Assignment> findAssignmentsByDate(int year, int month, String memberId) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT DISTINCT la.lectureassignmentid AS id, la.duedate, la.title, la.description, ");
		query.append("la.lectureid, l.name AS lectureName ");
		query.append("FROM lectureassignment la ");
		query.append("JOIN lecture l ON la.lectureid = l.lectureid ");
		query.append("WHERE (l.teacherId = ? ");
		query.append("OR EXISTS (SELECT 1 FROM LectureEnrollment le ");
		query.append("WHERE le.lectureId = l.lectureId AND le.stuId = ?)) ");
		query.append("AND EXTRACT(YEAR FROM la.duedate) = ? AND EXTRACT(MONTH FROM la.duedate) = ? ");
		query.append("ORDER BY la.duedate");

		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { memberId, memberId, year, month });
		List<Assignment> assignments = new ArrayList<>();

		try {
			ResultSet rs = jdbcUtil.executeQuery(); // 질의 실행
			while (rs.next()) {
				Assignment ass = new Assignment();
				ass.setId(rs.getInt("id"));
				ass.setTitle(rs.getString("title"));
				ass.setDescription(rs.getString("description"));
				ass.setLectureId(rs.getInt("lectureid"));
				ass.setDueDate(rs.getDate("duedate").toLocalDate());
				ass.setLectureName(rs.getString("lectureName")); // 강의 이름 설정

				assignments.add(ass); // 리스트에 과제 추가
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.close(); // ResultSet, PreparedStatement, Connection 등 해제
		}

		return assignments;
	}

	// 과제 상세 조회
	public Assignment findAssignmentById(int assignment_id) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT lectureassignmentid, duedate, title, description, createat, textfile, lectureid ");
		query.append("FROM lectureassignment ");
		query.append("WHERE lectureassignmentid = ? ");

		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { assignment_id });

		try {
			Assignment ass = null;
			ResultSet rs = jdbcUtil.executeQuery(); // 질의 실행
			if (rs.next()) { // test
				ass = new Assignment();
				ass.setId(assignment_id);
				ass.setTitle(rs.getString("title"));
				ass.setDescription(rs.getString("description"));
				ass.setTextFile(rs.getString("textfile"));
				ass.setLectureId(rs.getInt("lectureid"));
				Date sqlDate = rs.getDate("duedate");
				LocalDate localDate = sqlDate.toLocalDate();
				ass.setDueDate(localDate);

				sqlDate = rs.getDate("createat");
				localDate = sqlDate.toLocalDate();
				ass.setCreateat(localDate);

//                System.out.println(rs.getInt("lectureassignmentid") + rs.getString("title") + rs.getString("description") + rs.getInt("lectureid") + localDate);
//                System.out.println(ass);

			}
			return ass;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.close(); // ResultSet, PreparedStatement, Connection 등 해제
		}
		return null;
	}

	// 강의 아이디로 강의 과제 목록 조회
	public List<Assignment> findAssignmentsByLectureId(int lectureId) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT lectureassignmentid, duedate, title, description, createat, textfile, lectureid ");
		query.append("FROM lectureassignment ");
		query.append("WHERE lectureid = ? ");

		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { lectureId });
		List<Assignment> assignments = new ArrayList<>();

		try {
			ResultSet rs = jdbcUtil.executeQuery(); // 질의 실행
			while (rs.next()) {
				Assignment ass = new Assignment();
				ass.setId(rs.getInt("lectureassignmentid"));
				ass.setTitle(rs.getString("title"));
				ass.setDescription(rs.getString("description"));
				ass.setTextFile(rs.getString("textfile"));
				ass.setLectureId(rs.getInt("lectureid"));

				Date sqlDueDate = rs.getDate("duedate");
				if (sqlDueDate != null) {
					ass.setDueDate(sqlDueDate.toLocalDate());
				}

				Date sqlCreateAt = rs.getDate("createat");
				if (sqlCreateAt != null) {
					ass.setCreateat(sqlCreateAt.toLocalDate());
				}

				assignments.add(ass); // 리스트에 과제 추가
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.close(); // ResultSet, PreparedStatement, Connection 등 해제
		}

		return assignments;
	}

	// 강의 아이디랑 마감날짜로 강의 과제 목록 조회
	public List<Assignment> findAssignmentsByLectureIdAndDueDate(int lectureId, LocalDate dueDate) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT lectureassignmentid, duedate, title, description, createat, textfile, lectureid ");
		query.append("FROM lectureassignment ");
		query.append("WHERE lectureId = ? AND TRUNC(duedate) = ?");

		java.sql.Date sqlDueDate = java.sql.Date.valueOf(dueDate);

		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { lectureId, sqlDueDate });
		List<Assignment> assignments = new ArrayList<>();

		try {
			ResultSet rs = jdbcUtil.executeQuery(); // 질의 실행
			while (rs.next()) {
				Assignment ass = new Assignment();
				ass.setId(rs.getInt("lectureassignmentid"));
				ass.setTitle(rs.getString("title"));
				ass.setDescription(rs.getString("description"));
				ass.setTextFile(rs.getString("textfile"));
				ass.setLectureId(rs.getInt("lectureid"));
				if (sqlDueDate != null) {
					ass.setDueDate(rs.getDate("duedate").toLocalDate());
				}
				if (rs.getDate("createat") != null) {
					ass.setCreateat(rs.getDate("createat").toLocalDate());
				}

				assignments.add(ass); // 리스트에 과제 추가
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.close(); // ResultSet, PreparedStatement, Connection 등 해제
		}

		return assignments;
	}

	// 과제 생성
	public void createAssignment(int lectureId, String title, String description, LocalDate dueDate, String textFile) {
		StringBuffer query = new StringBuffer();
		query.append(
				"INSERT INTO lectureassignment (lectureassignmentid, lectureid, title, description, duedate, textfile, createat) ");
		query.append("VALUES (SEQ_LECTURE_ASSIGNMENT_ID.nextval, ?, ?, ?, ?, ?, SYSDATE)");

		// SQL 쿼리와 매개변수 설정
		jdbcUtil.setSqlAndParameters(query.toString(),
				new Object[] { lectureId, title, description, dueDate, textFile });

		try {
			int rs = jdbcUtil.executeUpdate(); // 질의 실행 (INSERT문은 executeUpdate로 실행)
			if (rs > 0) {
				System.out.println("과제가 성공적으로 생성되었습니다.");
			} else {
				System.out.println("과제 생성에 실패했습니다.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.commit();
			jdbcUtil.close(); // 연결 자원 해제
		}
	}

	// 과제 삭제
	public void deleteAssignmentById(int assignmentId) {
		StringBuffer query = new StringBuffer();
		query.append("DELETE FROM lectureassignment ");
		query.append("WHERE lectureassignmentid = ?");

		// SQL 쿼리와 매개변수 설정
		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { assignmentId });

		try {
			int rs = jdbcUtil.executeUpdate(); // DELETE 문은 executeUpdate로 실행
			if (rs > 0) {
				System.out.println("과제가 성공적으로 삭제되었습니다.");
			} else {
				System.out.println("과제 삭제에 실패했습니다. 해당 ID의 과제가 존재하지 않습니다.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.commit();
			jdbcUtil.close(); // 연결 자원 해제
		}
	}

	// 과제 수정
	public void updateAssignment(int assignmentId, String title, String description, Date dueDate, String textFile) {
		StringBuffer query = new StringBuffer();
		query.append("UPDATE lectureassignment ");
		query.append("SET title = ?, description = ?, duedate = ?, textfile = ? ");
		query.append("WHERE lectureassignmentid = ?");

		// SQL 쿼리와 매개변수 설정
		jdbcUtil.setSqlAndParameters(query.toString(),
				new Object[] { title, description, dueDate, textFile, assignmentId });

		try {
			int rs = jdbcUtil.executeUpdate(); // UPDATE 문은 executeUpdate로 실행
			if (rs > 0) {
				System.out.println("과제가 성공적으로 업데이트되었습니다.");
			} else {
				System.out.println("과제 업데이트에 실패했습니다. 해당 ID의 과제가 존재하지 않습니다.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.commit();
			jdbcUtil.close(); // 연결 자원 해제
		}
	}

}
