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

		TeacherManager tmanager = TeacherManager.getInstance();
		MemberManager manager = MemberManager.getInstance();

		// POST request (회원정보가 parameter로 전송됨)
		Teacher teacher = new Teacher(request.getParameter("id"), request.getParameter("pwd"),
				request.getParameter("name"), request.getParameter("email"), request.getParameter("phone"));

		Member member = new Member(request.getParameter("id"), request.getParameter("pwd"),
				request.getParameter("name"), request.getParameter("email"), request.getParameter("phone"));

		log.debug("Create User : {}", teacher);

		try {
			tmanager.create(teacher); // teacher DB에 생성
			manager.create(member); // member DB에 생성
			return "redirect:/main/main"; // 성공 시 사용자 리스트 화면으로 redirect

		} catch (ExistingMemberException e) { // 예외 발생 시 회원가입 form으로 forwarding
			request.setAttribute("registerFailed", true);
			request.setAttribute("exception", e);
			request.setAttribute("teacher", teacher);
			return "/member/teacherRegisterForm.jsp";
		}
	}
}
