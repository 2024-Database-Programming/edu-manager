package controller.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Controller;

public class RegisterStudent1Controller implements Controller {
	private static final Logger log = LoggerFactory.getLogger(RegisterMemberController.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();

		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		// 입력받은 데이터 세션에 저장
		session.setAttribute("registerId", id);
		session.setAttribute("registerPwd", pwd);
		session.setAttribute("registerName", name);
		session.setAttribute("registerEmail", email);
		session.setAttribute("registerPhone", phone);

		javax.servlet.http.Part profilePart = request.getPart("profileImg");
		if (profilePart != null && profilePart.getSize() > 0) {
			session.setAttribute("registerImgType", profilePart.getContentType());
			try (java.io.InputStream in = profilePart.getInputStream()) {
				session.setAttribute("registerImg", in.readAllBytes());
			}
		}

		log.debug("RegisterStudent1Controller - User Input: id={}, pwd={}, name={}, email={}, phone={}", id, name,
				email, phone);

		return "/member/onboardingAge.jsp";
	}
}
