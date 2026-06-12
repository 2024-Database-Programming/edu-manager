<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/theme.css'/>?v=20260613-ds4">
<link rel="stylesheet" href="<c:url value='/css/registration.css' />?v=20260613-ds4"
   type="text/css">
<title>EduManager</title>
</head>
<script src="<c:url value='/js/registration.js' />"></script>
<body>
   <div class="page">
      <!-- 네비게이션 포함 -->
      <jsp:include page="../navigation/navigation.jsp" />

      <section class="browse-head">
         <div class="browse-copy">
            <span class="page-eyebrow">COURSE & STUDY</span>
            <h1>강의/스터디 신청</h1>
            <p>관심 있는 강의와 스터디를 빠르게 찾아보세요.</p>
         </div>
         <div class="search">
            <form action="<c:url value='/registration/search' />"
               method="post" id="searchForm">
               <c:choose>
                  <c:when test="${searchParam == null}">
                     <input type="text" name="searchParam" placeholder="검색어를 입력하세요">
                  </c:when>
                  <c:otherwise>
                     <input type="text" name="searchParam" placeholder="${searchParam}">
                  </c:otherwise>
               </c:choose>
               
               <img src="<c:url value='/images/searchIcon.svg' />"
                  onclick="document.getElementById('searchForm').submit();">
            </form>

         </div>
      </section>

      <div id="tabBtn-container">
         <div class="tab-container">
            <div class="tabs on" onclick="openTab('Tab1')">
               <div>
                  <img src="<c:url value='/images/class.svg' />" id="tab1Icon">
               </div>
               <div>강의</div>
            </div>
            <div class="tabs" onclick="openTab('Tab2')"
               style="${isTeacher ? 'display:none;' : ''}">
               <div>
                  <img src="<c:url value='/images/studyIcon.svg' />" id="tab2Icon">
               </div>
               <div>스터디</div>
            </div>
         </div>
         <c:if test="${!isTeacher}">
            <div id="makeStudyBtn-container">
               <button id="makeStudyBtn" onclick="createStudy()"
                  style="font-family: 'Pretendard';">
                  <img src="<c:url value='/images/plus.svg' />" id="plusIcon">
                  스터디 만들기
               </button>
            </div>
         </c:if>

         <c:if test="${isTeacher}">
            <div id="makeLectureBtn-container">

               <button id="makeStudyBtn" onclick="createLecture()"
                  style="font-family: 'Pretendard';">
                  <img src="<c:url value='/images/plus.svg' />" id="plusIcon">
                  강의 만들기
               </button>
            </div>
         </c:if>
      </div>

      <div class="hr-wrapper">
         <hr id="registration_mainHr">
         <hr id="registration_hr1">
         <hr id="registration_hr2">
      </div>

      <div class="tab_wrap">
         <!-- Tab 1 -->
         <div id="Tab1" class="tab on">
            <div class="class">
               <c:forEach var="group" items="${lectureList}">
                  <div class="groupGallery" style="--category-color: ${group.categoryColor};">
                     <a
                        href="<c:url value='/lecture/over-view'><c:param name='lectureId' value='${group.lectureId}'/></c:url>">
                        <img class="groupGalleryImage" src="<c:url value='${group.img}' />" alt="${group.name}"
                           onload="if (this.src.indexOf('eduLogo.png') !== -1 || this.src.indexOf('white.png') !== -1) { this.closest('.groupGallery').classList.add('has-fallback-cover'); this.remove(); }"
                           onerror="this.closest('.groupGallery').classList.add('has-fallback-cover'); this.remove();">
                        <div class="default-cover" aria-hidden="true">
                           <span class="cover-kicker">EduManager</span>
                           <span class="cover-title">강의</span>
                        </div>
                        <span class="groupGalleryTitle">${group.name}</span>
                        <div class="gallery-meta">
                           <span class="groupGalleryCategory"
                              style="background-color: ${group.categoryColor};">${group.categoryName}</span>
                           <span class="groupGalleryTeacherName">${group.teacherName}</span>
                        </div>
                     </a>
                  </div>
               </c:forEach>
            </div>
         </div>

         <!-- Tab 2 -->
         <div id="Tab2" class="tab"
            style="${isTeacher ? 'display:none;' : ''}">
            <div class="class">
               <c:forEach var="studyGroup" items="${studyGroupList}">
                  <div class="groupGallery" style="--category-color: ${studyGroup.categoryColor};">
                     <a
                        href="<c:url value='/study/over-view'><c:param name='groupId' value='${studyGroup.studyGroupId}'/></c:url>">
                        <img class="groupGalleryImage" src="<c:url value='${studyGroup.img}' />" alt="${studyGroup.name}"
                           onload="if (this.src.indexOf('eduLogo.png') !== -1 || this.src.indexOf('white.png') !== -1) { this.closest('.groupGallery').classList.add('has-fallback-cover'); this.remove(); }"
                           onerror="this.closest('.groupGallery').classList.add('has-fallback-cover'); this.remove();">
                        <div class="default-cover" aria-hidden="true">
                           <span class="cover-kicker">EduManager</span>
                           <span class="cover-title">스터디</span>
                        </div>
                        <span class="groupGalleryTitle">${studyGroup.name}</span>
                        <div class="gallery-meta">
                           <span class="groupGalleryCategory"
                              style="background-color: ${studyGroup.categoryColor};">${studyGroup.categoryName}</span>
                        </div>
                     </a>
                  </div>
               </c:forEach>
            </div>
         </div>
      </div>
   </div>
   <script>
      function createStudy() {
         window.location.href = '<c:url value="/study/create" />';
      }
      function createLecture() {
         window.location.href = '<c:url value="/lecture/create" />';
      }
   </script>
</body>
</html>
