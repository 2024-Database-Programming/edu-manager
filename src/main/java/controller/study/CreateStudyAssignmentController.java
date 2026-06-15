package controller.study;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.AuthorizationUtils;
import controller.Controller;
import controller.member.MemberSessionUtils;
import model.domain.Assignment;
import model.service.StudyGroupManager;
import model.service.StudyManager;

public class CreateStudyAssignmentController implements Controller {
	private static final Logger log = LoggerFactory.getLogger(CreateStudyAssignmentController.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!MemberSessionUtils.hasLogined(request.getSession())) {
			return "redirect:/member/login/form";
		}

		String memberId = MemberSessionUtils.getLoginMemberId(request.getSession());
		long groupId = Long.parseLong(request.getParameter("groupId"));
		StudyManager manager = StudyManager.getInstance();
		StudyGroupManager studyGroupManager = StudyGroupManager.getInstance();
		if (!AuthorizationUtils.canManageStudy(studyGroupManager, memberId, groupId)) {
			return "redirect:/study/over-view?groupId=" + groupId;
		}

		if ("GET".equalsIgnoreCase(request.getMethod())) {
			request.setAttribute("groupId", request.getParameter("groupId"));
			request.setAttribute("startDate", parseDateOrToday(request.getParameter("selectedDate")));
			return "/study/addAssignment.jsp";
		}

		String startDateParam = request.getParameter("startDate");
		try {
			String title = requiredText(request, "title", "과제 제목을 입력해 주세요.");
			String description = requiredText(request, "description", "과제 세부 내용을 입력해 주세요.");
			LocalDate startDate = parseRequiredDate(request, "startDate", "과제 시작일을 선택해 주세요.");
			LocalTime startTime = parseRequiredTime(request, "startTime", "과제 시작시간을 선택해 주세요.");
			LocalDate dueDate = parseRequiredDate(request, "dueDate", "과제 마감일을 선택해 주세요.");
			LocalTime dueTime = parseRequiredTime(request, "dueTime", "과제 마감시간을 선택해 주세요.");

			if (LocalDateTime.of(startDate, startTime).isAfter(LocalDateTime.of(dueDate, dueTime))) {
				throw new IllegalArgumentException("과제 시작일시가 마감일시보다 늦습니다. 시작/마감 시간을 다시 확인해 주세요.");
			}

			Assignment assignment = new Assignment();
			assignment.setTitle(title);
			assignment.setDescription(description);
			assignment.setCreateat(startDate);
			assignment.setStartTime(startTime);
			assignment.setDueDate(dueDate);
			assignment.setDueTime(dueTime);
			assignment.setStudyId((int) groupId);
			assignment.setTextFile("");

			manager.createAssignment(assignment);

			return "redirect:/mystudy/view?groupId=" + groupId + "&selectedDate=" + startDate;
		} catch (Exception e) {
			log.warn("스터디 과제 등록 실패. groupId={}, startDate={}", groupId, startDateParam, e);
			request.getSession().setAttribute("flashError", buildAssignmentErrorMessage(e));
			return "redirect:/study/addAssignment?groupId=" + groupId + "&selectedDate="
					+ parseRedirectDate(startDateParam);
		}
	}

	private String requiredText(HttpServletRequest request, String name, String message) {
		String value = request.getParameter(name);
		if (value == null || value.trim().isEmpty()) {
			throw new IllegalArgumentException(message);
		}
		return value.trim();
	}

	private LocalDate parseRequiredDate(HttpServletRequest request, String name, String message) {
		String value = request.getParameter(name);
		if (value == null || value.trim().isEmpty()) {
			throw new IllegalArgumentException(message);
		}
		try {
			return LocalDate.parse(value);
		} catch (Exception e) {
			throw new IllegalArgumentException(message + " 날짜 형식이 올바르지 않습니다.", e);
		}
	}

	private LocalTime parseRequiredTime(HttpServletRequest request, String name, String message) {
		String value = request.getParameter(name);
		if (value == null || value.trim().isEmpty()) {
			throw new IllegalArgumentException(message);
		}
		try {
			return LocalTime.parse(value);
		} catch (Exception e) {
			throw new IllegalArgumentException(message + " 시간 형식이 올바르지 않습니다.", e);
		}
	}

	private LocalDate parseDateOrToday(String value) {
		if (value == null || value.trim().isEmpty()) {
			return LocalDate.now();
		}
		try {
			return LocalDate.parse(value);
		} catch (Exception e) {
			return LocalDate.now();
		}
	}

	private String parseRedirectDate(String value) {
		return parseDateOrToday(value).toString();
	}

	private String buildAssignmentErrorMessage(Exception e) {
		if (e instanceof IllegalArgumentException) {
			return e.getMessage();
		}

		String detail = rootMessage(e);
		if (detail.contains("ORA-12899")) {
			return "입력한 과제 제목 또는 세부 내용이 저장 가능한 길이를 넘었습니다. 내용을 조금 줄여 주세요.";
		}
		if (detail.contains("ORA-01400")) {
			return "필수 입력값이 비어 있습니다. 제목, 시작일시, 마감일시, 세부 내용을 모두 입력해 주세요.";
		}
		if (detail.contains("ORA-02291")) {
			return "연결된 스터디 정보를 찾을 수 없습니다. 스터디 상세 화면에서 다시 과제를 추가해 주세요.";
		}
		return "과제 등록에 실패했습니다. 제목, 시작일/시작시간, 마감일/마감시간을 확인해 주세요.";
	}

	private String rootMessage(Throwable throwable) {
		Throwable root = throwable;
		while (root.getCause() != null) {
			root = root.getCause();
		}
		return root.getMessage() == null ? "" : root.getMessage();
	}
}
