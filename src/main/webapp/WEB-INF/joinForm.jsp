<%@page contentType="text/html; charset=utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
  <head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/theme.css'/>?v=20260613-ds4">
	<link rel=stylesheet href="<c:url value='/css/joinForm.css' />?v=20260613-ds4" type="text/css">
	    <title>EduManager registerForm</title>
	    <script src="<c:url value='/js/joinForm.js' />"></script>
  </head>
  <body>
    <div class="page">
        <header>
	            <img src="<c:url value='/images/edumanager-logo.png' />">
	             <nav>
	                <ul>
	                    <li class="menu"><a href="<c:url value='/main/main' />">홈</a></li>
	                    <li class="menu"><a href="<c:url value='/registration' />">강의/스터디 신청</a></li>
	                    <li class="menu"><a href="<c:url value='/mypage' />">마이 페이지</a></li>
	                    <li class="menu"><a href="<c:url value='/member/logout' />">로그아웃</a></li>
                </ul>
            </nav>

        </header>
      <div class="subTitle">강의/스터디 신청하기</div>

      <div id="tabBtn-container">
      <div class="tab-container">
        <div class="tabs on " onclick="openTab('Tab1')">
	            <div><img src="<c:url value='/images/class.svg' />" id="tab1Icon"></div>
            <div>강의</div>
        </div>
        <div class="tabs" onclick="openTab('Tab2')">
	            <div><img src="<c:url value='/images/studyIcon.svg' />" id="tab2Icon"></div>
            <div>스터디</div>
        </div>
    </div>
	    <div id="makeStudyBtn-container" ><button id="makeStudyBtn" onclick="window.location.href='<c:url value='/study/create' />';"><img src="<c:url value='/images/plus.svg' />" id="plusIcon"> 스터디 만들기</button></div>
</div>
    <div class="hr-wrapper">
        <hr id="registration_mainHr">
        <hr id="registration_hr1">
        <hr id="registration_hr2">
      </div>
    <div class="tab_wrap">
      <div id="Tab1" class="tab on">
        <div id="leaderRoom1" class="class">
	          <img src="<c:url value='/images/white.png' />" class="groupGallery">
        </div>
        <div id="leaderRoom2" class="class">
	          <img src="<c:url value='/images/white.png' />" class="groupGallery">
          </div>
          <div id="leaderRoom3" class="class">
	          <img src="<c:url value='/images/white.png' />" class="groupGallery">
          </div>
      </div>
      <div id="Tab2" class="tab">
        <div id="studyRoom1" class="studyGroup">
	            <img src="<c:url value='/images/white.png' />" class="groupGallery">
          </div>
          <div id="studyRoom2" class="studyGroup">
	            <img src="<c:url value='/images/white.png' />" class="groupGallery">
            </div>
            <div id="studyRoom3" class="studyGroup">
	            <img src="<c:url value='/images/white.png' />" class="groupGallery">
      </div>
  </body>
  </html>
