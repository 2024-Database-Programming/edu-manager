package controller.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Controller;
import model.domain.member.Member;
import model.service.member.ExistingMemberException;
import model.service.member.MemberManager;

public class RegisterMemberController implements Controller {
	private static final Logger log = LoggerFactory.getLogger(RegisterMemberController.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (request.getMethod().equals("GET")) {
			// GET request: 역할 선택 form 요청
			log.debug("RegisterForm Request");
			return "/member/onboardingRole.jsp";
		}

		// POST request: 역할에 따라 회원가입 폼으로 이동
		String role = request.getParameter("role");
		log.debug("Selected Role: {}", role);

		if ("TEACHER".equalsIgnoreCase(role)) {
			return "/member/teacherRegisterForm.jsp"; // 강사 회원가입 페이지로 이동
		} else if ("STUDENT".equalsIgnoreCase(role)) {
			return "/member/studentRegisterForm.jsp"; // 학생 회원가입 페이지로 이동
		}

		// 기본 처리: 역할 선택 페이지로 리다이렉트
		return "/member/onboardingRole.jsp";
	}
}
