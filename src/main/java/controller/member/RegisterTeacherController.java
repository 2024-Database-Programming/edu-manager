package controller.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Controller;
import model.domain.member.Member;
import model.domain.member.Teacher;
import model.service.member.ExistingMemberException;
import model.service.member.MemberManager;
import model.service.member.TeacherManager;

public class RegisterTeacherController implements Controller {
	private static final Logger log = LoggerFactory.getLogger(RegisterTeacherController.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (request.getMethod().equals("GET")) {
			return "/member/teacherRegisterForm.jsp";
		}

		TeacherManager tmanager = TeacherManager.getInstance();
		MemberManager manager = MemberManager.getInstance();

		// POST request (회원정보가 parameter로 전송됨)
		Teacher teacher = new Teacher(request.getParameter("id"), request.getParameter("pwd"),
				request.getParameter("name"), request.getParameter("email"), request.getParameter("phone"));

		Member member = new Member(request.getParameter("id"), request.getParameter("pwd"),
				request.getParameter("name"), request.getParameter("email"), request.getParameter("phone"));

		byte[] imgBytes = null;
		String imgType = null;
		javax.servlet.http.Part profilePart = request.getPart("profileImg");
		if (profilePart != null && profilePart.getSize() > 0) {
			imgType = profilePart.getContentType();
			try (java.io.InputStream in = profilePart.getInputStream()) { imgBytes = in.readAllBytes(); }
		}

		log.debug("Create User : {}", teacher);
		boolean memberCreated = false;
		boolean teacherCreated = false;

		try {
			manager.create(member); // member DB에 생성
			memberCreated = true;

			tmanager.create(teacher); // teacher DB에 생성
			teacherCreated = true;
			if (imgBytes != null) {
				new model.dao.ImageDAO().save("member", member.getId(), imgBytes, imgType);
				member.setImg("/image?type=member&id=" + member.getId());
				new model.dao.member.MemberDAO().update(member);
			}
			return "redirect:/member/login/form"; // 성공 시 로그인 페이지로 redirect

		} catch (ExistingMemberException e) { // 예외 발생 시 회원가입 form으로 forwarding
			cleanupPartialRegistration(member.getId(), manager, tmanager, memberCreated, teacherCreated);
			request.setAttribute("registerFailed", true);
			request.setAttribute("exception", e);
			request.setAttribute("teacher", teacher);
			return "/member/teacherRegisterForm.jsp";
		} catch (Exception ex) {
			cleanupPartialRegistration(member.getId(), manager, tmanager, memberCreated, teacherCreated);
			request.setAttribute("registerFailed", true);
			request.setAttribute("exception", ex);
			request.setAttribute("teacher", teacher);
			return "/member/teacherRegisterForm.jsp";
		}
	}

	private void cleanupPartialRegistration(String id, MemberManager manager, TeacherManager tmanager,
			boolean memberCreated, boolean teacherCreated) {
		try {
			if (teacherCreated) {
				tmanager.remove(id);
			}
			if (memberCreated) {
				manager.remove(id);
			}
		} catch (Exception cleanupException) {
			log.error("Failed to clean up partial teacher registration for id={}", id, cleanupException);
		}
	}
}
