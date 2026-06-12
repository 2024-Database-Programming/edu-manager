<%@page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="model.domain.Schedule" %>
<%@ page import="java.util.*" %>
<%@ page import="java.util.stream.Collectors" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/study_make.css" />
<title>EduManager</title>
<script src="${pageContext.request.contextPath}/js/updateLecture.js"></script>
<script>
	// 서버에서 전달된 category 값을 전역 변수로 정의
	const selectedCategory = "${study.category}";
</script>
<!-- 수정해야하는 부분 -->
</head>
<body>
	<div class="page">
		<jsp:include page="../navigation/navigation.jsp" />
		<div id="makeStudy_container">
			<form id="makeStudy_form" method="post"
				action="${pageContext.request.contextPath}/study/update">
				<div class="subTitle">스터디 그룹 정보 수정하기</div>
				<hr style="margin: 20px 0px">
				<section class="study">
					<span>스터디명</span><span class="required">*</span><br /> <input
						type="text" name="name" value="${study.name}" required />
				</section>

				<!-- 스터디 이미지 (임시 값 추가) -->
				<input type="hidden" name="img" value="${study.img}" />

				<!-- 스터디 Id-->
				<input type="hidden" name="studyId" value="${study.studyGroupId}" />

				<section class="study">
					<span>스터디 소개</span><br /> <input type="text" name="description"
						value="${study.description}" />
				</section>
				<div style="display: flex;">
					<section class="study" style="width: 20%;">
						<span>스터디 장소</span><br /> <input type="text" name="place" value="${study.place }"/>
					</section>
					<section class="study" style="width: 20%;">
						<span>모집인원</span><span class="required">*</span> <br /> <input
							type="number" name="capacity" required min="1" max="99" value="${study.capacity}"/>
					</section>
				</div>
				<section class="study">
					<span>카테고리</span><br />
					<c:forEach var="category" items="${categories}">
						<label
							class="category ${category.id == study.category ? 'selected-category' : ''}"
							onclick="updateCategoryStyle(this)"> <input type="radio"
							name="category" value="${category.id}"
							<c:if test="${category.id == study.category}">checked</c:if> />
							${category.name}
						</label>
					</c:forEach>

				</section>

				<!-- 스타일은 추후에 만질예정-->

				<section id="schedule" class="study" style="display: inline-block">
               <span>정기 모임 요일</span><span class="required">*</span><br />
               <article class="schedule">
                  <%
                  // 서버에서 전달된 scheduleList 가져오기
                  List<Schedule> scheduleList = (List<Schedule>) request.getAttribute("scheduleList");
                  if (scheduleList == null) {
                     scheduleList = new ArrayList<>();
                  }

                  // 요일 리스트 추출
                  Set<String> selectedDays = scheduleList.stream().map(Schedule::getDayOfWeek) // Schedule 객체의 dayOfWeek 필드 추출
                        .collect(Collectors.toSet());
                  %>

                  <label> <input type="checkbox" name="dayOfWeek"
                     value="MONDAY"
                     <%= selectedDays.contains("MONDAY") ? "checked" : "" %>>
                     월
                  </label> <label> <input type="checkbox" name="dayOfWeek"
                     value="TUESDAY"
                     <%= selectedDays.contains("TUESDAY") ? "checked" : "" %>>
                     화
                  </label> <label> <input type="checkbox" name="dayOfWeek"
                     value="WEDNESDAY"
                     <%= selectedDays.contains("WEDNESDAY") ? "checked" : "" %>>
                     수
                  </label> <label> <input type="checkbox" name="dayOfWeek"
                     value="THURSDAY"
                     <%= selectedDays.contains("THURSDAY") ? "checked" : "" %>>
                     목
                  </label> <label> <input type="checkbox" name="dayOfWeek"
                     value="FRIDAY"
                     <%= selectedDays.contains("FRIDAY") ? "checked" : "" %>>
                     금
                  </label> <label> <input type="checkbox" name="dayOfWeek"
                     value="SATURDAY"
                     <%= selectedDays.contains("SATURDAY") ? "checked" : "" %>>
                     토
                  </label> <label> <input type="checkbox" name="dayOfWeek"
                     value="SUNDAY"
                     <%= selectedDays.contains("SUNDAY") ? "checked" : "" %>>
                     일
                  </label>
               </article>
            </section>

				<section>

					<button id="submit">스터디 그룹 정보 수정하기</button>
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
