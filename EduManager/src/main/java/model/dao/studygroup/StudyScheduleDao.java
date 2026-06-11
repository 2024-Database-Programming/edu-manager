package model.dao.studygroup;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import model.dao.JDBCUtil;
import model.domain.Schedule;

public class StudyScheduleDao {
	private JDBCUtil jdbcUtil = null;

	public StudyScheduleDao() {
		jdbcUtil = new JDBCUtil();
	}

	// 스케줄 조회 by 날짜 (년, 월, 로그인 사용자)
	public List<Schedule> findSchedulesByDate(int year, int month, String memberId) {
		YearMonth calendarMonth = YearMonth.of(year, month);
		LocalDate lastDayOfMonth = calendarMonth.atEndOfMonth();

		StringBuffer query = new StringBuffer();
		query.append("SELECT DISTINCT ls.studyscheduleid AS scheduleId, ");
		query.append("ls.startTime, ls.endTime, ls.STUDYGROUPID, ls.startDate, ");
		query.append("ls.dayofweek, ls.frequency, ls.type, ls.title, l.name AS lectureName ");
		query.append("FROM studyschedule ls ");
		query.append("JOIN studygroup l ON ls.STUDYGROUPID = l.STUDYGROUPID ");
		query.append("WHERE (l.leaderId = ? ");
		query.append("OR EXISTS (SELECT 1 FROM StudyGroupApplication sga ");
		query.append("WHERE sga.studyGroupId = l.studyGroupId AND sga.stuId = ? AND sga.status = '수락')) ");
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
				schedule.setLectureId(rs.getLong("STUDYGROUPID"));
				schedule.setStudyGroupId(rs.getLong("STUDYGROUPID"));
				schedule.setStartDate(rs.getDate("startDate").toLocalDate());
				schedule.setType(rs.getString("type"));
				schedule.setTitle(rs.getString("title"));
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
		copy.setLectureName(source.getLectureName());
		return copy;
	}

	// 스케줄 생성
	public int createSchedule(Schedule schedule) {
		StringBuffer query = new StringBuffer();
		query.append(
				"INSERT INTO studyschedule (studyscheduleid, dayofweek, starttime, endtime, frequency, studygroupid, startdate, type,title) ");
		query.append("VALUES (SEQ_STUDY_SCHEDULE_ID.nextval, ?, ?, ?, ?, ?, ?, ?,?)");

		jdbcUtil.setSqlAndParameters(query.toString(),
				new Object[] { schedule.getDayOfWeek(), schedule.getStartTime(), schedule.getEndTime(),
						schedule.getFrequency(), schedule.getStudyGroupId(), schedule.getStartDate(),
						schedule.getType(), schedule.getTitle() });

		// StudyGroup에 id setting
		String key[] = { "studyScheduleId" }; // PK 컬럼(들)의 이름 배열
		try {
			int result = jdbcUtil.executeUpdate(key);
			if (result > 0) {
				ResultSet rs = jdbcUtil.getGeneratedKeys();

				System.out.println("스케줄이 성공적으로 생성되었습니다.");
				if (rs.next()) {
					int generatedKey = rs.getInt(1); // 생성된 PK 값
					return generatedKey;
				}
			} else {
				System.out.println("스케줄 생성에 실패했습니다.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.commit();
			jdbcUtil.close();
		}
		return 0;
	}

	// 스케줄 조회 by ID
	public Schedule findScheduleById(int scheduleId) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT * ");
		query.append("FROM studyschedule ");
		query.append("WHERE studyscheduleid = ?");

		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { scheduleId });

		try {
			Schedule schedule = null;
			ResultSet rs = jdbcUtil.executeQuery();
			if (rs.next()) {
				schedule = new Schedule();
				schedule.setScheduleId(rs.getInt("studyscheduleid"));
				schedule.setDayOfWeek(rs.getString("dayofweek"));
				// starttime과 endtime에 대한 null 체크
				schedule.setStartTime(rs.getTime("starttime") != null ? rs.getTime("starttime").toLocalTime() : null);
				schedule.setEndTime(rs.getTime("endtime") != null ? rs.getTime("endtime").toLocalTime() : null);

				schedule.setFrequency(rs.getString("frequency"));
				schedule.setStudyGroupId(rs.getInt("studygroupid"));
				schedule.setStartDate(rs.getDate("startdate").toLocalDate());
				schedule.setType(rs.getString("type"));
				schedule.setTitle(rs.getString("title"));
			}
			return schedule;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.close();
		}
		return null;
	}
	// 스케줄 목록 조회 by StudyGroup ID
	public List<Schedule> findSchedulesByStudyId(long studygroupid, String type) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT * ");
		query.append("FROM studyschedule ");
		query.append("WHERE studygroupid = ? ");

