package controller.lecture;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.Controller;
import controller.member.MemberSessionUtils;
import model.domain.lecture.Lecture;
import model.service.LectureManager;
import model.service.member.StudentManager;

public class CreateLectureEnrollmentController implements Controller {

    private final LectureManager lectureManager;
    
    public CreateLectureEnrollmentController() {
        this.lectureManager = LectureManager.getInstance();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!MemberSessionUtils.hasLogined(request.getSession())) {
            return "redirect:/member/login/form";
        }

        String memberId = MemberSessionUtils.getLoginMemberId(request.getSession());
        Long lectureId = Long.parseLong(request.getParameter("lectureId"));
        Lecture lecture = lectureManager.findLectureById(lectureId);

        if (lecture == null || !StudentManager.getInstance().existingStudent(memberId)) {
            return "redirect:/lecture/over-view?lectureId=" + lectureId;
        }

        if (lectureManager.isEnrollmentExists(memberId, lectureId)
                || lectureManager.isLectureConflict(memberId, lectureId)
                || lectureManager.findLectureMembers(lectureId.intValue()).size() >= lecture.getCapacity()) {
            return "redirect:/lecture/over-view?lectureId=" + lectureId;
        }

        //가입 요청 생성
        lectureManager.createLectureEnrollment(memberId, lectureId);

        // 상태 변경 후 해당 페이지로 리다이렉트
        return "redirect:/lecture/over-view?lectureId=" + lectureId; // 페이지 새로고침
    }
}
