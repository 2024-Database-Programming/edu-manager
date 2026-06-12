package controller;

import java.sql.SQLException;

import model.domain.lecture.Lecture;
import model.domain.studyGroup.StudyGroup;
import model.service.LectureManager;
import model.service.StudyGroupManager;

public final class AuthorizationUtils {
    private AuthorizationUtils() {
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
