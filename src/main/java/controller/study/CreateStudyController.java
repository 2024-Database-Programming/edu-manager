package controller.study;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Controller;
import controller.member.MemberSessionUtils;
import model.dao.ImageDAO;
import model.dao.member.InterestCategoryDAO;
import model.dao.studygroup.StudyGroupDao;
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
			if (dayOfWeek == null || dayOfWeek.length == 0) {
				request.getSession().setAttribute("flashError", "정기 모임 요일을 1개 이상 선택해 주세요.");
				return "redirect:/study/create";
			}

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

			// 업로드된 스터디 사진을 DB(BLOB)에 저장하고 img 경로를 서빙 URL로 갱신
			Part imgPart = request.getPart("img");
			if (imgPart != null && imgPart.getSize() > 0) {
				byte[] imgData;
				try (InputStream in = imgPart.getInputStream()) {
					imgData = in.readAllBytes();
				}
				new ImageDAO().save("study", String.valueOf(study.getStudyGroupId()), imgData, imgPart.getContentType());
				study.setImg("/image?type=study&id=" + study.getStudyGroupId());
				new StudyGroupDao().update(study);
			}

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
			e.printStackTrace();
			request.getSession().setAttribute("flashError", "스터디 생성 중 오류가 발생했습니다.");
			return "redirect:/study/create";
		}
	}
}
