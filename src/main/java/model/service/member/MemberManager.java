package model.service.member;

import java.sql.SQLException;
import java.util.List;

import model.dao.member.MemberDAO;
import model.domain.member.Member;

/**
 * 사용자 관리 API를 사용하는 개발자들이 직접 접근하게 되는 클래스. UserDAO를 이용하여 데이터베이스에 데이터 조작 작업이 가능하도록
 * 하며, 데이터베이스의 데이터들을 이용하여 비지니스 로직을 수행하는 역할을 한다. 비지니스 로직이 복잡한 경우에는 비지니스 로직만을 전담하는
 * 클래스를 별도로 둘 수 있다.
 */
public class MemberManager {
	private static MemberManager eduManager = new MemberManager();
	private MemberDAO memberDAO;

	private MemberAnalysis memberAnalysis;

	private MemberManager() {
		try {
			memberDAO = new MemberDAO();
			memberAnalysis = new MemberAnalysis(memberDAO);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static MemberManager getInstance() {
		return eduManager;
	}

	public int create(Member member) throws SQLException, ExistingMemberException {
		if (memberDAO.existingMember(member.getId()) == true) {
			throw new ExistingMemberException(member.getId() + "는 존재하는 아이디입니다.");
		}
		return memberDAO.create(member);
	}

	public int update(Member member) throws SQLException, MemberNotFoundException {
//        int oldCommId = findMember(member.getMemberId()).getCommId();
//        if (user.getCommId() != oldCommId) { // 소속 커뮤티니가 변경됨
//            Community comm = commDAO.findCommunity(oldCommId); // 기존 소속 커뮤니티
//            if (comm != null && user.getUserId().equals(comm.getChairId())) {
//                // 사용자가 기존 소속 커뮤니티의 회장인 경우 -> 그 커뮤니티의 회장을 null로 변경 및 저장
//                comm.setChairId(null);
//                commDAO.updateChair(comm);
//            }
//        }
		return memberDAO.update(member);
	}

	public int remove(String id) throws SQLException, MemberNotFoundException {
//        int commId = findUser(userId).getCommId();
//        Community comm = commDAO.findCommunity(commId); // 소속 커뮤니티
//        if (comm != null && userId.equals(comm.getChairId())) {
//            // 사용자가 소속 커뮤니티의 회장인 경우 -> 그 커뮤니티의 회장을 null로 변경 및 저장
//            comm.setChairId(null);
//            commDAO.updateChair(comm);
//        }
		return memberDAO.remove(id);
	}

	public Member findMember(String id) throws SQLException, MemberNotFoundException {
		Member member = memberDAO.findMember(id);

		if (member == null) {
			throw new MemberNotFoundException(id + "는 존재하지 않는 아이디입니다.");
		}
		return member;
	}

	public String findName(String id) throws SQLException, MemberNotFoundException {
		String name = memberDAO.findName(id);

		if (name == null) {
			throw new MemberNotFoundException(id + "는 존재하지 않는 아이디입니다.");
		}
		return name;
	}

	public List<Member> findMemberList() throws SQLException {
		return memberDAO.findMemberList();
	}

	public List<Member> findMemberList(int currentPage, int countPerPage) throws SQLException {
		return memberDAO.findMemberList(currentPage, countPerPage);
	}

	public boolean login(String id, String pwd)
			throws SQLException, MemberNotFoundException, PasswordMismatchException {
		Member member = findMember(id);

		if (!member.matchPassword(pwd)) {
			throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
		}
		return true;
	}

	public List<Member> makeFriends(String id) throws Exception {
		return memberAnalysis.recommendFriends(id);
	}

	public MemberDAO getMemberDAO() {
		return this.memberDAO;
	}
}
