<%@page contentType="text/html; charset=utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
  <head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel=stylesheet href="<c:url value='../../css/registerForm.css' />" type="text/css">
    <title>EduManager registerForm</title>
    <script src="../../js/registerForm.js"></script>
  </head>
  <body>
    <div class="page">
        <header>
            <img src="../../images/eduLogo.png">
             <nav>
                <ul>
                    <li class="menu"><a href="main.jsp">홈</a></li>
                    <li class="menu"><a href="#">강의/스터디 신청</a></li>
                    <li class="menu"><a href="studentMypage.jsp">마이 페이지</a></li>
                    <li class="menu"><a href="">로그아웃</a></li>
                </ul>
            </nav>

        </header>
      <div class="subTitle">강의/스터디 신청하기</div>

      <div id="tabBtn-container">
      <div class="tab-container">
        <div class="tabs on " onclick="openTab('Tab1')">
            <div><img src="../../images/class.svg" id="tab1Icon"></div>
            <div>강의</div>
        </div>
        <div class="tabs" onclick="openTab('Tab2')">
            <div><img src="../../images/studyIcon.svg" id="tab2Icon"></div>
            <div>스터디</div>
        </div>
    </div>
    <div id="makeStudyBtn-container" ><button id="makeStudyBtn" onclick="window.location.href='study_make.jsp';"><img src="../images/plus.svg" id="plusIcon"> 스터디 만들기</button></div>
</div>
    <div class="hr-wrapper">
        <hr id="registration_mainHr">
        <hr id="registration_hr1">
        <hr id="registration_hr2">
      </div>
    <div class="tab_wrap">
      <div id="Tab1" class="tab on">
        <div id="leaderRoom1" class="class">
          <img src="../../images/white.png" class="groupGallery">
        </div>
        <div id="leaderRoom2" class="class">
          <img src="../../images/white.png" class="groupGallery">
          </div>
          <div id="leaderRoom3" class="class">
          <img src="../../images/white.png" class="groupGallery">
          </div>
      </div>
      <div id="Tab2" class="tab">
        <div id="studyRoom1" class="studyGroup">
            <img src="../../images/white.png" class="groupGallery">
          </div>
          <div id="studyRoom2" class="studyGroup">
            <img src="../../images/white.png" class="groupGallery">
            </div>
            <div id="studyRoom3" class="studyGroup">
            <img src="../../images/white.png" class="groupGallery">
      </div>
  </body>
  </html>
