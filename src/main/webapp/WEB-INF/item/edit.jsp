<%@page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>${itemLabel} 수정</title>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/theme.css'/>?v=20260621-toss1">
<link rel="stylesheet" href="<c:url value='/css/item_edit.css' />?v=20260616-edit1" type="text/css">
</head>
<body>
	<div class="page">
		<jsp:include page="../navigation/navigation.jsp" />
		<main class="edit-page">
			<div class="edit-toolbar">
				<a class="back-link" href="${backUrl}">상세 화면으로 돌아가기</a>
			</div>
			<section class="edit-panel">
				<c:if test="${not empty sessionScope.flashError}">
					<div class="edit-alert">${sessionScope.flashError}</div>
					<c:remove var="flashError" scope="session" />
				</c:if>
				<div class="edit-header">
					<span>${ownerType} / ${itemLabel}</span>
					<h1>${itemLabel} 수정</h1>
					<p><c:out value="${ownerTitle}" /></p>
				</div>
				<form action="${formAction}" method="POST" enctype="multipart/form-data" class="edit-form">
					<input type="hidden" name="type" value="${itemKind}" />
					<input type="hidden" name="id" value="${itemId}" />
					<input type="hidden" name="selectedDate" value="${selectedDate}" />

					<div class="form-row">
						<label for="title">제목</label>
						<input type="text" id="title" name="title" value="${fn:escapeXml(titleValue)}" required />
					</div>

					<c:if test="${itemKind == 'assignment'}">
						<div class="form-grid">
							<div class="form-row">
								<label for="startDate">시작일</label>
								<input type="date" id="startDate" name="startDate" value="${startDateValue}" required />
							</div>
							<div class="form-row">
								<label for="startTime">시작시간</label>
								<input type="time" id="startTime" name="startTime" value="${startTimeValue}" required />
							</div>
							<div class="form-row">
								<label for="dueDate">마감일</label>
								<input type="date" id="dueDate" name="dueDate" value="${dueDateValue}" required />
							</div>
							<div class="form-row">
								<label for="dueTime">마감시간</label>
								<input type="time" id="dueTime" name="dueTime" value="${dueTimeValue}" required />
							</div>
						</div>
					</c:if>

					<c:if test="${itemKind == 'schedule'}">
						<div class="form-row">
							<label for="category">카테고리</label>
							<select id="category" name="category" required>
								<option value="regular" ${categoryValue == 'regular' ? 'selected' : ''}>정기 수업</option>
								<option value="class" ${categoryValue == 'class' ? 'selected' : ''}>단기 수업</option>
								<option value="event" ${categoryValue == 'event' ? 'selected' : ''}>일정</option>
								<option value="exam" ${categoryValue == 'exam' ? 'selected' : ''}>시험</option>
							</select>
						</div>
						<div class="form-grid">
							<div class="form-row">
								<label for="startDate">일정 날짜</label>
								<input type="date" id="startDate" name="startDate" value="${startDateValue}" required />
							</div>
							<div class="form-row">
								<label for="startTime">시작</label>
								<input type="time" id="startTime" name="startTime" value="${startTimeValue}" required />
							</div>
							<div class="form-row">
								<label for="endTime">종료</label>
								<input type="time" id="endTime" name="endTime" value="${dueTimeValue}" required />
							</div>
						</div>
					</c:if>

					<div class="form-row">
						<label for="description">상세 내용</label>
						<textarea id="description" name="description" required><c:out value="${descriptionValue}" /></textarea>
					</div>

					<div class="form-row">
						<label for="attachments">첨부파일 추가</label>
						<input type="file" id="attachments" name="attachments" class="file-input" multiple />
					</div>

					<div class="form-actions">
						<a href="${backUrl}">취소</a>
						<button type="submit">수정 완료</button>
					</div>
				</form>
			</section>
		</main>
	</div>
</body>
</html>
