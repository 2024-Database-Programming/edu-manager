<%@page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>일정 추가하기</title>
<link rel="stylesheet" href="<c:url value='/css/addSchedule.css' />" />
</head>
<body>
	<div class="main-container">
		<div style="width: 90%; justify-self: center;"><jsp:include
				page="../navigation/navigation.jsp" /></div>
		<form action="<c:url value='/lecture/addSchedule' />" method="POST">
			<span class="title">일정 추가하기</span>
			<div class="form">
				<!-- 일정 명 -->
				<div class="task-name-input">
					<label for="schedule-name" class="assignment-name">제목</label> <input
						type="text" id="schedule-name" name="title"
						placeholder="제목을 입력하세요" class="input" required />
				</div>
				<!-- 일정 날짜 -->
				<div class="deadline-input">
					<label for="schedule-date" class="deadline">일정 날짜</label> <input
						type="date" id="schedule-date" name="startDate" value="${startDate}"
						class="input-field" readonly/>
				</div>
				<!-- 시작 시간 -->
				<div class="time-input">
					<label for="start-time" class="time-label">시작</label> <input
						type="time" id="start-time" name="startTime" class="time-selector"
						required />
				</div>
				<!-- 끝나는 시간 -->
				<div class="time-input">
					<label for="end-time" class="time-label">종료</label> <input
						type="time" id="end-time" name="endTime" class="time-selector"
						required />
				</div>
			</div>
			<!-- 제출 버튼 -->
			<input type="hidden" name="lectureId" id="lectureId"
					value="${lectureId}">
					<input type="hidden" name="startDate" id="startDate"
					value="${startDate}">
			<div class="button-container">
				<button type="submit" class="complete-button">완료</button>
			</div>
		</form>
	</div>
</body>
</html>