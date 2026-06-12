package controller.mypage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.Controller;
import model.dao.member.MemberDAO;
import model.dao.member.StudentDAO;
import model.dao.member.TeacherDAO;
import controller.member.MemberSessionUtils;


public class DeleteAccountController implements Controller {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String memberId = (String) session.getAttribute("id");

        if (memberId == null) {
            return "redirect:/member/login/form";
        }

        // 비밀번호 확인 파라미터 추가
        String inputPassword = request.getParameter("pwd");
        if (inputPassword == null || inputPassword.isEmpty()) {
            request.setAttribute("error", "비밀번호를 입력해주세요.");
            return "/mypage/deleteConfirm.jsp"; // 비밀번호 입력 페이지로
        }

        MemberDAO memberDAO = new MemberDAO();
        StudentDAO studentDAO = new StudentDAO();
        TeacherDAO teacherDAO = new TeacherDAO();

        try {
            // 비밀번호 확인
            boolean isValidPassword = memberDAO.verifyPassword(memberId, inputPassword);
            if (!isValidPassword) {
                request.setAttribute("error", "비밀번호가 일치하지 않습니다.");
                return "/mypage/deleteConfirm.jsp";
            }

            // Member 데이터 삭제 처리
            if (memberDAO.remove(memberId) > 0) {
                if (studentDAO.existingStudent(memberId)) {
                    studentDAO.remove(memberId);
                } else if (teacherDAO.existingTeacher(memberId)) {
                    teacherDAO.remove(memberId);
                }
            }

            session.invalidate(); // 세션 무효화
            // 탈퇴 성공 상태를 JSP에 전달
            request.setAttribute("success", true);
            return "/mypage/deleteConfirm.jsp"; // JSP에서 alert 후 리다이렉트 처리
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "계정 삭제 중 오류가 발생했습니다.");
            return "/mypage/deleteConfirm.jsp";
        }
    }
}
