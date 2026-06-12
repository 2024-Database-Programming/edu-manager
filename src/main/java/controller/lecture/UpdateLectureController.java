package controller.lecture;

import java.time.LocalTime;
import java.util.ArrayList;
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
import model.service.LectureManager;
import model.service.member.MemberManager;

public class UpdateLectureController implements Controller {
	private static final Logger log = LoggerFactory.getLogger(UpdateLectureController.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!MemberSessionUtils.hasLogined(request.getSession())) {
			return "redirect:/member/login/form"; // login form 요청으로 redirect
		}
		LectureManager manager = LectureManager.getInstance();
		HttpSession session = request.getSession();

		String teacherId = MemberSessionUtils.getLoginMemberId(request.getSession());
		System.out.print("내 아이디 : 선생:" + teacherId);

		Long updateLectureId = Long.parseLong(request.getParameter("lectureId"));
		// 임시
//		Long updateLectureId = 19L;

		log.debug("UpdateForm Request : {}", updateLectureId);
		if (request.getMethod().equals("GET")) {

			// GET request: 회원정보 수정 form 요청

			Lecture lecture = manager.findLectureById(updateLectureId);// 수정하려는 강의 정보 검색
			log.debug("UpdateForm Request : {}", lecture);

//    		if (MemberSessionUtils.isLoginMember(teacherId, session)) { 
			if (MemberSessionUtils.isLoginMember(lecture.getTeacherId(), session)
					&& lecture.getTeacherId().equals(teacherId)) {
				// 현재 로그인한 사용자가 해당 강의의 강사인 경우 -> 수정 가능
				// 강의 일정 리스트 검색 
				List<Schedule> scheduleList = manager.findScheduleById(updateLectureId);
				request.setAttribute("scheduleList", scheduleList);
				request.setAttribute("scheduleCount", scheduleList.size()); // 길이를 추가

				// teacher name을 넘겨줘야함.

				MemberManager memberManager = MemberManager.getInstance();
				String teacherName = memberManager.findName(teacherId);
				lecture.setTeacherName(teacherName);

				request.setAttribute("lecture", lecture);
				
				InterestCategoryDAO interestCategoryDAO = new InterestCategoryDAO();

				// DB에서 관심 분야 목록을 가져옴
				List<Map<String, Object>> categories = interestCategoryDAO.getCategories();
				
				request.setAttribute("categories", categories);
				
				return "/lecture/updateForm.jsp"; // 검색한 사용자 정보 및 커뮤니티 리스트를 updateForm으로 전송
			}
		}

		// POST request (회원정보가 parameter로 전송됨)
		Lecture updateLecture = new Lecture(Long.parseLong(request.getParameter("lectureId")),
				request.getParameter("name"), request.getParameter("img"), request.getParameter("category"),
				Long.parseLong(request.getParameter("capacity")), // String -> long 변환
				Integer.parseInt(request.getParameter("level")), request.getParameter("description"), teacherId, // String
				Integer.parseInt(request.getParameter("lectureRoom"))// String -> Integer 변환
		);

		log.debug("Update Lecture : {}", updateLecture);
		manager.updateLecture(updateLecture);

		// 강의 일정 리스트도 updateLecutreId이용해서 update
		try {
			 List<Integer> originalIds = manager.findScheduleIdsBylectureId(updateLecture.getLectureId()); 
				log.debug("originalIds : {}", originalIds);

			int scheduleCount = Integer.parseInt(request.getParameter("scheduleCount"));// 처음에 value를 안정해줌. jsp에서 오류계속																						// 났었음.
//			// 일정의 개수
			log.debug("scheduleCount : {}", scheduleCount);
			
			   // 이번 요청에서 유지된 일정 ID를 저장할 리스트
		    List<Integer> updatedIds = new ArrayList<>();
	

			for (int i = 0; i < scheduleCount; i++) { // 각 일정 항목의 값들을 받아오기 String
				String dayOfWeek = request.getParameter("schedule[" + i + "][day]");
				log.debug("dayOfWeek : {}", dayOfWeek);

				LocalTime startTime = LocalTime.parse(request.getParameter("schedule[" + i + "][startTime]"));
				LocalTime endTime = LocalTime.parse(request.getParameter("schedule[" + i + "][endTime]"));
				log.debug("startTime : {} endTime:{}", startTime, endTime);

				Schedule schedule = new Schedule(dayOfWeek, startTime, endTime, null, updateLecture.getLectureId(),
						"regular", "정기수업");
				
				log.debug("Schedule{} : {}", i, schedule);

			    String scheduleIdStr = request.getParameter("schedule[" + i + "][scheduleId]");
			    int scheduleId = scheduleIdStr.equals("") ? 0 : Integer.parseInt(scheduleIdStr);
			    log.debug("scheduleId : {}", scheduleId);
			    
				if(scheduleId > 0) {//있던 일정이면 추가
					scheduleId = Integer.parseInt(request.getParameter("schedule[" + i + "][scheduleId]"));
					log.debug("scheduleId : {}", scheduleId);
					schedule.setScheduleId(scheduleId);
					manager.updateSchedule(schedule);
				}else {//없던 일정이면 생성
					scheduleId = manager.createSchedule(schedule);
					log.debug("Create Schedule : {}", scheduleId);
				}

		        // 처리된 일정 ID를 업데이트된 목록에 추가
		        updatedIds.add(scheduleId);

			}
			
			//있었는데 없어진 일정이면 삭제
		    // originalIds에서 updatedIds에 포함되지 않은 ID의 스케줄 삭제
		    for (int originalId : originalIds) {
		        if (!updatedIds.contains(originalId)) {
		            manager.deleteScheduleById(originalId);
		            log.debug("Deleted Schedule ID : {}", originalId);
		        }
		    }
			log.debug("updatedIds : {}", updatedIds);

			return "redirect:/mylecture/view?lectureId=" + Long.parseLong(request.getParameter("lectureId"));
		} catch (Exception e) { // 예외 발생 시 입력 form으로 forwarding
			request.setAttribute("creationFailed", true);
			request.setAttribute("exception", e);
			System.out.print(e);

			request.setAttribute("updateLecture", updateLecture);
			return "redirect:/member/login/form";
		}
	}
}
