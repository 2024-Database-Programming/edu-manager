<%@page contentType="text/html; charset=utf-8"%>
<%@page import="java.util.HashMap, java.util.Map" %>
<%@page contentType="text/html; charset=utf-8"%>
<%-- <%@page import="java.util.*, model.domain.*" %> --%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />

<link rel="stylesheet" href="<c:url value='/css/study_details.css' />"
	type="text/css">
<script src="<c:url value='/js/study_calendar.js' />"></script>
<title>강의 상세보기</title>
</head>
<%
    Map<String, String> dayMap = new HashMap<>();
    dayMap.put("MONDAY", "월");
    dayMap.put("TUESDAY", "화");
    dayMap.put("WEDNESDAY", "수");
    dayMap.put("THURSDAY", "목");
    dayMap.put("FRIDAY", "금");
    dayMap.put("SATURDAY", "토");
    dayMap.put("SUNDAY", "일");
    request.setAttribute("dayMap", dayMap);
%>
<body>
	<jsp:include page="../navigation/navigation.jsp" />
<input type="hidden" id="eventsData" value='${events}'/>

	<div class="study-container">
		<div class="study-detail">
			<h2 class="study-title">강의 상세보기</h2>
			<c:if test="${isTeacher}">
				<a class="complete-button"
					href="<c:url value="/lecture/update">
				<c:param name="lectureId" value="${lectureInfo.lectureId}" />
								</c:url>">강의
					정보 수정하기</a>
			</c:if>
<c:if test="${!isTeacher}">
				<a class="complete-button"
					href="<c:url value="/lecture/over-view">
				<c:param name="lectureId" value="${lectureInfo.lectureId}" />
								</c:url>">강의 후기 작성하기</a>
			</c:if>

			<div class="study-info-box"></div>

			<div id="teacher_name">${lectureInfo.teacherName} 강사님</div>

			<table class="study-location">
				<tr class="icon">
					<td><img src="<c:url value='/images/phoneIcon.svg"' />"
						class="infoIcon" /></td>
					<td class="location-inform">${lectureInfo.phone}</td>
				</tr>
				<tr class="icon">
					<td class="location-icon"></td>
					<td class="location-inform">${lectureInfo.lectureRoom}호</td>
				</tr>
				<tr class="icon">
					<td class="time-icon"></td>

					<c:forEach var="schedule" items="${regularSchedules}">
						<td class="location-inform">  ${dayMap[schedule.dayOfWeek]}요일 ${schedule.startTime}
							</td>
					</c:forEach>

				</tr>
			</table>
			<div class="team-members-box">
				<div class="team-header">
					<img src="<c:url value='/images/members.png' />" alt="members"
						style="height: 10px; margin-right: 5px" /> <span
						class="team-count">수강생
						(${fn:length(members)}/${lectureInfo.capacity})</span>
				</div>
				<ul class="member-list">

					<c:forEach var="member" items="${members}">
						<li class="member-item"><span class="member-name">${member}</span>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>

		<div class="mainInform">
			<div class="title">${lectureInfo.name}</div>
			<div id="calendarHeader">
				<span class="year">2024</span> <span class="month"> <img
					src="<c:url value='/images/previousMonth.svg' />"
					id="previousMonthIcon" alt="" /> <span>11</span>월<img
					src="<c:url value='/images/nextMonth.svg'  />" id="nextMonthIcon"
					alt="" />
				</span>
			</div>
			<table class="calendarTable">
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
				<tbody></tbody>
				<!-- JavaScript에서 동적으로 채움 -->
			</table>
			<form id="dateForm" action="<c:url value='/mylecture/view' />"
				method="post">
				<input type="hidden" name="selectedDate" id="selectedDate"
					value="${selectedDate}"> <input type="hidden"
					name="lectureId" id="lectureId" value="${lectureInfo.lectureId}">
			</form>
			<div class="main-container">
				<div class="rectangle-1">
					<div class="schedule">
						<div class="schedule_title">일정</div>
						<div class="schedule_content">
							<ul>
								<c:forEach var="schedule" items="${regularSchedules}">
									<li class="schedule-item">${schedule.title}<%-- ${schedule.startTime}~ ${schedule.endTime}  --%>
										<!-- 다른 schedule 속성들도 필요에 따라 추가 -->
									</li>
								</c:forEach>
								<c:forEach var="schedule" items="${specialSchedules}">
									<li class="schedule-item">${schedule.title}<%-- ${schedule.startTime}~ ${schedule.endTime}  --%>
										<!-- 다른 schedule 속성들도 필요에 따라 추가 -->
									</li>
								</c:forEach>
							</ul>
							<c:if test="${isTeacher}">
								<a class="plus_button"
									href="<c:url value="/lecture/addSchedule">
									<c:param name="lectureId" value="${lectureInfo.lectureId}" />
									<c:param name="selectedDate" value="${selectedDate}" />
								</c:url>">
									+ </a>
							</c:if>

						</div>
					</div>
					<div class="notice">
						<div class="notice_title">공지 사항</div>
						<div class="notice_content">
							<ul>
								<c:forEach var="notice" items="${noticeList}">
									<li class="notice-item">${notice.title}</li>
								</c:forEach>
							</ul>
							<c:if test="${isTeacher}">
								<a class="plus_button"
									href="<c:url value="/lecture/addNotice">
									<c:param name="lectureId" value="${lectureInfo.lectureId}" />
									<c:param name="selectedDate" value="${selectedDate}" />
								</c:url>">
									+ </a>
							</c:if>
						</div>
					</div>
					<div class="assignment">
						<div class="assignment_title">과제</div>
						<div class="assignment_content">
							<ul>
								<c:forEach var="assignment" items="${assignmentList}">
									<li class="assignment-item">${assignment.title}</li>
								</c:forEach>
							</ul>
							<c:if test="${isTeacher}">
								<a class="plus_button"
									href="<c:url value="/lecture/addAssignment">
										<c:param name="lectureId" value="${lectureInfo.lectureId}" />
										<c:param name="selectedDate" value="${selectedDate}" />
									</c:url>">
									+ </a>
							</c:if>
						</div>
					</div>
				</div>
			</div>
			<div class="rectangle">
				<div class="flex-column-fc">
					<div class="flex-column">
						<div class="notice_main">공지사항</div>
						<a
							href="<c:url value='/lecture/listNotice'/>?groupId=${lectureInfo.lectureId}"
							class="more-link"> 더보기 </a>
					</div>
					<div class="important-notice">
						<ul>
							<c:forEach var="notice" items="${noticeList}">
								<li class="assignment-item">${notice.title}</li>
							</c:forEach>
						</ul>

					</div>
				</div>
			</div>
			<div class="rectangle">
				<div class="flex-column-fc">
					<div class="flex-column">
						<div class="notice_main">과제</div>
							<a
								href="<c:url value='/lecture/listAssignment'><c:param name='groupId' value='${lectureInfo.lectureId}' /></c:url>"
								class="more-link">더보기</a>
					</div>
					<div class="important-notice">
						<ul>
							<c:forEach var="assignment" items="${assignmentList}">
								<li class="assignment-item">${assignment.title}</li>
							</c:forEach>
						</ul>

					</div>
				</div>
			</div>
		</div>

	</div>
</body>
</html>
