<%@page contentType="text/html; charset=utf-8"%>
<%@ page import="java.time.LocalDate"%>
<%@ page import="java.time.LocalTime"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Collections"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="model.domain.Assignment"%>
<%@ page import="model.domain.Schedule"%>
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

private boolean isBlank(String value) {
	return value == null || value.trim().isEmpty();
}

private String formatTime(LocalTime time) {
	return time == null ? "" : time.toString();
}

private String formatTimeRange(Schedule schedule) {
	if (schedule.getStartTime() == null && schedule.getEndTime() == null) {
		return "";
	}
	if (schedule.getStartTime() == null) {
		return "~ " + formatTime(schedule.getEndTime());
	}
	if (schedule.getEndTime() == null) {
		return formatTime(schedule.getStartTime()) + " ~";
	}
	return formatTime(schedule.getStartTime()) + " ~ " + formatTime(schedule.getEndTime());
}

private String scheduleTitle(Schedule schedule) {
	if (!isBlank(schedule.getTitle())) {
		return schedule.getTitle().trim();
	}
	if ("regular".equalsIgnoreCase(schedule.getType())) {
		return "정기 수업";
	}
	if ("class".equalsIgnoreCase(schedule.getType())) {
		return "단기 수업";
	}
	if ("exam".equalsIgnoreCase(schedule.getType())) {
		return "시험";
	}
	return "단기 수업";
}

private String scheduleCategory(Schedule schedule) {
	String type = schedule.getType();
	String title = schedule.getTitle();
	if ("exam".equalsIgnoreCase(type) || (!isBlank(title) && (title.contains("시험") || title.contains("중간") || title.contains("기말") || title.contains("평가")))) {
		return "시험";
	}
	if ("regular".equalsIgnoreCase(type) || "class".equalsIgnoreCase(type)) {
		return "수업";
	}
	return "일정";
}

private String scheduleSummary(Schedule schedule) {
	String timeRange = formatTimeRange(schedule);
	if (isBlank(timeRange)) {
		return scheduleTitle(schedule);
	}
	return scheduleTitle(schedule) + " · " + timeRange;
}

private String assignmentPeriod(Assignment assignment) {
	String startDate = assignment.getCreateat() == null ? "" : assignment.getCreateat().toString();
	String startTime = assignment.getStartTime() == null ? "" : formatTime(assignment.getStartTime());
	String dueDate = assignment.getDueDate() == null ? "" : assignment.getDueDate().toString();
	String dueTime = assignment.getDueTime() == null ? "" : formatTime(assignment.getDueTime());

	String start = (startDate + " " + startTime).trim();
	String due = (dueDate + " " + dueTime).trim();
	if (start.isEmpty()) {
		return due.isEmpty() ? "" : "마감 " + due;
	}
	if (due.isEmpty()) {
		return "시작 " + start;
	}
	return start + " ~ " + due;
}

private String focusType(Schedule schedule) {
	String category = scheduleCategory(schedule);
	if ("시험".equals(category)) {
		return "exam";
	}
	if ("일정".equals(category)) {
		return "event";
	}
	return "class";
}

private String detailUrl(String contextPath, boolean study, long ownerId, LocalDate selectedDate, String focusType, int focusId) {
	StringBuilder url = new StringBuilder(contextPath);
	if (study) {
		url.append("/study/itemDetail?groupId=").append(ownerId);
	} else {
		url.append("/lecture/itemDetail?lectureId=").append(ownerId);
	}
	url.append("&selectedDate=").append(selectedDate);
	url.append("&type=").append(focusType);
	url.append("&id=").append(focusId);
	return url.toString();
}

private boolean isSameDate(LocalDate date, int year, int month, int day) {
	return date != null && date.getYear() == year && date.getMonthValue() == month && date.getDayOfMonth() == day;
}

private boolean isAssignmentOnDate(Assignment assignment, LocalDate date) {
	if (date == null) {
		return false;
	}
	// 과제는 마감일에만 표시 (시작~마감 기간 내내 X)
	LocalDate due = assignment.getDueDate() != null ? assignment.getDueDate() : assignment.getCreateat();
	return date.equals(due);
}

private <T> List<T> safeList(Object value) {
	if (value == null) {
		return Collections.emptyList();
	}
	return (List<T>) value;
}

