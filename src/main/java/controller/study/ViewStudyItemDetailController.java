package controller.study;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.AuthorizationUtils;
import controller.Controller;
import controller.member.MemberSessionUtils;
import model.domain.Assignment;
import model.domain.Notice;
import model.domain.Schedule;
import model.domain.studyGroup.StudyGroup;
import model.service.StudyGroupManager;
import model.service.StudyManager;

public class ViewStudyItemDetailController implements Controller {
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!MemberSessionUtils.hasLogined(request.getSession())) {
			return "redirect:/member/login/form";
		}

		String type = request.getParameter("type");
		int id = Integer.parseInt(request.getParameter("id"));
		String memberId = MemberSessionUtils.getLoginMemberId(request.getSession());
		StudyManager manager = StudyManager.getInstance();
		StudyGroupManager accessManager = StudyGroupManager.getInstance();

		int groupId;
		String label;
		String title;
		String meta;
		String description;
		String attachment = "";

		if ("assignment".equals(type)) {
			Assignment assignment = manager.findAssignmentById(id);
			if (assignment == null) {
				return "redirect:/study/list";
			}
			groupId = assignment.getStudyId();
			label = "과제";
			title = safe(assignment.getTitle(), "과제");
			meta = assignmentPeriod(assignment);
			description = safe(assignment.getDescription(), "등록된 상세 설명이 없습니다.");
			attachment = safe(assignment.getTextFile(), "");
		} else if ("notice".equals(type)) {
			Notice notice = manager.findNoticeById(id);
			if (notice == null) {
				return "redirect:/study/list";
			}
			groupId = notice.getStudyId();
			label = "공지사항";
			title = safe(notice.getTitle(), "공지사항");
			meta = notice.getCreateat() == null ? "" : "등록일 " + notice.getCreateat();
			description = safe(notice.getDescription(), "등록된 상세 설명이 없습니다.");
		} else {
			Schedule schedule = manager.findScheduleDetailById(id);
			if (schedule == null) {
				return "redirect:/study/list";
			}
			groupId = (int) schedule.getStudyGroupId();
			label = scheduleLabel(type, schedule);
			title = scheduleTitle(type, schedule);
			meta = scheduleMeta(schedule, request.getParameter("selectedDate"));
			description = scheduleDescription(type);
		}

		StudyGroup study = manager.findStudyById(groupId);
		if (!AuthorizationUtils.canViewStudy(accessManager, memberId, groupId)) {
			return "redirect:/study/over-view?groupId=" + groupId;
		}

		String selectedDate = request.getParameter("selectedDate");
		if (selectedDate == null || selectedDate.isBlank()) {
			selectedDate = LocalDate.now().toString();
		}
		String backUrl = request.getContextPath() + "/mystudy/view?groupId=" + groupId
				+ "&selectedDate=" + selectedDate;
		String listUrl = backUrl;
		String listLabel = "캘린더";
		if ("assignment".equals(type)) {
			listUrl = request.getContextPath() + "/study/listAssignment?groupId=" + groupId;
			listLabel = "목록";
		} else if ("notice".equals(type)) {
			listUrl = request.getContextPath() + "/study/listNotice?groupId=" + groupId;
			listLabel = "목록";
		}

		request.setAttribute("ownerType", "스터디");
		request.setAttribute("ownerTitle", study.getName());
		request.setAttribute("itemLabel", label);
		request.setAttribute("itemTitle", title);
		request.setAttribute("itemMeta", meta);
		request.setAttribute("itemDescription", description);
		request.setAttribute("itemAttachment", attachment);
		request.setAttribute("backUrl", backUrl);
		request.setAttribute("listUrl", listUrl);
		request.setAttribute("listLabel", listLabel);
		request.setAttribute("backLabel", "스터디 화면으로 돌아가기");
		return "/item/detail.jsp";
	}

	private String scheduleLabel(String type, Schedule schedule) {
		if ("exam".equals(type) || containsExamKeyword(schedule.getTitle())) {
			return "시험";
		}
		if ("event".equals(type)) {
			return "일정";
		}
		return "수업";
	}

	private String scheduleTitle(String type, Schedule schedule) {
		if (schedule.getTitle() != null && !schedule.getTitle().isBlank()) {
			return schedule.getTitle();
		}
		if ("regular".equals(schedule.getType())) {
			return "정기모임";
		}
		if ("exam".equals(type) || "exam".equals(schedule.getType())) {
			return "시험";
		}
		if ("event".equals(type)) {
			return "일정";
		}
		return "단기 수업";
	}

	private String scheduleMeta(Schedule schedule, String selectedDate) {
		String date = selectedDate != null && !selectedDate.isBlank()
				? selectedDate
				: schedule.getStartDate() == null ? "" : schedule.getStartDate().toString();
		String time = timeRange(schedule.getStartTime(), schedule.getEndTime());
		return time.isBlank() ? date : date + " · " + time;
	}

	private String scheduleDescription(String type) {
		if ("exam".equals(type)) {
			return "선택한 날짜에 등록된 시험입니다.";
		}
		if ("event".equals(type)) {
			return "선택한 날짜에 등록된 일정입니다.";
		}
		return "선택한 날짜에 진행되는 수업/모임입니다.";
	}

	private String assignmentPeriod(Assignment assignment) {
		String start = joinDateTime(assignment.getCreateat(), assignment.getStartTime());
		String end = joinDateTime(assignment.getDueDate(), assignment.getDueTime());
		if (start.isBlank()) {
			return end.isBlank() ? "" : "마감 " + end;
		}
		if (end.isBlank()) {
			return "시작 " + start;
		}
		return start + " ~ " + end;
	}

	private String joinDateTime(LocalDate date, LocalTime time) {
		if (date == null) {
			return "";
		}
		return time == null ? date.toString() : date + " " + time;
	}

	private String timeRange(LocalTime start, LocalTime end) {
		if (start == null && end == null) {
			return "";
		}
		if (start == null) {
			return "~ " + end;
		}
		if (end == null) {
			return start + " ~";
		}
		return start + "~" + end;
	}

	private boolean containsExamKeyword(String title) {
		return title != null && (title.contains("시험") || title.contains("중간") || title.contains("기말") || title.contains("평가"));
	}

	private String safe(String value, String fallback) {
		return value == null || value.isBlank() ? fallback : value;
	}
}
