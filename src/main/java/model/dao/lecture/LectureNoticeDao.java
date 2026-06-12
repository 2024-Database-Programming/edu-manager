package model.dao.lecture;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.dao.JDBCUtil;
import model.domain.Notice;
import java.time.LocalDate;

public class LectureNoticeDao {
	private JDBCUtil jdbcUtil = null; // JDBCUtil 필드 선언

	public LectureNoticeDao() { // 생성자
		jdbcUtil = new JDBCUtil(); // JDBCUtil 객체 생성
	}

	// 공지 조회 by 날짜 (년, 월)
	public List<Notice> findNoticesByDate(int year, int month, String memberId) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT DISTINCT ln.lecturenoticeid AS id, ln.title, ln.description, ln.createat, ln.lectureId ");
		query.append("FROM lecturenotice ln ");
		query.append("JOIN lecture l ON ln.lectureId = l.lectureId ");
		query.append("WHERE (l.teacherId = ? ");
		query.append("OR EXISTS (SELECT 1 FROM LectureEnrollment le ");
		query.append("WHERE le.lectureId = l.lectureId AND le.stuId = ?)) ");
		query.append("AND EXTRACT(YEAR FROM ln.createat) = ? AND EXTRACT(MONTH FROM ln.createat) = ? ");
		query.append("ORDER BY ln.createat");

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
				notice.setLectureId(rs.getInt("lectureId"));

//				Date sqlDate = rs.getDate("createat");
//				LocalDate localDate = sqlDate.toLocalDate();
//				notice.setCreateat(localDate);

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
	public void createNotice(int lectureId, String title, String description, LocalDate date) {
		StringBuffer query = new StringBuffer();
		query.append("INSERT INTO lecturenotice (lecturenoticeid, lectureid, title, description, createat) ");
		query.append("VALUES (SEQ_LECTURE_NOTICE_ID.nextval, ?, ?, ?, ?)");

		// SQL 쿼리와 매개변수 설정
		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { lectureId, title, description, date });

		try {
			int rs = jdbcUtil.executeUpdate(); // 질의 실행 (INSERT문은 executeUpdate로 실행)
			if (rs > 0) {
				System.out.println("공지사항이 성공적으로 생성되었습니다.");
			} else {
				System.out.println("공지 생성에 실패했습니다.");
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
		query.append("SELECT lecturenoticeid, lectureid, title, description, createat ");
		query.append("FROM lecturenotice ");
		query.append("WHERE lecturenoticeid = ? ");

		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { noticeId });

		try {
			Notice notice = null;
			ResultSet rs = jdbcUtil.executeQuery(); // 질의 실행
			if (rs.next()) {
				notice = new Notice();
				notice.setId(noticeId);
				notice.setTitle(rs.getString("title"));
				notice.setDescription(rs.getString("description"));
				notice.setLectureId(rs.getInt("lectureid"));

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

	// 강의 아이디& 공지일로 공지목록 조회
	public List<Notice> findNoticesByLectureIdAndDate(int lectureId, LocalDate createdAt) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT lecturenoticeid, title, description, createat ");
		query.append("FROM lecturenotice ");
		query.append("WHERE lectureid = ? AND TRUNC(createat) = ?");

		java.sql.Date sqlDueDate = java.sql.Date.valueOf(createdAt);

		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { lectureId, sqlDueDate });
		List<Notice> notices = new ArrayList<>();

		try {
			ResultSet rs = jdbcUtil.executeQuery(); // 질의 실행
			while (rs.next()) {
				Notice notice = new Notice();
				notice.setId(rs.getInt("lecturenoticeid"));
				notice.setTitle(rs.getString("title"));
				notice.setDescription(rs.getString("description"));
				notice.setLectureId(lectureId);

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
	public List<Notice> findNoticesByLectureId(int lectureId) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT lecturenoticeid, title, description, createat ");
		query.append("FROM lecturenotice ");
		query.append("WHERE lectureid = ? ");

		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { lectureId });
		List<Notice> notices = new ArrayList<>();

		try {
			ResultSet rs = jdbcUtil.executeQuery(); // 질의 실행
			while (rs.next()) {
				Notice notice = new Notice();
				notice.setId(rs.getInt("lecturenoticeid"));
				notice.setTitle(rs.getString("title"));
				notice.setDescription(rs.getString("description"));
				notice.setLectureId(lectureId);

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

	public List<Notice> searchNotices(int lectureId, String searchParam) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT lecturenoticeid, title, description, createat ");
		query.append("FROM lecturenotice ");
		query.append("WHERE lectureid = ? ");

		List<Object> params = new ArrayList<>();
		params.add(lectureId);

		if (searchParam != null && !searchParam.trim().isEmpty()) {
			query.append("AND (title LIKE ? OR description LIKE ?) ");
			params.add("%" + searchParam + "%"); // 제목 검색 조건
			params.add("%" + searchParam + "%"); // 내용 검색 조건
		}

		jdbcUtil.setSqlAndParameters(query.toString(), params.toArray()); // 매개변수 전달

		List<Notice> notices = new ArrayList<>();
		try {
			ResultSet rs = jdbcUtil.executeQuery(); // 질의 실행
			while (rs.next()) {
				Notice notice = new Notice();
				notice.setId(rs.getInt("lecturenoticeid"));
				notice.setTitle(rs.getString("title"));
				notice.setDescription(rs.getString("description"));
				notice.setLectureId(lectureId);

				Date sqlDate = rs.getDate("createat");
				if (sqlDate != null) {
					LocalDate localDate = sqlDate.toLocalDate();
					notice.setCreateat(localDate);
				}

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
		query.append("DELETE FROM lecturenotice ");
		query.append("WHERE lecturenoticeid = ?");

		// SQL 쿼리와 매개변수 설정
		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { noticeId });

		try {
			int rs = jdbcUtil.executeUpdate(); // DELETE 문은 executeUpdate로 실행
			if (rs > 0) {
				System.out.println("공지사항이 성공적으로 삭제되었습니다.");
			} else {
				System.out.println("공지 삭제에 실패했습니다. 해당 ID의 공지가 존재하지 않습니다.");
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
		query.append("UPDATE lecturenotice ");
		query.append("SET title = ?, description = ? ");
		query.append("WHERE lecturenoticeid = ?");

		// SQL 쿼리와 매개변수 설정
		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { title, description, noticeId });

		try {
			int rs = jdbcUtil.executeUpdate(); // UPDATE 문은 executeUpdate로 실행
			if (rs > 0) {
				System.out.println("공지사항이 성공적으로 업데이트되었습니다.");
			} else {
				System.out.println("공지 업데이트에 실패했습니다. 해당 ID의 공지가 존재하지 않습니다.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.commit();
			jdbcUtil.close(); // 연결 자원 해제
		}
	}
}
