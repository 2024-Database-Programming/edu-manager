<%@page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/study_make.css" />
<title>EduManager</title>
<script src="${pageContext.request.contextPath}/js/updateLecture.js"></script>
<script>
	// 서버에서 전달된 category 값을 전역 변수로 정의
	const selectedCategory = "${lecture.category}";
</script>
<!-- 수정해야하는 부분 -->
</head>
<body>
	<div class="page">
		<jsp:include page="../navigation/navigation.jsp" />
		<div id="makeStudy_container">
			<form id="makeStudy_form" method="post"
				action="${pageContext.request.contextPath}/lecture/update">
				<!-- 나중에 다른 uri로 바꿔 줄것임. -->
				<div class="subTitle">강의 정보 수정하기</div>
				<hr style="margin: 20px 0px">
				<section class="study">
					<span>강의명</span><span class="required">*</span><br /> <input
						type="text" name="name" value="${lecture.name}" required />
				</section>

				<!-- 강의 이미지 (임시 값 추가) -->
				<input type="hidden" name="img" value="${lecture.img}" />

				<!-- 강의 Id-->
				<input type="hidden" name="lectureId" value="${lecture.lectureId}" />

				<section class="study">
					<span>강의 소개</span><br /> <input type="text" name="description"
						value="${lecture.description}" />
				</section>
				<div style="display: flex;">
					<section class="study" style="width: 20%;">
						<span>강사명</span><br /> <input type="text" name="teacherName"
							value="${lecture.teacherName}" readonly />
					</section>
					<section class="study" style="width: 20%;">
						<span>강의실</span><br /> <input type="text" name="lectureRoom"
							value="${lecture.lectureRoom}" />
					</section>
					<section class="study" style="width: 20%;">
						<span>모집인원</span><span class="required">*</span> <br /> <input
							type="number" name="capacity" required min="1" max="99"
							value="${lecture.capacity}" />
					</section>

					<section class="study" style="width: 20%;">
						<span>난이도</span><br /> <select class="small" name="level"
							style="width: 100%;">
							<option value="1" ${lecture.level == 1 ? 'selected' : ''}>초급</option>
							<option value="2" ${lecture.level == 2 ? 'selected' : ''}>중급</option>
							<option value="3" ${lecture.level == 3 ? 'selected' : ''}>고급</option>
						</select>
					</section>
				</div>
				<section class="study">
					<span>카테고리</span><br />
					<c:forEach var="category" items="${categories}">
						<label
							class="category ${category.id == lecture.category ? 'selected-category' : ''}"
							onclick="updateCategoryStyle(this)"> <input type="radio"
							name="category" value="${category.id}"
							<c:if test="${category.id == lecture.category}">checked</c:if> />
							${category.name}
						</label>
					</c:forEach>

				</section>
				<!-- 정기 수업 일정은 일단 요청 따로 처리 -->
				<section id="schedule" class="study" style="display: inline-block">
					<span>정기 수업 일정</span><span class="required">*</span><br />
					<c:forEach var="schedule" items="${scheduleList}"
						varStatus="status">
						<article class="schedule">
							<span>요일</span> <select class="small"
								name="schedule[${status.index}][day]" required>
								<option value="MONDAY" ${schedule.dayOfWeek == 'MONDAY' ? 'selected' : ''}>월</option>
								<option value="TUESDAY" ${schedule.dayOfWeek == 'TUESDAY' ? 'selected' : ''}>화</option>
								<option value="WEDNESDAY" ${schedule.dayOfWeek == 'WEDNESDAY' ? 'selected' : ''}>수</option>
								<option value="THURSDAY" ${schedule.dayOfWeek == 'THURSDAY' ? 'selected' : ''}>목</option>
								<option value="FRIDAY" ${schedule.dayOfWeek == 'FRIDAY' ? 'selected' : ''}>금</option>
								<option value="SATURDAY" ${schedule.dayOfWeek == 'SATURDAY' ? 'selected' : ''}>토</option>
								<option value="SUNDAY" ${schedule.dayOfWeek == 'SUNDAY' ? 'selected' : ''}>일</option>
							</select> <span>시간</span> <input type="time"
								name="schedule[${status.index}][startTime]"
								value="${schedule.startTime}" required/> ~ <input type="time"
								name="schedule[${status.index}][endTime]"
								value="${schedule.endTime}" required />
							<button type="button" class="delete_btn"
								onClick="deleteSchedule(this)">삭제</button>
							<input id="scheduleId" type="hidden"
								value="${schedule.scheduleId}" name="schedule[${status.index}][scheduleId]">
						</article>
					</c:forEach>
					<button type="button" id="plus_btn" onClick="addSchedule()">+</button>
				</section>

				<section>
					<input id="scheduleCountInput" type="hidden"
						value="${scheduleCount}" name="scheduleCount">

					<button id="submit">강의 정보 수정하기</button>
				</section>
			</form>
		</div>
	</div>
</body>
<script>
function updateCategoryStyle(selectedLabel) {
    // 모든 라벨에서 선택된 클래스 제거
    const labels = document.querySelectorAll('.category');
    labels.forEach(label => {
        label.classList.remove('selected-category');
    });

    // 선택된 라벨에만 클래스 추가
    selectedLabel.classList.add('selected-category');
}
</script>
</html>
