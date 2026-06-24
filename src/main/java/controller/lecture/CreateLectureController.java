package controller.lecture;

import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Controller;
import controller.member.MemberSessionUtils;
import model.dao.ImageDAO;
import model.dao.member.InterestCategoryDAO;
import model.domain.Schedule;
import model.domain.lecture.Lecture;
import model.service.lecture.LectureManager;
import model.service.member.MemberManager;

public class CreateLectureController implements Controller {
	private static final Logger log = LoggerFactory.getLogger(CreateLectureController.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!MemberSessionUtils.hasLogined(request.getSession())) {
			return "redirect:/member/login/form"; // login form 요청으로 redirect
		}

		String teacherId = MemberSessionUtils.getLoginMemberId(request.getSession());

		// 강사만 강의를 만들 수 있음(권한 체크) — 학생/일반 회원은 차단
		if (!new model.dao.member.TeacherDAO().existingTeacher(teacherId)) {
			request.getSession().setAttribute("flashError", "강사만 강의를 만들 수 있습니다.");
			return "redirect:/registration";
		}

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

		try {
			LectureManager manager = LectureManager.getInstance();

			String name = requiredParam(request, "name", "강의명을 입력해 주세요.");
			checkMaxLength(name, 100, "강의명");
			String category = requiredParam(request, "category", "카테고리를 선택해 주세요.");
			String description = optionalParam(request, "description");
			checkMaxLength(description, 1000, "강의 소개");
			long capacity = parseLongInRange(request, "capacity", "모집인원", 1, 99);
			int level = parseIntInRange(request, "level", "난이도", 1, 3);
			int lectureRoom = parseIntInRange(request, "lectureRoom", "강의실", 1, 99999);

			List<Schedule> schedules = parseSchedules(request);
			for (Schedule schedule : schedules) {
				Boolean isLectureConflict = manager.isLectureConflict(teacherId, schedule.getDayOfWeek(),
						schedule.getStartTime(), schedule.getEndTime());
				if (isLectureConflict) {
					throw new IllegalArgumentException(formatSchedule(schedule) + " 일정이 기존 강의와 겹칩니다.");
				}
			}

			Lecture lecture = new Lecture(0L, name, request.getParameter("img"), category, capacity, level, description,
					teacherId, lectureRoom);
			lecture = manager.createLecture(lecture);
			if (lecture == null || lecture.getLectureId() <= 0) {
				throw new SQLException("강의 저장 후 생성된 ID를 받지 못했습니다.");
			}
			log.debug("Create Lecture : {}", lecture.getLectureId());

			// 업로드된 강의 사진을 DB(BLOB)에 저장하고 img 경로를 서빙 URL로 갱신
			Part imgPart = request.getPart("img");
			if (imgPart != null && imgPart.getSize() > 0) {
				byte[] imgData;
				try (InputStream in = imgPart.getInputStream()) {
					imgData = in.readAllBytes();
				}
				new ImageDAO().save("lecture", String.valueOf(lecture.getLectureId()), imgData, imgPart.getContentType());
				lecture.setImg("/image?type=lecture&id=" + lecture.getLectureId());
				manager.updateLecture(lecture);
			}

			for (Schedule schedule : schedules) {
				schedule.setLectureId(lecture.getLectureId());
				schedule.setStartDate(LocalDate.now());

				log.debug("Schedule : {}", schedule);

				int scheduleId = manager.createSchedule(schedule);
				log.debug("Create Schedule : {}", scheduleId);
			}

		 return "redirect:/lecture/list";
		} catch (Exception e) { // 예외 발생 시 입력 form으로 forwarding
			
			log.warn("Failed to create lecture for teacherId={}", teacherId, e);
			request.getSession().setAttribute("flashError", buildCreateErrorMessage(e));
			return "redirect:/lecture/create";
		}
	}

	private String requiredParam(HttpServletRequest request, String name, String message) {
		String value = request.getParameter(name);
		if (value == null || value.trim().isEmpty()) {
			throw new IllegalArgumentException(message);
		}
		return value.trim();
	}

	private String optionalParam(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		return value == null ? "" : value.trim();
	}

	private void checkMaxLength(String value, int maxLength, String label) {
		if (value != null && value.length() > maxLength) {
			throw new IllegalArgumentException(label + "은(는) " + maxLength + "자 이하로 입력해 주세요.");
		}
	}

	private int parseIntInRange(HttpServletRequest request, String name, String label, int min, int max) {
		try {
			int value = Integer.parseInt(requiredParam(request, name, label + "을(를) 입력해 주세요."));
			if (value < min || value > max) {
				throw new IllegalArgumentException(label + "은(는) " + min + "부터 " + max + " 사이로 입력해 주세요.");
			}
			return value;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(label + "은(는) 숫자로 입력해 주세요.");
		}
	}

	private long parseLongInRange(HttpServletRequest request, String name, String label, long min, long max) {
		try {
			long value = Long.parseLong(requiredParam(request, name, label + "을(를) 입력해 주세요."));
			if (value < min || value > max) {
				throw new IllegalArgumentException(label + "은(는) " + min + "부터 " + max + " 사이로 입력해 주세요.");
			}
			return value;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(label + "은(는) 숫자로 입력해 주세요.");
		}
	}

	private List<Schedule> parseSchedules(HttpServletRequest request) {
		int scheduleCount = parseIntInRange(request, "scheduleCount", "정기 수업 일정 개수", 1, 20);
		List<Schedule> schedules = new ArrayList<>();

		for (int i = 0; i < scheduleCount; i++) {
			String prefix = "schedule[" + i + "]";
			String dayOfWeek = requiredParam(request, prefix + "[day]", (i + 1) + "번째 일정의 요일을 선택해 주세요.");
			LocalTime startTime = parseTime(requiredParam(request, prefix + "[startTime]",
					(i + 1) + "번째 일정의 시작 시간을 입력해 주세요."), (i + 1) + "번째 일정 시작 시간");
			LocalTime endTime = parseTime(requiredParam(request, prefix + "[endTime]",
					(i + 1) + "번째 일정의 종료 시간을 입력해 주세요."), (i + 1) + "번째 일정 종료 시간");

			if (!startTime.isBefore(endTime)) {
				throw new IllegalArgumentException((i + 1) + "번째 일정은 시작 시간이 종료 시간보다 빨라야 합니다.");
			}

			for (Schedule existing : schedules) {
				if (existing.getDayOfWeek().equals(dayOfWeek)
						&& isTimeOverlapped(existing.getStartTime(), existing.getEndTime(), startTime, endTime)) {
					throw new IllegalArgumentException((i + 1) + "번째 일정이 앞에서 입력한 "
							+ formatSchedule(existing) + " 일정과 겹칩니다.");
				}
			}

			schedules.add(new Schedule(dayOfWeek, startTime, endTime, null, 0L, "regular", null));
		}

		return schedules;
	}

	private LocalTime parseTime(String value, String label) {
		try {
			return LocalTime.parse(value);
		} catch (Exception e) {
			throw new IllegalArgumentException(label + " 형식이 올바르지 않습니다.");
		}
	}

	private boolean isTimeOverlapped(LocalTime startA, LocalTime endA, LocalTime startB, LocalTime endB) {
		return startA.isBefore(endB) && startB.isBefore(endA);
	}

	private String buildCreateErrorMessage(Exception e) {
		if (e instanceof IllegalArgumentException) {
			return e.getMessage();
		}

		SQLException sqlException = findSqlException(e);
		if (sqlException != null) {
			int errorCode = sqlException.getErrorCode();
			if (errorCode == 12899) {
				return "입력한 값이 저장 가능한 길이를 초과했습니다. 강의명 또는 강의 소개를 조금 줄여 주세요.";
			}
			if (errorCode == 1400) {
				return "필수 입력값이 비어 있습니다. 강의명, 모집인원, 강의실, 카테고리, 일정을 확인해 주세요.";
			}
			if (errorCode == 2291) {
				return "선택한 카테고리 또는 강사 정보가 유효하지 않습니다. 다시 로그인하거나 카테고리를 다시 선택해 주세요.";
			}
			if (errorCode == 1) {
				return "이미 등록된 강의 정보와 중복됩니다.";
			}
			return "DB 저장 중 오류가 발생했습니다. 오류 코드 ORA-" + errorCode + "를 확인해 주세요.";
		}

		return "강의 생성 중 알 수 없는 오류가 발생했습니다. 입력값을 다시 확인해 주세요.";
	}

	private SQLException findSqlException(Throwable throwable) {
		Throwable current = throwable;
		while (current != null) {
			if (current instanceof SQLException) {
				return (SQLException) current;
			}
			current = current.getCause();
		}
		return null;
	}

	private String formatSchedule(Schedule schedule) {
		return formatDayOfWeek(schedule.getDayOfWeek()) + " " + schedule.getStartTime() + "~" + schedule.getEndTime();
	}

	private String formatDayOfWeek(String dayOfWeek) {
		switch (dayOfWeek) {
		case "MONDAY":
			return "월요일";
		case "TUESDAY":
			return "화요일";
		case "WEDNESDAY":
			return "수요일";
		case "THURSDAY":
			return "목요일";
		case "FRIDAY":
			return "금요일";
		case "SATURDAY":
			return "토요일";
		case "SUNDAY":
			return "일요일";
		default:
			return dayOfWeek;
		}
	}
}
