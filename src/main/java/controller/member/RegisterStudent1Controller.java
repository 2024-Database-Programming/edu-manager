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
		session.setAttribute("id", id);
		session.setAttribute("pwd", pwd);
		session.setAttribute("name", name);
		session.setAttribute("email", email);
		session.setAttribute("phone", phone);

		log.debug("RegisterStudent1Controller - User Input: id={}, pwd={}, name={}, email={}, phone={}", id, name,
				email, phone);

		return "/member/onboardingAge.jsp";
	}
}
