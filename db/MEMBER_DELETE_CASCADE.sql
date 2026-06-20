-- ============================================================================
-- 회원 삭제 시 연관 데이터 정리 (고아 데이터·명단 JOIN 붕괴 방지)
-- ----------------------------------------------------------------------------
-- 배경: 현재 회원 탈퇴/삭제는 STUDENT/TEACHER → MEMBER 행만 지우고
--       수강신청·신청·좋아요·후기·과제제출·프로필이미지가 그대로 남아
--       고아 데이터가 쌓이고 INNER JOIN 명단에서 행이 조용히 사라진다.
--
-- 해결: MEMBER 한 행이 삭제되면 그 회원의 모든 연관 행을 같은 트랜잭션에서
--       함께 지우는 BEFORE DELETE 트리거. (IMAGE/ASSIGNMENTSUBMISSION 은
--       OWNER_TYPE/STUDENTID 기반 폴리모픽이라 단순 FK CASCADE가 어려워
--       트리거로 일괄 처리하는 것이 가장 견고하다.)
--
-- ⚠️ 실행 전 반드시:
--   1) 운영 데이터 백업.
--   2) 아래 테이블/컬럼명이 실제 스키마와 일치하는지 확인(미사용 테이블은 줄 삭제).
--   3) 회원 id가 STUDENT/TEACHER 가 아니라 MEMBER 기준임을 확인.
--   4) 스키마 소유 계정으로 SQL*Plus / SQL Developer 에서 실행.
-- ============================================================================

-- [선택] 기존에 이미 쌓인 고아 데이터 정리 (탈퇴했는데 안 지워진 행들)
-- 필요 시 주석 해제 후 실행. MEMBER 에 없는 소유자 행을 제거한다.
-- DELETE FROM ASSIGNMENTSUBMISSION    WHERE STUDENTID NOT IN (SELECT id FROM MEMBER);
-- DELETE FROM IMAGE                   WHERE OWNER_TYPE = 'member' AND OWNER_ID NOT IN (SELECT id FROM MEMBER);
-- DELETE FROM LectureLike             WHERE stuId    NOT IN (SELECT id FROM MEMBER);
-- DELETE FROM StudyGroupLike          WHERE memberId NOT IN (SELECT id FROM MEMBER);
-- DELETE FROM LectureReview           WHERE stuId    NOT IN (SELECT id FROM MEMBER);
-- DELETE FROM StudyGroupReview        WHERE stuId    NOT IN (SELECT id FROM MEMBER);
-- DELETE FROM LectureEnrollment       WHERE STUID    NOT IN (SELECT id FROM MEMBER);
-- DELETE FROM StudyGroupApplication   WHERE stuId    NOT IN (SELECT id FROM MEMBER);
-- COMMIT;

-- ----------------------------------------------------------------------------
-- 회원 삭제 시 연관 데이터 일괄 정리 트리거
-- ----------------------------------------------------------------------------
CREATE OR REPLACE TRIGGER TRG_MEMBER_CLEANUP
BEFORE DELETE ON MEMBER
FOR EACH ROW
BEGIN
    -- 과제 제출물(BLOB 포함) / 프로필 이미지(BLOB)
    DELETE FROM ASSIGNMENTSUBMISSION  WHERE STUDENTID = :OLD.id;
    DELETE FROM IMAGE                 WHERE OWNER_TYPE = 'member' AND OWNER_ID = :OLD.id;

    -- 좋아요 / 후기
    DELETE FROM LectureLike           WHERE stuId    = :OLD.id;
    DELETE FROM StudyGroupLike        WHERE memberId = :OLD.id;
    DELETE FROM LectureReview         WHERE stuId    = :OLD.id;
    DELETE FROM StudyGroupReview      WHERE stuId    = :OLD.id;

    -- 수강신청 / 스터디 가입신청
    DELETE FROM LectureEnrollment     WHERE STUID    = :OLD.id;
    DELETE FROM StudyGroupApplication WHERE stuId    = :OLD.id;

    -- 역할 테이블 (컨트롤러가 먼저 지웠으면 0건 — 안전망)
    DELETE FROM STUDENT               WHERE id       = :OLD.id;
    DELETE FROM TEACHER               WHERE id       = :OLD.id;
END;
/

-- 적용 후: 애플리케이션이 MEMBER 한 행만 삭제해도 위 연관 행이 같은 트랜잭션에서 모두 정리된다.
-- (DeleteAccountController / DeleteMemberController 의 기존 STUDENT/TEACHER 선삭제는 그대로 둬도 무방 — 트리거가 재실행해도 0건.)

-- 참고: 강의/스터디(주최자) 삭제 시의 연관 정리는 별도 정책이 필요하다.
--       강사/리더가 만든 강의·스터디까지 같이 지울지(소유 콘텐츠 삭제) 또는
--       이관/보존할지는 서비스 정책 결정 후 유사 트리거로 확장할 것.
