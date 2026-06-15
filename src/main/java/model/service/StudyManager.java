package model.service;

import java.sql.Date;
import java.sql.SQLException;

import java.time.LocalDate;
import java.time.LocalTime;

import model.dao.studygroup.StudyAssignmentDao;
import model.dao.studygroup.StudyGroupDao;
import model.dao.studygroup.StudyNoticeDao;
import model.dao.studygroup.StudyScheduleDao;
import model.domain.Assignment;
import model.domain.Notice;
import model.domain.Schedule;
import model.domain.member.Member;
import model.domain.studyGroup.StudyGroup;

import java.util.List;

public class StudyManager {
	private static StudyManager instance = new StudyManager();
	private StudyGroupDao studyDao;
	private StudyScheduleDao scheduleDao;
	private StudyAssignmentDao assignmentDao;
	private StudyNoticeDao noticeDao;

	private StudyManager() {
		studyDao = new StudyGroupDao();
		scheduleDao = new StudyScheduleDao();
		assignmentDao = new StudyAssignmentDao();
		noticeDao = new StudyNoticeDao();
	}

	public static StudyManager getInstance() {
		return instance;
	}

	/*
	 * public List<Lecture> findLectureList() throws SQLException{ return
	 * lectureDao.getAllLectures(); }
	 */
	// 특정 연도와 월의 데이터를 캘린더에 띄우기
	public List<Schedule> getScheduleCalendarList(int year, int month, String memberId) {

		List<Schedule> schedules = scheduleDao.findSchedulesByDate(year, month, memberId);

		return schedules;
	}

	public List<Notice> getNoticeCalendarList(int year, int month, String memberId) {

		List<Notice> notices = noticeDao.findNoticesByDate(year, month, memberId);

		return notices;
	}

	public List<Assignment> getAssignmentCalendarList(int year, int month, String memberId) {

		List<Assignment> assignments = assignmentDao.findAssignmentsByDate(year, month, memberId);

		return assignments;
	}

	public StudyGroup findStudyById(long studyId) throws SQLException {
		return studyDao.findGroupInfo(studyId);
	}

	public StudyGroup createStudy(StudyGroup study) throws SQLException {
		return studyDao.create(study);
	}

	public int updateStudy(StudyGroup study) throws SQLException {
		return studyDao.update(study);

	}	
    
    //정기 일정
    public int createSchedule (Schedule schedule)throws SQLException{
    	return scheduleDao.createSchedule(schedule);
    }
    public List<Schedule> findScheduleById(long studyId, String type) throws SQLException {
    	return scheduleDao.findSchedulesByStudyId(studyId, type);
    }
    public Schedule findScheduleDetailById(int scheduleId) {
    	return scheduleDao.findScheduleById(scheduleId);
    }
    public void updateSchedule (Schedule schedule)throws SQLException{
    	scheduleDao.updateSchedule(schedule);
    }
    public List<Integer> findScheduleIdsByStudyId(long studyId) throws SQLException {
    	return scheduleDao.findScheduleIdsByStudyId(studyId);
    }
    public void deleteScheduleById(int scheduleId) {
    	scheduleDao.deleteScheduleById(scheduleId);
    }
    
    
    //스터디 과제 목록 조회(특정 날짜)
    public List<Assignment> findAssignmentsByStudyIdAndDueDate(int studyId, LocalDate dueDate) {
    	return assignmentDao.findAssignmentsByStudyIdAndDueDate(studyId, dueDate);
    }
    //스터디 공지 목록 조회(특정 날짜)
    public List<Notice> findNoticesByStudyIdAndDueDate(int studyId, LocalDate createdAt) {
    	return noticeDao.findNoticesByStudyIdAndDueDate(studyId, createdAt);
    }
    
    public List<Notice> findNoticesBystudygroupid(int studygroupid) {
        return noticeDao.findNoticesBystudygroupid(studygroupid);
    }

    public Notice findNoticeById(int noticeId) {
        return noticeDao.findNoticeById(noticeId);
    }
    
    public List<Notice> searchNotices(int studygroupid, String searchParam) {
        return noticeDao.searchNotices(studygroupid, searchParam);
    }
    //스터디 일정 목록 조회
    public List<Schedule> findSchedulesByFilters(long studygroupid, LocalDate startDate, String type, String dayOfWeek) {
    	return scheduleDao.findSchedulesByFilters(studygroupid, startDate, type, dayOfWeek);
    }

    //스터디 공지 추가
    public void createNotice(Notice notice) {
    	noticeDao.createNotice(notice.getStudyId(), notice.getTitle(), notice.getDescription(), notice.getCreateat());
    }
    public void createAssignment(Assignment ass) {
    	assignmentDao.createAssignment(ass);
    }
    //스터디 멤버 조회
    public List<String> findStudyMembers(int studyGruopId) throws SQLException {
    	return studyDao.findStudyMembers(studyGruopId);
    }

    public List<Member> findStudyMemberDetails(int studyGroupId) throws SQLException {
    	return studyDao.findStudyMemberDetails(studyGroupId);
    }
    
    public List<Assignment> findAssignmentsByStudyId(int studyId) {
        return assignmentDao.findAssignmentsByStudyId(studyId);
    }

    public Assignment findAssignmentById(int assignmentId) {
        return assignmentDao.findAssignmentById(assignmentId);
    }
    
    public boolean isStudyConflict(String stuId, String newDayOfWeek) throws SQLException {
		return studyDao.isStudyConflict(stuId, newDayOfWeek);
    }

    public List<LocalDate> findMonthSchedule(int studyId, int month, int year) throws SQLException {
    	return studyDao.findMonthSchedule(studyId, month, year);
    }
    
}
