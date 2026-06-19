package model.dao.lecture;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import model.dao.JDBCUtil;
import model.domain.Assignment;

public class LectureAssignmentDao {
	private JDBCUtil jdbcUtil = null;

	public LectureAssignmentDao() {
		jdbcUtil = new JDBCUtil();
	}

	public List<Assignment> findAssignmentsByDate(int year, int month, String memberId) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT DISTINCT la.lectureassignmentid AS id, la.duedate, la.createat, ");
		query.append("la.starttime, la.duetime, la.title, la.description, la.textfile, ");
		query.append("la.lectureid, l.name AS lectureName ");
		query.append("FROM lectureassignment la ");
		query.append("JOIN lecture l ON la.lectureid = l.lectureid ");
		query.append("WHERE (l.teacherId = ? ");
		query.append("OR EXISTS (SELECT 1 FROM LectureEnrollment le ");
		query.append("WHERE le.lectureId = l.lectureId AND le.stuId = ?)) ");
		query.append("AND (");
		query.append("(EXTRACT(YEAR FROM la.duedate) = ? AND EXTRACT(MONTH FROM la.duedate) = ?) ");
		query.append("OR (la.createat IS NOT NULL AND EXTRACT(YEAR FROM la.createat) = ? ");
		query.append("AND EXTRACT(MONTH FROM la.createat) = ?)");
		query.append(") ");
		query.append("ORDER BY la.duedate");

		jdbcUtil.setSqlAndParameters(query.toString(),
				new Object[] { memberId, memberId, year, month, year, month });
		List<Assignment> assignments = new ArrayList<>();

		try {
			ResultSet rs = jdbcUtil.executeQuery();
			while (rs.next()) {
				assignments.add(mapAssignment(rs, "id", "lectureid", true));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.close();
		}

		return assignments;
	}

