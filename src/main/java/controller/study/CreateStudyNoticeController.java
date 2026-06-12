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
import model.domain.Notice;
import model.domain.Schedule;
import model.domain.studyGroup.StudyGroup;
import model.service.StudyGroupManager;
import model.service.StudyManager;

public class CreateStudyNoticeController implements Controller {
	private static final Logger log = LoggerFactory.getLogger(CreateStudyNoticeController.class);

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
			System.out.print("내 아이디 : 스터디 리더:" + memberId);

		// GET요청
		if (request.getMethod().equals("GET")) {
			request.setAttribute("groupId", request.getParameter("groupId"));
			request.setAttribute("startDate", LocalDate.parse(request.getParameter("selectedDate")));
			return "/study/addNotice.jsp";
		};

		// POST요청
		try {
			Notice notice = new Notice();
			notice.setTitle(request.getParameter("title"));
			notice.setDescription(request.getParameter("description"));
			notice.setCreateat(LocalDate.parse(request.getParameter("startDate")));
			notice.setStudyId(Integer.parseInt(request.getParameter("groupId")));
			
				manager.createNotice(notice);

			return "redirect:/mystudy/view?groupId=" + Long.parseLong(request.getParameter("groupId")) 
		       + "&selectedDate=" + LocalDate.parse(request.getParameter("startDate"));
			
		} catch (Exception e) { // 예외 발생 시 입력 form으로 forwarding
			request.setAttribute("creationFailed", true);
			request.setAttribute("exception", e);
			System.out.print(e);

			return "redirect:/member/login/form";
		}
	}
}
