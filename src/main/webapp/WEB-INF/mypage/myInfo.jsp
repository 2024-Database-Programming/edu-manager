<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
    
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" href="<c:url value='/css/myInfo.css' />" type="text/css">
    <title>내 정보 확인</title>
</head>
<body>
    <div class="page">
        <jsp:include page="/WEB-INF/navigation/navigation.jsp" />  
        <div id="form-container">
            <div id="info-container">
                <div id="info-title">내 정보</div>
                
                <!-- 아이디 -->
                <div>
                    <label class="info-label" for="id">아이디</label>
                    <div class="info-box" id="id">
                        <c:out value="${member.id}" />
                    </div>
                </div>
                
                <!-- 이름 -->
                <div>
                    <label class="info-label" for="name">이름</label>
                    <div class="info-box" id="name">
                        <c:out value="${member.name}" />
                    </div>
                </div>
                
                <!-- 이메일 -->
                <div>
                    <label class="info-label" for="email">이메일</label>
                    <div class="info-box" id="email">
                        <c:out value="${member.email}" />
                    </div>
                </div>
                
                <!-- 전화번호 -->
                <div>
                    <label class="info-label" for="phoneNumber">전화번호</label>
                    <div class="info-box" id="phoneNumber">
                        <c:out value="${member.phone}" />
                    </div>
                </div>
                
                <!-- 버튼 -->
                <div id="button-container">
                    <!-- 탈퇴하기 버튼 -->
                    
                    <form action="<c:url value='/mypage/deleteConfirm' />" method="get">
                        <button class="footer-button" id="deleteAccount" type="submit">탈퇴하기</button>
                    </form>
                    <!-- 수정하기 버튼 -->
                    <form action="<c:url value='/mypage/editMyInfo' />" method="get">
                        <button class="footer-button" id="editInfo" type="submit">수정하기</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
