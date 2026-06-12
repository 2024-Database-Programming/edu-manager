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
<link rel="stylesheet" href="<c:url value='/css/study_details.css' />?v=20260613-ds4"
   type="text/css">
<script src="<c:url value='/js/study_calendar.js' />"></script>
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
         <div class="team-members-box">
            <div class="team-header">
               <img src="<c:url value='/images/members.png' />" alt="members" 
               style="height: 10px; margin-right: 5px"/>
               <span class="team-count">스터디원 (${fn:length(members) + 1}/${studyInfo.capacity})</span>
            </div>
            <ul class="member-list">
               <li class="member-item">
                  <span class="member-name">${studyInfo.leaderName}</span>
                  <img src="<c:url value='/images/mdi_crown.png' />" alt="leader" 
               style="height: 15px; margin-left: 5px"/> 
               </li>
               <c:forEach var="member" items="${members}">
                  <li class="member-item">
                     <span class="member-name">${member}</span>
                  </li>
               </c:forEach>
            </ul>
         </div>
      </div>


		<div class="mainInform">
			<div class="title">${studyInfo.name}</div>
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
			<form id="dateForm" action="<c:url value='/mystudy/view' />"
				method="post">
				<input type="hidden" name="selectedDate" id="selectedDate"
					value="${selectedDate}">
					<input type="hidden" name="groupId" id="groupId"
					value="${studyInfo.studyGroupId}">

			</form>
			<div class="main-container">
				<div class="rectangle-1">
					<div class="schedule">
						<div class="schedule_title">일정</div>
						<div class="schedule_content">
						<ul>
							<c:forEach var="schedule" items="${regularSchedules}">
								<li class="schedule-item">${schedule.title}
									<%-- ${schedule.startTime}~ ${schedule.endTime}  --%><!-- 다른 schedule 속성들도 필요에 따라 추가 -->
								</li>
							</c:forEach>
							<c:forEach var="schedule" items="${specialSchedules}">
								<li class="schedule-item">${schedule.title}
									<%-- ${schedule.startTime}~ ${schedule.endTime}  --%><!-- 다른 schedule 속성들도 필요에 따라 추가 -->
								</li>
							</c:forEach>
						</ul>
							
								<c:if test="${isLeader}">
									<a class="plus_button" href="<c:url value='/study/addSchedule'>
										<c:param name="groupId" value="${studyInfo.studyGroupId}" />
										<c:param name="selectedDate" value="${selectedDate}" />
									</c:url>"> + </a>
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
								<c:if test="${isLeader}">
									<a class="plus_button" href="<c:url value='/study/addNotice'>
										<c:param name="groupId" value="${studyInfo.studyGroupId}" />
										<c:param name="selectedDate" value="${selectedDate}" />
									</c:url>"> + </a>
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
							
								<c:if test="${isLeader}">
									<a class="plus_button" href="<c:url value='/study/addAssignment'>
											<c:param name="groupId" value="${studyInfo.studyGroupId}" />
											<c:param name="selectedDate" value="${selectedDate}" />
										</c:url>"> + </a>
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
							href="<c:url value='/study/listNotice'/>?groupId=${studyInfo.studyGroupId}"
							class="more-link">더보기</a>
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
							href="<c:url value='/study/listAssignment'/>?groupId=${studyInfo.studyGroupId}"
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
   </div>
</body>
</html>
