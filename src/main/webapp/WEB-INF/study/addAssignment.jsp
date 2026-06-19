<%@page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>과제 추가하기</title>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/theme.css'/>?v=20260613-ds4">
<link rel="stylesheet" href="<c:url value='/css/addSchedule.css' />?v=20260620-wide2" />
</head>
<body>
	<div class="main-container">
		<div style="width: 90%; justify-self: center;"><jsp:include
				page="../navigation/navigation.jsp" /></div>
		<form action="<c:url value='/study/addAssignment' />" method="POST" enctype="multipart/form-data">
			<span class="title">과제 추가하기</span>
			<div class="form">
				<!-- 과제 명 -->
				<div class="task-name-input">
					<label for="title" class="assignment-name">제목</label> <input
						type="text" name="title" placeholder="과제명을 입력해주세요" class="input"
						required />
				</div>
				<div class="date-time-grid">
					<div class="time-input">
						<label for="startDate" class="time-label">시작일</label>
						<input type="date" id="startDate" name="startDate" value="${startDate}"
							class="time-selector" required />
					</div>
					<div class="time-input">
						<label for="startTime" class="time-label">시작시간</label>
						<input type="time" id="startTime" name="startTime" class="time-selector" required />
					</div>
					<div class="time-input">
						<label for="dueDate" class="time-label">마감일</label>
						<input type="date" id="dueDate" name="dueDate" value="${startDate}"
							class="time-selector" required />
					</div>
					<div class="time-input">
						<label for="dueTime" class="time-label">마감시간</label>
						<input type="time" id="dueTime" name="dueTime" class="time-selector" required />
					</div>
				</div>
				<!-- 세부 내용 -->
				<div class="detail-input">
					<label for="description" class="detail-text">세부 내용</label>
					<textarea id="description" name="description" placeholder="세부 내용을 입력하세요."
						class="input-field-1" required></textarea>
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
