package controller.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Controller;
import model.dao.member.StudentInterestCategoryDAO;
import model.domain.member.Member;
import model.domain.member.Student;
import model.service.member.ExistingMemberException;
import model.service.member.MemberManager;
import model.service.member.StudentManager;

public class RegisterStudentController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(RegisterStudentController.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 객체 초기화
        StudentManager smanager = StudentManager.getInstance();
        MemberManager manager = MemberManager.getInstance();
        StudentInterestCategoryDAO dao = new StudentInterestCategoryDAO();

        // 세션에서 데이터 가져오기
        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("registerId");
        String pwd = (String) session.getAttribute("registerPwd");
        String name = (String) session.getAttribute("registerName");
        String email = (String) session.getAttribute("registerEmail");
        String phone = (String) session.getAttribute("registerPhone");
        String ageRange = (String) session.getAttribute("age");
        
        int[] interestCategoryIds = (int[]) session.getAttribute("interest");

        if (id == null || pwd == null || name == null || email == null || phone == null || ageRange == null) {
            return "redirect:/member/register/form";
        }

        // 학생 객체 생성
        Student student = new Student(id, pwd, name, email, phone, ageRange);
        Member member = new Member(id, pwd, name, email, phone);
        boolean memberCreated = false;
        boolean studentCreated = false;

        try {
            // 데이터베이스 등록
            log.debug("Registering member: {}", member);
            manager.create(member);
            memberCreated = true;

            log.debug("Registering student: {}", student);
            smanager.create(student);
            studentCreated = true;
            
            //관심분야등록
            if (interestCategoryIds != null && interestCategoryIds.length != 0) {
            	for (int interestId : interestCategoryIds) {
                    log.debug("Registering interest category: {}", interestId);
                    int result = dao.create(id, interestId);
                    log.debug("Interest category registration result: {}", result);
                }
            }
            
            // 세션 무효화
            session.invalidate();
            log.debug("Session invalidated.");

            return "redirect:/member/login/form"; // 성공 시 로그인 페이지로 리다이렉트

        } catch (ExistingMemberException e) {
            cleanupPartialRegistration(id, manager, smanager, memberCreated, studentCreated);
            log.error("ExistingMemberException occurred: ", e);
            request.setAttribute("registerFailed", true);
            request.setAttribute("exception", e);
            request.setAttribute("student", student);
            return "/member/studentRegisterForm.jsp";
        } catch (Exception ex) {
            cleanupPartialRegistration(id, manager, smanager, memberCreated, studentCreated);
            log.error("Unexpected error during registration: ", ex);
            request.setAttribute("registerFailed", true);
            request.setAttribute("exception", ex);
            request.setAttribute("student", student);
            return "/member/studentRegisterForm.jsp";
        }
    }

    private void cleanupPartialRegistration(String id, MemberManager manager, StudentManager smanager,
            boolean memberCreated, boolean studentCreated) {
        try {
            if (studentCreated) {
                new StudentInterestCategoryDAO().removeByStudentId(id);
                smanager.remove(id);
            }
            if (memberCreated) {
                manager.remove(id);
            }
        } catch (Exception cleanupException) {
            log.error("Failed to clean up partial student registration for id={}", id, cleanupException);
        }
    }
}
