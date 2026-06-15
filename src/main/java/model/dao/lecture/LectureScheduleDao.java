package model.dao.lecture;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import model.dao.JDBCUtil;
import model.domain.Schedule;
import model.domain.calendar.CalendarDTO;

public class LectureScheduleDao {
	private JDBCUtil jdbcUtil = null;

	public LectureScheduleDao() {
		jdbcUtil = new JDBCUtil();
	}

	// 스케줄 조회 by 날짜 (년, 월, 로그인 사용자)
	public List<Schedule> findSchedulesByDate(int year, int month, String memberId) {
		YearMonth calendarMonth = YearMonth.of(year, month);
		LocalDate lastDayOfMonth = calendarMonth.atEndOfMonth();

		StringBuffer query = new StringBuffer();
		query.append("SELECT DISTINCT ls.lecturescheduleid AS scheduleId, ");
		query.append("ls.startTime, ls.endTime, ls.lectureId, ls.startDate, ");
		query.append("ls.dayofweek, ls.frequency, ls.type, ls.title, ls.description, l.name AS lectureName ");
		query.append("FROM lectureschedule ls ");
		query.append("JOIN lecture l ON ls.lectureId = l.lectureId ");
		query.append("WHERE (l.teacherId = ? ");
		query.append("OR EXISTS (SELECT 1 FROM LectureEnrollment le ");
		query.append("WHERE le.lectureId = l.lectureId AND le.stuId = ?)) ");
		query.append("AND ((ls.type = 'regular' AND ls.startDate <= ?) ");
		query.append("OR ((ls.type <> 'regular' OR ls.type IS NULL) ");
		query.append("AND EXTRACT(YEAR FROM ls.startDate) = ? ");
		query.append("AND EXTRACT(MONTH FROM ls.startDate) = ?)) ");
		query.append("ORDER BY ls.startDate, ls.startTime");

		jdbcUtil.setSqlAndParameters(query.toString(),
				new Object[] { memberId, memberId, java.sql.Date.valueOf(lastDayOfMonth), year, month });
		List<Schedule> schedules = new ArrayList<>();

		try {
			ResultSet rs = jdbcUtil.executeQuery();
			while (rs.next()) {
				Schedule schedule = new Schedule();
				schedule.setScheduleId(rs.getInt("scheduleId"));
				schedule.setDayOfWeek(rs.getString("dayofweek"));
				schedule.setStartTime(rs.getTime("startTime") != null ? rs.getTime("startTime").toLocalTime() : null);
				schedule.setEndTime(rs.getTime("endTime") != null ? rs.getTime("endTime").toLocalTime() : null);
				schedule.setFrequency(rs.getString("frequency"));
				schedule.setLectureId(rs.getLong("lectureId"));
				schedule.setStartDate(rs.getDate("startDate").toLocalDate());
				schedule.setType(rs.getString("type"));
				schedule.setTitle(rs.getString("title"));
				schedule.setDescription(rs.getString("description"));
				schedule.setLectureName(rs.getString("lectureName")); // Lecture Name 추가

				addScheduleOccurrences(schedules, schedule, calendarMonth);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.close();
		}

		return schedules;
	}

	private void addScheduleOccurrences(List<Schedule> schedules, Schedule schedule, YearMonth calendarMonth) {
		if (!"regular".equals(schedule.getType()) || schedule.getDayOfWeek() == null) {
			schedules.add(schedule);
			return;
		}

		DayOfWeek targetDay = null;
		try {
			targetDay = DayOfWeek.valueOf(schedule.getDayOfWeek());
		} catch (IllegalArgumentException ex) {
			if (schedule.getStartDate() != null && YearMonth.from(schedule.getStartDate()).equals(calendarMonth)) {
				schedules.add(schedule);
			}
			return;
		}
		LocalDate start = calendarMonth.atDay(1);
		if (schedule.getStartDate() != null && schedule.getStartDate().isAfter(start)) {
			start = schedule.getStartDate();
		}

		for (LocalDate date = start; !date.isAfter(calendarMonth.atEndOfMonth()); date = date.plusDays(1)) {
			if (date.getDayOfWeek() == targetDay) {
				Schedule occurrence = copySchedule(schedule);
				occurrence.setStartDate(date);
				schedules.add(occurrence);
			}
		}
	}

	private Schedule copySchedule(Schedule source) {
		Schedule copy = new Schedule();
		copy.setScheduleId(source.getScheduleId());
		copy.setDayOfWeek(source.getDayOfWeek());
		copy.setStartTime(source.getStartTime());
		copy.setEndTime(source.getEndTime());
		copy.setFrequency(source.getFrequency());
		copy.setLectureId(source.getLectureId());
		copy.setStudyGroupId(source.getStudyGroupId());
		copy.setStartDate(source.getStartDate());
		copy.setType(source.getType());
		copy.setTitle(source.getTitle());
		copy.setDescription(source.getDescription());
		copy.setLectureName(source.getLectureName());
		return copy;
	}

	// 스케줄 생성
	public int createSchedule(Schedule schedule) throws SQLException {
		StringBuffer query = new StringBuffer();
		int scheduleId = nextScheduleId();

		query.append(
				"INSERT INTO lectureschedule (lecturescheduleid, dayofweek, starttime, endtime, frequency, lectureid, startdate, type, title, description) ");
		query.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		jdbcUtil.setSqlAndParameters(query.toString(),
				new Object[] { scheduleId, schedule.getDayOfWeek(), schedule.getStartTime(), schedule.getEndTime(),
						schedule.getFrequency(), schedule.getLectureId(), schedule.getStartDate(), schedule.getType(),
						schedule.getTitle(), schedule.getDescription() });

		try {
			int result = jdbcUtil.executeUpdate();
			if (result > 0) {
				jdbcUtil.commit();
				return scheduleId;
			}
			jdbcUtil.rollback();
			throw new SQLException("강의 일정 저장 결과가 없습니다.");
		} catch (SQLException ex) {
			jdbcUtil.rollback();
			throw ex;
		} catch (Exception ex) {
			jdbcUtil.rollback();
			throw new SQLException("강의 일정 저장 중 오류가 발생했습니다.", ex);
		} finally {
			jdbcUtil.close();
		}

	}

	private int nextScheduleId() {
		JDBCUtil sequenceJdbcUtil = new JDBCUtil();
		sequenceJdbcUtil.setSqlAndParameters("SELECT SEQ_LECTURE_SCHEDULE_ID.nextval AS id FROM dual", new Object[] {});
		try {
			ResultSet rs = sequenceJdbcUtil.executeQuery();
			if (rs.next()) {
				return rs.getInt("id");
			}
			throw new IllegalStateException("강의 일정 식별자를 생성하지 못했습니다.");
		} catch (Exception ex) {
			throw new RuntimeException("강의 일정 식별자 생성에 실패했습니다.", ex);
		} finally {
			sequenceJdbcUtil.close();
		}
	}

	// 스케줄 업데이트
	public void updateSchedule(Schedule schedule) {
		StringBuffer query = new StringBuffer();
		query.append("UPDATE lectureschedule ");
		query.append("SET dayofweek = ?, starttime = ?, endtime = ?, frequency = ?, title = ?, description = ? ");
		query.append("WHERE lecturescheduleid = ?");

		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { schedule.getDayOfWeek(), schedule.getStartTime(),
				schedule.getEndTime(), schedule.getFrequency(), schedule.getTitle(), schedule.getDescription(),
				schedule.getScheduleId() });

		try {
			int rs = jdbcUtil.executeUpdate();
			if (rs > 0) {
			} else {
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.commit();
			jdbcUtil.close();
		}
	}

	// 스케줄 조회 by ID
	public Schedule findScheduleById(int scheduleId) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT * ");
		query.append("FROM lectureschedule ");
		query.append("WHERE lecturescheduleid = ?");

		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { scheduleId });

