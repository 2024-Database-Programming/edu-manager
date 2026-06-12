<%@page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>과제 추가하기</title>
<link rel="stylesheet" href="<c:url value='/css/addSchedule.css' />" />
</head>
<body>
	<div class="main-container">
		<div style="width: 90%; justify-self: center;"><jsp:include
				page="../navigation/navigation.jsp" /></div>
		<form action="<c:url value='/lecture/addAssignment' />" method="POST">
			<span class="title">과제 추가하기</span>
			<div class="form">
				<!-- 과제 명 -->
				<div class="task-name-input">
					<label for="title" class="assignment-name">제목</label> <input
						type="text" name="title" placeholder="과제명을 입력해주세요" class="input"
						required />
				</div>
				<!-- 끝나는 시간 -->
				<div class="time-input">
					<label for="dueDate" class="time-label">마감일</label> <input
						type="date" id="dueDate" name="dueDate" value="${startDate}" class="time-selector"
						required readonly/>
				</div>
				<!-- 세부 내용 -->
				<div class="detail-input">
					<label for="description" class="detail-text">세부 내용</label>
					<textarea id="description" name="description" placeholder="세부 내용을 입력하세요."
						class="input-field-1" required></textarea>
				</div>
			</div>

			<!-- 제출 버튼 -->
			<input type="hidden" name="lectureId" id="lectureId"
					value="${lectureId}">
					<input type=hidden name="startDate" id="startDate"
					value="${startDate}">
			<div class="button-container">
				<button type="submit" class="complete-button">완료</button>
			</div>
		</form>
	</div>
</body>
</html>
