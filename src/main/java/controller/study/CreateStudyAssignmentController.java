package controller.study;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.AuthorizationUtils;
import controller.Controller;
import controller.member.MemberSessionUtils;
import model.domain.Assignment;
import model.domain.Notice;
import model.domain.Schedule;
import model.domain.studyGroup.StudyGroup;
import model.service.StudyGroupManager;
import model.service.StudyManager;

public class CreateStudyAssignmentController implements Controller {
	private static final Logger log = LoggerFactory.getLogger(CreateStudyAssignmentController.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!MemberSessionUtils.hasLogined(request.getSession())) {
			return "redirect:/member/login/form"; // login form 요청으로 redirect
			}

			String memberId = MemberSessionUtils.getLoginMemberId(request.getSession());
			long groupId = Long.parseLong(request.getParameter("groupId"));
			StudyManager manager = StudyManager.getInstance();
			StudyGroupManager studyGroupManager = StudyGroupManager.getInstance();
			if (!AuthorizationUtils.canManageStudy(studyGroupManager, memberId, groupId)) {
				return "redirect:/study/over-view?groupId=" + groupId;
			}

		// GET요청
		if (request.getMethod().equals("GET")) {
			request.setAttribute("groupId", request.getParameter("groupId"));
			request.setAttribute("startDate", LocalDate.parse(request.getParameter("selectedDate")));
			return "/study/addAssignment.jsp";
		};

		// POST요청
		try {
			Assignment assignment = new Assignment();
			
			assignment.setTitle(request.getParameter("title"));
			assignment.setDescription(request.getParameter("description"));
			assignment.setDueDate(LocalDate.parse(request.getParameter("dueDate")));
			assignment.setStudyId(Integer.parseInt(request.getParameter("groupId")));
			
				manager.createAssignment(assignment);

			return "redirect:/mystudy/view?groupId=" + Long.parseLong(request.getParameter("groupId")) 
		       + "&selectedDate=" + LocalDate.parse(request.getParameter("startDate"));
			
		} catch (Exception e) { // 예외 발생 시 입력 form으로 forwarding
			e.printStackTrace();
			request.getSession().setAttribute("flashError", "과제 등록 중 오류가 발생했습니다.");
			return "redirect:/study/over-view?groupId=" + groupId;
		}
	}
}
