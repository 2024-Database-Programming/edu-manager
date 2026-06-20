<%@page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>EduManager</title>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/theme.css'/>?v=20260613-ds4">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/navigation.css?v=20260613-ds4" />
<style>
.member-page {
	width: min(720px, calc(100% - 2 * var(--gutter)));
	margin: 8px auto 64px;
}
.member-title {
	color: var(--ink);
	font-size: 22px;
	font-weight: 800;
	letter-spacing: -0.01em;
	margin: 8px 0 18px;
}
.member-card {
	background: var(--surface);
	border: 1px solid var(--line);
	border-radius: var(--r-lg);
	box-shadow: var(--shadow);
	padding: 28px;
}
.member-row {
	display: grid;
	grid-template-columns: 120px 1fr;
	gap: 16px;
	padding: 14px 0;
	border-bottom: 1px solid var(--line);
	color: var(--ink);
	font-weight: 600;
}
.member-row:last-child {
	border-bottom: 0;
}
.member-label {
	color: var(--muted);
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
	height: 44px;
	padding: 0 18px;
	border-radius: var(--r-sm);
	border: 1px solid transparent;
	background: var(--brand);
	color: var(--on-brand);
	font-weight: 700;
	text-decoration: none;
	box-shadow: var(--shadow-brand);
	transition: background-color 0.18s ease;
}
.member-button:hover {
	background: var(--brand-strong);
}
.member-button.secondary {
	background: var(--surface);
	border-color: var(--line-strong);
	color: var(--ink-2);
	box-shadow: none;
}
.member-button.secondary:hover {
	background: var(--surface-3);
	border-color: var(--brand-line);
	color: var(--brand);
}
.member-button.danger {
	background: var(--danger);
	box-shadow: none;
}
.member-button.danger:hover {
	background: #b91c1c;
}
.member-alert {
	margin-bottom: 16px;
	padding: 12px 16px;
	border-radius: var(--r-sm);
	background: var(--danger-bg);
	color: var(--danger);
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
					<div><c:out value="${member.name}"/></div>
				</div>
				<div class="member-row">
					<div class="member-label">이메일</div>
					<div><c:out value="${member.email}"/></div>
				</div>
				<div class="member-row">
					<div class="member-label">전화번호</div>
					<div><c:out value="${member.phone}"/></div>
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
