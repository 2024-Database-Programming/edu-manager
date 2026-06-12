<%@page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/study_make.css" />
<title>EduManager</title>
<script src="${pageContext.request.contextPath}/js/study_make.js"></script>
<!-- 수정해야하는 부분 -->
</head>
<% 
    // 세션에서 creationFailed와 exception 값을 가져옵니다.
    Boolean creationFailed = (Boolean) session.getAttribute("creationFailed");
    if (creationFailed != null && creationFailed) {
%>
    <script type="text/javascript">
        alert("다른 강의 일정이 존재합니다.");
    </script>
<% 
        // 세션에서 값 삭제 (한 번만 표시하기 위해)
        session.removeAttribute("creationFailed");
        session.removeAttribute("exception");
    }
%>

<body>
	<div class="page">
		<jsp:include page="../navigation/navigation.jsp" />
		<div id="makeStudy_container">
			<form id="makeStudy_form" method="post"
				action="${pageContext.request.contextPath}/lecture/create">
				<!-- 나중에 다른 uri로 바꿔 줄것임. -->
				<div class="subTitle">강의 만들기</div>
				<hr style="margin: 20px 0px">
				<section class="study">
					<span>강의명</span><span class="required">*</span><br /> <input
						type="text" name="name" required />
				</section>

				<!-- 강의 이미지 (임시 값 추가) -->
				<input type="hidden" name="img" />

				<!-- 강의 Id-->
				<input type="hidden" name="lectureId" />

				<section class="study">
					<span>강의 소개</span><br /> <input type="text" name="description" />
				</section>
				<div style="display: flex;">
					<section class="study" style="width: 20%;">
						<span>강사명</span><br /> <input type="text" name="teacherName"
							value="${teacherName}" readonly />
					</section>
					<section class="study" style="width: 20%;">
						<span>강의실</span><br /> <input type="text" name="lectureRoom" />
					</section>
					<section class="study" style="width: 20%;">
						<span>모집인원</span><span class="required">*</span> <br /> <input
							type="number" name="capacity" required min="1" max="99" />
					</section>

					<section class="study" style="width: 20%;">
						<span>난이도</span><br /> <select class="small" name="level"
							style="width: 100%;">
							<option value="1">초급</option>
							<option value="2">중급</option>
							<option value="3">고급</option>
						</select>
					</section>
				</div>

				<section class="study">
					<span>카테고리</span><br />
					<c:forEach var="category" items="${categories}">
						<label class="category"> <input type="radio" name="category"
							value="${category.id}" onclick="updateCategoryStyle(this)" />
							${category.name}
						</label>
					</c:forEach>
				
				</section>

				<!-- 정기 수업 일정은 일단 요청 따로 처리 -->
				<section id="schedule" class="study" style="display: inline-block">
					<span>정기 수업 일정</span><span class="required">*</span><br />
					<article class="schedule">
						<span>요일</span> <select class="small" name="schedule[0][day]" required>
							<option value="MONDAY">월</option>
							<option value="TUESDAY">화</option>
							<option value="WEDNESDAY">수</option>
							<option value="THURSDAY">목</option>
							<option value="FRIDAY">금</option>
							<option value="SATURDAY">토</option>
							<option value="SUNDAY">일</option>
						</select> <span>시간</span> <input type="time" name="schedule[0][startTime]"/> ~ <input type="time" name="schedule[0][endTime]"/>
						<button type="button" class="delete_btn"
							onClick="deleteSchedule(this)">삭제</button>
					</article>
					<button type="button" id="plus_btn" onClick="addSchedule()">+</button>
				</section>


				<section>
					<button id="submit">강의 만들기</button>
				</section>
				<input id="scheduleCountInput" type="hidden" value="1" name="scheduleCount">
				<!-- 일정의 개수 -->
			</form>
		</div>
	</div>
</body>
<script>
function updateCategoryStyle(selectedInput) {
    // '카테고리' 라벨 그룹 내에서만 선택된 클래스 제거
    const categorySection = selectedInput.closest('.study');
    const labels = categorySection.querySelectorAll('.category');
    labels.forEach(label => {
        label.classList.remove('selected-category');
    });

    // 선택된 라벨에만 클래스 추가
    const label = selectedInput.closest('label');
    label.classList.add('selected-category');
}
</script>
</html>