		List<Object> params = new ArrayList<>();
		params.add(studygroupid);

		// type 조건 추가
		if (type != null) {
			query.append("AND type = ? "); // 필터: startdate가 오늘 이전
			params.add(type);
		}

		jdbcUtil.setSqlAndParameters(query.toString(), params.toArray());

		List<Schedule> schedules = new ArrayList<>();

		try {
			ResultSet rs = jdbcUtil.executeQuery();
			while (rs.next()) {
				Schedule schedule = new Schedule();
				schedule.setScheduleId(rs.getInt("studyscheduleid"));
				schedule.setDayOfWeek(rs.getString("dayofweek"));
				// starttime과 endtime에 대한 null 체크
				schedule.setStartTime(rs.getTime("starttime") != null ? rs.getTime("starttime").toLocalTime() : null);
				schedule.setEndTime(rs.getTime("endtime") != null ? rs.getTime("endtime").toLocalTime() : null);
				schedule.setFrequency(rs.getString("frequency"));
				schedule.setStudyGroupId(studygroupid);
				schedule.setStartDate(rs.getDate("startdate").toLocalDate());
				schedule.setType(rs.getString("type"));
				schedule.setTitle(rs.getString("title"));
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
	public List<Schedule> findSchedulesByFilters(long studygroupid, LocalDate startDate, String type,
			String dayOfWeek) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT * ");
		query.append("FROM studyschedule ");
		query.append("WHERE studygroupid = ? ");
		query.append("AND type = ? ");

		List<Object> params = new ArrayList<>();
		params.add(studygroupid);
		params.add(type);

		// startdate 조건 추가
		if (type.equals("regular")) {
			query.append("AND startdate <= ? "); // 필터: startdate가 오늘 이전
		} else {
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
				schedule.setScheduleId(rs.getInt("studyscheduleid"));
				schedule.setDayOfWeek(rs.getString("dayofweek"));
				// starttime과 endtime에 대한 null 체크
				schedule.setStartTime(rs.getTime("starttime") != null ? rs.getTime("starttime").toLocalTime() : null);
				schedule.setEndTime(rs.getTime("endtime") != null ? rs.getTime("endtime").toLocalTime() : null);

				schedule.setFrequency(rs.getString("frequency"));
				schedule.setStudyGroupId(studygroupid);
				schedule.setStartDate(rs.getDate("startdate").toLocalDate());
				schedule.setType(rs.getString("type"));
				schedule.setTitle(rs.getString("title"));
				schedules.add(schedule);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.close();
		}

		return schedules;
	}

	// 스케줄 목록 id 조회 by studyId
	public List<Integer> findScheduleIdsByStudyId(long studyId) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT studyscheduleid ");
		query.append("FROM studyschedule ");
		query.append("WHERE studygroupid = ?");

		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { studyId });
		List<Integer> scheduleIds = new ArrayList<>();

		try {
			ResultSet rs = jdbcUtil.executeQuery();
			while (rs.next()) {
				scheduleIds.add(rs.getInt("studyscheduleid"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.close();
		}

		return scheduleIds;
	}

	// 스케줄 업데이트
	public void updateSchedule(Schedule schedule) {
		StringBuffer query = new StringBuffer();
		query.append("UPDATE studyschedule ");
		query.append("SET dayofweek = ?, starttime = ?, endtime = ?, frequency = ?, title = ? ");
		query.append("WHERE studyscheduleid = ?");

		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { schedule.getDayOfWeek(), schedule.getStartTime(),
				schedule.getEndTime(), schedule.getFrequency(), schedule.getTitle(), schedule.getScheduleId() });

		try {
			int rs = jdbcUtil.executeUpdate();
			if (rs > 0) {
				System.out.println("스케줄이 성공적으로 업데이트되었습니다.");
			} else {
				System.out.println("스케줄 업데이트에 실패했습니다.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.commit();
			jdbcUtil.close();
		}
	}

	// 스케줄 삭제
	public void deleteScheduleById(int scheduleId) {
		StringBuffer query = new StringBuffer();
		query.append("DELETE FROM studyschedule ");
		query.append("WHERE studyscheduleid = ?");

		jdbcUtil.setSqlAndParameters(query.toString(), new Object[] { scheduleId });

		try {
			int rs = jdbcUtil.executeUpdate();
			if (rs > 0) {
				System.out.println("스케줄이 성공적으로 삭제되었습니다.");
			} else {
				System.out.println("스케줄 삭제에 실패했습니다. 해당 ID의 스케줄이 존재하지 않습니다.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.commit();
			jdbcUtil.close();
		}
	}
}
