package controller.lecture;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.AuthorizationUtils;
import controller.Controller;
import controller.member.MemberSessionUtils;
import model.domain.Assignment;
import model.service.LectureManager;

public class ViewLectureAssignmentsController implements Controller {

    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!MemberSessionUtils.hasLogined(request.getSession())) {
            return "redirect:/member/login/form";
        }

        String lectureIdParam = request.getParameter("groupId");
        if (lectureIdParam == null || lectureIdParam.trim().isEmpty()) {
            lectureIdParam = request.getParameter("lectureId");
        }
        if (lectureIdParam == null || lectureIdParam.trim().isEmpty()) {
            return "redirect:/lecture/list";
        }

        int lectureId = Integer.parseInt(lectureIdParam);
        String memberId = MemberSessionUtils.getLoginMemberId(request.getSession());
        LectureManager lectureManager = LectureManager.getInstance();

        if (!AuthorizationUtils.canViewLecture(lectureManager, memberId, lectureId)) {
            return "redirect:/lecture/over-view?lectureId=" + lectureId;
        }

        List<Assignment> lectureAssignmentList = lectureManager.findAssignmentsByLectureId(lectureId);

        request.setAttribute("lectureAssignmentList", lectureAssignmentList);
        request.setAttribute("groupId", lectureId);

        return "/lecture/listAssignment.jsp";
    }
}
