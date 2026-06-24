package controller;

import java.sql.SQLException;

import model.domain.lecture.Lecture;
import model.domain.study.StudyGroup;
import model.service.lecture.LectureManager;
import model.service.study.StudyGroupManager;

public final class AuthorizationUtils {
    private AuthorizationUtils() {
    }

    // 관리자 여부 (현재 규칙: id == "admin". 추후 MEMBER.role 컬럼 도입 시 교체)
    public static boolean isAdmin(String memberId) {
        return "admin".equals(memberId);
    }

    public static boolean canViewLecture(LectureManager manager, String memberId, Lecture lecture) throws SQLException {
        if (memberId == null || lecture == null) {
            return false;
        }

        return memberId.equals(lecture.getTeacherId())
                || manager.isEnrolledInLecture(memberId, lecture.getLectureId());
    }

    public static boolean canViewLecture(LectureManager manager, String memberId, long lectureId) throws SQLException {
        return canViewLecture(manager, memberId, manager.findLectureById(lectureId));
    }

    public static boolean canManageLecture(String memberId, Lecture lecture) {
        return memberId != null && lecture != null && memberId.equals(lecture.getTeacherId());
    }

    public static boolean canManageLecture(LectureManager manager, String memberId, long lectureId) throws SQLException {
        return canManageLecture(memberId, manager.findLectureById(lectureId));
    }

    public static boolean canViewStudy(StudyGroupManager manager, String memberId, long groupId) throws SQLException {
        return memberId != null && manager.isMemberOfStudyGroup(memberId, groupId);
    }

    public static boolean canManageStudy(String memberId, StudyGroup studyGroup) {
        return memberId != null && studyGroup != null && memberId.equals(studyGroup.getLeaderId());
    }

    public static boolean canManageStudy(StudyGroupManager manager, String memberId, long groupId) throws SQLException {
        return canManageStudy(memberId, manager.findStudyGroupById(groupId));
    }
}
