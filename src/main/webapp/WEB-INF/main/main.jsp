<%@page contentType="text/html; charset=utf-8"%>
<%-- <%@page import="java.util.*, model.domain.*" %> --%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="model.domain.Schedule"%>
<%@ page import="model.domain.Notice"%>
<%@ page import="model.domain.Assignment"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%!
private String escapeHtml(String value) {
	if (value == null) {
		return "";
	}
	return value.replace("&", "&amp;")
			.replace("<", "&lt;")
			.replace(">", "&gt;")
			.replace("\"", "&quot;")
			.replace("'", "&#39;");
}

private String formatTimeRange(Schedule schedule) {
	if (schedule.getStartTime() == null && schedule.getEndTime() == null) {
		return "";
	}
	if (schedule.getStartTime() == null) {
		return " (~ " + schedule.getEndTime() + ")";
	}
	if (schedule.getEndTime() == null) {
		return " (" + schedule.getStartTime() + " ~)";
	}
	return " (" + schedule.getStartTime() + " ~ " + schedule.getEndTime() + ")";
}
%>
<%
// 캘린더 가져오기 

// 현재 연도와 월 가져오기
java.util.Calendar calendar = java.util.Calendar.getInstance();
int currentYear = (int) request.getAttribute("year");
int currentMonth = (int) request.getAttribute("month"); // 0부터 시작 (0 = 1월)
int selectedDay = (int) request.getAttribute("selectedDay");

System.out.println("DEBUG: Year = " + currentYear + ", Month = " + currentMonth + ", Selected Day = " + selectedDay);

/* // 이전/다음 달 이동 시 파라미터 처리
String yearParam = request.getParameter("year");
String monthParam = request.getParameter("month");
if (yearParam != null && monthParam != null) {
	currentYear = Integer.parseInt(yearParam);
	currentMonth = Integer.parseInt(monthParam) - 1; // 0부터 시작하는 월로 변환
} */

// 해당 월의 총 일수와 시작 요일 계산
calendar.set(currentYear, currentMonth - 1, 1);
int daysInMonth = calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
int firstDayOfWeek = calendar.get(java.util.Calendar.DAY_OF_WEEK);

java.util.Calendar now = java.util.Calendar.getInstance(); // 현재 날짜 다시 가져오기
int todayYear = now.get(java.util.Calendar.YEAR);
int todayMonth = now.get(java.util.Calendar.MONTH) + 1;
int todayDate = now.get(java.util.Calendar.DATE);

System.out.println("DEBUG: todayYear = " + todayYear + ", todayMonth = " + todayMonth + ", todayDate = " + todayDate);

int day = 1;
boolean started = false;

@SuppressWarnings("unchecked")
List<Schedule> lectureScheduleEntries = (List<Schedule>) request.getAttribute("lectureScheduleEntries");
@SuppressWarnings("unchecked")
List<Notice> lectureNoticeEntries = (List<Notice>) request.getAttribute("lectureNoticeEntries");
@SuppressWarnings("unchecked")
List<Assignment> lectureAssignmentEntries = (List<Assignment>) request.getAttribute("lectureAssignmentEntries");
@SuppressWarnings("unchecked")
List<Schedule> studyScheduleEntries = (List<Schedule>) request.getAttribute("studyScheduleEntries");
@SuppressWarnings("unchecked")
List<Notice> studyNoticeEntries = (List<Notice>) request.getAttribute("studyNoticeEntries");
@SuppressWarnings("unchecked")
List<Assignment> studyAssignmentEntries = (List<Assignment>) request.getAttribute("studyAssignmentEntries");

// Map으로 날짜별 데이터 그룹화
Map<Integer, List<Schedule>> lectureScheduleMap = new HashMap<>();
Map<Integer, List<Notice>> lectureNoticeMap = new HashMap<>();
Map<Integer, List<Assignment>> lectureAssignmentMap = new HashMap<>();
Map<Integer, List<Schedule>> studyScheduleMap = new HashMap<>();
Map<Integer, List<Notice>> studyNoticeMap = new HashMap<>();
Map<Integer, List<Assignment>> studyAssignmentMap = new HashMap<>();

