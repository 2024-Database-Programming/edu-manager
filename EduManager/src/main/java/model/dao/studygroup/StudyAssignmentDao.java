package model.dao.studygroup;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.dao.JDBCUtil;
import model.domain.Assignment;

public class StudyAssignmentDao {
	private JDBCUtil jdbcUtil = null; // JDBCUtil 필드 선언

	public StudyAssignmentDao() { // 생성자
		jdbcUtil = new JDBCUtil(); // JDBCUtil 객체 생성
	}

	// 날짜로 스터디 과제 목록 조회
	public List<Assignment> findAssignmentsByDate(int year, int month, String memberId) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT DISTINCT la.studyassignmentid AS id, la.duedate, la.title, la.description, ");
		query.append("la.studygroupid, l.name AS lectureName ");
		query.append("FROM studyassignment la ");
		query.append("JOIN studygroup l ON la.studygroupid = l.studygroupid ");
		query.append("WHERE (l.leaderId = ? ");
		query.append("OR EXISTS (SELECT 1 FROM StudyGroupApplication sga ");
		query.append("WHERE sga.studyGroupId = l.studyGroupId AND sga.stuId = ? AND sga.status = '수락')) ");
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
				ass.setStudyId(rs.getInt("studygroupid"));
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

	// 과제 생성
	public void createAssignment(int studyId, String title, String description, LocalDate dueDate, String textFile) {
		StringBuffer query = new StringBuffer();
		query.append(
				"INSERT INTO studyassignment (studyassignmentid, studygroupid, title, description, duedate, textfile, createat) ");
		query.append("VALUES (SEQ_STUDY_ASSIGNMENT_ID.nextval, ?, ?, ?, ?, ?, SYSDATE)");

		// SQL 쿼리와 매개변수 설정
		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { studyId, title, description, dueDate, textFile });

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

	// 과제 상세 조회
	public Assignment findAssignmentById(int assignment_id) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT studyassignmentid, duedate, title, description, createat, textfile, studygroupid ");
		query.append("FROM studyassignment ");
		query.append("WHERE studyassignmentid = ?");

		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { assignment_id });

		try {
			Assignment ass = null;
			ResultSet rs = jdbcUtil.executeQuery(); // 질의 실행
			if (rs.next()) {
				ass = new Assignment();
				ass.setId(assignment_id);
				ass.setTitle(rs.getString("title"));
				ass.setDescription(rs.getString("description"));
				ass.setTextFile(rs.getString("textfile"));
				ass.setLectureId(rs.getInt("studygroupid"));

				Date sqlDate = rs.getDate("duedate");
				if (sqlDate != null) {
					ass.setDueDate(sqlDate.toLocalDate());
				}

				sqlDate = rs.getDate("createat");
				if (sqlDate != null) {
					ass.setCreateat(sqlDate.toLocalDate());
				}
			}
			return ass;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.close(); // ResultSet, PreparedStatement, Connection 등 해제
		}
		return null;
	}

	// 스터디 아이디로 스터디 과제 목록 조회
	public List<Assignment> findAssignmentsByStudyId(int studyId) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT studyassignmentid, duedate, title, description, createat, textfile, studygroupid ");
		query.append("FROM studyassignment ");
		query.append("WHERE studygroupid = ?");

		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { studyId });
		List<Assignment> assignments = new ArrayList<>();

		try {
			ResultSet rs = jdbcUtil.executeQuery(); // 질의 실행
			while (rs.next()) {
				Assignment ass = new Assignment();
				ass.setId(rs.getInt("studyassignmentid"));
				ass.setTitle(rs.getString("title"));
				ass.setDescription(rs.getString("description"));
				ass.setTextFile(rs.getString("textfile"));
				ass.setLectureId(rs.getInt("studygroupid"));

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

	// 스터디 아이디랑 마감날짜로 스터디 과제 목록 조회
	public List<Assignment> findAssignmentsByStudyIdAndDueDate(int studyId, LocalDate dueDate) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT studyassignmentid, duedate, title, description, createat, textfile, studygroupid ");
		query.append("FROM studyassignment ");
		query.append("WHERE studygroupid = ? AND TRUNC(duedate) = ?");

		java.sql.Date sqlDueDate = java.sql.Date.valueOf(dueDate);

		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { studyId, sqlDueDate });
		List<Assignment> assignments = new ArrayList<>();

		try {
			ResultSet rs = jdbcUtil.executeQuery(); // 질의 실행
			while (rs.next()) {
				Assignment ass = new Assignment();
				ass.setId(rs.getInt("studyassignmentid"));
				ass.setTitle(rs.getString("title"));
				ass.setDescription(rs.getString("description"));
				ass.setTextFile(rs.getString("textfile"));
				ass.setLectureId(rs.getInt("studygroupid"));

				ass.setDueDate(rs.getDate("duedate").toLocalDate());

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

	// 과제 삭제
	public void deleteAssignmentById(int assignmentId) {
		StringBuffer query = new StringBuffer();
		query.append("DELETE FROM studyassignment ");
		query.append("WHERE studyassignmentid = ?");

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
		query.append("UPDATE studyassignment ");
		query.append("SET title = ?, description = ?, duedate = ?, textfile = ? ");
		query.append("WHERE studyassignmentid = ?");

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
