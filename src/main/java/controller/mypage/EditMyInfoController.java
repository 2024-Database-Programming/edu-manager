package controller.mypage;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import controller.Controller;
import controller.member.MemberSessionUtils;
import model.dao.ImageDAO;
import model.dao.member.MemberDAO;
import model.dao.member.StudentDAO;
import model.dao.member.TeacherDAO;
import model.domain.member.Member;
import model.domain.member.Student;
import model.domain.member.Teacher;

public class EditMyInfoController implements Controller {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String memberId = MemberSessionUtils.getLoginMemberId(request.getSession());
        if (memberId == null) {
            return "redirect:/member/login/form";
        }

        // 업로드된 프로필 사진(있으면) 바이트로 읽기
        byte[] imgBytes = null;
        String imgType = null;
        Part filePart = request.getPart("profileImg");
        if (filePart != null && filePart.getSize() > 0) {
            imgType = filePart.getContentType();
            try (InputStream in = filePart.getInputStream()) {
                imgBytes = in.readAllBytes();
            }
        }

        try {
            MemberDAO memberDAO = new MemberDAO();
            StudentDAO studentDAO = new StudentDAO();
            TeacherDAO teacherDAO = new TeacherDAO();

            Member member = memberDAO.findMember(memberId);
            if (member == null) {
                request.getSession().setAttribute("flashError", "사용자 정보를 찾을 수 없습니다.");
                return "redirect:/mypage/myInfo";
            }

            // 사진이 업로드된 경우: DB(IMAGE 테이블)에 BLOB로 저장하고 img를 서빙 URL로 설정
            String newImgUrl = null;
            if (imgBytes != null) {
                new ImageDAO().save("member", memberId, imgBytes, imgType);
                newImgUrl = "/image?type=member&id=" + memberId;
            }

            String newPwd = request.getParameter("password");
            String newEmail = request.getParameter("email");
            String newPhone = request.getParameter("phone");

            if (studentDAO.existingStudent(memberId)) {
                Student student = studentDAO.findStudent(memberId);
                if (newPwd != null && !newPwd.isEmpty()) { student.setPwd(newPwd); member.setPwd(newPwd); }
                if (newEmail != null && !newEmail.isEmpty()) { student.setEmail(newEmail); member.setEmail(newEmail); }
                if (newPhone != null && !newPhone.isEmpty()) { student.setPhone(newPhone); member.setPhone(newPhone); }
                if (newImgUrl != null) { member.setImg(newImgUrl); }
                studentDAO.update(student);
                memberDAO.update(member);
            } else if (teacherDAO.existingTeacher(memberId)) {
                Teacher teacher = teacherDAO.findTeacher(memberId);
                if (newPwd != null && !newPwd.isEmpty()) { teacher.setPwd(newPwd); member.setPwd(newPwd); }
                if (newEmail != null && !newEmail.isEmpty()) { teacher.setEmail(newEmail); member.setEmail(newEmail); }
                if (newPhone != null && !newPhone.isEmpty()) { teacher.setPhone(newPhone); member.setPhone(newPhone); }
                if (newImgUrl != null) { member.setImg(newImgUrl); }
                teacherDAO.update(teacher);
                memberDAO.update(member);
            } else {
                // 역할 정보가 없어도 member는 최소한 갱신
                if (newPwd != null && !newPwd.isEmpty()) member.setPwd(newPwd);
                if (newEmail != null && !newEmail.isEmpty()) member.setEmail(newEmail);
                if (newPhone != null && !newPhone.isEmpty()) member.setPhone(newPhone);
                if (newImgUrl != null) member.setImg(newImgUrl);
                memberDAO.update(member);
            }

            return "redirect:/mypage/myInfo";
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("flashError", "내 정보를 수정하는 도중 오류가 발생했습니다.");
            return "redirect:/mypage/myInfo";
        }
    }
}