private <T> void addToDayMap(Map<Integer, List<T>> map, LocalDate date, T value, int year, int month) {
	if (date == null || date.getYear() != year || date.getMonthValue() != month) {
		return;
	}
	int day = date.getDayOfMonth();
	map.putIfAbsent(day, new ArrayList<T>());
	map.get(day).add(value);
}

private void addAssignmentToDayMap(Map<Integer, List<Assignment>> map, Assignment assignment, int year, int month) {
	// 과제는 마감일에만 그리드에 표시
	LocalDate due = assignment.getDueDate() != null ? assignment.getDueDate() : assignment.getCreateat();
	addToDayMap(map, due, assignment, year, month);
}
%>
<%
int currentYear = (int) request.getAttribute("year");
int currentMonth = (int) request.getAttribute("month");
int selectedDay = (int) request.getAttribute("selectedDay");

java.util.Calendar calendar = java.util.Calendar.getInstance();
calendar.set(currentYear, currentMonth - 1, 1);
int daysInMonth = calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
int firstDayOfWeek = calendar.get(java.util.Calendar.DAY_OF_WEEK);

java.util.Calendar now = java.util.Calendar.getInstance();
int todayYear = now.get(java.util.Calendar.YEAR);
int todayMonth = now.get(java.util.Calendar.MONTH) + 1;
int todayDate = now.get(java.util.Calendar.DATE);
LocalDate selectedDate = LocalDate.of(currentYear, currentMonth, Math.min(selectedDay, daysInMonth));
LocalDate today = LocalDate.of(todayYear, todayMonth, todayDate);

List<Schedule> lectureScheduleEntries = safeList(request.getAttribute("lectureScheduleEntries"));
List<Assignment> lectureAssignmentEntries = safeList(request.getAttribute("lectureAssignmentEntries"));
List<Schedule> studyScheduleEntries = safeList(request.getAttribute("studyScheduleEntries"));
List<Assignment> studyAssignmentEntries = safeList(request.getAttribute("studyAssignmentEntries"));

List<Schedule> todayLectureScheduleEntries = safeList(request.getAttribute("todayLectureScheduleEntries"));
List<Assignment> todayLectureAssignmentEntries = safeList(request.getAttribute("todayLectureAssignmentEntries"));
List<Schedule> todayStudyScheduleEntries = safeList(request.getAttribute("todayStudyScheduleEntries"));
List<Assignment> todayStudyAssignmentEntries = safeList(request.getAttribute("todayStudyAssignmentEntries"));

Map<Integer, List<Schedule>> lectureScheduleMap = new HashMap<>();
Map<Integer, List<Assignment>> lectureAssignmentMap = new HashMap<>();
Map<Integer, List<Schedule>> studyScheduleMap = new HashMap<>();
Map<Integer, List<Assignment>> studyAssignmentMap = new HashMap<>();

