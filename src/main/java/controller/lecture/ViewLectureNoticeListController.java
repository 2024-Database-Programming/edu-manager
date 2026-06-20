package controller.lecture;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.AuthorizationUtils;
import controller.Controller;
import controller.member.MemberSessionUtils;
import model.domain.Notice;
import model.service.LectureManager;

public class ViewLectureNoticeListController implements Controller {

    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 로그인 여부 확인
        if (!MemberSessionUtils.hasLogined(request.getSession())) {
            return "redirect:/member/login/form"; // login form 요청으로 redirect
        }

        int groupId;
        try {
            groupId = Integer.parseInt(request.getParameter("groupId"));
        } catch (NumberFormatException ex) {
            return "redirect:/registration";
        }
        String memberId = MemberSessionUtils.getLoginMemberId(request.getSession());
        LectureManager lectureManager = LectureManager.getInstance();

        if (!AuthorizationUtils.canViewLecture(lectureManager, memberId, groupId)) {
            return "redirect:/lecture/over-view?lectureId=" + groupId;
        }

        String searchParam = request.getParameter("searchParam");

        if (searchParam != null && !searchParam.trim().isEmpty()) {

            List<Notice> lectureNoticeList = lectureManager.searchNotices(groupId, searchParam);

            request.setAttribute("lectureNoticeList", lectureNoticeList);

            request.setAttribute("groupId", groupId);
            request.setAttribute("searchParam", searchParam);

        } else {

            List<Notice> lectureNoticeList = lectureManager.findNoticesByLectureId(groupId);

            request.setAttribute("lectureNoticeList", lectureNoticeList);
            
            request.setAttribute("groupId", groupId);
            request.setAttribute("searchParam", searchParam);

        }

        // 스터디 목록을 request 객체에 저장

        return "/lecture/listNotice.jsp";

    }
}
