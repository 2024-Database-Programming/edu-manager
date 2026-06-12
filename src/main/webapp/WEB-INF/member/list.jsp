<%@page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>EduManager</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/navigation.css" />
<style>
.member-page {
	width: min(960px, calc(100% - 80px));
	margin: 48px auto;
}
.member-title {
	font-size: 26px;
	font-weight: 700;
	margin-bottom: 24px;
}
.member-table {
	width: 100%;
	border-collapse: collapse;
	background: #fff;
}
.member-table th,
.member-table td {
	padding: 14px 16px;
	border-bottom: 1px solid #e5e7eb;
	text-align: left;
}
.member-table th {
	color: #6b7280;
	font-size: 14px;
}
.member-link {
	color: #1E2A7C;
	font-weight: 700;
	text-decoration: none;
}
</style>
</head>
<body>
	<div class="page">
		<jsp:include page="../navigation/navigation.jsp" />
		<main class="member-page">
			<h1 class="member-title">회원 목록</h1>
			<table class="member-table">
				<thead>
					<tr>
						<th>아이디</th>
						<th>이름</th>
						<th>이메일</th>
						<th>전화번호</th>
						<th>상세</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="member" items="${memberList}">
						<tr>
							<td>${member.id}</td>
							<td>${member.name}</td>
							<td>${member.email}</td>
							<td>${member.phone}</td>
							<td><a class="member-link" href="<c:url value='/member/view'><c:param name='id' value='${member.id}' /></c:url>">보기</a></td>
						</tr>
					</c:forEach>
					<c:if test="${empty memberList}">
						<tr>
							<td colspan="5">등록된 회원이 없습니다.</td>
						</tr>
					</c:if>
				</tbody>
			</table>
		</main>
	</div>
</body>
</html>
