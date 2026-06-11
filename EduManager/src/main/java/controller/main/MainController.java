package controller.main;

import java.time.LocalDate;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import controller.Controller;
import controller.member.MemberSessionUtils;
import model.domain.Schedule;
import model.domain.Assignment;
import model.domain.Notice;
import model.domain.calendar.CalendarDTO;
import model.service.LectureManager;
import model.service.StudyManager;

public class MainController implements Controller {
	// private static final int countPerPage = 100; // 한 화면에 출력할 사용자 수

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 로그인 여부 확인
		if (!MemberSessionUtils.hasLogined(request.getSession())) {
			return "redirect:/member/login/form"; // login form 요청으로 redirect
		}

		/*
		 * String currentPageStr = request.getParameter("currentPage"); int currentPage
		 * = 1; if (currentPageStr != null && !currentPageStr.equals("")) { currentPage
		 * = Integer.parseInt(currentPageStr); }
		 */

		// 현재 날짜를 기준으로 기본값 설정
		LocalDate currentDate = LocalDate.now();

		// 파라미터로 연도와 월 가져오기 (기본값: 현재 날짜)
		String yearParam = request.getParameter("year");
		String monthParam = request.getParameter("month");
		String selectedDayParam = request.getParameter("selectedDay");

		// 파라미터가 없으면 현재 연도와 월을 기본값으로 설정
		int year = (yearParam != null && !yearParam.isEmpty()) ? Integer.parseInt(yearParam) : currentDate.getYear();
		int month = (monthParam != null && !monthParam.isEmpty()) ? Integer.parseInt(monthParam)
				: currentDate.getMonthValue();
		int selectedDay = (selectedDayParam != null && !selectedDayParam.isEmpty()) ? Integer.parseInt(selectedDayParam)
				: currentDate.getDayOfMonth();

		System.out.println("DEBUG: Year = " + year + ", Month = " + month + ", Selected Day = " + selectedDay);

		String memberId = MemberSessionUtils.getLoginMemberId(request.getSession());

		// LectureManager를 통해 데이터 가져오기
		LectureManager lectureManager = LectureManager.getInstance();
		List<Schedule> lectureScheduleEntries = lectureManager.getScheduleCalendarList(year, month, memberId);// LectureManager를
																									// 통해 데이터
		List<Notice> lectureNoticeEntries = lectureManager.getNoticeCalendarList(year, month, memberId);// LectureManager를 통해 데이터
																								// 가져오기
		List<Assignment> lectureAssignmentEntries = lectureManager.getAssignmentCalendarList(year, month, memberId);// LectureManager를
																											// 통해

		StudyManager studyManager = StudyManager.getInstance();
		List<Schedule> studyScheduleEntries = studyManager.getScheduleCalendarList(year, month, memberId);// LectureManager를
		// 통해 데이터
		List<Notice> studyNoticeEntries = studyManager.getNoticeCalendarList(year, month, memberId);// LectureManager를 통해 데이터
		// 가져오기
		List<Assignment> studyAssignmentEntries = studyManager.getAssignmentCalendarList(year, month, memberId);// LectureManager를

// 데이터
		// 가져오기

		// 데이터를 JSP에 전달
		request.setAttribute("lectureScheduleEntries", lectureScheduleEntries);
		request.setAttribute("lectureNoticeEntries", lectureNoticeEntries);
		request.setAttribute("lectureAssignmentEntries", lectureAssignmentEntries);
		request.setAttribute("studyScheduleEntries", studyScheduleEntries);
		request.setAttribute("studyNoticeEntries", studyNoticeEntries);
		request.setAttribute("studyAssignmentEntries", studyAssignmentEntries);
		request.setAttribute("year", year);
		request.setAttribute("month", month);
		request.setAttribute("selectedDay", selectedDay);

		// main 화면으로 이동(forwarding)
		return "/main/main.jsp";
	}
}
