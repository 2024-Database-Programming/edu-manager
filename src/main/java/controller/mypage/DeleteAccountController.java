package controller.mypage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.Controller;
import model.dao.member.MemberDAO;
import model.dao.member.StudentDAO;
import model.dao.member.TeacherDAO;


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

            // 소프트 삭제(탈퇴): 행을 지우지 않고 status='WITHDRAWN' + 개인정보 익명화.
            // 역할 행(STUDENT/TEACHER)도 함께 익명화한 뒤 MEMBER를 처리한다.
            // (수강/제출/후기/스터디 등 자식 데이터와 FK 무결성은 보존된다.)
            if (studentDAO.existingStudent(memberId)) {
                studentDAO.softDelete(memberId);
            } else if (teacherDAO.existingTeacher(memberId)) {
                teacherDAO.softDelete(memberId);
            }
            memberDAO.softDelete(memberId);

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
