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
import model.domain.Schedule;
import model.domain.study.StudyGroup;
import model.service.lecture.LectureManager;
import model.service.study.StudyManager;

public class CreateLectureScheduleController implements Controller {
	private static final Logger log = LoggerFactory.getLogger(CreateLectureScheduleController.class);

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
			request.setAttribute("category", normalizeScheduleCategory(request.getParameter("category")));
			return "/lecture/addSchedule.jsp";
		};

		// POST요청
		try {
			Schedule schedule = new Schedule();
			schedule.setLectureId(Long.parseLong(request.getParameter("lectureId")));
			schedule.setTitle(request.getParameter("title"));
			schedule.setStartDate(LocalDate.parse(request.getParameter("startDate")));
			schedule.setStartTime(LocalTime.parse(request.getParameter("startTime")));
			schedule.setEndTime(LocalTime.parse(request.getParameter("endTime")));
			schedule.setType(normalizeScheduleCategory(request.getParameter("category")));
			schedule.setDescription(request.getParameter("description"));
			
			int scheduleId = manager.createSchedule(schedule);
			MultipartUploadUtils.saveAttachments(request, "lecture", "schedule", scheduleId);
			log.debug("Create Schedule : {}", scheduleId);

//			request.setAttribute("lectureId", Long.parseLong(request.getParameter("lectureId")));
//			request.setAttribute("selectedDate", LocalDate.parse(request.getParameter("startDate")));

			return "redirect:/mylecture/view?lectureId=" + Long.parseLong(request.getParameter("lectureId")) 
		       + "&selectedDate=" + LocalDate.parse(request.getParameter("startDate"));
			
		} catch (Exception e) { // 예외 발생 시 입력 form으로 forwarding
			e.printStackTrace();
			request.getSession().setAttribute("flashError", "일정 추가 중 오류가 발생했습니다.");
			return "redirect:/lecture/over-view?lectureId=" + lectureId;
		}
	}

	private String normalizeScheduleCategory(String category) {
		if ("class".equals(category) || "exam".equals(category) || "event".equals(category)) {
			return category;
		}
		return "event";
	}
}
