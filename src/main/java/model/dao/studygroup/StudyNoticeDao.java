package model.dao.studygroup;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.dao.JDBCUtil;
import model.domain.Assignment;
import model.domain.Notice;

import java.time.LocalDate;

public class StudyNoticeDao {
	private JDBCUtil jdbcUtil = null; // JDBCUtil 필드 선언

	public StudyNoticeDao() { // 생성자
		jdbcUtil = new JDBCUtil(); // JDBCUtil 객체 생성
	}

	// 공지 조회 by 날짜 (년, 월)
	public List<Notice> findNoticesByDate(int year, int month, String memberId) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT DISTINCT sn.studynoticeid AS id, sn.title, sn.description, sn.createat, sn.studygroupId ");
		query.append("FROM studynotice sn ");
		query.append("JOIN studygroup sg ON sn.studygroupId = sg.studygroupId ");
		query.append("WHERE (sg.leaderId = ? ");
		query.append("OR EXISTS (SELECT 1 FROM StudyGroupApplication sga ");
		query.append("WHERE sga.studyGroupId = sg.studyGroupId AND sga.stuId = ? AND sga.status = '수락')) ");
		query.append("AND EXTRACT(YEAR FROM sn.createat) = ? AND EXTRACT(MONTH FROM sn.createat) = ? ");
		query.append("ORDER BY sn.createat");

		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { memberId, memberId, year, month });
		List<Notice> notices = new ArrayList<>();

		try {
			ResultSet rs = jdbcUtil.executeQuery(); // 질의 실행
			while (rs.next()) {
				Notice notice = new Notice();
				notice.setId(rs.getInt("id"));
				notice.setTitle(rs.getString("title"));
				notice.setDescription(rs.getString("description"));
				notice.setCreateat(rs.getDate("createat").toLocalDate());
				notice.setStudyId(rs.getInt("studygroupId"));

// 				Date sqlDate = rs.getDate("createat");
// 				LocalDate localDate = sqlDate.toLocalDate();
// 				notice.setCreateat(localDate);

				notices.add(notice); // 리스트에 공지 추가
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.close(); // ResultSet, PreparedStatement, Connection 등 해제
		}

		return notices;
	}

	// 공지 생성
	public void createNotice(int studygroupid, String title, String description, LocalDate createdAt) {
		StringBuffer query = new StringBuffer();
		query.append("INSERT INTO studynotice (studynoticeid, studygroupid, title, description, createat) ");
		query.append("VALUES (SEQ_STUDY_NOTICE_ID.nextval, ?, ?, ?, ?)");

		// SQL 쿼리와 매개변수 설정
		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { studygroupid, title, description, createdAt });

		try {
			int rs = jdbcUtil.executeUpdate(); // 질의 실행 (INSERT문은 executeUpdate로 실행)
			if (rs > 0) {
			} else {
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.commit();
			jdbcUtil.close(); // 연결 자원 해제
		}
	}

	// 공지 상세 조회
	public Notice findNoticeById(int noticeId) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT studynoticeid, studygroupid, title, description, createat ");
		query.append("FROM studynotice ");
		query.append("WHERE studynoticeid = ? ");

		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { noticeId });

		try {
			Notice notice = null;
			ResultSet rs = jdbcUtil.executeQuery(); // 질의 실행
			if (rs.next()) {
				notice = new Notice();
				notice.setId(noticeId);
				notice.setTitle(rs.getString("title"));
				notice.setDescription(rs.getString("description"));
				notice.setStudyId(rs.getInt("studygroupid"));

				Date sqlDate = rs.getDate("createat");
				LocalDate localDate = sqlDate.toLocalDate();
				notice.setCreateat(localDate);
			}
			return notice;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.close(); // ResultSet, PreparedStatement, Connection 등 해제
		}
		return null;
	}

	// 강의 아이디로 강의 공지 목록 조회
	public List<Notice> findNoticesBystudygroupid(int studygroupid) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT studynoticeid, title, description, createat ");
		query.append("FROM studynotice ");
		query.append("WHERE studygroupid = ? ");

		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { studygroupid });
		List<Notice> notices = new ArrayList<>();

		try {
			ResultSet rs = jdbcUtil.executeQuery(); // 질의 실행
			while (rs.next()) {
				Notice notice = new Notice();
				notice.setId(rs.getInt("studynoticeid"));
				notice.setTitle(rs.getString("title"));
				notice.setDescription(rs.getString("description"));
				notice.setStudyId(studygroupid);

				Date sqlDate = rs.getDate("createat");
				LocalDate localDate = sqlDate.toLocalDate();
				notice.setCreateat(localDate);

				notices.add(notice); // 리스트에 공지 추가
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.close(); // ResultSet, PreparedStatement, Connection 등 해제
		}

		return notices;
	}

	public List<Notice> searchNotices(int studygroupid, String searchParam) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT studynoticeid, title, description, createat ");
		query.append("FROM studynotice ");
		query.append("WHERE studygroupid = ? ");

		if (searchParam != null && !searchParam.trim().isEmpty()) {
			query.append("AND (title LIKE ? OR description LIKE ?) ");
		}

		List<Object> params = new ArrayList<>();
		params.add(studygroupid); // studygroupid 추가

		if (searchParam != null && !searchParam.trim().isEmpty()) {
			params.add("%" + searchParam + "%"); // 제목 검색 조건
			params.add("%" + searchParam + "%"); // 내용 검색 조건
		}

		jdbcUtil.setSqlAndParameters(query.toString(), params.toArray()); // SQL과 파라미터 설정
		List<Notice> notices = new ArrayList<>();

		try {
			ResultSet rs = jdbcUtil.executeQuery(); // 쿼리 실행
			while (rs.next()) {
				Notice notice = new Notice();
				notice.setId(rs.getInt("studynoticeid"));
				notice.setTitle(rs.getString("title"));
				notice.setDescription(rs.getString("description"));
				notice.setStudyId(studygroupid);

				Date sqlDate = rs.getDate("createat");
				LocalDate localDate = sqlDate.toLocalDate();
				notice.setCreateat(localDate);

				notices.add(notice); // 리스트에 공지 추가
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.close(); // ResultSet, PreparedStatement, Connection 등 해제
		}

		return notices;
	}

	// 강의 아이디로 강의 공지 목록 조회
	public List<Notice> findNoticesByStudyIdAndDueDate(int studygroupid, LocalDate createdAt) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT studynoticeid, title, description, createat ");
		query.append("FROM studynotice ");
		query.append("WHERE studygroupid = ? AND TRUNC(createat) = ?");

		java.sql.Date sqlDueDate = java.sql.Date.valueOf(createdAt);

		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { studygroupid, sqlDueDate });
		List<Notice> notices = new ArrayList<>();

		try {
			ResultSet rs = jdbcUtil.executeQuery(); // 질의 실행
			while (rs.next()) {
				Notice notice = new Notice();
				notice.setId(rs.getInt("studynoticeid"));
				notice.setTitle(rs.getString("title"));
				notice.setDescription(rs.getString("description"));
				notice.setStudyId(studygroupid);
				notice.setCreateat(rs.getDate("createat").toLocalDate());

				notices.add(notice); // 리스트에 공지 추가
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.close(); // ResultSet, PreparedStatement, Connection 등 해제
		}

		return notices;
	}

	// 공지 삭제
	public void deleteNoticeById(int noticeId) {
		StringBuffer query = new StringBuffer();
		query.append("DELETE FROM studynotice ");
		query.append("WHERE studynoticeid = ?");

		// SQL 쿼리와 매개변수 설정
		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { noticeId });

		try {
			int rs = jdbcUtil.executeUpdate(); // DELETE 문은 executeUpdate로 실행
			if (rs > 0) {
			} else {
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.commit();
			jdbcUtil.close(); // 연결 자원 해제
		}
	}

	// 공지 수정
	public void updateNotice(int noticeId, String title, String description) {
		StringBuffer query = new StringBuffer();
		query.append("UPDATE studynotice ");
		query.append("SET title = ?, description = ? ");
		query.append("WHERE studynoticeid = ?");

		// SQL 쿼리와 매개변수 설정
		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { title, description, noticeId });

		try {
			int rs = jdbcUtil.executeUpdate(); // UPDATE 문은 executeUpdate로 실행
			if (rs > 0) {
			} else {
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.commit();
			jdbcUtil.close(); // 연결 자원 해제
		}
	}
}
