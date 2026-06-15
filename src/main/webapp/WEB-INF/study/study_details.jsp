<%@page contentType="text/html; charset=utf-8"%>
<%@page import="java.util.HashMap, java.util.Map" %>
<%-- <%@page import="java.util.*, model.domain.*" %> --%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />

<link rel="stylesheet" type="text/css" href="<c:url value='/css/theme.css'/>?v=20260613-ds4">
<link rel="stylesheet" href="<c:url value='/css/study_details.css' />?v=20260616-detail2"
	type="text/css">
<script src="<c:url value='/js/study_calendar.js' />?v=20260616-detail2"></script>
<title>스터디 상세보기</title>
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
   <div class="page">
   <jsp:include page="../navigation/navigation.jsp" />
   <input type="hidden" id="eventsData" value='${events}'/>
   <script type="application/json" id="calendarEventsJson">${calendarEventsJson}</script>

   <div class="study-container">
      <div class="study-detail">
         <h2 class="study-title">스터디 상세보기</h2>
         
            
         
         <c:if test="${isLeader}">
            <a class="complete-button" href="<c:url value='/study/update'>
            <c:param name="studyId" value="${studyInfo.studyGroupId}" />
                        </c:url>">스터디 정보 수정하기</a>
         </c:if>

				<a class="complete-button"
				style="margin-top:20px"
					href="<c:url value='/study/over-view'>
					<c:param name="groupId" value="${studyInfo.studyGroupId}" />
									</c:url>">스터디 후기 작성하기</a>


			<div class="study-info-box"></div>
 <%--         <table class="study-location">
            <tr class="icon">
               <td class="location-icon"></td>
               <td class="location-inform">${studyInfo.place}호</td>
            </tr>
            <tr class="icon">
       	<td class="time-icon"></td>

					<c:forEach var="schedule" items="${regularSchedules}">
						<td class="location-inform">  ${dayMap[schedule.dayOfWeek]}요일 ${schedule.startTime}
							</td>
					</c:forEach>
		</tr>
         </table> --%>
         <div class="pinned-notice-box">
            <div class="team-header">
               <span class="team-count">공지사항</span>
               <a href="<c:url value='/study/listNotice'/>?groupId=${studyInfo.studyGroupId}" class="more-link">더보기</a>
            </div>
            <ul class="member-list">
               <c:choose>
                  <c:when test="${empty noticeList}">
                     <li class="empty-detail-item">등록된 공지사항이 없습니다.</li>
                  </c:when>
                  <c:otherwise>
                     <c:forEach var="notice" items="${noticeList}">
                        <li class="member-item">
                           <c:url var="noticeDetailUrl" value="/study/itemDetail">
                              <c:param name="groupId" value="${studyInfo.studyGroupId}" />
                              <c:param name="selectedDate" value="${notice.createat}" />
                              <c:param name="type" value="notice" />
                              <c:param name="id" value="${notice.id}" />
                           </c:url>
                           <a class="detail-item side-info-item notice-item" href="${noticeDetailUrl}">
                              <span class="item-title"><c:out value="${notice.title}" /></span>
                              <span class="item-meta">등록일 ${notice.createat}</span>
                           </a>
                        </li>
                     </c:forEach>
                  </c:otherwise>
               </c:choose>
            </ul>
         </div>
         <div class="pinned-assignment-box">
            <div class="team-header">
               <span class="team-count">과제</span>
               <a href="<c:url value='/study/listAssignment'/>?groupId=${studyInfo.studyGroupId}" class="more-link">더보기</a>
            </div>
            <ul class="member-list">
               <c:choose>
                  <c:when test="${empty assignmentList}">
                     <li class="empty-detail-item">등록된 과제가 없습니다.</li>
                  </c:when>
                  <c:otherwise>
                     <c:forEach var="assignment" items="${assignmentList}">
                        <li class="member-item">
                           <c:url var="assignmentDetailUrl" value="/study/itemDetail">
                              <c:param name="groupId" value="${studyInfo.studyGroupId}" />
                              <c:param name="selectedDate" value="${assignment.dueDate}" />
                              <c:param name="type" value="assignment" />
                              <c:param name="id" value="${assignment.id}" />
                           </c:url>
                           <a class="detail-item side-info-item assignment-item" href="${assignmentDetailUrl}">
                              <span class="item-title"><c:out value="${assignment.title}" /></span>
                              <span class="item-meta">${assignment.createat}<c:if test="${not empty assignment.startTime}"> ${assignment.startTime}</c:if> ~ ${assignment.dueDate}<c:if test="${not empty assignment.dueTime}"> ${assignment.dueTime}</c:if></span>
                           </a>
                        </li>
                     </c:forEach>
                  </c:otherwise>
               </c:choose>
            </ul>
         </div>
         <div class="team-members-box">
            <div class="team-header">
               <img src="<c:url value='/images/members.png' />" alt="members" 
               style="height: 10px; margin-right: 5px"/>
               <span class="team-count">스터디원 (${fn:length(members) + 1}/${studyInfo.capacity})</span>
            </div>
            <ul class="member-list">
               <li class="member-item">
                  <a class="member-link" href="<c:url value='/member/view'><c:param name='id' value='${studyInfo.leaderId}' /></c:url>">
                  	<span class="member-name">${studyInfo.leaderName}</span>
                  </a>
                  <img src="<c:url value='/images/mdi_crown.png' />" alt="leader" 
               style="height: 15px; margin-left: 5px"/> 
               </li>
               <c:forEach var="member" items="${members}">
                  <li class="member-item">
                     <a class="member-link" href="<c:url value='/member/view'><c:param name='id' value='${member.id}' /></c:url>">
                     	<span class="member-name">${member.name}</span>
                     </a>
                  </li>
               </c:forEach>
            </ul>
         </div>
      </div>


		<div class="mainInform">
			<div class="detail-topbar">
			<div class="title">${studyInfo.name}</div>
			<div id="calendarHeader">
				<span class="year">2024</span> <span class="month"> <img
					src="<c:url value='/images/previousMonth.svg' />"
					id="previousMonthIcon" alt="" /> <span>11</span>월<img
					src="<c:url value='/images/nextMonth.svg'  />" id="nextMonthIcon"
					alt="" />
				</span>
			</div>
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
			<form id="dateForm" action="<c:url value='/mystudy/view' />"
				method="get">
				<input type="hidden" name="selectedDate" id="selectedDate"
					value="${selectedDate}">
					<input type="hidden" name="groupId" id="groupId"
					value="${studyInfo.studyGroupId}">

			</form>
			<div class="main-container">
				<div class="rectangle-1">
					<div class="classroom">
						<div class="classroom_title">수업</div>
						<div class="classroom_content">
							<ul>
								<c:forEach var="schedule" items="${regularSchedules}">
									<li class="detail-list-row">
										<c:url var="classDetailUrl" value="/study/itemDetail">
											<c:param name="groupId" value="${studyInfo.studyGroupId}" />
											<c:param name="selectedDate" value="${selectedDate}" />
											<c:param name="type" value="class" />
											<c:param name="id" value="${schedule.scheduleId}" />
										</c:url>
										<a class="detail-item schedule-item" href="${classDetailUrl}">
											<span class="item-title">
												<c:choose>
													<c:when test="${not empty schedule.title}"><c:out value="${schedule.title}" /></c:when>
													<c:otherwise>정기모임 - ${dayMap[schedule.dayOfWeek]}요일</c:otherwise>
												</c:choose>
											</span>
											<span class="item-meta">${selectedDate}<c:if test="${not empty schedule.startTime || not empty schedule.endTime}"> · ${schedule.startTime}~${schedule.endTime}</c:if></span>
										</a>
									</li>
								</c:forEach>
								<c:forEach var="schedule" items="${specialSchedules}">
									<c:if test="${schedule.type == 'class'}">
										<li class="detail-list-row">
											<c:url var="classDetailUrl" value="/study/itemDetail">
												<c:param name="groupId" value="${studyInfo.studyGroupId}" />
												<c:param name="selectedDate" value="${selectedDate}" />
												<c:param name="type" value="class" />
												<c:param name="id" value="${schedule.scheduleId}" />
											</c:url>
											<a class="detail-item schedule-item" href="${classDetailUrl}">
												<span class="item-title">
													<c:choose>
														<c:when test="${not empty schedule.title}"><c:out value="${schedule.title}" /></c:when>
														<c:otherwise>단기 수업</c:otherwise>
													</c:choose>
												</span>
												<span class="item-meta">${schedule.startDate}<c:if test="${not empty schedule.startTime || not empty schedule.endTime}"> · ${schedule.startTime}~${schedule.endTime}</c:if></span>
											</a>
										</li>
									</c:if>
								</c:forEach>
							</ul>
							<c:if test="${isLeader}">
								<a class="plus_button" href="<c:url value='/study/addSchedule'>
									<c:param name="groupId" value="${studyInfo.studyGroupId}" />
									<c:param name="selectedDate" value="${selectedDate}" />
									<c:param name="category" value="class" />
								</c:url>">+</a>
							</c:if>
						</div>
					</div>
					<div class="schedule">
						<div class="schedule_title">일정</div>
						<div class="schedule_content">
							<ul>
								<c:forEach var="schedule" items="${specialSchedules}">
									<c:if test="${schedule.type != 'class' && schedule.type != 'exam' && !fn:contains(schedule.title, '시험') && !fn:contains(schedule.title, '중간') && !fn:contains(schedule.title, '기말') && !fn:contains(schedule.title, '평가')}">
										<li class="detail-list-row">
											<c:url var="eventDetailUrl" value="/study/itemDetail">
												<c:param name="groupId" value="${studyInfo.studyGroupId}" />
												<c:param name="selectedDate" value="${selectedDate}" />
												<c:param name="type" value="event" />
												<c:param name="id" value="${schedule.scheduleId}" />
											</c:url>
											<a class="detail-item schedule-item" href="${eventDetailUrl}">
												<span class="item-title">
													<c:choose>
														<c:when test="${not empty schedule.title}"><c:out value="${schedule.title}" /></c:when>
														<c:otherwise>일정</c:otherwise>
													</c:choose>
												</span>
												<span class="item-meta">${schedule.startDate}<c:if test="${not empty schedule.startTime || not empty schedule.endTime}"> · ${schedule.startTime}~${schedule.endTime}</c:if></span>
											</a>
										</li>
									</c:if>
								</c:forEach>
							</ul>
							<c:if test="${isLeader}">
								<a class="plus_button" href="<c:url value='/study/addSchedule'>
									<c:param name="groupId" value="${studyInfo.studyGroupId}" />
									<c:param name="selectedDate" value="${selectedDate}" />
									<c:param name="category" value="event" />
								</c:url>">+</a>
							</c:if>
						</div>
					</div>
					<div class="exam">
						<div class="exam_title">시험</div>
						<div class="exam_content">
							<ul>
								<c:forEach var="schedule" items="${specialSchedules}">
									<c:if test="${schedule.type == 'exam' || fn:contains(schedule.title, '시험') || fn:contains(schedule.title, '중간') || fn:contains(schedule.title, '기말') || fn:contains(schedule.title, '평가')}">
										<li class="detail-list-row">
											<c:url var="examDetailUrl" value="/study/itemDetail">
												<c:param name="groupId" value="${studyInfo.studyGroupId}" />
												<c:param name="selectedDate" value="${selectedDate}" />
												<c:param name="type" value="exam" />
												<c:param name="id" value="${schedule.scheduleId}" />
											</c:url>
											<a class="detail-item schedule-item" href="${examDetailUrl}">
												<span class="item-title">
													<c:choose>
														<c:when test="${not empty schedule.title}"><c:out value="${schedule.title}" /></c:when>
														<c:otherwise>시험</c:otherwise>
													</c:choose>
												</span>
												<span class="item-meta">${schedule.startDate}<c:if test="${not empty schedule.startTime || not empty schedule.endTime}"> · ${schedule.startTime}~${schedule.endTime}</c:if></span>
											</a>
										</li>
									</c:if>
								</c:forEach>
							</ul>
							<c:if test="${isLeader}">
								<a class="plus_button" href="<c:url value='/study/addSchedule'>
									<c:param name="groupId" value="${studyInfo.studyGroupId}" />
									<c:param name="selectedDate" value="${selectedDate}" />
									<c:param name="category" value="exam" />
								</c:url>">+</a>
							</c:if>
						</div>
					</div>
					<div class="assignment">
						<div class="assignment_title">과제</div>
						<div class="assignment_content">
							<ul>
								<c:forEach var="assignment" items="${selectedAssignmentList}">
									<li class="detail-list-row">
										<c:url var="selectedAssignmentDetailUrl" value="/study/itemDetail">
											<c:param name="groupId" value="${studyInfo.studyGroupId}" />
											<c:param name="selectedDate" value="${selectedDate}" />
											<c:param name="type" value="assignment" />
											<c:param name="id" value="${assignment.id}" />
										</c:url>
										<a class="detail-item assignment-item" href="${selectedAssignmentDetailUrl}">
											<span class="item-title"><c:out value="${assignment.title}" /></span>
											<span class="item-meta">${assignment.createat}<c:if test="${not empty assignment.startTime}"> ${assignment.startTime}</c:if> ~ ${assignment.dueDate}<c:if test="${not empty assignment.dueTime}"> ${assignment.dueTime}</c:if></span>
										</a>
									</li>
								</c:forEach>
							</ul>
							<c:if test="${isLeader}">
								<a class="plus_button" href="<c:url value='/study/addAssignment'>
									<c:param name="groupId" value="${studyInfo.studyGroupId}" />
									<c:param name="selectedDate" value="${selectedDate}" />
								</c:url>">+</a>
							</c:if>
						</div>
					</div>
						</div>
					</div>
			</div>

	</div>
   </div>
</body>
</html>
