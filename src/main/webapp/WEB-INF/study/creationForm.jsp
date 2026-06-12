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
<body>
	<div class="page">
		<jsp:include page="../navigation/navigation.jsp" />
		<div id="makeStudy_container">
			<form id="makeStudy_form" method="post"
				action="${pageContext.request.contextPath}/study/create">
				<!-- 나중에 다른 uri로 바꿔 줄것임. -->
				<div class="subTitle">스터디 그룹 만들기</div>
				<hr style="margin: 20px 0px">
				<section class="study">
					<span>스터디명</span><span class="required">*</span><br /> <input
						type="text" name="name" required />
				</section>

				<!-- 강의 이미지 (임시 값 추가) -->
				<input type="hidden" name="img" />

				<!-- 강의 Id-->
				<input type="hidden" name="lectureId" />

				<section class="study">
					<span>스터디 소개</span><br /> <input type="text" name="description" />
				</section>
				<div style="display: flex;">
					<section class="study" style="width: 20%;">
						<span>스터디 장소</span><br /> <input type="text" name="place" />
					</section>
					<section class="study" style="width: 20%;">
						<span>모집인원</span><span class="required">*</span> <br /> <input
							type="number" name="capacity" required min="1" max="99" />
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

					<!-- 스타일은 추후에 만질예정-->
				<section id="schedule" class="study" style="display: inline-block">
					<span>정기 모임 요일</span><span class="required">*</span><br />
							<label class="category"> <input type="checkbox" name="dayOfWeek"
								value="MONDAY" onclick="updateDayStyle(this)"> 월
							</label > <label class="category"> <input type="checkbox" name="dayOfWeek"
								value="TUESDAY" onclick="updateDayStyle(this)"> 화
							</label> <label class="category"> <input type="checkbox" name="dayOfWeek"
								value="WEDNESDAY" onclick="updateDayStyle(this)"> 수
							</label> <label class="category"> <input type="checkbox" name="dayOfWeek"
								value="THURSDAY" onclick="updateDayStyle(this)"> 목
							</label> <label class="category"> <input type="checkbox" name="dayOfWeek"
								value="FRIDAY" onclick="updateDayStyle(this)"> 금
							</label> <label class="category"> <input type="checkbox" name="dayOfWeek"
								value="SATURDAY" onclick="updateDayStyle(this)"> 토
							</label> <label class="category"> <input type="checkbox" name="dayOfWeek"
								value="SUNDAY" onclick="updateDayStyle(this)"> 일
							</label>

				</section>
				<section>
					<button id="submit">스터디 그룹 만들기</button>
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


function updateDayStyle(selectedInput) {
    const label = selectedInput.closest('label');

    // 체크박스가 선택된 상태라면 클래스 추가, 아니면 제거
    if (selectedInput.checked) {
        label.classList.add('selected-category');
    } else {
        label.classList.remove('selected-category');
    }
}

</script>
</html>