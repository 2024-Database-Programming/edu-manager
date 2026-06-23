# EDUMANAGER

> ### "흩어진 강의와 스터디, 한 곳에서 관리하다"
>
> 강사는 강의를 열어 일정·공지·과제를 관리하고,
> 학생은 강의를 신청하고 스터디 그룹을 만들어 함께 공부하는 교육 관리 플랫폼

- **서비스명**: EduManager
- **개발 기간**: 2024.10 ~ 2024.12
- **개발 인원**: 4명
- **프로젝트**: 데이터베이스 프로그래밍 팀 프로젝트
- **서비스 목적**: 강의 모집부터 수강 신청, 스터디 운영, 일정·공지·과제 관리까지 — 교육 활동의 전 과정을 하나의 웹 서비스로 제공

<div align="center">
  <img src="src/main/webapp/images/edumanager-logo.png" alt="EduManager" width="200" />
  <br/><br/>
  <img src="./docs/calendar.png" alt="EduManager 메인 — 일정 캘린더" width="780" />
</div>

# 목차

- [팀원](#팀원)
- [기획 배경](#기획-배경)
- [서비스 소개](#서비스-소개)
- [주요 화면 및 기능 소개](#주요-화면-및-기능-소개)
- [프로젝트 핵심 기술](#프로젝트-핵심-기술)
- [시스템 아키텍처](#시스템-아키텍처)
- [ERD](#erd)
- [DB 적용 및 실행 확인](#db-적용-및-실행-확인)
- [시연 체크리스트](#시연-체크리스트)
- [프로젝트 구조](#프로젝트-구조)
- [기술 스택](#기술-스택)

# 팀원

<table>
  <tr>
    <td align="center" width="160">
      <a href="https://github.com/sondahyun"><img src="https://github.com/sondahyun.png" width="100" height="100" style="border-radius:50%"/><br/><b>손다현</b></a><br/>
      <sub>@sondahyun</sub>
    </td>
    <td align="center" width="160">
      <a href="https://github.com/heessunny"><img src="https://github.com/heessunny.png" width="100" height="100" style="border-radius:50%"/><br/><b>김희선</b></a><br/>
      <sub>@heessunny</sub>
    </td>
    <td align="center" width="160">
      <a href="https://github.com/JoEunHyang"><img src="https://github.com/JoEunHyang.png" width="100" height="100" style="border-radius:50%"/><br/><b>조은향</b></a><br/>
      <sub>@JoEunHyang</sub>
    </td>
    <td align="center" width="160">
      <a href="https://github.com/zzinggny"><img src="https://github.com/zzinggny.png" width="100" height="100" style="border-radius:50%"/><br/><b>zzinggny</b></a><br/>
      <sub>@zzinggny</sub>
    </td>
  </tr>
</table>

# 기획 배경

강의와 스터디를 운영할 때 필요한 정보는 보통 카카오톡 공지, 엑셀 수강생 명단, 구두 전달처럼 여러 곳에 흩어져 있습니다. 강사는 일정·공지·과제·수강생을 따로따로 관리해야 하고, 학생은 지금 어떤 강의와 스터디가 열려 있는지 한눈에 파악하기 어렵습니다.

EduManager는 이 **분산된 교육 활동을 하나의 흐름으로 묶기 위해** 기획되었습니다. `강의 모집 → 수강 신청 → 일정·공지·과제 관리 → 스터디 모임`으로 이어지는 과정을 한 플랫폼 안에서 처리하고, 강사와 학생이 각자의 역할에 맞는 화면에서 필요한 작업만 빠르게 끝낼 수 있도록 설계했습니다.

또한 **데이터베이스 프로그래밍** 교과 프로젝트인 만큼, 스프링·마이바티스 같은 프레임워크에 기대지 않고 **관계형 DB 설계와 순수 JDBC로 직접 구현**하여 웹 백엔드의 동작 원리를 밑바닥부터 다루는 데 초점을 맞췄습니다.

# 서비스 소개

EduManager는 **역할(학생 · 강사) 기반**의 교육 관리 웹 서비스입니다.

- **강사**는 강의를 개설하고 정기 수업 일정, 공지, 과제를 등록하며 수강생을 관리합니다.
- **학생**은 관심 강의를 검색해 수강 신청하고, 마음에 드는 강의를 찜하거나 리뷰를 남기며, 직접 스터디 그룹을 만들어 팀원을 모집합니다.
- 수업·스터디·과제·공지 일정은 **메인 캘린더**에서 통합으로 확인합니다.

단순한 정보 게시판이 아니라, 사용자가 **직접 만들고(강의·스터디) · 참여하고(수강·가입) · 함께 운영하는(공지·과제·일정)** 흐름을 중심으로 구성된 점이 특징입니다.

# 주요 화면 및 기능 소개

> 강사 계정으로 로그인해 캡처한 실제 서비스 화면입니다.

## 회원 · 인증

<table>
  <tr>
    <th width="33%">로그인</th>
    <th width="33%">회원가입 (역할 선택)</th>
    <th width="33%">내 정보</th>
  </tr>
  <tr>
    <td><img src="./docs/login.png" alt="로그인" width="100%"/></td>
    <td><img src="./docs/register.png" alt="회원가입 역할 선택" width="100%"/></td>
    <td><img src="./docs/myinfo.png" alt="내 정보" width="100%"/></td>
  </tr>
</table>

- 학생 / 강사 **역할 기반 회원가입** (단계별 입력)과 로그인·로그아웃
- 세션 기반 인증 및 권한별 접근 제어 (예: 강의 개설은 강사만)
- **프로필 사진 업로드** — Oracle BLOB에 저장하고 `/image` 로 스트리밍 서빙
- 아이디 **중복 확인**(AJAX 인라인 검사), 내 정보 수정, **회원 탈퇴(소프트 삭제 · 개인정보 익명화)**

## 강의

<table>
  <tr>
    <th width="33%">강의 · 스터디 신청 목록</th>
    <th width="33%">강의 상세</th>
    <th width="33%">강의 개설 (강사)</th>
  </tr>
  <tr>
    <td><img src="./docs/registration.png" alt="신청 목록" width="100%"/></td>
    <td><img src="./docs/lecture-detail.png" alt="강의 상세" width="100%"/></td>
    <td><img src="./docs/lecture-create.png" alt="강의 개설" width="100%"/></td>
  </tr>
</table>

- 강사: 강의 **개설·수정**, 정기/단기 수업 일정, 시험, 공지, 과제 등록, 강의 대표 사진 업로드
- 학생: 강의 목록·상세 조회, **수강 신청**, **찜(좋아요)**, **리뷰** 작성
- 수업·일정·시험·공지·과제 항목은 각각 상세 페이지에서 조회하고, 운영자는 수정·삭제 가능
- 공지·과제·일정 등록 시 상세 내용, 이미지, 첨부파일 업로드 가능
- 과제는 시작일·시작시간, 마감일·마감시간을 관리하고 학생 제출 파일을 확인 가능
- 일정 충돌 검사 등 강의 운영에 필요한 검증 포함

## 스터디 그룹

<table>
  <tr>
    <th width="50%">스터디 개설</th>
    <th width="50%">스터디 상세 · 가입</th>
  </tr>
  <tr>
    <td><img src="./docs/study-create.png" alt="스터디 개설" width="100%"/></td>
    <td><img src="./docs/study-detail.png" alt="스터디 상세" width="100%"/></td>
  </tr>
</table>

- 스터디 **개설·수정**, 모집 인원과 정기 모임 요일 설정
- **가입 신청 → 리더의 수락 / 거절** 흐름
- 스터디 수업·일정·시험·공지·과제, 첨부파일, 과제 제출, 찜, 리뷰 기능 제공

## 메인 캘린더 · 마이페이지

<table>
  <tr>
    <th width="50%">통합 일정 캘린더</th>
    <th width="50%">마이페이지</th>
  </tr>
  <tr>
    <td><img src="./docs/calendar.png" alt="일정 캘린더" width="100%"/></td>
    <td><img src="./docs/mypage.png" alt="마이페이지" width="100%"/></td>
  </tr>
</table>

- **통합 일정 캘린더** — 수업·일정·시험·과제를 날짜별로 확인하고 항목 클릭 시 상세 페이지로 이동
- 마이페이지: 내 정보 · 내 강의 · 내 스터디 · 찜 목록

## 공지 · 과제 관리 (강사)

<table>
  <tr>
    <th width="33%">공지 등록</th>
    <th width="33%">과제 등록</th>
    <th width="33%">강의 수정</th>
  </tr>
  <tr>
    <td><img src="./docs/add-notice.png" alt="공지 등록" width="100%"/></td>
    <td><img src="./docs/add-assignment.png" alt="과제 등록" width="100%"/></td>
    <td><img src="./docs/lecture-edit.png" alt="강의 수정" width="100%"/></td>
  </tr>
</table>

## 과제함 · 제출 (학생)

<table>
  <tr>
    <th width="33%">과제함 · 마감 관리</th>
    <th width="33%">과제 제출</th>
    <th width="33%">마감 시 제출 차단</th>
  </tr>
  <tr>
    <td><img src="./docs/assignment-list.png" alt="과제함" width="100%"/></td>
    <td><img src="./docs/assignment-submit.png" alt="과제 제출" width="100%"/></td>
    <td><img src="./docs/assignment-closed.png" alt="마감 시 제출 차단" width="100%"/></td>
  </tr>
</table>

- 공지·과제 **등록 시 파일 첨부**, 항목 **수정·삭제** 지원
- 과제 **제출**(설명 + 파일 업로드), 과제함에 `D-3`·`기한마감` 자동 표시
- **마감 시간이 지나면 제출 차단** (서버 검증 + UI에서 제출 폼 숨김)
- **제출물 접근 제어**: 강사는 전체 제출물 열람·다운로드, 학생은 **본인 제출물만**

# 프로젝트 핵심 기술

## 프레임워크 없는 MVC — 프런트 컨트롤러 직접 구현

- 모든 요청을 **`DispatcherServlet`(Front Controller)** 한 곳으로 받고, `RequestMapping`이 URL → `Controller`로 분기합니다.
- 각 `Controller`는 공통 인터페이스(`execute`)를 구현해 처리 결과로 **forward(JSP)** 또는 **redirect** 경로를 반환하고, 디스패처가 이동을 담당합니다.
- 매핑이 없는 요청은 **404**로 처리해, 라우팅 책임을 한 곳에 모았습니다.

## 커넥션 풀 기반 DB 접근 (DBCP2)

- 매 요청마다 커넥션을 새로 만들지 않고 **Apache Commons DBCP2 커넥션 풀**에서 빌려 쓰고 반납합니다.
- `ConnectionManager`(풀 래퍼) → `JDBCUtil`(쿼리 실행/트랜잭션) → `DAO` 로 이어지는 데이터 접근 계층을 직접 설계했습니다.
- 트랜잭션 커밋/롤백과 자원 반납을 명시적으로 다루며 순수 JDBC의 동작을 학습했습니다.

## 이미지 BLOB 저장 · 스트리밍 서빙

- 프로필·강의·스터디 이미지를 파일 시스템이 아닌 **Oracle `BLOB`** 으로 DB에 저장합니다.
- 기존 테이블을 건드리지 않도록 별도 `IMAGE` 테이블에 `(OWNER_TYPE, OWNER_ID)` 키로 보관하고, `setBinaryStream` / `getBlob` 으로 처리합니다.
- 저장된 이미지는 `ViewImageController`가 `/image?type=&id=` 로 **스트리밍 서빙**합니다.

## 항목 첨부파일 · 과제 제출 관리

- 강의/스터디의 공지, 과제, 일정, 시험 항목에 첨부파일을 연결할 수 있도록 `ITEMATTACHMENT` 테이블을 분리했습니다.
- 업로드 파일은 확장자 허용 목록과 파일 수·용량 제한을 통과한 뒤 Oracle `BLOB`으로 저장합니다.
- 과제는 학생별 제출 내용을 `ASSIGNMENTSUBMISSION`에 저장하고, 같은 학생이 다시 제출하면 최신 제출로 교체합니다.
- 첨부파일과 제출 파일 다운로드는 강의 수강생/강사, 스터디 멤버/리더 권한을 확인한 뒤 허용합니다.

## 보안 · 데이터 무결성

- **비밀번호 해싱(PBKDF2)** — 비밀번호를 평문이 아닌 `PBKDF2WithHmacSHA256`(임의 솔트 + 12만 회 반복) 해시로 저장합니다. 외부 라이브러리 없이 JDK만으로 구현했고, 기존 평문 계정도 로그인 시 자동 호환(레거시 폴백) 후 점진적으로 해시로 전환됩니다.
- **회원 탈퇴 — 소프트 삭제 + 익명화** — 행을 물리 삭제하면, 강사를 지울 때 그 강의·수강생·제출 이력까지, 스터디 리더를 지울 때 그룹 전체가 함께 사라지거나 고아 데이터가 됩니다. 그래서 탈퇴를 `status='WITHDRAWN'` 표시 + 개인정보(이름·이메일·연락처·비밀번호)만 익명화/제거하는 **소프트 삭제**로 처리했습니다. 수강·제출·후기 등 이력과 외래키 무결성은 보존되고, 작성자는 "(탈퇴회원)"으로 표기되며, 탈퇴 계정은 로그인이 차단됩니다.
- **권한 기반 접근 제어** — 회원 명부·타인 정보 조회는 관리자/본인만, 강의·항목의 수정·삭제는 소유 강사·스터디 리더만 통과하도록 컨트롤러 단에서 검증합니다.
- **저장형 XSS 방어** — 사용자 입력을 출력하는 모든 JSP에서 `<c:out>`으로 이스케이프해 스크립트 주입을 차단합니다.
- **자격증명 분리** — DB 접속 정보(`context.properties`)는 버전 관리에서 제외하고 `*.example` 템플릿만 공개합니다.

## 실패 UX — PRG + 세션 Flash 메시지

- 작업 실패 시 `request`가 아닌 **세션**에 메시지를 담고 **redirect**하는 **PRG(Post-Redirect-Get)** 패턴을 적용했습니다.
- 공통 네비게이션이 세션의 `flashError`를 읽어 **한 번만 표시한 뒤 즉시 제거**합니다.
- 덕분에 "새로고침 시 폼 재전송 / 알림이 안 사라짐" 같은 문제 없이 실패 사유를 일관되게 전달합니다.

## 안전한 에러 처리 — 커스텀 404 / 500

- 없는 주소·서버 오류를 톰캣 기본 화면 대신 **네이비 테마 커스텀 에러 페이지**로 응답합니다.
- 예외 스택트레이스(내부 클래스·프레임워크 정보)가 사용자에게 노출되지 않도록 차단했습니다.

# 시스템 아키텍처

프런트 컨트롤러 패턴 기반의 **계층형 MVC** 구조입니다.

```text
User Browser
  └─ Filter (Encoding · Resource)
      └─ DispatcherServlet  (Front Controller)
          └─ RequestMapping  (URL → Controller 매핑)
              └─ Controller   (요청 처리, forward / redirect 결정)
                  └─ Manager  (Service · 비즈니스 로직, 싱글톤)
                      └─ DAO → JDBCUtil → ConnectionManager (DBCP2 Pool)
                          └─ Oracle DB
          └─ View : JSP + JSTL   (forward 시 렌더링)
```

**요청 처리 흐름**

1. 모든 요청은 `Filter`(인코딩·정적 리소스)를 거쳐 `DispatcherServlet`으로 진입
2. `RequestMapping`이 URL에 맞는 `Controller`를 찾음 (없으면 404)
3. `Controller` → `Manager` → `DAO` → 커넥션 풀 순으로 DB 작업 수행
4. 결과에 따라 JSP **forward** 또는 **redirect** 반환
5. 실패 시 세션 `flashError` + PRG로 사용자에게 1회성 알림

# ERD

핵심 엔티티 관계 (간략화)

```mermaid
erDiagram
    MEMBER ||--o| STUDENT : "이다"
    MEMBER ||--o| TEACHER : "이다"
    TEACHER ||--o{ LECTURE : "개설"
    MEMBER  ||--o{ STUDYGROUP : "리더"
    STUDENT ||--o{ LECTURE_ENROLLMENT : "수강신청"
    LECTURE ||--o{ LECTURE_ENROLLMENT : "수강생"
    LECTURE ||--o{ LECTURE_LIKE : "찜"
    LECTURE ||--o{ LECTURE_REVIEW : "리뷰"
    LECTURE ||--o{ SCHEDULE : "정기일정"
    LECTURE ||--o{ NOTICE : "공지"
    LECTURE ||--o{ ASSIGNMENT : "과제"
    LECTURE ||--o{ ITEM_ATTACHMENT : "항목첨부"
    STUDYGROUP ||--o{ STUDYGROUP_APPLICATION : "가입신청"
    STUDYGROUP ||--o{ STUDYGROUP_REVIEW : "리뷰"
    STUDYGROUP ||--o{ SCHEDULE : "모임일정"
    STUDYGROUP ||--o{ ITEM_ATTACHMENT : "항목첨부"
    ASSIGNMENT ||--o{ ASSIGNMENT_SUBMISSION : "제출"
```

> 프로필·강의·스터디 **이미지**는 별도 `IMAGE` 테이블에 `(OWNER_TYPE, OWNER_ID)` 키 + `IMG_DATA BLOB`으로 저장합니다. 공지·과제·일정·시험 첨부파일은 `ITEMATTACHMENT`, 과제 제출물은 `ASSIGNMENTSUBMISSION`에 BLOB으로 저장합니다.

# DB 적용 및 실행 확인

신규 기능을 반영하려면 기존 기본 스키마에 아래 SQL을 순서대로 적용합니다.

```sql
@db/IMAGE.sql
@db/ALTER_LECTURE_NAME_LENGTH.sql
@db/ALTER_ASSIGNMENT_TIME_COLUMNS.sql
@db/ALTER_SCHEDULE_DESCRIPTION.sql
@db/CREATE_ITEM_ATTACHMENT.sql
@db/CREATE_ASSIGNMENT_SUBMISSION.sql
@db/WIDEN_PWD_FOR_HASH.sql
@db/SOFT_DELETE_MEMBER.sql
@db/MEMBER_DELETE_CASCADE.sql
```

> - `WIDEN_PWD_FOR_HASH.sql` — 비밀번호 해시(PBKDF2) 저장을 위한 `pwd` 컬럼 확대 (미적용 시 신규 회원가입 실패)
> - `SOFT_DELETE_MEMBER.sql` — 회원 탈퇴를 **소프트 삭제+익명화**로 전환하기 위한 `MEMBER.status` 컬럼 추가 (미적용 시 로그인·회원목록·탈퇴 쿼리가 `ORA-00904`로 실패)
> - `MEMBER_DELETE_CASCADE.sql` — *(선택)* 과거 하드 삭제용 연쇄정리 트리거. 소프트 삭제 도입으로 더 이상 발화하지 않으며, 관리자가 SQL로 직접 하드 삭제할 때를 대비한 안전망으로만 의미가 있음

빌드 검증은 Maven으로 수행합니다.

```bash
mvn -q -DskipTests package
```

성공하면 `target/EduManager.war`를 Tomcat 9에 배포해 JSP/Servlet MVC 흐름을 확인합니다.

# 시연 체크리스트

- 강사 로그인 후 강의 개설, 대표 이미지 업로드, 정기 수업 일정 등록
- 강의 상세에서 수업·일정·시험·공지·과제 등록, 첨부파일 추가, 상세 페이지 조회
- 등록한 항목을 캘린더와 날짜별 상세 목록에서 클릭해 해당 상세 페이지로 이동
- 운영자 권한으로 항목 수정·삭제, 첨부파일 다운로드 확인
- 학생 로그인 후 강의/스터디 신청 목록에서 신청한 항목 확인, 상세 페이지 진입
- 학생이 과제 상세 페이지에서 파일과 설명을 제출하고, 강사가 제출 목록 확인
- 수강생/스터디원 이름 클릭 시 회원 정보 확인
- 권한 없는 사용자의 수정 페이지 접근과 파일 다운로드 차단 확인

# 프로젝트 구조

```text
EduManager/
├── db/                                # DB 스키마 (IMAGE, 첨부파일, 과제 제출 등)
├── pom.xml                            # Maven 의존성 / 빌드 설정
└── src/main
    ├── java
    │   ├── controller                 # 프런트 컨트롤러 + 도메인별 Controller
    │   │   ├── DispatcherServlet.java #   └ 진입점 (Front Controller)
    │   │   ├── RequestMapping.java    #   └ URL ↔ Controller 매핑
    │   │   └── lecture / study / studyGroup / member / mypage / main
    │   ├── filter                     # EncodingFilter, ResourceFilter
    │   └── model
    │       ├── domain                 # 도메인 객체 (Member, Lecture, StudyGroup …)
    │       ├── service                # Manager (비즈니스 로직, 싱글톤)
    │       └── dao                    # DAO + JDBCUtil + ConnectionManager
    ├── resources                      # context.properties (DB 접속 정보)
    └── webapp
        ├── css / js / images          # 정적 리소스 (네이비 디자인 시스템)
        └── WEB-INF
            ├── web.xml                # 서블릿 · 필터 · 에러 페이지 매핑
            ├── error.jsp              # 커스텀 404 / 500 페이지
            ├── navigation/            # 공통 네비게이션 (+ flash 배너)
            └── item / lecture / study / member / mypage / main / registration
```

# 기술 스택

### Language · View

<div>
  <img src="https://img.shields.io/badge/Java_17-007396?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/JSP-007396?style=for-the-badge&logo=oracle&logoColor=white"/>
  <img src="https://img.shields.io/badge/JSTL-4B8BBE?style=for-the-badge"/>
</div>

### Web · DB

<div>
  <img src="https://img.shields.io/badge/Servlet_4.0-2E6DB4?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Custom_MVC-555555?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/JDBC-F80000?style=for-the-badge&logo=oracle&logoColor=white"/>
  <img src="https://img.shields.io/badge/Oracle_DB-F80000?style=for-the-badge&logo=oracle&logoColor=white"/>
  <img src="https://img.shields.io/badge/Commons_DBCP2-D22128?style=for-the-badge&logo=apache&logoColor=white"/>
</div>

### Build · Etc.

<div>
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white"/>
  <img src="https://img.shields.io/badge/Tomcat_9-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=black"/>
  <img src="https://img.shields.io/badge/Logback-24292E?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Jackson-1572B6?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/JUnit-25A162?style=for-the-badge&logo=junit5&logoColor=white"/>
</div>

<br/>

<div align="center">

**EduManager** · 2024 Database Programming Team Project

</div>
