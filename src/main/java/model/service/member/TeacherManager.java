package model.service.member;

import java.sql.SQLException;
import java.util.List;

import model.dao.member.TeacherDAO;
import model.domain.member.Teacher;

/**
 * 사용자 관리 API를 사용하는 개발자들이 직접 접근하게 되는 클래스. UserDAO를 이용하여 데이터베이스에 데이터 조작 작업이 가능하도록
 * 하며, 데이터베이스의 데이터들을 이용하여 비지니스 로직을 수행하는 역할을 한다. 비지니스 로직이 복잡한 경우에는 비지니스 로직만을 전담하는
 * 클래스를 별도로 둘 수 있다.
 */
public class TeacherManager {
	private static TeacherManager eduManager = new TeacherManager();
	private TeacherDAO teacherDAO;

	private TeacherAnalysis teacherAnalysis;

	private TeacherManager() {
		try {
			teacherDAO = new TeacherDAO();
			teacherAnalysis = new TeacherAnalysis(teacherDAO);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static TeacherManager getInstance() {
		return eduManager;
	}

	public int create(Teacher teacher) throws SQLException, ExistingMemberException {
		if (teacherDAO.existingTeacher(teacher.getId()) == true) {
			throw new ExistingMemberException(teacher.getId() + "는 존재하는 아이디입니다.");
		}
		int result = teacherDAO.create(teacher);

		return result;
	}

	public int update(Teacher teacher) throws SQLException, MemberNotFoundException {
//        int oldCommId = findMember(member.getMemberId()).getCommId();
//        if (user.getCommId() != oldCommId) { // 소속 커뮤티니가 변경됨
//            Community comm = commDAO.findCommunity(oldCommId); // 기존 소속 커뮤니티
//            if (comm != null && user.getUserId().equals(comm.getChairId())) {
//                // 사용자가 기존 소속 커뮤니티의 회장인 경우 -> 그 커뮤니티의 회장을 null로 변경 및 저장
//                comm.setChairId(null);
//                commDAO.updateChair(comm);
//            }
//        }
		return teacherDAO.update(teacher);
	}

	public int remove(String id) throws SQLException, MemberNotFoundException {
//        int commId = findUser(userId).getCommId();
//        Community comm = commDAO.findCommunity(commId); // 소속 커뮤니티
//        if (comm != null && userId.equals(comm.getChairId())) {
//            // 사용자가 소속 커뮤니티의 회장인 경우 -> 그 커뮤니티의 회장을 null로 변경 및 저장
//            comm.setChairId(null);
//            commDAO.updateChair(comm);
//        }
		return teacherDAO.remove(id);
	}

	public Teacher findTeacher(String id) throws SQLException, MemberNotFoundException {
		Teacher teacher = teacherDAO.findTeacher(id);

		if (teacher == null) {
			throw new MemberNotFoundException(id + "는 존재하지 않는 아이디입니다.");
		}
		return teacher;
	}
	public Boolean isTeacher(String id) throws SQLException, MemberNotFoundException {
		Teacher teacher = teacherDAO.findTeacher(id);

		if (teacher == null) {
			return false;
		}
		return true;
	}

	public List<Teacher> findTeacherList() throws SQLException {
		return teacherDAO.findTeacherList();
	}

	public List<Teacher> findTeacherList(int currentPage, int countPerPage) throws SQLException {
		return teacherDAO.findTeacherList(currentPage, countPerPage);
	}

	public boolean login(String id, String pwd)
			throws SQLException, MemberNotFoundException, PasswordMismatchException {
		Teacher teacher = findTeacher(id);

		if (!teacher.matchPassword(pwd)) {
			throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
		}
		return true;
	}

	public List<Teacher> makeFriends(String id) throws Exception {
		return teacherAnalysis.recommendFriends(id);
	}

	public TeacherDAO getTeacherDAO() {
		return this.teacherDAO;
	}
}
