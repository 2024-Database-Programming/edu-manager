package model.service.member;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.dao.member.MemberDAO;
import model.domain.member.Member;

// an example business class
public class MemberAnalysis {
	private MemberDAO dao;

	public MemberAnalysis() {
	}

	public MemberAnalysis(MemberDAO dao) {
		super();
		this.dao = dao;
	}

	// an example business method
	public List<Member> recommendFriends(String id) throws Exception {
		Member thisuser = dao.findMember(id);
		if (thisuser == null) {
			throw new Exception("invalid user ID!");
		}
		int m1 = id.indexOf('@');
		if (m1 == -1)
			return null;
		String mserver1 = thisuser.getEmail().substring(m1);

		List<Member> friends = new ArrayList<Member>();

		List<Member> memberList = dao.findMemberList(1, 10000);
		Iterator<Member> memberIter = memberList.iterator();
		while (memberIter.hasNext()) {
			Member member = (Member) memberIter.next();

			if (member.getId().equals(id))
				continue;
			int m2 = member.getEmail().indexOf('@');
			if (m2 == -1)
				continue;
			String mserver2 = member.getEmail().substring(m2);

			if (mserver1.equals(mserver2))
				friends.add(member);
		}
		return friends;
	}

}
