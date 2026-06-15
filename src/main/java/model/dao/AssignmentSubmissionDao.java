package model.dao;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import model.domain.AssignmentSubmission;

public class AssignmentSubmissionDao {
	public int saveOrReplace(AssignmentSubmission submission) {
		ConnectionManager cm = new ConnectionManager();
		Connection conn = cm.getConnection();
		try {
			conn.setAutoCommit(false);
			try (PreparedStatement delete = conn.prepareStatement(
					"DELETE FROM ASSIGNMENTSUBMISSION "
							+ "WHERE OWNERTYPE = ? AND ASSIGNMENTID = ? AND STUDENTID = ?")) {
				delete.setString(1, submission.getOwnerType());
				delete.setInt(2, submission.getAssignmentId());
				delete.setString(3, submission.getStudentId());
				delete.executeUpdate();
			}

			int id = nextId(conn);
			try (PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO ASSIGNMENTSUBMISSION "
							+ "(SUBMISSIONID, OWNERTYPE, ASSIGNMENTID, STUDENTID, DESCRIPTION, "
							+ "FILENAME, CONTENTTYPE, FILEDATA, SUBMITTEDAT) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)")) {
				ps.setInt(1, id);
				ps.setString(2, submission.getOwnerType());
				ps.setInt(3, submission.getAssignmentId());
				ps.setString(4, submission.getStudentId());
				ps.setString(5, submission.getDescription());
				ps.setString(6, submission.getFileName());
				ps.setString(7, submission.getContentType());
				if (submission.getData() != null && submission.getData().length > 0) {
					ps.setBinaryStream(8, new ByteArrayInputStream(submission.getData()), submission.getData().length);
				} else {
					ps.setNull(8, Types.BLOB);
				}
				ps.executeUpdate();
			}
			conn.commit();
			return id;
		} catch (Exception ex) {
			try {
				conn.rollback();
			} catch (Exception ignore) {
			}
			throw new RuntimeException("과제 제출 저장에 실패했습니다.", ex);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception ignore) {
			}
		}
	}

	public List<AssignmentSubmission> findByAssignment(String ownerType, int assignmentId) {
		ConnectionManager cm = new ConnectionManager();
		Connection conn = cm.getConnection();
		List<AssignmentSubmission> submissions = new ArrayList<>();
		try (PreparedStatement ps = conn.prepareStatement(
				"SELECT SUBMISSIONID, OWNERTYPE, ASSIGNMENTID, STUDENTID, DESCRIPTION, "
						+ "FILENAME, CONTENTTYPE, SUBMITTEDAT "
						+ "FROM ASSIGNMENTSUBMISSION "
						+ "WHERE OWNERTYPE = ? AND ASSIGNMENTID = ? "
						+ "ORDER BY SUBMITTEDAT DESC")) {
			ps.setString(1, ownerType);
			ps.setInt(2, assignmentId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					submissions.add(map(rs, false));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception ignore) {
			}
		}
		return submissions;
	}

	public AssignmentSubmission findByStudent(String ownerType, int assignmentId, String studentId) {
		ConnectionManager cm = new ConnectionManager();
		Connection conn = cm.getConnection();
		try (PreparedStatement ps = conn.prepareStatement(
				"SELECT SUBMISSIONID, OWNERTYPE, ASSIGNMENTID, STUDENTID, DESCRIPTION, "
						+ "FILENAME, CONTENTTYPE, SUBMITTEDAT "
						+ "FROM ASSIGNMENTSUBMISSION "
						+ "WHERE OWNERTYPE = ? AND ASSIGNMENTID = ? AND STUDENTID = ?")) {
			ps.setString(1, ownerType);
			ps.setInt(2, assignmentId);
			ps.setString(3, studentId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return map(rs, false);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception ignore) {
			}
		}
		return null;
	}

	public AssignmentSubmission findFileById(int id) {
		ConnectionManager cm = new ConnectionManager();
		Connection conn = cm.getConnection();
		try (PreparedStatement ps = conn.prepareStatement(
				"SELECT SUBMISSIONID, OWNERTYPE, ASSIGNMENTID, STUDENTID, DESCRIPTION, "
						+ "FILENAME, CONTENTTYPE, FILEDATA, SUBMITTEDAT "
						+ "FROM ASSIGNMENTSUBMISSION WHERE SUBMISSIONID = ?")) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return map(rs, true);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception ignore) {
			}
		}
		return null;
	}

	public void deleteByAssignment(String ownerType, int assignmentId) {
		ConnectionManager cm = new ConnectionManager();
		Connection conn = cm.getConnection();
		try (PreparedStatement ps = conn.prepareStatement(
				"DELETE FROM ASSIGNMENTSUBMISSION WHERE OWNERTYPE = ? AND ASSIGNMENTID = ?")) {
			conn.setAutoCommit(false);
			ps.setString(1, ownerType);
			ps.setInt(2, assignmentId);
			ps.executeUpdate();
			conn.commit();
		} catch (Exception ex) {
			try {
				conn.rollback();
			} catch (Exception ignore) {
			}
			throw new RuntimeException("과제 제출 삭제에 실패했습니다.", ex);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception ignore) {
			}
		}
	}

	private int nextId(Connection conn) throws Exception {
		try (PreparedStatement ps = conn.prepareStatement("SELECT SEQ_ASSIGNMENT_SUBMISSION_ID.nextval FROM dual");
				ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				return rs.getInt(1);
			}
		}
		throw new IllegalStateException("과제 제출 식별자를 생성하지 못했습니다.");
	}

	private AssignmentSubmission map(ResultSet rs, boolean includeData) throws Exception {
		AssignmentSubmission submission = new AssignmentSubmission();
		submission.setId(rs.getInt("SUBMISSIONID"));
		submission.setOwnerType(rs.getString("OWNERTYPE"));
		submission.setAssignmentId(rs.getInt("ASSIGNMENTID"));
		submission.setStudentId(rs.getString("STUDENTID"));
		submission.setDescription(rs.getString("DESCRIPTION"));
		submission.setFileName(rs.getString("FILENAME"));
		submission.setContentType(rs.getString("CONTENTTYPE"));
		Timestamp submittedAt = rs.getTimestamp("SUBMITTEDAT");
		if (submittedAt != null) {
			submission.setSubmittedAt(submittedAt.toLocalDateTime());
		}
		if (includeData) {
			Blob blob = rs.getBlob("FILEDATA");
			if (blob != null) {
				submission.setData(blob.getBytes(1, (int) blob.length()));
			}
		}
		return submission;
	}
}
