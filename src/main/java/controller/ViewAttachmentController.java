package controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.member.MemberSessionUtils;
import model.dao.AttachmentDao;
import model.domain.Attachment;

public class ViewAttachmentController implements Controller {
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!MemberSessionUtils.hasLogined(request.getSession())) {
			return "redirect:/member/login/form";
		}

		int id = Integer.parseInt(request.getParameter("id"));
		Attachment attachment = new AttachmentDao().findById(id);
		if (attachment == null || attachment.getData() == null || attachment.getData().length == 0) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		boolean inline = "true".equals(request.getParameter("preview")) && attachment.isImage();
		String contentType = attachment.getContentType() == null || attachment.getContentType().isBlank()
				? "application/octet-stream"
				: attachment.getContentType();
		String encodedFileName = URLEncoder.encode(attachment.getFileName(), StandardCharsets.UTF_8).replace("+", "%20");

		response.setContentType(contentType);
		response.setContentLength(attachment.getData().length);
		response.setHeader("Content-Disposition",
				(inline ? "inline" : "attachment") + "; filename*=UTF-8''" + encodedFileName);
		response.setHeader("Cache-Control", "max-age=300");
		ServletOutputStream out = response.getOutputStream();
		out.write(attachment.getData());
		out.flush();
		return null;
	}
}
