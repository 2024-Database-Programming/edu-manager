package controller;

import model.domain.Assignment;
import model.domain.AssignmentSubmission;
import model.domain.Attachment;
import model.domain.Notice;
import model.domain.Schedule;
import model.domain.lecture.Lecture;
import model.domain.studyGroup.StudyGroup;
import model.service.LectureManager;
import model.service.StudyGroupManager;
import model.service.StudyManager;

public final class ItemAccessUtils {
	private ItemAccessUtils() {
	}

	public static boolean canAccessAttachment(Attachment attachment, String memberId) throws Exception {
		if (attachment == null || memberId == null) {
			return false;
		}
		if ("lecture".equals(attachment.getOwnerType())) {
			LectureManager manager = LectureManager.getInstance();
			int lectureId = findLectureId(manager, attachment.getItemType(), attachment.getItemId());
			return lectureId > 0 && AuthorizationUtils.canViewLecture(manager, memberId, lectureId);
		}
		if ("study".equals(attachment.getOwnerType())) {
			StudyGroupManager accessManager = StudyGroupManager.getInstance();
			StudyManager manager = StudyManager.getInstance();
			int groupId = findStudyId(manager, attachment.getItemType(), attachment.getItemId());
			return groupId > 0 && AuthorizationUtils.canViewStudy(accessManager, memberId, groupId);
		}
		return false;
	}

	public static boolean canAccessSubmission(AssignmentSubmission submission, String memberId) throws Exception {
		if (submission == null || memberId == null) {
			return false;
		}
		if ("lecture".equals(submission.getOwnerType())) {
			LectureManager manager = LectureManager.getInstance();
			Assignment assignment = manager.findAssignmentById(submission.getAssignmentId());
			if (assignment == null) {
				return false;
			}
			Lecture lecture = manager.findLectureById(assignment.getLectureId());
			return AuthorizationUtils.canManageLecture(memberId, lecture)
					|| (memberId.equals(submission.getStudentId())
							&& AuthorizationUtils.canViewLecture(manager, memberId, lecture));
		}
		if ("study".equals(submission.getOwnerType())) {
			StudyManager manager = StudyManager.getInstance();
			StudyGroupManager accessManager = StudyGroupManager.getInstance();
			Assignment assignment = manager.findAssignmentById(submission.getAssignmentId());
			if (assignment == null) {
				return false;
			}
			StudyGroup study = manager.findStudyById(assignment.getStudyId());
			return AuthorizationUtils.canManageStudy(memberId, study)
					|| (memberId.equals(submission.getStudentId())
							&& AuthorizationUtils.canViewStudy(accessManager, memberId, assignment.getStudyId()));
		}
		return false;
	}

	private static int findLectureId(LectureManager manager, String itemType, int itemId) {
		if ("assignment".equals(itemType)) {
			Assignment assignment = manager.findAssignmentById(itemId);
			return assignment == null ? 0 : assignment.getLectureId();
		}
		if ("notice".equals(itemType)) {
			Notice notice = manager.findNoticeById(itemId);
			return notice == null ? 0 : notice.getLectureId();
		}
		Schedule schedule = manager.findScheduleDetailById(itemId);
		return schedule == null ? 0 : (int) schedule.getLectureId();
	}

	private static int findStudyId(StudyManager manager, String itemType, int itemId) {
		if ("assignment".equals(itemType)) {
			Assignment assignment = manager.findAssignmentById(itemId);
			return assignment == null ? 0 : assignment.getStudyId();
		}
		if ("notice".equals(itemType)) {
			Notice notice = manager.findNoticeById(itemId);
			return notice == null ? 0 : notice.getStudyId();
		}
		Schedule schedule = manager.findScheduleDetailById(itemId);
		return schedule == null ? 0 : (int) schedule.getStudyGroupId();
	}
}
