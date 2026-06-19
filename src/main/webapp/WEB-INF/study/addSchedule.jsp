<%@page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>일정 추가하기</title>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/theme.css'/>?v=20260613-ds4">
<link rel="stylesheet" href="<c:url value='/css/addSchedule.css' />?v=20260620-wide2" />
</head>
<body>
	<div class="main-container">
		<div style="width: 90%; justify-self: center;"><jsp:include
				page="../navigation/navigation.jsp" /></div>
		<form action="<c:url value='/study/addSchedule' />" method="POST" enctype="multipart/form-data">
			<span class="title">일정 추가하기</span>
			<div class="form">
				<!-- 일정 명 -->
				<div class="task-name-input">
					<label for="schedule-name" class="assignment-name">제목</label> <input
						type="text" id="schedule-name" name="title"
						placeholder="제목을 입력하세요" class="input" required />
				</div>
				<div class="deadline-input">
					<label for="category" class="deadline">카테고리</label>
					<select id="category" name="category" class="time-selector" required>
						<option value="event" ${category == 'event' ? 'selected' : ''}>일정</option>
						<option value="class" ${category == 'class' ? 'selected' : ''}>수업</option>
						<option value="exam" ${category == 'exam' ? 'selected' : ''}>시험</option>
					</select>
				</div>
				<!-- 일정 날짜 -->
				<div class="deadline-input">
					<label for="schedule-date" class="deadline">일정 날짜</label> <input
						type="date" id="schedule-date" name="startDate" value="${startDate}"
						class="input-field" required />
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
				<div class="detail-input">
					<label for="description" class="detail-text">상세 내용</label>
					<textarea id="description" name="description" placeholder="장소, 준비물, 시험 범위 등을 입력하세요."
						class="input-field-1"></textarea>
				</div>
				<div class="file-input">
					<label for="attachments" class="detail-text">첨부파일 / 이미지</label>
					<input type="file" id="attachments" name="attachments" class="file-selector" multiple />
				</div>
			</div>
			<!-- 제출 버튼 -->
			<input type="hidden" name="groupId" id="groupId"
					value="${groupId}">
			<div class="button-container">
				<button type="submit" class="complete-button">완료</button>
			</div>
		</form>
	</div>
</body>
</html>
