package controller.lecture;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.AuthorizationUtils;
import controller.Controller;
import controller.MultipartUploadUtils;
import controller.member.MemberSessionUtils;
import model.domain.Assignment;
import model.domain.Notice;
import model.domain.Schedule;
import model.domain.lecture.Lecture;
import model.service.lecture.LectureManager;

public class EditLectureItemController implements Controller {
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!MemberSessionUtils.hasLogined(request.getSession())) {
			return "redirect:/member/login/form";
		}

		String type = normalizeType(request.getParameter("type"));
		int id = Integer.parseInt(request.getParameter("id"));
		String selectedDate = safeDate(request.getParameter("selectedDate"));
		String memberId = MemberSessionUtils.getLoginMemberId(request.getSession());
		LectureManager manager = LectureManager.getInstance();
		ItemContext context = loadContext(manager, type, id);
		if (context == null) {
			return "redirect:/lecture/list";
		}

		Lecture lecture = manager.findLectureById(context.ownerId);
		if (!AuthorizationUtils.canManageLecture(memberId, lecture)) {
			return "redirect:/mylecture/view?lectureId=" + context.ownerId + "&selectedDate=" + selectedDate;
		}

		if ("GET".equalsIgnoreCase(request.getMethod())) {
			fillFormAttributes(request, context, lecture, selectedDate);
			return "/item/edit.jsp";
		}

		try {
			if ("assignment".equals(type)) {
				updateAssignment(request, manager, context.assignment);
			} else if ("notice".equals(type)) {
				manager.updateNoticeById(id, requiredText(request, "title", "제목을 입력해 주세요."),
						requiredText(request, "description", "상세 내용을 입력해 주세요."));
			} else {
				updateSchedule(request, manager, context.schedule);
			}
			MultipartUploadUtils.saveAttachments(request, "lecture", context.itemType, id);
			request.getSession().setAttribute("flashMessage", "항목이 수정되었습니다.");
			return "redirect:/lecture/itemDetail?type=" + context.itemType + "&id=" + id + "&selectedDate="
					+ selectedDate;
		} catch (IllegalArgumentException ex) {
			request.getSession().setAttribute("flashError", ex.getMessage());
			return "redirect:/lecture/editItem?type=" + context.itemType + "&id=" + id + "&selectedDate="
					+ selectedDate;
		} catch (Exception ex) {
			request.getSession().setAttribute("flashError", "항목 수정에 실패했습니다. 입력값과 첨부파일을 확인해 주세요.");
			return "redirect:/lecture/editItem?type=" + context.itemType + "&id=" + id + "&selectedDate="
					+ selectedDate;
		}
	}

	private void updateAssignment(HttpServletRequest request, LectureManager manager, Assignment original) {
		LocalDate startDate = parseRequiredDate(request, "startDate", "과제 시작일을 선택해 주세요.");
		LocalTime startTime = parseRequiredTime(request, "startTime", "과제 시작시간을 선택해 주세요.");
		LocalDate dueDate = parseRequiredDate(request, "dueDate", "과제 마감일을 선택해 주세요.");
		LocalTime dueTime = parseRequiredTime(request, "dueTime", "과제 마감시간을 선택해 주세요.");
		if (LocalDateTime.of(startDate, startTime).isAfter(LocalDateTime.of(dueDate, dueTime))) {
			throw new IllegalArgumentException("과제 시작일시가 마감일시보다 늦습니다.");
		}
		Assignment assignment = new Assignment();
		assignment.setId(original.getId());
		assignment.setLectureId(original.getLectureId());
		assignment.setTextFile(original.getTextFile());
		assignment.setTitle(requiredText(request, "title", "제목을 입력해 주세요."));
		assignment.setDescription(requiredText(request, "description", "상세 내용을 입력해 주세요."));
		assignment.setCreateat(startDate);
		assignment.setStartTime(startTime);
		assignment.setDueDate(dueDate);
		assignment.setDueTime(dueTime);
		manager.updateAssignment(assignment);
	}

	private void updateSchedule(HttpServletRequest request, LectureManager manager, Schedule original) throws Exception {
		Schedule schedule = new Schedule();
		schedule.setScheduleId(original.getScheduleId());
		schedule.setLectureId(original.getLectureId());
		schedule.setDayOfWeek(original.getDayOfWeek());
		schedule.setFrequency(original.getFrequency());
		schedule.setTitle(requiredText(request, "title", "제목을 입력해 주세요."));
		schedule.setDescription(trim(request.getParameter("description")));
		schedule.setStartDate(parseRequiredDate(request, "startDate", "일정 날짜를 선택해 주세요."));
		schedule.setStartTime(parseRequiredTime(request, "startTime", "시작 시간을 선택해 주세요."));
		schedule.setEndTime(parseRequiredTime(request, "endTime", "종료 시간을 선택해 주세요."));
		schedule.setType(normalizeScheduleCategory(request.getParameter("category"), original.getType()));
		manager.updateSchedule(schedule);
	}

	private void fillFormAttributes(HttpServletRequest request, ItemContext context, Lecture lecture,
			String selectedDate) {
		request.setAttribute("ownerType", "강의");
		request.setAttribute("ownerTitle", lecture.getName());
		request.setAttribute("itemKind", context.itemType);
		request.setAttribute("itemLabel", context.label);
		request.setAttribute("itemId", context.id);
		request.setAttribute("selectedDate", selectedDate);
		request.setAttribute("formAction", request.getContextPath() + "/lecture/editItem");
		request.setAttribute("backUrl", request.getContextPath() + "/lecture/itemDetail?type=" + context.itemType
				+ "&id=" + context.id + "&selectedDate=" + selectedDate);
		request.setAttribute("titleValue", context.title);
		request.setAttribute("descriptionValue", context.description);
		request.setAttribute("startDateValue", context.startDate);
		request.setAttribute("startTimeValue", context.startTime);
		request.setAttribute("dueDateValue", context.dueDate);
		request.setAttribute("dueTimeValue", context.dueTime);
		request.setAttribute("categoryValue", context.category);
	}

	private ItemContext loadContext(LectureManager manager, String type, int id) {
		if ("assignment".equals(type)) {
			Assignment assignment = manager.findAssignmentById(id);
			if (assignment == null) {
				return null;
			}
			ItemContext context = baseContext(id, "assignment", "과제", assignment.getLectureId(),
					assignment.getTitle(), assignment.getDescription());
			context.assignment = assignment;
			context.startDate = assignment.getCreateat();
			context.startTime = assignment.getStartTime() == null ? LocalTime.of(0, 0) : assignment.getStartTime();
			context.dueDate = assignment.getDueDate();
			context.dueTime = assignment.getDueTime() == null ? LocalTime.of(23, 59) : assignment.getDueTime();
			return context;
		}
		if ("notice".equals(type)) {
			Notice notice = manager.findNoticeById(id);
			return notice == null ? null : baseContext(id, "notice", "공지사항", notice.getLectureId(), notice.getTitle(),
					notice.getDescription());
		}
		Schedule schedule = manager.findScheduleDetailById(id);
		if (schedule == null) {
			return null;
		}
		ItemContext context = baseContext(id, "schedule", scheduleLabel(schedule), (int) schedule.getLectureId(),
				scheduleTitle(schedule), schedule.getDescription());
		context.schedule = schedule;
		context.startDate = schedule.getStartDate();
		context.startTime = schedule.getStartTime();
		context.dueTime = schedule.getEndTime();
		context.category = schedule.getType();
		return context;
	}

	private ItemContext baseContext(int id, String itemType, String label, int ownerId, String title,
			String description) {
		ItemContext context = new ItemContext();
		context.id = id;
		context.itemType = itemType;
		context.label = label;
		context.ownerId = ownerId;
		context.title = title;
		context.description = description;
		return context;
	}

	private String normalizeType(String type) {
		return "assignment".equals(type) || "notice".equals(type) ? type : "schedule";
	}

	private String normalizeScheduleCategory(String category, String fallback) {
		if ("regular".equals(category) || "class".equals(category) || "exam".equals(category) || "event".equals(category)) {
			return category;
		}
		return fallback == null || fallback.isBlank() ? "event" : fallback;
	}

	private String scheduleLabel(Schedule schedule) {
		if ("exam".equals(schedule.getType())) {
			return "시험";
		}
		if ("event".equals(schedule.getType())) {
			return "일정";
		}
		return "수업";
	}

	private String scheduleTitle(Schedule schedule) {
		if (schedule.getTitle() != null && !schedule.getTitle().isBlank()) {
			return schedule.getTitle();
		}
		return "regular".equals(schedule.getType()) ? "정기 수업" : scheduleLabel(schedule);
	}

	private String requiredText(HttpServletRequest request, String name, String message) {
		String value = trim(request.getParameter(name));
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException(message);
		}
		return value;
	}

	private LocalDate parseRequiredDate(HttpServletRequest request, String name, String message) {
		try {
			String value = requiredText(request, name, message);
			return LocalDate.parse(value);
		} catch (IllegalArgumentException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new IllegalArgumentException(message);
		}
	}

	private LocalTime parseRequiredTime(HttpServletRequest request, String name, String message) {
		try {
			String value = requiredText(request, name, message);
			return LocalTime.parse(value);
		} catch (IllegalArgumentException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new IllegalArgumentException(message);
		}
	}

	private String trim(String value) {
		return value == null ? null : value.trim();
	}

	private String safeDate(String value) {
		try {
			return value == null || value.isBlank() ? LocalDate.now().toString() : LocalDate.parse(value).toString();
		} catch (Exception ex) {
			return LocalDate.now().toString();
		}
	}

	private static class ItemContext {
		int id;
		int ownerId;
		String itemType;
		String label;
		String title;
		String description;
		LocalDate startDate;
		LocalTime startTime;
		LocalDate dueDate;
		LocalTime dueTime;
		String category;
		Assignment assignment;
		Schedule schedule;
	}
}