		try {
			Schedule schedule = null;
			ResultSet rs = jdbcUtil.executeQuery();
			if (rs.next()) {
				schedule = new Schedule();
				schedule.setScheduleId(rs.getInt("lecturescheduleid"));
				schedule.setDayOfWeek(rs.getString("dayofweek"));
				schedule.setStartTime(rs.getTime("starttime") != null ? rs.getTime("starttime").toLocalTime() : null);
				schedule.setEndTime(rs.getTime("endtime") != null ? rs.getTime("endtime").toLocalTime() : null);
				schedule.setFrequency(rs.getString("frequency"));
				schedule.setLectureId(rs.getInt("lectureid"));
				schedule.setStartDate(rs.getDate("startdate").toLocalDate());
				schedule.setType(rs.getString("type"));
				schedule.setTitle(rs.getString("title"));
				schedule.setDescription(rs.getString("description"));
			}
			return schedule;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.close();
		}
		return null;
	}

	// 스케줄 목록 조회 by lecture ID
	public List<Schedule> findSchedulesBylectureId(long lectureid) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT * ");
		query.append("FROM lectureschedule ");
		query.append("WHERE lectureid = ?");

		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { lectureid });
		List<Schedule> schedules = new ArrayList<>();

		try {
			ResultSet rs = jdbcUtil.executeQuery();
			while (rs.next()) {
				Schedule schedule = new Schedule();
				schedule.setScheduleId(rs.getInt("lecturescheduleid"));
				schedule.setDayOfWeek(rs.getString("dayofweek"));
				schedule.setStartTime(rs.getTime("starttime").toLocalTime());
				schedule.setEndTime(rs.getTime("endtime").toLocalTime());
				schedule.setFrequency(rs.getString("frequency"));
				schedule.setLectureId(lectureid);
				schedule.setStartDate(rs.getDate("startdate").toLocalDate());
				schedule.setType(rs.getString("type"));
				schedule.setTitle(rs.getString("title"));
				schedule.setDescription(rs.getString("description"));
				schedules.add(schedule);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.close();
		}

		return schedules;
	}

	// 스케줄 목록 조회 by StudyGroup ID, Type, StartDate, DayOfWeek
	public List<Schedule> findSchedulesByFilters(long lectureId, LocalDate startDate, String type, String dayOfWeek) {
		StringBuffer query = new StringBuffer();

		query.append("SELECT * ");
		query.append("FROM lectureschedule ");
		query.append("WHERE lectureid = ? ");
		List<Object> params = new ArrayList<>();
		params.add(lectureId);

		// startdate 조건 추가
		if (type.equals("regular")) {
			query.append("AND type = ? ");
			params.add(type);
			query.append("AND startdate <= ? "); // 필터: startdate가 오늘 이전
		} else if ("special".equals(type)) {
			query.append("AND (type <> 'regular' OR type IS NULL) ");
			query.append("AND TRUNC(startdate) = ? "); // 필터: startdate가 오늘
		} else {
			query.append("AND type = ? ");
			params.add(type);
			query.append("AND TRUNC(startdate) = ? "); // 필터: startdate가 오늘
		}
		params.add(java.sql.Date.valueOf(startDate)); // startDate를 SQL Date로 변환

		// dayOfWeek 조건 추가
		if (dayOfWeek != null && !dayOfWeek.isEmpty()) {
			query.append("AND dayofweek = ? ");
			params.add(dayOfWeek);
		}

		// 매개변수 설정
		jdbcUtil.setSqlAndParameters(query.toString(), params.toArray());

		List<Schedule> schedules = new ArrayList<>();

		try {
			ResultSet rs = jdbcUtil.executeQuery();
			while (rs.next()) {
				Schedule schedule = new Schedule();
				schedule.setScheduleId(rs.getInt("lecturescheduleid"));
				schedule.setDayOfWeek(rs.getString("dayofweek"));
				schedule.setStartTime(rs.getTime("starttime") != null ? rs.getTime("starttime").toLocalTime() : null);
				schedule.setEndTime(rs.getTime("endtime") != null ? rs.getTime("endtime").toLocalTime() : null);
				schedule.setFrequency(rs.getString("frequency"));
				schedule.setLectureId(lectureId);
				schedule.setStartDate(rs.getDate("startdate").toLocalDate());
				schedule.setType(rs.getString("type"));
				schedule.setTitle(rs.getString("title"));
				schedule.setDescription(rs.getString("description"));
				schedules.add(schedule);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.close();
		}

		return schedules;
	}

	// 스케줄 목록 id 조회 by lecture ID
	public List<Integer> findScheduleIdsBylectureId(long lectureid) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT lecturescheduleid ");
		query.append("FROM lectureschedule ");
		query.append("WHERE lectureid = ?");

		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { lectureid });
		List<Integer> scheduleIds = new ArrayList<>();

		try {
			ResultSet rs = jdbcUtil.executeQuery();
			while (rs.next()) {
				scheduleIds.add(rs.getInt("lecturescheduleid"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.close();
		}

		return scheduleIds;
	}

	// 스케줄 삭제
	public void deleteScheduleById(int scheduleId) {
		StringBuffer query = new StringBuffer();
		query.append("DELETE FROM lectureschedule ");
		query.append("WHERE lecturescheduleid = ?");

		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { scheduleId });

		try {
			int rs = jdbcUtil.executeUpdate();
			if (rs > 0) {
			} else {
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.commit();
			jdbcUtil.close();
		}
	}
}
