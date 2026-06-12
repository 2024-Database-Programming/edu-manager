<%@page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>EduManager</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/navigation.css" />
<style>
.member-page {
	width: min(720px, calc(100% - 80px));
	margin: 48px auto;
}
.member-title {
	font-size: 26px;
	font-weight: 700;
	margin-bottom: 24px;
}
.member-card {
	background: #fff;
	border: 1px solid #e5e7eb;
	border-radius: 8px;
	padding: 28px;
}
.member-row {
	display: grid;
	grid-template-columns: 120px 1fr;
	gap: 16px;
	padding: 12px 0;
	border-bottom: 1px solid #f1f3f5;
}
.member-row:last-child {
	border-bottom: 0;
}
.member-label {
	color: #6b7280;
	font-weight: 700;
}
.member-actions {
	display: flex;
	gap: 10px;
	margin-top: 24px;
}
.member-button {
	display: inline-flex;
	align-items: center;
	justify-content: center;
	min-width: 92px;
	height: 40px;
	border-radius: 6px;
	border: 0;
	background: #1E2A7C;
	color: #fff;
	font-weight: 700;
	text-decoration: none;
}
.member-button.secondary {
	background: #E5E5EA;
	color: #28292a;
}
.member-button.danger {
	background: #DB1D1D;
}
.member-alert {
	margin-bottom: 16px;
	color: #DB1D1D;
	font-weight: 700;
}
</style>
</head>
<body>
	<div class="page">
		<jsp:include page="../navigation/navigation.jsp" />
		<main class="member-page">
			<h1 class="member-title">회원 상세</h1>
			<c:if test="${deleteFailed || updateFailed}">
				<div class="member-alert">${exception.message}</div>
			</c:if>
			<section class="member-card">
				<div class="member-row">
					<div class="member-label">아이디</div>
					<div>${member.id}</div>
				</div>
				<div class="member-row">
					<div class="member-label">이름</div>
					<div>${member.name}</div>
				</div>
				<div class="member-row">
					<div class="member-label">이메일</div>
					<div>${member.email}</div>
				</div>
				<div class="member-row">
					<div class="member-label">전화번호</div>
					<div>${member.phone}</div>
				</div>
				<div class="member-actions">
					<c:if test="${curUserId == member.id || curUserId == 'admin'}">
						<a class="member-button" href="<c:url value='/member/update'><c:param name='id' value='${member.id}' /></c:url>">수정</a>
						<a class="member-button danger" href="<c:url value='/member/delete'><c:param name='id' value='${member.id}' /></c:url>">삭제</a>
					</c:if>
					<a class="member-button secondary" href="<c:url value='/member/list' />">목록</a>
				</div>
			</section>
		</main>
	</div>
</body>
</html>