for (Schedule entry : lectureScheduleEntries) {
	addToDayMap(lectureScheduleMap, entry.getStartDate(), entry, currentYear, currentMonth);
}
for (Assignment entry : lectureAssignmentEntries) {
	addAssignmentToDayMap(lectureAssignmentMap, entry, currentYear, currentMonth);
}
for (Schedule entry : studyScheduleEntries) {
	addToDayMap(studyScheduleMap, entry.getStartDate(), entry, currentYear, currentMonth);
}
for (Assignment entry : studyAssignmentEntries) {
	addAssignmentToDayMap(studyAssignmentMap, entry, currentYear, currentMonth);
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/theme.css'/>?v=20260621-toss1">
<link rel="stylesheet" href="<c:url value='/css/main.css' />?v=20260616-focus1" type="text/css">
<script>
	function changeMonth(offset) {
		const currentYear = parseInt(document.getElementById("year").value);
		const currentMonth = parseInt(document.getElementById("month").value) - 1;
		let newYear = currentYear;
		let newMonth = currentMonth + offset;

		if (newMonth < 0) {
			newMonth = 11;
			newYear--;
		} else if (newMonth > 11) {
			newMonth = 0;
			newYear++;
		}

		document.getElementById("year").value = newYear;
		document.getElementById("month").value = newMonth + 1;
		document.getElementById("selectedDay").value = 1;
		document.getElementById("calendarForm").submit();
	}

	function selectDate(day) {
		document.getElementById("selectedDay").value = day;
		document.getElementById("calendarForm").submit();
	}
</script>
<title>EduManager</title>
</head>
<body>
	<div class="page calendar-page">
		<jsp:include page="../navigation/navigation.jsp" />
		<section class="calendar-head">
			<div>
				<span class="page-eyebrow">SCHEDULE</span>
				<h1>일정 캘린더</h1>
				<p>수업, 일정, 시험, 과제를 날짜별로 확인하세요.</p>
			</div>
		</section>
		<div id="body">
			<aside id="schedule">
				<div id="todaySchedule">
					<div class="ScheduleText today">오늘 일정</div>
					<ul class="todaySchedule-list">
						<%
						boolean hasTodaySchedule = false;
						for (Schedule lectureSchedule : todayLectureScheduleEntries) {
							if (isSameDate(lectureSchedule.getStartDate(), todayYear, todayMonth, todayDate)) {
								hasTodaySchedule = true;
							%>
							<li class="todaySchedules list1">
								<a class="summary-link" href="<%=detailUrl(request.getContextPath(), false, lectureSchedule.getLectureId(), lectureSchedule.getStartDate(), focusType(lectureSchedule), lectureSchedule.getScheduleId())%>">
									<span class="item-badge badge-<%=scheduleCategory(lectureSchedule).equals("시험") ? "exam" : scheduleCategory(lectureSchedule).equals("일정") ? "event" : "class"%>"><%=scheduleCategory(lectureSchedule)%></span>
									<span>[강의] <%=escapeHtml(lectureSchedule.getLectureName())%> · <%=escapeHtml(scheduleSummary(lectureSchedule))%></span>
								</a>
							</li>
						<%
							}
						}
						for (Schedule studySchedule : todayStudyScheduleEntries) {
							if (isSameDate(studySchedule.getStartDate(), todayYear, todayMonth, todayDate)) {
								hasTodaySchedule = true;
							%>
							<li class="todaySchedules list2">
								<a class="summary-link" href="<%=detailUrl(request.getContextPath(), true, studySchedule.getStudyGroupId(), studySchedule.getStartDate(), focusType(studySchedule), studySchedule.getScheduleId())%>">
									<span class="item-badge badge-<%=scheduleCategory(studySchedule).equals("시험") ? "exam" : scheduleCategory(studySchedule).equals("일정") ? "event" : "class"%>"><%=scheduleCategory(studySchedule)%></span>
									<span>[스터디] <%=escapeHtml(studySchedule.getLectureName())%> · <%=escapeHtml(scheduleSummary(studySchedule))%></span>
								</a>
							</li>
						<%
							}
						}
						if (!hasTodaySchedule) {
						%>
						<li class="empty-list-item">오늘 등록된 일정이 없습니다.</li>
						<%
						}
						%>
					</ul>
				</div>
				<div id="assignment">
					<div class="ScheduleText assignment">오늘 과제</div>
					<ul class="assignment-list">
						<%
						boolean hasTodayAssignment = false;
						for (Assignment lectureAssignment : todayLectureAssignmentEntries) {
							if (isAssignmentOnDate(lectureAssignment, today)) {
								hasTodayAssignment = true;
							%>
							<li class="assignments list1">
								<a class="summary-link" href="<%=detailUrl(request.getContextPath(), false, lectureAssignment.getLectureId(), today, "assignment", lectureAssignment.getId())%>">
									<span class="item-badge badge-assignment">과제</span>
									<span>[강의] <%=escapeHtml(lectureAssignment.getLectureName())%> · <%=escapeHtml(lectureAssignment.getTitle())%></span>
								</a>
							</li>
						<%
							}
						}
						for (Assignment studyAssignment : todayStudyAssignmentEntries) {
							if (isAssignmentOnDate(studyAssignment, today)) {
								hasTodayAssignment = true;
							%>
							<li class="assignments list2">
								<a class="summary-link" href="<%=detailUrl(request.getContextPath(), true, studyAssignment.getStudyId(), today, "assignment", studyAssignment.getId())%>">
									<span class="item-badge badge-assignment">과제</span>
									<span>[스터디] <%=escapeHtml(studyAssignment.getLectureName())%> · <%=escapeHtml(studyAssignment.getTitle())%></span>
								</a>
							</li>
						<%
							}
						}
						if (!hasTodayAssignment) {
						%>
						<li class="empty-list-item">오늘 진행 중인 과제가 없습니다.</li>
						<%
						}
						%>
					</ul>
				</div>
			</aside>

			<div id="calendar">
				<div id="calendarHeader">
					<div class="calendar-month">
						<span class="year"><%=currentYear%>년</span>
						<span class="month"><%=currentMonth%>월</span>
					</div>
					<div class="calendar-controls">
						<img src="<c:url value='/images/previousMonth.svg' />" id="previousMonthIcon"
							onclick="changeMonth(-1)" alt="이전 달" />
						<img src="<c:url value='/images/nextMonth.svg' />" id="nextMonthIcon"
							onclick="changeMonth(1)" alt="다음 달" />
					</div>
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
						int day = 1;
						boolean started = false;
						for (int i = 0; i < 6; i++) {
							out.println("<tr>");
							for (int j = 1; j <= 7; j++) {
								if (!started && j == firstDayOfWeek) {
									started = true;
								}
								if (started && day <= daysInMonth) {
									boolean isToday = currentYear == todayYear && currentMonth == todayMonth && day == todayDate;
									boolean isSelected = day == selectedDate.getDayOfMonth();
									String tdClass = isSelected ? "selected-date" : "";
									if (isToday) {
										tdClass = (tdClass + " current-today").trim();
									}
									out.print("<td" + (tdClass.isEmpty() ? "" : " class='" + tdClass + "'") + " onclick='selectDate(" + day + ")'>");
									out.print("<div class='date-number" + (isToday ? " todayDate" : "") + "'><strong>" + day + "</strong></div>");
									out.println("<div class='calendar-events'>");
									LocalDate cellDate = LocalDate.of(currentYear, currentMonth, day);

									if (lectureScheduleMap.containsKey(day)) {
										for (Schedule lectureSchedule : lectureScheduleMap.get(day)) {
											String label = scheduleCategory(lectureSchedule) + " · " + escapeHtml(lectureSchedule.getLectureName()) + " - "
													+ escapeHtml(scheduleSummary(lectureSchedule));
											String eventClass = scheduleCategory(lectureSchedule).equals("시험") ? "calendar-event--exam" : scheduleCategory(lectureSchedule).equals("일정") ? "calendar-event--event" : "calendar-event--lecture";
											String url = detailUrl(request.getContextPath(), false, lectureSchedule.getLectureId(), cellDate, focusType(lectureSchedule), lectureSchedule.getScheduleId());
											out.print("<a class='calendar-event " + eventClass + "' href='" + url + "' onclick='event.stopPropagation()' title='" + label + "'>" + label + "</a>");
										}
									}
									if (studyScheduleMap.containsKey(day)) {
										for (Schedule studySchedule : studyScheduleMap.get(day)) {
											String label = scheduleCategory(studySchedule) + " · " + escapeHtml(studySchedule.getLectureName()) + " - "
													+ escapeHtml(scheduleSummary(studySchedule));
											String eventClass = scheduleCategory(studySchedule).equals("시험") ? "calendar-event--exam" : scheduleCategory(studySchedule).equals("일정") ? "calendar-event--event" : "calendar-event--study";
											String url = detailUrl(request.getContextPath(), true, studySchedule.getStudyGroupId(), cellDate, focusType(studySchedule), studySchedule.getScheduleId());
											out.print("<a class='calendar-event " + eventClass + "' href='" + url + "' onclick='event.stopPropagation()' title='" + label + "'>" + label + "</a>");
										}
									}
									if (lectureAssignmentMap.containsKey(day)) {
										for (Assignment lectureAssignment : lectureAssignmentMap.get(day)) {
											String label = "과제 · " + escapeHtml(lectureAssignment.getLectureName()) + " - "
													+ escapeHtml(lectureAssignment.getTitle());
											String url = detailUrl(request.getContextPath(), false, lectureAssignment.getLectureId(), cellDate, "assignment", lectureAssignment.getId());
											out.print("<a class='calendar-event calendar-event--assignment' href='" + url + "' onclick='event.stopPropagation()' title='" + label + "'>" + label + "</a>");
										}
									}
									if (studyAssignmentMap.containsKey(day)) {
										for (Assignment studyAssignment : studyAssignmentMap.get(day)) {
											String label = "과제 · " + escapeHtml(studyAssignment.getLectureName()) + " - "
													+ escapeHtml(studyAssignment.getTitle());
											String url = detailUrl(request.getContextPath(), true, studyAssignment.getStudyId(), cellDate, "assignment", studyAssignment.getId());
											out.print("<a class='calendar-event calendar-event--study-assignment' href='" + url + "' onclick='event.stopPropagation()' title='" + label + "'>" + label + "</a>");
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
							if (day > daysInMonth) {
								break;
							}
						}
						%>
					</tbody>
				</table>
			</div>

			<section id="selected-detail">
				<div class="detail-header">
					<div>
						<span class="detail-eyebrow">SELECTED DATE</span>
						<h2>선택한 날짜 상세</h2>
					</div>
					<span class="detail-date"><%=currentYear%>. <%=currentMonth%>. <%=selectedDate.getDayOfMonth()%>.</span>
				</div>
				<div class="detail-grid">
					<div class="detail-card detail-card--class">
						<div class="detail-card-title">수업</div>
						<ul class="detail-list">
							<%
							boolean hasSelectedClass = false;
							for (Schedule lectureSchedule : lectureScheduleEntries) {
								if (isSameDate(lectureSchedule.getStartDate(), currentYear, currentMonth, selectedDate.getDayOfMonth())
										&& "수업".equals(scheduleCategory(lectureSchedule))) {
									hasSelectedClass = true;
							%>
							<li>
								<a class="detail-item detail-link" href="<%=detailUrl(request.getContextPath(), false, lectureSchedule.getLectureId(), selectedDate, focusType(lectureSchedule), lectureSchedule.getScheduleId())%>">
									<div class="detail-item-title">[강의] <%=escapeHtml(lectureSchedule.getLectureName())%></div>
									<div class="detail-meta"><%=escapeHtml(scheduleSummary(lectureSchedule))%></div>
								</a>
							</li>
							<%
								}
							}
							for (Schedule studySchedule : studyScheduleEntries) {
								if (isSameDate(studySchedule.getStartDate(), currentYear, currentMonth, selectedDate.getDayOfMonth())
										&& "수업".equals(scheduleCategory(studySchedule))) {
									hasSelectedClass = true;
							%>
							<li>
								<a class="detail-item detail-link" href="<%=detailUrl(request.getContextPath(), true, studySchedule.getStudyGroupId(), selectedDate, focusType(studySchedule), studySchedule.getScheduleId())%>">
									<div class="detail-item-title">[스터디] <%=escapeHtml(studySchedule.getLectureName())%></div>
									<div class="detail-meta"><%=escapeHtml(scheduleSummary(studySchedule))%></div>
								</a>
							</li>
							<%
								}
							}
							if (!hasSelectedClass) {
							%>
							<li class="empty-list-item">선택한 날짜의 수업이 없습니다.</li>
							<%
							}
							%>
						</ul>
					</div>
					<div class="detail-card detail-card--event">
						<div class="detail-card-title">일정</div>
						<ul class="detail-list">
							<%
							boolean hasSelectedEvent = false;
							for (Schedule lectureSchedule : lectureScheduleEntries) {
								if (isSameDate(lectureSchedule.getStartDate(), currentYear, currentMonth, selectedDate.getDayOfMonth())
										&& "일정".equals(scheduleCategory(lectureSchedule))) {
									hasSelectedEvent = true;
							%>
							<li>
								<a class="detail-item detail-link" href="<%=detailUrl(request.getContextPath(), false, lectureSchedule.getLectureId(), selectedDate, focusType(lectureSchedule), lectureSchedule.getScheduleId())%>">
									<div class="detail-item-title">[강의] <%=escapeHtml(lectureSchedule.getLectureName())%></div>
									<div class="detail-meta"><%=escapeHtml(scheduleSummary(lectureSchedule))%></div>
								</a>
							</li>
							<%
								}
							}
							for (Schedule studySchedule : studyScheduleEntries) {
								if (isSameDate(studySchedule.getStartDate(), currentYear, currentMonth, selectedDate.getDayOfMonth())
										&& "일정".equals(scheduleCategory(studySchedule))) {
									hasSelectedEvent = true;
							%>
							<li>
								<a class="detail-item detail-link" href="<%=detailUrl(request.getContextPath(), true, studySchedule.getStudyGroupId(), selectedDate, focusType(studySchedule), studySchedule.getScheduleId())%>">
									<div class="detail-item-title">[스터디] <%=escapeHtml(studySchedule.getLectureName())%></div>
									<div class="detail-meta"><%=escapeHtml(scheduleSummary(studySchedule))%></div>
								</a>
							</li>
							<%
								}
							}
							if (!hasSelectedEvent) {
							%>
							<li class="empty-list-item">선택한 날짜의 일정이 없습니다.</li>
							<%
							}
							%>
						</ul>
					</div>
					<div class="detail-card detail-card--exam">
						<div class="detail-card-title">시험</div>
						<ul class="detail-list">
							<%
							boolean hasSelectedExam = false;
							for (Schedule lectureSchedule : lectureScheduleEntries) {
								if (isSameDate(lectureSchedule.getStartDate(), currentYear, currentMonth, selectedDate.getDayOfMonth())
										&& "시험".equals(scheduleCategory(lectureSchedule))) {
									hasSelectedExam = true;
							%>
							<li>
								<a class="detail-item detail-link" href="<%=detailUrl(request.getContextPath(), false, lectureSchedule.getLectureId(), selectedDate, focusType(lectureSchedule), lectureSchedule.getScheduleId())%>">
									<div class="detail-item-title">[강의] <%=escapeHtml(lectureSchedule.getLectureName())%></div>
									<div class="detail-meta"><%=escapeHtml(scheduleSummary(lectureSchedule))%></div>
								</a>
							</li>
							<%
								}
							}
							for (Schedule studySchedule : studyScheduleEntries) {
								if (isSameDate(studySchedule.getStartDate(), currentYear, currentMonth, selectedDate.getDayOfMonth())
										&& "시험".equals(scheduleCategory(studySchedule))) {
									hasSelectedExam = true;
							%>
							<li>
								<a class="detail-item detail-link" href="<%=detailUrl(request.getContextPath(), true, studySchedule.getStudyGroupId(), selectedDate, focusType(studySchedule), studySchedule.getScheduleId())%>">
									<div class="detail-item-title">[스터디] <%=escapeHtml(studySchedule.getLectureName())%></div>
									<div class="detail-meta"><%=escapeHtml(scheduleSummary(studySchedule))%></div>
								</a>
							</li>
							<%
								}
							}
							if (!hasSelectedExam) {
							%>
							<li class="empty-list-item">선택한 날짜의 시험이 없습니다.</li>
							<%
							}
							%>
						</ul>
					</div>
					<div class="detail-card detail-card--assignment">
						<div class="detail-card-title">과제</div>
						<ul class="detail-list">
							<%
							boolean hasSelectedAssignment = false;
							for (Assignment lectureAssignment : lectureAssignmentEntries) {
								if (isAssignmentOnDate(lectureAssignment, selectedDate)) {
									hasSelectedAssignment = true;
							%>
							<li>
								<a class="detail-item detail-link" href="<%=detailUrl(request.getContextPath(), false, lectureAssignment.getLectureId(), selectedDate, "assignment", lectureAssignment.getId())%>">
									<div class="detail-item-title">[강의] <%=escapeHtml(lectureAssignment.getLectureName())%> · <%=escapeHtml(lectureAssignment.getTitle())%></div>
									<div class="detail-meta"><%=escapeHtml(assignmentPeriod(lectureAssignment))%></div>
									<div class="detail-description"><%=escapeHtml(lectureAssignment.getDescription())%></div>
								</a>
							</li>
							<%
								}
							}
							for (Assignment studyAssignment : studyAssignmentEntries) {
								if (isAssignmentOnDate(studyAssignment, selectedDate)) {
									hasSelectedAssignment = true;
							%>
							<li>
								<a class="detail-item detail-link" href="<%=detailUrl(request.getContextPath(), true, studyAssignment.getStudyId(), selectedDate, "assignment", studyAssignment.getId())%>">
									<div class="detail-item-title">[스터디] <%=escapeHtml(studyAssignment.getLectureName())%> · <%=escapeHtml(studyAssignment.getTitle())%></div>
									<div class="detail-meta"><%=escapeHtml(assignmentPeriod(studyAssignment))%></div>
									<div class="detail-description"><%=escapeHtml(studyAssignment.getDescription())%></div>
								</a>
							</li>
							<%
								}
							}
							if (!hasSelectedAssignment) {
							%>
							<li class="empty-list-item">선택한 날짜의 과제가 없습니다.</li>
							<%
							}
							%>
						</ul>
					</div>
					</div>
				</section>

			<form id="calendarForm" method="get" action="<c:url value='/main' />">
				<input type="hidden" id="year" name="year" value="<%=currentYear%>" />
				<input type="hidden" id="month" name="month" value="<%=currentMonth%>" />
				<input type="hidden" id="selectedDay" name="selectedDay" value="<%=selectedDate.getDayOfMonth()%>" />
			</form>
		</div>
	</div>
</body>
</html>