	public Assignment findAssignmentById(int assignmentId) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT lectureassignmentid, duedate, title, description, createat, ");
		query.append("starttime, duetime, textfile, lectureid ");
		query.append("FROM lectureassignment ");
		query.append("WHERE lectureassignmentid = ? ");

		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { assignmentId });

		try {
			ResultSet rs = jdbcUtil.executeQuery();
			if (rs.next()) {
				return mapAssignment(rs, "lectureassignmentid", "lectureid", true);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.close();
		}
		return null;
	}

	public List<Assignment> findAssignmentsByLectureId(int lectureId) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT lectureassignmentid, duedate, title, description, createat, ");
		query.append("starttime, duetime, textfile, lectureid ");
		query.append("FROM lectureassignment ");
		query.append("WHERE lectureid = ? ");
		query.append("ORDER BY duedate, duetime, title ");

		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { lectureId });
		List<Assignment> assignments = new ArrayList<>();

		try {
			ResultSet rs = jdbcUtil.executeQuery();
			while (rs.next()) {
				assignments.add(mapAssignment(rs, "lectureassignmentid", "lectureid", true));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.close();
		}

		return assignments;
	}

	public List<Assignment> findAssignmentsByLectureIdAndDueDate(int lectureId, LocalDate dueDate) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT lectureassignmentid, duedate, title, description, createat, ");
		query.append("starttime, duetime, textfile, lectureid ");
		query.append("FROM lectureassignment ");
		query.append("WHERE lectureId = ? AND TRUNC(duedate) = ? ");
		query.append("ORDER BY duedate, duetime, title ");

		Date sqlDueDate = Date.valueOf(dueDate);

		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { lectureId, sqlDueDate });
		List<Assignment> assignments = new ArrayList<>();

		try {
			ResultSet rs = jdbcUtil.executeQuery();
			while (rs.next()) {
				assignments.add(mapAssignment(rs, "lectureassignmentid", "lectureid", true));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.close();
		}

		return assignments;
	}

	public int createAssignment(Assignment assignment) {
		StringBuffer query = new StringBuffer();
		int assignmentId = nextAssignmentId();
		query.append("INSERT INTO lectureassignment ");
		query.append("(lectureassignmentid, lectureid, title, description, duedate, starttime, duetime, textfile, createat) ");
		query.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

		jdbcUtil.setSqlAndParameters(query.toString(),
				new Object[] { assignmentId, assignment.getLectureId(), assignment.getTitle(),
						assignment.getDescription(), toSqlDate(assignment.getDueDate()), toSqlTime(assignment.getStartTime()),
						toSqlTime(assignment.getDueTime()), assignment.getTextFile(),
						toSqlDate(assignment.getCreateat()) });

		try {
			jdbcUtil.executeUpdate();
			jdbcUtil.commit();
			return assignmentId;
		} catch (Exception ex) {
			jdbcUtil.rollback();
			throw new RuntimeException("강의 과제 저장에 실패했습니다.", ex);
		} finally {
			jdbcUtil.close();
		}
	}

	public void deleteAssignmentById(int assignmentId) {
		StringBuffer query = new StringBuffer();
		query.append("DELETE FROM lectureassignment ");
		query.append("WHERE lectureassignmentid = ?");

		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { assignmentId });

		try {
			jdbcUtil.executeUpdate();
			jdbcUtil.commit();
		} catch (Exception ex) {
			jdbcUtil.rollback();
			ex.printStackTrace();
		} finally {
			jdbcUtil.close();
		}
	}

	public void updateAssignment(int assignmentId, String title, String description, Date dueDate, String textFile) {
		StringBuffer query = new StringBuffer();
		query.append("UPDATE lectureassignment ");
		query.append("SET title = ?, description = ?, duedate = ?, textfile = ? ");
		query.append("WHERE lectureassignmentid = ?");

		jdbcUtil.setSqlAndParameters(query.toString(),
				new Object[] { title, description, dueDate, textFile, assignmentId });

		try {
			jdbcUtil.executeUpdate();
			jdbcUtil.commit();
		} catch (Exception ex) {
			jdbcUtil.rollback();
			ex.printStackTrace();
		} finally {
			jdbcUtil.close();
		}
	}

	public void updateAssignment(Assignment assignment) {
		StringBuffer query = new StringBuffer();
		query.append("UPDATE lectureassignment ");
		query.append("SET title = ?, description = ?, createat = ?, starttime = ?, duedate = ?, duetime = ?, textfile = ? ");
		query.append("WHERE lectureassignmentid = ?");

		jdbcUtil.setSqlAndParameters(query.toString(),
				new Object[] { assignment.getTitle(), assignment.getDescription(), toSqlDate(assignment.getCreateat()),
						toSqlTime(assignment.getStartTime()), toSqlDate(assignment.getDueDate()),
						toSqlTime(assignment.getDueTime()), assignment.getTextFile(), assignment.getId() });

		try {
			jdbcUtil.executeUpdate();
			jdbcUtil.commit();
		} catch (Exception ex) {
			jdbcUtil.rollback();
			throw new RuntimeException("강의 과제 수정에 실패했습니다.", ex);
		} finally {
			jdbcUtil.close();
		}
	}

	private Assignment mapAssignment(ResultSet rs, String idColumn, String ownerColumn, boolean lectureOwner)
			throws SQLException {
		Assignment assignment = new Assignment();
		assignment.setId(rs.getInt(idColumn));
		assignment.setTitle(rs.getString("title"));
		assignment.setDescription(rs.getString("description"));
		assignment.setTextFile(rs.getString("textfile"));

		if (lectureOwner) {
			assignment.setLectureId(rs.getInt(ownerColumn));
		} else {
			assignment.setStudyId(rs.getInt(ownerColumn));
		}

		Date dueDate = rs.getDate("duedate");
		if (dueDate != null) {
			assignment.setDueDate(dueDate.toLocalDate());
		}

		Date createAt = rs.getDate("createat");
		if (createAt != null) {
			assignment.setCreateat(createAt.toLocalDate());
		}

		Time startTime = rs.getTime("starttime");
		if (startTime != null) {
			assignment.setStartTime(startTime.toLocalTime());
		}

		Time dueTime = rs.getTime("duetime");
		if (dueTime != null) {
			assignment.setDueTime(dueTime.toLocalTime());
		}

		setLectureNameIfPresent(assignment, rs);
		return assignment;
	}

	private void setLectureNameIfPresent(Assignment assignment, ResultSet rs) {
		try {
			assignment.setLectureName(rs.getString("lectureName"));
		} catch (SQLException ignored) {
			// Some detail queries do not include the parent lecture name.
		}
	}

	private Date toSqlDate(LocalDate date) {
		return date == null ? null : Date.valueOf(date);
	}

	private Time toSqlTime(LocalTime time) {
		return time == null ? null : Time.valueOf(time);
	}

	private int nextAssignmentId() {
		JDBCUtil sequenceJdbcUtil = new JDBCUtil();
		sequenceJdbcUtil.setSqlAndParameters("SELECT SEQ_LECTURE_ASSIGNMENT_ID.nextval AS id FROM dual", new Object[] {});
		try {
			ResultSet rs = sequenceJdbcUtil.executeQuery();
			if (rs.next()) {
				return rs.getInt("id");
			}
			throw new IllegalStateException("강의 과제 식별자를 생성하지 못했습니다.");
		} catch (Exception ex) {
			throw new RuntimeException("강의 과제 식별자 생성에 실패했습니다.", ex);
		} finally {
			sequenceJdbcUtil.close();
		}
	}
}
