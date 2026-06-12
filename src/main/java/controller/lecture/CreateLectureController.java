package controller.lecture;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Controller;
import controller.member.MemberSessionUtils;
import model.dao.member.InterestCategoryDAO;
import model.domain.Schedule;
import model.domain.lecture.Lecture;
import model.service.LectureManager;
import model.service.member.MemberManager;

public class CreateLectureController implements Controller {
	private static final Logger log = LoggerFactory.getLogger(CreateLectureController.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!MemberSessionUtils.hasLogined(request.getSession())) {
			return "redirect:/member/login/form"; // login form 요청으로 redirect
		}

		String teacherId = MemberSessionUtils.getLoginMemberId(request.getSession());
		System.out.print("내 아이디 : 선생일 때:" + teacherId);

		// GET요청
		if (request.getMethod().equals("GET")) {
			MemberManager memberManager = MemberManager.getInstance();
			String teacherName = memberManager.findName(teacherId);

			
			InterestCategoryDAO interestCategoryDAO = new InterestCategoryDAO();

			// DB에서 관심 분야 목록을 가져옴
			List<Map<String, Object>> categories = interestCategoryDAO.getCategories();
			
			request.setAttribute("categories", categories);
			request.setAttribute("teacherName", teacherName);
			return "/lecture/creationForm.jsp";
		}

		// POST요청
		Lecture lecture = new Lecture(0L, request.getParameter("name"), request.getParameter("img"),
				request.getParameter("category"), Long.parseLong(request.getParameter("capacity")), // String -> long 변환
				Integer.parseInt(request.getParameter("level")), request.getParameter("description"), teacherId, // String
				Integer.parseInt(request.getParameter("lectureRoom"))// String -> Integer 변환
		);
		
		try {
			LectureManager manager = LectureManager.getInstance();

			int scheduleCount = Integer.parseInt(request.getParameter("scheduleCount"));//처음에 value를 안정해줌. jsp에서 오류계속 났었음.
			for (int i = 0; i < scheduleCount; i++) { // 각 일정 항목의 값들을 받아오기 String

				LocalTime startTime = LocalTime.parse(request.getParameter("schedule[" + i + "][startTime]"));
				LocalTime endTime = LocalTime.parse(request.getParameter("schedule[" + i + "][endTime]"));
				
				String dayOfWeek = request.getParameter("schedule[" + i + "][day]");
				Boolean isLectureConflict = manager.isLectureConflict(teacherId, dayOfWeek, startTime, endTime);
						
				if (isLectureConflict) {
				    throw new Exception("The lecture schedule conflicts with an existing lecture schedule.");
				}	
			}
			lecture = manager.createLecture(lecture);
			log.debug("Create Lecture : {}", lecture.getLectureId());
			for (int i = 0; i < scheduleCount; i++) { // 각 일정 항목의 값들을 받아오기 String
				
//				log.debug("dayOfWeek : {}", dayOfWeek);

				LocalTime startTime = LocalTime.parse(request.getParameter("schedule[" + i + "][startTime]"));
				LocalTime endTime = LocalTime.parse(request.getParameter("schedule[" + i + "][endTime]"));
//				log.debug("startTime : {} endTime:{}", startTime, endTime);


//				Schedule schedule = new Schedule(dayOfWeek, startTime, endTime, null, 19L, "regular",
//						null);
//				log.debug("Schedule{} : {}", i, schedule);
				
				String dayOfWeek = request.getParameter("schedule[" + i + "][day]");
				
				
				Schedule schedule = new Schedule(dayOfWeek, startTime, endTime, null, lecture.getLectureId(), "regular",
						null);
				schedule.setStartDate(LocalDate.now());

				log.debug("Schedule{} : {}", i, schedule);

				int scheduleId = manager.createSchedule(schedule);
				log.debug("Create Schedule : {}", scheduleId);
			}

		 return "redirect:/lecture/list";
		} catch (Exception e) { // 예외 발생 시 입력 form으로 forwarding
			
			request.getSession().setAttribute("creationFailed", true);
			request.setAttribute("exception", e);
			System.out.print(e);
			/*
			 * MemberManager memberManager = MemberManager.getInstance(); String teacherName
			 * = memberManager.findName(teacherId);
			 * 
			 * request.setAttribute("teacherName", teacherName);
			 * 
			 * request.setAttribute("teacherId", teacherId);
			 */
			return "redirect:/lecture/create";
		}
	}
}
