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
	width: min(640px, calc(100% - 2 * var(--gutter)));
	margin: 8px auto 64px;
}
.member-title {
	color: var(--ink);
	font-size: 22px;
	font-weight: 800;
	letter-spacing: -0.01em;
	margin: 8px 0 18px;
}
.member-form {
	background: var(--surface);
	border: 1px solid var(--line);
	border-radius: var(--r-lg);
	box-shadow: var(--shadow);
	padding: 28px;
}
.member-field {
	margin-bottom: 18px;
}
.member-field label {
	display: block;
	margin-bottom: 8px;
	color: var(--ink-2);
	font-size: 13px;
	font-weight: 700;
}
.member-field input {
	width: 100%;
	height: 46px;
	box-sizing: border-box;
	border: 1px solid var(--line-strong);
	border-radius: var(--r-sm);
	padding: 0 14px;
	font-size: 15px;
	font-family: 'Pretendard', inherit;
	outline: none;
	transition: border-color 0.18s ease, box-shadow 0.18s ease;
}
.member-field input:focus {
	border-color: var(--brand);
	box-shadow: 0 0 0 4px var(--ring);
}
.member-field input[readonly] {
	background: var(--surface-3);
	color: var(--muted);
}
.member-actions {
	display: flex;
	justify-content: flex-end;
	gap: 10px;
	margin-top: 22px;
}
.member-button {
	display: inline-flex;
	align-items: center;
	justify-content: center;
	min-width: 92px;
	height: 46px;
	padding: 0 20px;
	border-radius: var(--r-sm);
	border: 1px solid transparent;
	background: var(--brand);
	color: var(--on-brand);
	font-weight: 700;
	text-decoration: none;
	cursor: pointer;
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
</style>
</head>
<body>
	<div class="page">
		<jsp:include page="../navigation/navigation.jsp" />
		<main class="member-page">
			<h1 class="member-title">회원 정보 수정</h1>
			<form class="member-form" method="post" action="${pageContext.request.contextPath}/member/update">
				<input type="hidden" name="id" value="${member.id}" />
				<div class="member-field">
					<label>아이디</label>
					<input type="text" value="${member.id}" readonly />
				</div>
				<div class="member-field">
					<label>이름</label>
					<input type="text" value="${member.name}" readonly />
				</div>
				<div class="member-field">
					<label>비밀번호</label>
					<input type="password" name="pwd" value="${member.pwd}" required />
				</div>
				<div class="member-field">
					<label>이메일</label>
					<input type="email" name="email" value="${member.email}" required />
				</div>
				<div class="member-field">
					<label>전화번호</label>
					<input type="text" name="phone" value="${member.phone}" required />
				</div>
				<div class="member-actions">
					<a class="member-button secondary" href="<c:url value='/member/view'><c:param name='id' value='${member.id}' /></c:url>">취소</a>
					<button class="member-button" type="submit">저장</button>
				</div>
			</form>
		</main>
	</div>
</body>
</html>
