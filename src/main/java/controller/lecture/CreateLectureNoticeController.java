package controller.lecture;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.AuthorizationUtils;
import controller.Controller;
import controller.MultipartUploadUtils;
import controller.member.MemberSessionUtils;
import model.domain.Notice;
import model.domain.Schedule;
import model.domain.study.StudyGroup;
import model.service.lecture.LectureManager;
import model.service.study.StudyManager;

public class CreateLectureNoticeController implements Controller {
	private static final Logger log = LoggerFactory.getLogger(CreateLectureNoticeController.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!MemberSessionUtils.hasLogined(request.getSession())) {
			return "redirect:/member/login/form"; // login form 요청으로 redirect
			}

			String memberId = MemberSessionUtils.getLoginMemberId(request.getSession());
			long lectureId = Long.parseLong(request.getParameter("lectureId"));
			LectureManager manager = LectureManager.getInstance();
			if (!AuthorizationUtils.canManageLecture(manager, memberId, lectureId)) {
				return "redirect:/lecture/over-view?lectureId=" + lectureId;
			}

		// GET요청
		if (request.getMethod().equals("GET")) {
			request.setAttribute("lectureId", request.getParameter("lectureId"));
			request.setAttribute("startDate", LocalDate.parse(request.getParameter("selectedDate")));
			return "/lecture/addNotice.jsp";
		};

		// POST요청
		try {
			Notice notice = new Notice();
			notice.setTitle(request.getParameter("title"));
			notice.setDescription(request.getParameter("description"));
			notice.setCreateat(LocalDate.parse(request.getParameter("startDate")));
			notice.setLectureId(Integer.parseInt(request.getParameter("lectureId")));
			
			int noticeId = manager.createNotice(notice);
			MultipartUploadUtils.saveAttachments(request, "lecture", "notice", noticeId);

			return "redirect:/mylecture/view?lectureId=" + Long.parseLong(request.getParameter("lectureId")) 
		       + "&selectedDate=" + LocalDate.parse(request.getParameter("startDate"));
			
		} catch (Exception e) { // 예외 발생 시 입력 form으로 forwarding
			e.printStackTrace();
			request.getSession().setAttribute("flashError", "공지 등록 중 오류가 발생했습니다.");
			return "redirect:/lecture/over-view?lectureId=" + lectureId;
		}
	}
}
