package controller.study;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Controller;
import controller.member.MemberSessionUtils;
import model.dao.member.InterestCategoryDAO;
import model.domain.Schedule;
import model.domain.studyGroup.StudyGroup;
import model.service.StudyManager;

public class CreateStudyController implements Controller {
	private static final Logger log = LoggerFactory.getLogger(CreateStudyController.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!MemberSessionUtils.hasLogined(request.getSession())) {
			return "redirect:/member/login/form"; // login form 요청으로 redirect
		}

		String leaderId = MemberSessionUtils.getLoginMemberId(request.getSession());
		System.out.print("내 아이디 : 스터디 리더:" + leaderId);

		// GET요청
		if (request.getMethod().equals("GET")) {
			InterestCategoryDAO interestCategoryDAO = new InterestCategoryDAO();

			// DB에서 관심 분야 목록을 가져옴
			List<Map<String, Object>> categories = interestCategoryDAO.getCategories();
			
			request.setAttribute("categories", categories);
			return "/study/creationForm.jsp";
		}

		// POST요청
		StudyGroup study = new StudyGroup(0L, request.getParameter("name"), request.getParameter("img"),request.getParameter("description"), Long.parseLong(request.getParameter("capacity")),
				request.getParameter("category"), null, leaderId);
		study.setPlace(request.getParameter("place"));
		
		try {
			StudyManager manager = StudyManager.getInstance();

			String[] dayOfWeek = request.getParameterValues("dayOfWeek");
			log.debug("dayOfWeek{}",dayOfWeek);

			for (int i = 0; i < dayOfWeek.length; i++) { // 각 일정 항목의 값들을 받아오기 String
				Boolean isStudyConflict = manager.isStudyConflict(leaderId,dayOfWeek[i]);
				log.debug("isStudyConflict : {}",isStudyConflict);

				if (isStudyConflict) {
				    throw new Exception("The lecture schedule conflicts with an existing lecture schedule.");
				}	
			}
			
			study = manager.createStudy(study);
			log.debug("Create Lecture : {}", study.getStudyGroupId());
			
			for (int i = 0; i < dayOfWeek.length; i++) { // 각 일정 항목의 값들을 받아오기 String
				Schedule schedule = new Schedule(dayOfWeek[i], null, null, null, 0L, "regular",
						"정기모임");
				schedule.setStudyGroupId(study.getStudyGroupId());
				schedule.setStartDate(LocalDate.now());

				log.debug("Schedule{} : {}", i, schedule);

				int scheduleId = manager.createSchedule(schedule);
				log.debug("Create Schedule : {}", scheduleId);
			}

			return "redirect:/study/list";
		} catch (Exception e) { // 예외 발생 시 입력 form으로 forwarding
			request.setAttribute("creationFailed", true);
			request.setAttribute("exception", e);
			System.out.print(e);

			request.setAttribute("study", study);
			return "redirect:/member/login/form";
		}
	}
}