for (Schedule lectureSchedule : lectureScheduleEntries) {
	int dayday = lectureSchedule.getStartDate().getDayOfMonth();
	lectureScheduleMap.putIfAbsent(dayday, new ArrayList<>());
	lectureScheduleMap.get(dayday).add(lectureSchedule);
}
for (Notice lectureNotice : lectureNoticeEntries) {
	int dayday = lectureNotice.getCreateat().getDayOfMonth();
	lectureNoticeMap.putIfAbsent(dayday, new ArrayList<>());
	lectureNoticeMap.get(dayday).add(lectureNotice);
}
for (Assignment lectureAssignment : lectureAssignmentEntries) {
	int dayday = lectureAssignment.getDueDate().getDayOfMonth();
	lectureAssignmentMap.putIfAbsent(dayday, new ArrayList<>());
	lectureAssignmentMap.get(dayday).add(lectureAssignment);
}
for (Schedule studySchedule : studyScheduleEntries) {
	int dayday = studySchedule.getStartDate().getDayOfMonth();
	studyScheduleMap.putIfAbsent(dayday, new ArrayList<>());
	studyScheduleMap.get(dayday).add(studySchedule);
}
for (Notice studyNotice : studyNoticeEntries) {
	int dayday = studyNotice.getCreateat().getDayOfMonth();
	studyNoticeMap.putIfAbsent(dayday, new ArrayList<>());
	studyNoticeMap.get(dayday).add(studyNotice);
}
for (Assignment studyAssignment : studyAssignmentEntries) {
	int dayday = studyAssignment.getDueDate().getDayOfMonth();
	studyAssignmentMap.putIfAbsent(dayday, new ArrayList<>());
	studyAssignmentMap.get(dayday).add(studyAssignment);
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel=stylesheet href="<c:url value='/css/main.css' />"
	type="text/css">
<script>
	// 이전/다음 달 이동 함수
	function changeMonth(offset) {
		const currentYear = parseInt(document.getElementById("year").value);
		const currentMonth = parseInt(document.getElementById("month").value) - 1; // Convert to 0-based index for JavaScript
		let newYear = currentYear;
		let newMonth = currentMonth + offset;

		if (newMonth < 0) { // Move to the previous year
			newMonth = 11;
			newYear--;
		} else if (newMonth > 11) { // Move to the next year
			newMonth = 0;
			newYear++;
		}

		// Log updated values for debugging
		console.log(`Updated Year: ${newYear}, Updated Month: ${newMonth + 1}`);

		// Update form values and submit
		document.getElementById("year").value = newYear;
		document.getElementById("month").value = newMonth + 1; // Convert back to 1-based index for server
		document.getElementById("calendarForm").submit();

	}

	// 날짜 클릭 시 서버로 전달
	function selectDate(day) {
		document.getElementById("selectedDay").value = day;
		document.getElementById("calendarForm").submit();
	}
</script>
<title>EduManager</title>
</head>
<body>
	<div class="page">
		<jsp:include page="../navigation/navigation.jsp" />
		<div id="body">
			<div id="schedule">
				<div id="todaySchedule">
					<div class="ScheduleText today">Today Schedule</div>
					<ul class="todaySchedule-list">
						<%
						boolean hasTodaySchedule = false;
						for (Schedule lectureSchedule : lectureScheduleEntries) {
							if (lectureSchedule.getStartDate().getYear() == currentYear
							&& lectureSchedule.getStartDate().getMonthValue() == (currentMonth)
							&& lectureSchedule.getStartDate().getDayOfMonth() == selectedDay) {
								hasTodaySchedule = true;
						%>
						<li class="todaySchedules list1">[강의]<%=escapeHtml(lectureSchedule.getLectureName())%>
							- <%=escapeHtml(lectureSchedule.getTitle())%><%=formatTimeRange(lectureSchedule)%>
						</li>
						<%
						}
						}
						for (Schedule studySchedule : studyScheduleEntries) {
						if (studySchedule.getStartDate().getYear() == currentYear
								&& studySchedule.getStartDate().getMonthValue() == (currentMonth)
								&& studySchedule.getStartDate().getDayOfMonth() == selectedDay) {
							hasTodaySchedule = true;
						%>
						<li class="todaySchedules list2">[스터디]<%=escapeHtml(studySchedule.getLectureName())%>
							- <%=escapeHtml(studySchedule.getTitle())%><%=formatTimeRange(studySchedule)%>
						</li>
						<%
						}
						}
						if (!hasTodaySchedule) {
						%>
						<li class="empty-list-item">선택한 날짜의 일정이 없습니다.</li>
						<%
						}
						%>
					</ul>
				</div>
				<div id="assignment">
					<div class="ScheduleText assignment">Assignment</div>

					<ul class="assignment-list">
						<%
						boolean hasAssignment = false;
						for (Assignment lectureAssignment : lectureAssignmentEntries) {
							if (lectureAssignment.getDueDate().getYear() == currentYear
							&& lectureAssignment.getDueDate().getMonthValue() == (currentMonth)
							&& lectureAssignment.getDueDate().getDayOfMonth() == selectedDay) {
								hasAssignment = true;
						%>
						<li class="assignments list1">[강의][<%=escapeHtml(lectureAssignment.getLectureName())%>]
							<%=escapeHtml(lectureAssignment.getTitle())%></li>
						<%
						}
						}
						for (Assignment studyAssignment : studyAssignmentEntries) {
						if (studyAssignment.getDueDate().getYear() == currentYear
								&& studyAssignment.getDueDate().getMonthValue() == (currentMonth)
								&& studyAssignment.getDueDate().getDayOfMonth() == selectedDay) {
							hasAssignment = true;
						%>
						<li class="assignments list2">[스터디][<%=escapeHtml(studyAssignment.getLectureName())%>]
							<%=escapeHtml(studyAssignment.getTitle())%></li>
						<%
						}
						}
						if (!hasAssignment) {
						%>
						<li class="empty-list-item">선택한 날짜의 과제가 없습니다.</li>
						<%
						}
						%>
					</ul>
				</div>
				
			</div>
			<div id="calendar">
				<div id="calendarHeader">
					<!-- 현재 연도와 월을 숨겨서 JavaScript에서 사용 -->
					<img src="<c:url value='/images/previousMonth.svg' />"
						id="previousMonthIcon" onclick="changeMonth(-1)" /> <span
						class="month"><%=currentMonth%>월</span> <img
						src="<c:url value='/images/nextMonth.svg' />" id="nextMonthIcon"
						onclick="changeMonth(1)" /> <span class="year"><%=currentYear%>년</span>
				</div>
				<table>
					<thead>
						<tr>
							<th class="sunday">일</th>
							<th>월</th>
							<th>화</th>
							<th>수</th>
							<th>목</th>
							<th>금</th>
							<th>토</th>
						</tr>
					</thead>
					<tbody>
						<%
						for (int i = 0; i < 6; i++) { // 최대 6줄 (달력 한 페이지 기준)
							out.println("<tr>");
							for (int j = 1; j <= 7; j++) { // 한 주의 7일
								if (!started && j == firstDayOfWeek) {
							started = true; // 달의 첫 시작점
								}
								if (started && day <= daysInMonth) {
							out.print("<td onclick='selectDate(" + day + ")'>");
							boolean isToday = (currentYear == todayYear && currentMonth == todayMonth && day == todayDate);

							// 현재 날짜 강조
							if (isToday) {
								out.print("<div class='todayDate'><strong>" + day + "</strong></div>");
							} else {
								out.print("<div>" + day + "</div>");
							}

							out.println("<div class='calendar-events'>");
							if (lectureScheduleMap.containsKey(day)) {
								for (Schedule lectureSchedule : lectureScheduleMap.get(day)) {
									String label = "[강의] " + escapeHtml(lectureSchedule.getLectureName()) + " - "
											+ escapeHtml(lectureSchedule.getTitle());
									out.print("<div class='calendar-event calendar-event--lecture' title='" + label + "'>" + label + "</div>");
								}
							}

							if (lectureNoticeMap.containsKey(day)) {
								for (Notice lectureNotice : lectureNoticeMap.get(day)) {
									String label = "[강의공지] " + escapeHtml(lectureNotice.getTitle());
									out.print("<div class='calendar-event calendar-event--notice' title='" + label + "'>" + label + "</div>");
								}
							}

							if (lectureAssignmentMap.containsKey(day)) {
								for (Assignment lectureAssignment : lectureAssignmentMap.get(day)) {
									String label = "[강의과제] " + escapeHtml(lectureAssignment.getLectureName()) + " - "
											+ escapeHtml(lectureAssignment.getTitle());
									out.print("<div class='calendar-event calendar-event--assignment' title='" + label + "'>" + label + "</div>");
								}
							}

							if (studyScheduleMap.containsKey(day)) {
								for (Schedule studySchedule : studyScheduleMap.get(day)) {
									String label = "[스터디] " + escapeHtml(studySchedule.getLectureName()) + " - "
											+ escapeHtml(studySchedule.getTitle());
									out.print("<div class='calendar-event calendar-event--study' title='" + label + "'>" + label + "</div>");
								}
							}

							if (studyNoticeMap.containsKey(day)) {
								for (Notice studyNotice : studyNoticeMap.get(day)) {
									String label = "[스터디공지] " + escapeHtml(studyNotice.getTitle());
									out.print("<div class='calendar-event calendar-event--study-notice' title='" + label + "'>" + label + "</div>");
								}
							}

							if (studyAssignmentMap.containsKey(day)) {
								for (Assignment studyAssignment : studyAssignmentMap.get(day)) {
									String label = "[스터디과제] " + escapeHtml(studyAssignment.getLectureName()) + " - "
											+ escapeHtml(studyAssignment.getTitle());
									out.print("<div class='calendar-event calendar-event--study-assignment' title='" + label + "'>" + label + "</div>");
								}
							}
							out.println("</div>");

							day++;
								} else {
							out.print("<td class='empty-date'>");
								}
								out.print("</td>");
							}
							out.println("</tr>");
							if (day > daysInMonth)
								break; // 모든 날짜 출력 완료
						}
						%>
					</tbody>
				</table>
			</div>

			<form id="calendarForm" method="get"
				action="<c:url value='/main/main' />">
				<input type="hidden" id="year" name="year" value="<%=currentYear%>" />
				<input type="hidden" id="month" name="month"
					value="<%=currentMonth%>" /> <input type="hidden" id="selectedDay"
					name="selectedDay" value="<%=selectedDay%>" />
			</form>

		</div>
	</div>
</body>
</html>
