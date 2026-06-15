package controller.lecture;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.AuthorizationUtils;
import controller.Controller;
import controller.MultipartUploadUtils;
import controller.MultipartUploadUtils.UploadedFile;
import controller.member.MemberSessionUtils;
import model.dao.AssignmentSubmissionDao;
import model.domain.Assignment;
import model.domain.AssignmentSubmission;
import model.domain.lecture.Lecture;
import model.service.LectureManager;

public class SubmitLectureAssignmentController implements Controller {
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!MemberSessionUtils.hasLogined(request.getSession())) {
			return "redirect:/member/login/form";
		}

		String memberId = MemberSessionUtils.getLoginMemberId(request.getSession());
		int assignmentId = Integer.parseInt(request.getParameter("assignmentId"));
		String selectedDate = safeDate(request.getParameter("selectedDate"));
		LectureManager manager = LectureManager.getInstance();
		Assignment assignment = manager.findAssignmentById(assignmentId);
		if (assignment == null) {
			return "redirect:/lecture/list";
		}

		Lecture lecture = manager.findLectureById(assignment.getLectureId());
		if (!AuthorizationUtils.canViewLecture(manager, memberId, lecture)
				|| AuthorizationUtils.canManageLecture(memberId, lecture)) {
			return "redirect:/lecture/itemDetail?type=assignment&id=" + assignmentId + "&selectedDate=" + selectedDate;
		}

		String description = trim(request.getParameter("submissionDescription"));
		UploadedFile file = MultipartUploadUtils.readOptionalFile(request, "submissionFile");
		if ((description == null || description.isBlank()) && file == null) {
			request.getSession().setAttribute("flashError", "제출 내용 또는 파일을 하나 이상 입력해 주세요.");
			return "redirect:/lecture/itemDetail?type=assignment&id=" + assignmentId + "&selectedDate=" + selectedDate;
		}

		AssignmentSubmission submission = new AssignmentSubmission();
		submission.setOwnerType("lecture");
		submission.setAssignmentId(assignmentId);
		submission.setStudentId(memberId);
		submission.setDescription(description);
		if (file != null) {
			submission.setFileName(file.fileName);
			submission.setContentType(file.contentType);
			submission.setData(file.data);
		}
		new AssignmentSubmissionDao().saveOrReplace(submission);
		request.getSession().setAttribute("flashMessage", "과제가 제출되었습니다.");
		return "redirect:/lecture/itemDetail?type=assignment&id=" + assignmentId + "&selectedDate=" + selectedDate;
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
}
