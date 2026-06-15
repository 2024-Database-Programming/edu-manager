package controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.member.MemberSessionUtils;
import model.dao.AssignmentSubmissionDao;
import model.domain.AssignmentSubmission;

public class ViewSubmissionFileController implements Controller {
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!MemberSessionUtils.hasLogined(request.getSession())) {
			return "redirect:/member/login/form";
		}

		int id = Integer.parseInt(request.getParameter("id"));
		String memberId = MemberSessionUtils.getLoginMemberId(request.getSession());
		AssignmentSubmission submission = new AssignmentSubmissionDao().findFileById(id);
		if (submission == null || submission.getData() == null || submission.getData().length == 0) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		if (!ItemAccessUtils.canAccessSubmission(submission, memberId)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}

		String contentType = submission.getContentType() == null || submission.getContentType().isBlank()
				? "application/octet-stream"
				: submission.getContentType();
		String encodedFileName = URLEncoder.encode(submission.getFileName(), StandardCharsets.UTF_8).replace("+", "%20");

		response.setContentType(contentType);
		response.setContentLength(submission.getData().length);
		response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
		ServletOutputStream out = response.getOutputStream();
		out.write(submission.getData());
		out.flush();
		return null;
	}
}
