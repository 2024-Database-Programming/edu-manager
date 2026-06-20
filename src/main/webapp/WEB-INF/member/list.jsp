<%@page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>EduManager</title>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/theme.css'/>?v=20260621-toss1">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/navigation.css?v=20260621-toss1" />
<style>
.member-page {
	width: min(960px, calc(100% - 2 * var(--gutter)));
	margin: 8px auto 64px;
}
.member-title {
	color: var(--ink);
	font-size: 22px;
	font-weight: 800;
	letter-spacing: -0.01em;
	margin: 8px 0 18px;
}
.member-table {
	width: 100%;
	border-collapse: separate;
	border-spacing: 0;
	overflow: hidden;
	background: var(--surface);
	border: 1px solid var(--line);
	border-radius: var(--r-lg);
	box-shadow: var(--shadow);
}
.member-table th,
.member-table td {
	padding: 14px 16px;
	border-bottom: 1px solid var(--line);
	text-align: left;
}
.member-table th {
	background: var(--surface-3);
	color: var(--muted);
	font-size: 13px;
	font-weight: 800;
}
.member-table tbody tr:last-child td {
	border-bottom: 0;
}
.member-table tbody tr:hover {
	background: var(--surface-2);
}
.member-link {
	color: var(--brand);
	font-weight: 700;
	text-decoration: none;
}
.member-link:hover {
	text-decoration: underline;
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
							<td><c:out value="${member.name}"/></td>
							<td><c:out value="${member.email}"/></td>
							<td><c:out value="${member.phone}"/></td>
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
