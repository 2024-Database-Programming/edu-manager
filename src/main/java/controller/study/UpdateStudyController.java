package controller.study;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import controller.Controller;
import controller.member.MemberSessionUtils;
import model.dao.member.InterestCategoryDAO;
import model.domain.Schedule;
import model.domain.lecture.Lecture;
import model.domain.studyGroup.StudyGroup;
import model.service.LectureManager;
import model.service.StudyManager;
import model.service.member.MemberManager;

public class UpdateStudyController implements Controller {
	private static final Logger log = LoggerFactory.getLogger(UpdateStudyController.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!MemberSessionUtils.hasLogined(request.getSession())) {
			return "redirect:/member/login/form"; // login form 요청으로 redirect
		}
		StudyManager manager = StudyManager.getInstance();
		HttpSession session = request.getSession();

		String leaderId = MemberSessionUtils.getLoginMemberId(request.getSession());
		System.out.print("내 아이디 : 스터디 리더:" + leaderId);

		Long updateStudyId = Long.parseLong(request.getParameter("studyId"));
		// 임시
//		Long updateStudyId = 10L;

		log.debug("UpdateForm Request : {}", updateStudyId);
		if (request.getMethod().equals("GET")) {

			// GET request: 회원정보 수정 form 요청

			StudyGroup study = manager.findStudyById(updateStudyId);// 수정하려는 강의 정보 검색
			log.debug("UpdateForm Request : {}", study);

//    		if (MemberSessionUtils.isLoginMember(teacherId, session)) { 
			if (study.getLeaderId().equals(leaderId)) {
				// 현재 로그인한 사용자가 해당 강의의 강사인 경우 -> 수정 가능
				// 강의 일정 리스트 검색 
				List<Schedule> scheduleList = manager.findScheduleById(updateStudyId, "regular");
				request.setAttribute("scheduleList", scheduleList);
				request.setAttribute("scheduleCount", scheduleList.size()); // 길이를 추가

				
				InterestCategoryDAO interestCategoryDAO = new InterestCategoryDAO();

				// DB에서 관심 분야 목록을 가져옴
				List<Map<String, Object>> categories = interestCategoryDAO.getCategories();
				
				request.setAttribute("categories", categories);
				
				request.setAttribute("study", study);
				return "/study/updateForm.jsp"; // 검색한 사용자 정보 및 커뮤니티 리스트를 updateForm으로 전송
			}
		}
		
		// POST요청
		StudyGroup updateStudy = new StudyGroup(Long.parseLong(request.getParameter("studyId")),
				request.getParameter("name"), request.getParameter("img"), request.getParameter("description"),
				Long.parseLong(request.getParameter("capacity")), request.getParameter("category"), null, leaderId);

		log.debug("Update Study : {}", updateStudy);
		manager.updateStudy(updateStudy);

		// 강의 일정 리스트도 updateLecutreId이용해서 update
		try {
			List<Schedule> scheduleList = manager.findScheduleById(updateStudyId, "regular");
			String[] dayOfWeek = request.getParameterValues("dayOfWeek");

			// 3. 현재 데이터베이스에 있는 스케줄의 요일을 비교
			for (Schedule schedule : scheduleList) {
			    String scheduleDay = schedule.getDayOfWeek();  // 기존 스케줄의 요일

			    // 4. dayOfWeek 배열에 해당 요일이 포함되어 있지 않으면 DB에서 삭제
			    if (!Arrays.asList(dayOfWeek).contains(scheduleDay)) {
			        // scheduleId를 통해 해당 스케줄 삭제
			        manager.deleteScheduleById(schedule.getScheduleId());
			    }
			}
			// 5. dayOfWeek 배열에 포함된 요일 중, 기존 스케줄에 없는 요일은 새로 추가
			for (String day : dayOfWeek) {
			    boolean isExisting = false;

			    // 기존 스케줄 목록을 순회하여 해당 요일이 있는지 확인
			    for (Schedule schedule : scheduleList) {
			        if (schedule.getDayOfWeek().equals(day)) {
			            isExisting = true;
			            break;
			        }
			    }

			    // 해당 요일이 기존 스케줄 목록에 없으면 새 스케줄을 추가
			    if (!isExisting) {			        
			        Schedule newSchedule = new Schedule(day, null, null, null, 0L, "regular","정기모임");
			        
			        newSchedule.setStudyGroupId(Long.parseLong(request.getParameter("studyId")));
			        newSchedule.setStartDate(LocalDate.now());
			       
			        // 새 스케줄을 DB에 저장
					int scheduleId = manager.createSchedule(newSchedule);
			    }
			}
	
			return "redirect:/mystudy/view?groupId=" + Long.parseLong(request.getParameter("studyId"));
		} catch (Exception e) { // 예외 발생 시 입력 form으로 forwarding
			request.setAttribute("creationFailed", true);
			request.setAttribute("exception", e);
			System.out.print(e);

			request.setAttribute("updateStudy", updateStudy);
			return "redirect:/member/login/form";
		}
	}
}
