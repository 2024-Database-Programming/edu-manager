package controller.lecture;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.domain.Assignment;
import model.domain.Notice;
import model.domain.Schedule;
import model.domain.lecture.Lecture;
import model.domain.member.Member;
import model.domain.study.StudyGroup;
import model.service.lecture.LectureManager;
import model.service.study.StudyManager;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import controller.AuthorizationUtils;
import controller.CalendarEventJsonUtils;
import controller.Controller;
import controller.member.MemberSessionUtils;


public class ViewMyLectureController implements Controller {
	private static final Logger log = LoggerFactory.getLogger(ViewMyLectureController.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!MemberSessionUtils.hasLogined(request.getSession())) {
			return "redirect:/member/login/form"; // login form 요청으로 redirect
		}
        LectureManager manager = LectureManager.getInstance();
        String memberId = MemberSessionUtils.getLoginMemberId(request.getSession());


        // 클라이언트에서 보낸 날짜 받기
        String selectedDateStr = request.getParameter("selectedDate");
        Integer lectureId = Integer.parseInt(request.getParameter("lectureId"));
        Lecture lectureInfo = manager.findLectureById(lectureId);
        if (!AuthorizationUtils.canViewLecture(manager, memberId, lectureInfo)) {
            return "redirect:/lecture/over-view?lectureId=" + lectureId;
        }
//        Integer studyId = 10;
        LocalDate selectedDate;
        if (selectedDateStr != null && !selectedDateStr.isEmpty()) {
            selectedDate = LocalDate.parse(selectedDateStr); // 날짜 포맷: YYYY-MM-DD
        }else {
            selectedDate = LocalDate.now(); // 오늘 날짜로 설정
        }
            log.debug("선택된 날짜: " + selectedDate);
 
            List<Notice> noticeList = manager.findNoticesByLectureId(lectureId);
            log.debug("공지목록: " + noticeList);
            
//            LocalDate today = LocalDate.now();
            String DayOfWeek = selectedDate.getDayOfWeek().toString(); // 오늘 요일 (예: "MONDAY")
            List<Schedule> regularSchedules = manager.findSchedulesByFilters(lectureId, selectedDate, "regular", DayOfWeek);
            log.debug("정기일정: " + regularSchedules);
            log.debug("요일 " + DayOfWeek);
            
            List<Schedule> specialSchedules = manager.findSchedulesByFilters(lectureId, selectedDate, "special", null);
            log.debug("특수일정: " + specialSchedules);
            
            

            List<Assignment> assignments = manager.findAssignmentsByLectureId(lectureId);
            List<Assignment> selectedAssignments = manager.findAssignmentsByLectureIdAndDueDate(lectureId, selectedDate);
            List<Schedule> calendarSchedules = manager.getScheduleCalendarList(
                    selectedDate.getYear(), selectedDate.getMonthValue(), memberId);
            String calendarEventsJson = CalendarEventJsonUtils.build(
                    calendarSchedules, assignments, lectureId, YearMonth.from(selectedDate), false);
            log.debug("과제: " + assignments);

            // 필요한 비즈니스 로직 수행 (예: DB 조회)
            request.setAttribute("selectedDate", selectedDate);
            request.setAttribute("assignmentList", assignments);
            request.setAttribute("selectedAssignmentList", selectedAssignments);
            request.setAttribute("noticeList", noticeList);
            request.setAttribute("regularSchedules", regularSchedules);
            request.setAttribute("specialSchedules", specialSchedules);
            request.setAttribute("calendarEventsJson", calendarEventsJson);

            log.debug("lectureInfo: " + lectureId);

            List<Member> members = manager.findLectureMemberDetails(lectureId);
            log.debug("members: " + members);

    		Boolean isTeacher= AuthorizationUtils.canManageLecture(memberId, lectureInfo);
            
    		
    		
    	    List<LocalDate> events = manager.findMonthSchedule(lectureId, selectedDate.getMonthValue(), selectedDate.getYear());
        log.debug("events: " + events);

//    	     List<Schedule> regularEvents = manager.findSchedulesByFilters(lectureId, selectedDate.withDayOfMonth(selectedDate.lengthOfMonth()), "regular", null);
//          log.debug("regularEvents: " + regularEvents);
////
////          log.debug("uniqueDates: " + uniqueDates);
//
//
//         // selectedDate의 해당 월 시작과 끝 날짜
//            LocalDate startOfMonth = selectedDate.withDayOfMonth(1);
//            LocalDate endOfMonth = selectedDate.withDayOfMonth(selectedDate.lengthOfMonth());
//
//            List<LocalDate> repeatingDatesInMonth = new ArrayList<>();
//
//            // 각 regularEvent를 확인
//            for (Schedule event : regularEvents) {
//                LocalDate startDate = event.getStartDate();  // 일정 시작 날짜
//                String dayOfWeek = event.getDayOfWeek();  // 반복되는 요일
//
//                if (startDate.isBefore(selectedDate)) {
//                    // 해당 월에 반복되는 일정들을 찾기 위한 로직
//                    LocalDate currentDate = startDate;
//
//                    // 반복되는 일정이 해당 달에 포함되는지 확인
//                    while (!currentDate.isAfter(endOfMonth)) {
//                        if (currentDate.isAfter(startOfMonth) && currentDate.getDayOfWeek().toString() == dayOfWeek) {
//                        	repeatingDatesInMonth.add(currentDate);
//                            break;
//                        }
//                        // 다음 주로 이동
//                        currentDate = currentDate.plusWeeks(1);
//                    }
//                }
//            }
//            events.addAll(repeatingDatesInMonth);
          log.debug("events: " + events);
//          log.debug("repeatingDatesInMonth: " + repeatingDatesInMonth);
//    		
    		//study 기본 정보
            request.setAttribute("events", events);
            request.setAttribute("lectureInfo", lectureInfo);
            request.setAttribute("isTeacher", isTeacher);
            request.setAttribute("members", members);
        // JSP 페이지로 포워딩 (예: 결과 표시)
        return "/lecture/lecture_details.jsp"; // 뷰로 이동
	}
}
