package controller.study;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.AuthorizationUtils;
import controller.Controller;
import controller.member.MemberSessionUtils;
import model.dao.AssignmentSubmissionDao;
import model.dao.AttachmentDao;
import model.domain.Assignment;
import model.domain.Notice;
import model.domain.Schedule;
import model.service.StudyGroupManager;
import model.service.StudyManager;

public class DeleteStudyItemController implements Controller {
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!MemberSessionUtils.hasLogined(request.getSession())) {
			return "redirect:/member/login/form";
		}

		String type = request.getParameter("type");
		int id = Integer.parseInt(request.getParameter("id"));
		String selectedDate = safeDate(request.getParameter("selectedDate"));
		String memberId = MemberSessionUtils.getLoginMemberId(request.getSession());
		StudyManager manager = StudyManager.getInstance();
		StudyGroupManager accessManager = StudyGroupManager.getInstance();

		int groupId;
		String itemType;
		if ("assignment".equals(type)) {
			Assignment assignment = manager.findAssignmentById(id);
			if (assignment == null) {
				return "redirect:/study/list";
			}
			groupId = assignment.getStudyId();
			itemType = "assignment";
		} else if ("notice".equals(type)) {
			Notice notice = manager.findNoticeById(id);
			if (notice == null) {
				return "redirect:/study/list";
			}
			groupId = notice.getStudyId();
			itemType = "notice";
		} else {
			Schedule schedule = manager.findScheduleDetailById(id);
			if (schedule == null) {
				return "redirect:/study/list";
			}
			groupId = (int) schedule.getStudyGroupId();
			itemType = "schedule";
		}

		if (!AuthorizationUtils.canManageStudy(accessManager, memberId, groupId)) {
			return "redirect:/mystudy/view?groupId=" + groupId + "&selectedDate=" + selectedDate;
		}

		new AttachmentDao().deleteByItem("study", itemType, id);
		if ("assignment".equals(itemType)) {
			new AssignmentSubmissionDao().deleteByAssignment("study", id);
			manager.deleteAssignmentById(id);
		} else if ("notice".equals(itemType)) {
			manager.deleteNoticeById(id);
		} else {
			manager.deleteScheduleById(id);
		}

		request.getSession().setAttribute("flashMessage", "항목이 삭제되었습니다.");
		return "redirect:/mystudy/view?groupId=" + groupId + "&selectedDate=" + selectedDate;
	}

	private String safeDate(String value) {
		try {
			return value == null || value.isBlank() ? LocalDate.now().toString() : LocalDate.parse(value).toString();
		} catch (Exception ex) {
			return LocalDate.now().toString();
		}
	}
}
