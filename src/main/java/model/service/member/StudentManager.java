package model.service.member;

import java.sql.SQLException;
import java.util.List;

import model.dao.member.StudentDAO;
import model.domain.member.Student;

/**
 * 사용자 관리 API를 사용하는 개발자들이 직접 접근하게 되는 클래스. UserDAO를 이용하여 데이터베이스에 데이터 조작 작업이 가능하도록
 * 하며, 데이터베이스의 데이터들을 이용하여 비지니스 로직을 수행하는 역할을 한다. 비지니스 로직이 복잡한 경우에는 비지니스 로직만을 전담하는
 * 클래스를 별도로 둘 수 있다.
 */
public class StudentManager {
	private static StudentManager eduManager = new StudentManager();
	private StudentDAO studentDAO;

	private StudentAnalysis studentAnalysis;

	private StudentManager() {
		try {
			studentDAO = new StudentDAO();
			// memberAnalysis = new MemberAnalysis(studentDAO);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static StudentManager getInstance() {
		return eduManager;
	}

	public int create(Student student) throws SQLException, ExistingMemberException {
		if (studentDAO.existingStudent(student.getId()) == true) {
			throw new ExistingMemberException(student.getId() + "는 존재하는 아이디입니다.");
		}
		return studentDAO.create(student);
	}

	public int update(Student student) throws SQLException, MemberNotFoundException {
//        int oldCommId = findMember(member.getMemberId()).getCommId();
//        if (user.getCommId() != oldCommId) { // 소속 커뮤티니가 변경됨
//            Community comm = commDAO.findCommunity(oldCommId); // 기존 소속 커뮤니티
//            if (comm != null && user.getUserId().equals(comm.getChairId())) {
//                // 사용자가 기존 소속 커뮤니티의 회장인 경우 -> 그 커뮤니티의 회장을 null로 변경 및 저장
//                comm.setChairId(null);
//                commDAO.updateChair(comm);
//            }
//        }
		return studentDAO.update(student);
	}

	public int remove(String id) throws SQLException, MemberNotFoundException {
//        int commId = findUser(userId).getCommId();
//        Community comm = commDAO.findCommunity(commId); // 소속 커뮤니티
//        if (comm != null && userId.equals(comm.getChairId())) {
//            // 사용자가 소속 커뮤니티의 회장인 경우 -> 그 커뮤니티의 회장을 null로 변경 및 저장
//            comm.setChairId(null);
//            commDAO.updateChair(comm);
//        }
		return studentDAO.remove(id);
	}

	public Student findMember(String id) throws SQLException, MemberNotFoundException {
		Student user = studentDAO.findStudent(id);

		if (user == null) {
			throw new MemberNotFoundException(id + "는 존재하지 않는 아이디입니다.");
		}
		return user;
	}

	public List<Student> findStudentList() throws SQLException {
		return studentDAO.findStudentList();
	}

	public List<Student> findStudentList(int currentPage, int countPerPage) throws SQLException {
		return studentDAO.findStudentList(currentPage, countPerPage);
	}

	public boolean login(String id, String pwd)
			throws SQLException, MemberNotFoundException, PasswordMismatchException {
		Student student = studentDAO.findStudent(id);

		if (!student.matchPassword(pwd)) {
			throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
		}
		return true;
	}

	public List<Student> makeFriends(String id) throws Exception {
		return studentAnalysis.recommendFriends(id);
	}

	public StudentDAO getStudentDAO() {
		return this.studentDAO;
	}
	
	
	public boolean existingStudent(String studenId) throws SQLException {
       
        return studentDAO.existingStudent(studenId);
    }
}
