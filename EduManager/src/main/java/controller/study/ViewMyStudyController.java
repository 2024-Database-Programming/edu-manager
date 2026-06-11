package controller.study;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.domain.Assignment;
import model.domain.Notice;
import model.domain.Schedule;
import model.domain.studyGroup.StudyGroup;
import model.service.StudyManager;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import controller.AuthorizationUtils;
import controller.Controller;
import controller.member.MemberSessionUtils;
import model.service.StudyGroupManager;


public class ViewMyStudyController implements Controller {
	private static final Logger log = LoggerFactory.getLogger(ViewMyStudyController.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!MemberSessionUtils.hasLogined(request.getSession())) {
			return "redirect:/member/login/form"; // login form 요청으로 redirect
		}
		StudyManager manager = StudyManager.getInstance();
		StudyGroupManager studyGroupManager = StudyGroupManager.getInstance();
		String memberId = MemberSessionUtils.getLoginMemberId(request.getSession());


        // 클라이언트에서 보낸 날짜 받기
        String selectedDateStr = request.getParameter("selectedDate");
        Integer studyId = Integer.parseInt(request.getParameter("groupId"));
        StudyGroup studyInfo = manager.findStudyById(studyId);
        if (!AuthorizationUtils.canViewStudy(studyGroupManager, memberId, studyId)) {
            return "redirect:/study/over-view?groupId=" + studyId;
        }
//        Integer studyId = 10;
        LocalDate selectedDate;
        if (selectedDateStr != null && !selectedDateStr.isEmpty()) {
            selectedDate = LocalDate.parse(selectedDateStr); // 날짜 포맷: YYYY-MM-DD
        }else {
            selectedDate = LocalDate.now(); // 오늘 날짜로 설정
        }
            log.debug("선택된 날짜: " + selectedDate);
 
            List<Assignment> assignmentList = manager.findAssignmentsByStudyIdAndDueDate(studyId, selectedDate);
            log.debug("과제목록: " + assignmentList);

            List<Notice> noticeList = manager.findNoticesByStudyIdAndDueDate(studyId, selectedDate);
            log.debug("공지목록: " + noticeList);
            
//            LocalDate today = LocalDate.now();
            String DayOfWeek = selectedDate.getDayOfWeek().toString(); // 오늘 요일 (예: "MONDAY")
            List<Schedule> regularSchedules = manager.findSchedulesByFilters(studyId, selectedDate, "regular", DayOfWeek);
            log.debug("정기일정: " + regularSchedules);
            log.debug("요일 " + DayOfWeek);
            
            List<Schedule> specialSchedules = manager.findSchedulesByFilters(studyId, selectedDate, "special", null);
            log.debug("특수일정: " + specialSchedules);
            
            // 필요한 비즈니스 로직 수행 (예: DB 조회)
            request.setAttribute("selectedDate", selectedDate);
            request.setAttribute("assignmentList", assignmentList);
            request.setAttribute("noticeList", noticeList);
            request.setAttribute("regularSchedules", regularSchedules);
            request.setAttribute("specialSchedules", specialSchedules);

            log.debug("studyInfo: " + studyInfo);

            List<String> members = manager.findStudyMembers(studyId);
            log.debug("members: " + members);

    		Boolean isLeader = AuthorizationUtils.canManageStudy(memberId, studyInfo);
            
      //study 기본 정보
            request.setAttribute("studyInfo", studyInfo);
            request.setAttribute("isLeader", isLeader);
            request.setAttribute("members", members);
        // JSP 페이지로 포워딩 (예: 결과 표시)
        return "/study/study_details.jsp"; // 뷰로 이동
	}
}
