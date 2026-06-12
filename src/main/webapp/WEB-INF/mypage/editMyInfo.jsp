<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" href="<c:url value='/css/editInfo.css' />" type="text/css">
    <title>회원 정보 수정</title>
</head>
<body>
    <div class="page">
        <jsp:include page="/WEB-INF/navigation/navigation.jsp" />
        <div id="form-container">
            <form action="<c:url value='/mypage/edit' />" method="post" enctype="multipart/form-data">
                <div id="info-container">
                    <div id="info-title">내 정보 수정</div>

						<div id="profile_img_container">
    						<img id="profile_img" src="<c:url value='${member.img}' />" >
						    <input type="file" id="profile_img_upload" name="profileImg" accept="image/*">
						</div>
                    <!-- 아이디 -->
                    <div>
                        <label class="info-label" for="id">아이디</label>
                        <input type="text" class="info-box" id="id" name="id" value="<c:out value='${member.id}' />" readonly />
                    </div>

                    <!-- 이름 -->
                    <div>
                        <label class="info-label" for="name">이름</label>
                        <input type="text" class="info-box" id="name" name="name" value="<c:out value='${member.name}' />" readonly />
                    </div>

                    <!-- 이메일 -->
                    <div>
                        <label class="info-label" for="email">이메일</label>
                        <input type="email" class="info-box" id="email" name="email" value="<c:out value='${member.email}' />" required />
                    </div>

                    <!-- 전화번호 -->
                    <div>
                        <label class="info-label" for="phoneNumber">전화번호</label>
                        <input type="text" class="info-box" id="phoneNumber" name="phone" value="<c:out value='${member.phone}' />" required />
                    </div>

                    <!-- 비밀번호 -->
                    <div>
                        <label class="info-label" for="password">새 비밀번호</label>
                        <input type="password" class="info-box" id="password" name="password" placeholder="&#9679;&#9679;&#9679;&#9679;&#9679;&#9679" required />
                    </div>
                </div>

                <div id="form-controls">
                    <button type="submit" id="updateBt">수정하기</button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
