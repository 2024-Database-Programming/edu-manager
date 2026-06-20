package controller.lecture;

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
import model.service.LectureManager;

public class DeleteLectureItemController implements Controller {
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!MemberSessionUtils.hasLogined(request.getSession())) {
			return "redirect:/member/login/form";
		}

		String type = request.getParameter("type");
		int id = Integer.parseInt(request.getParameter("id"));
		String selectedDate = safeDate(request.getParameter("selectedDate"));
		String memberId = MemberSessionUtils.getLoginMemberId(request.getSession());
		LectureManager manager = LectureManager.getInstance();

		int lectureId;
		String itemType;
		if ("assignment".equals(type)) {
			Assignment assignment = manager.findAssignmentById(id);
			if (assignment == null) {
				return "redirect:/lecture/list";
			}
			lectureId = assignment.getLectureId();
			itemType = "assignment";
		} else if ("notice".equals(type)) {
			Notice notice = manager.findNoticeById(id);
			if (notice == null) {
				return "redirect:/lecture/list";
			}
			lectureId = notice.getLectureId();
			itemType = "notice";
		} else {
			Schedule schedule = manager.findScheduleDetailById(id);
			if (schedule == null) {
				return "redirect:/lecture/list";
			}
			lectureId = (int) schedule.getLectureId();
			itemType = "schedule";
		}

		if (!AuthorizationUtils.canManageLecture(manager, memberId, lectureId)) {
			return "redirect:/mylecture/view?lectureId=" + lectureId + "&selectedDate=" + selectedDate;
		}

		try {
			new AttachmentDao().deleteByItem("lecture", itemType, id);
			if ("assignment".equals(itemType)) {
				new AssignmentSubmissionDao().deleteByAssignment("lecture", id);
				manager.deleteAssignmentById(id);
			} else if ("notice".equals(itemType)) {
				manager.deleteNoticeById(id);
			} else {
				manager.deleteScheduleById(id);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			request.getSession().setAttribute("flashError", "삭제 중 오류가 발생했습니다. 다시 시도해 주세요.");
			return "redirect:/mylecture/view?lectureId=" + lectureId + "&selectedDate=" + selectedDate;
		}

		request.getSession().setAttribute("flashMessage", "항목이 삭제되었습니다.");
		return "redirect:/mylecture/view?lectureId=" + lectureId + "&selectedDate=" + selectedDate;
	}

	private String safeDate(String value) {
		try {
			return value == null || value.isBlank() ? LocalDate.now().toString() : LocalDate.parse(value).toString();
		} catch (Exception ex) {
			return LocalDate.now().toString();
		}
	}
}
