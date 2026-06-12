-- 이미지(BLOB) 저장용 테이블
-- 프로필/강의/스터디 사진을 DB에 직접 보관한다(파일시스템 X → 재배포해도 유지).
-- 사용처:
--   ImageDAO            : 저장(setBinaryStream) / 조회(getBlob)
--   ViewImageController : /image?type=member|lecture|study&id=... 로 스트리밍 서빙
--
-- 적용: 이 스크립트를 한 번 실행해 테이블을 생성하면 된다.

CREATE TABLE IMAGE (
    OWNER_TYPE VARCHAR2(20)  NOT NULL,   -- 'member' | 'lecture' | 'study'
    OWNER_ID   VARCHAR2(100) NOT NULL,   -- 회원 id 또는 강의/스터디 id(문자열)
    IMG_DATA   BLOB,                     -- 실제 이미지 바이트
    IMG_TYPE   VARCHAR2(100),            -- content-type (예: image/png)
    CONSTRAINT PK_IMAGE PRIMARY KEY (OWNER_TYPE, OWNER_ID)
);
