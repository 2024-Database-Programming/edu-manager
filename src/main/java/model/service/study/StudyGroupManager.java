package model.service.study;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import model.dao.study.StudyGroupDao;
import model.domain.Notice;
import model.domain.study.StudyGroup;
import model.domain.study.StudyGroupApplication;
import model.domain.study.StudyGroupReview;


public class StudyGroupManager {
    
    private static StudyGroupManager instance = new StudyGroupManager();
    private StudyGroupDao studyGroupDao;

    private StudyGroupManager() {
        studyGroupDao = new StudyGroupDao();
    }

    public static StudyGroupManager getInstance() {
        return instance;
    }

    public StudyGroup findStudyGroupById(long studyGroupId) throws SQLException {
        return studyGroupDao.findGroupInfo(studyGroupId);
    }
    public List<StudyGroup> getStudyGroupsExcludingStudent(String stuId) throws SQLException {
        return studyGroupDao.getStudyGroupsExcludingStudent(stuId);
    }
    
    public List<StudyGroup> getStudyGroupsSearch(String stuId, String searchParam) {
        return studyGroupDao.getStudyGroupsSearch(stuId,searchParam);
    }
    

    public List<StudyGroup> StudyGroupListByLeader(String stuId) throws SQLException {
        return studyGroupDao.getStudyGroupListByLeader(stuId);
    }
    
    public List<StudyGroup> StudyGroupListByMember(String stuId) throws SQLException {
        return studyGroupDao.getStudyGroupListByMember(stuId);
    }
    
    public List<StudyGroup> StudyGroupLikeList(String stuId) throws SQLException {
        return studyGroupDao.getLikedStudyGroups(stuId);
    }
    
    public boolean isLikedByUser(String memberId, long studyGroupId) throws SQLException {
        return studyGroupDao.isLikedByUser(memberId, studyGroupId);
    }

    // 좋아요 토글
    public void toggleStudyGroupLike(String memberId, long studyGroupId) throws SQLException {
        boolean isLiked = isLikedByUser(memberId, studyGroupId);
        if (isLiked) {
            // 좋아요가 있다면 삭제
            studyGroupDao.removeLike(memberId, studyGroupId);
        } else {
            // 좋아요가 없다면 추가
            studyGroupDao.addLike(memberId, studyGroupId);
        }
    }
    

    public boolean isMemberOfStudyGroup(String memberId, long studyGroupId) throws SQLException {
        return studyGroupDao.isMemberOfStudyGroup(memberId, studyGroupId);
    }
    
    
    //스터디 가입 요청 보내기
    public StudyGroupApplication createApplication(String memberId, long studyGroupId) throws SQLException {
        return studyGroupDao.createApplication(memberId, studyGroupId);
                 
    }
    
    //스터디 가입 요청 리스트 
    public List<StudyGroupApplication> getStudyRequestList(long studyGroupId) throws SQLException {
        return  studyGroupDao.getStudyRequestList(studyGroupId);
    }

    public StudyGroupApplication findApplicationById(long applicationId) throws SQLException {
        return studyGroupDao.findById(applicationId);
    }
 
    
    //스터디 요청 상태 확인하기
    public String getStatusByMemberIdAndGroupId(String memberId, long studyGroupId) throws SQLException {
        return studyGroupDao.getStatusByMemberIdAndGroupId(memberId, studyGroupId);
                 
    }
    
    
    public void  acceptApplication(long applicationId) throws SQLException {
        studyGroupDao.acceptApplication(applicationId);
        
    }
    
    public void  deleteApplication(long applicationId) throws SQLException {
        studyGroupDao.deleteApplication(applicationId);
        
    }
    
    
    //스터디 후기 작성
    public StudyGroupReview createStudyReview(StudyGroupReview groupReview) throws SQLException {
        return studyGroupDao.insertReview(groupReview);
                 
    }
   
    //강의 후기 가져오기
    public List< StudyGroupReview> getReviewsByGroupId(Long groupId) throws SQLException {
        return  studyGroupDao.getReviewsByGroupId(groupId);
    }
    
    
   
  
}
