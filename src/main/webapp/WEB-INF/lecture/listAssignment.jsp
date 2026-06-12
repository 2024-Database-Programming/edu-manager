<%@page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>공지사항 리스트</title>
<link rel=stylesheet href="<c:url value='/css/listAssignmentAndNotice.css' />"
	type="text/css">
</head>
<body>
	<div class="page">
		<jsp:include page="../navigation/navigation.jsp" />
		<div class="subTitle">과제함</div>
 
		<table class="table">
			<thead>
				<tr>
					<th class="table-header">제목</th>
					<th class="table-header">내용</th>
					<th class="table-header">마감일</th>
				</tr>
			</thead>
			<tbody>
				<!-- JSTL 반복문으로 리스트 출력 -->
				<c:forEach var="assignment" items="${lectureAssignmentList}">
					<tr class="notice-row">
						<td class="title">${assignment.title}</td>
						<td class="description">${assignment.description}</td>
						<td class="createat">
							<span id="dueDate-${assignment.id}"></span>
						</td>
					</tr>
					<script type="text/javascript">
						// JavaScript로 dueDate 처리
						var dueDateStr = "${assignment.dueDate}";
						var dueDate = new Date(dueDateStr); // dueDate를 Date 객체로 변환
						var today = new Date(); // 오늘 날짜

						// 남은 일수 계산
						var timeDiff = dueDate - today; // 밀리초 단위 차이
						var daysLeft = Math.ceil(timeDiff / (1000 * 3600 * 24)); // 남은 일수 (밀리초를 일수로 변환)

						// 디데이 표시
						var dueDateElement = document.getElementById('dueDate-${assignment.id}');
						if (daysLeft > 0) {
							dueDateElement.textContent = "D-" + daysLeft; // D-남은 일수
						} else if (daysLeft === 0) {
							dueDateElement.textContent = "D-Day"; // 오늘이 마감일
						} else {
							dueDateElement.textContent = "기한마감"; // 마감일이 지난 경우
						}
					</script>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>
