package model.service.member;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.dao.member.TeacherDAO;
import model.domain.member.Teacher;

// an example business class
public class TeacherAnalysis {
	private TeacherDAO dao;

	public TeacherAnalysis() {
	}

	public TeacherAnalysis(TeacherDAO dao) {
		super();
		this.dao = dao;
	}

	// an example business method
	public List<Teacher> recommendFriends(String id) throws Exception {
		Teacher thisuser = dao.findTeacher(id);
		if (thisuser == null) {
			throw new Exception("invalid user ID!");
		}
		int m1 = id.indexOf('@');
		if (m1 == -1)
			return null;
		String mserver1 = thisuser.getEmail().substring(m1);

		List<Teacher> friends = new ArrayList<Teacher>();

		List<Teacher> teacherList = dao.findTeacherList(1, 10000);
		Iterator<Teacher> teacherIter = teacherList.iterator();
		while (teacherIter.hasNext()) {
			Teacher teacher = (Teacher) teacherIter.next();

			if (teacher.getId().equals(id))
				continue;
			int m2 = teacher.getEmail().indexOf('@');
			if (m2 == -1)
				continue;
			String mserver2 = teacher.getEmail().substring(m2);

			if (mserver1.equals(mserver2))
				friends.add(teacher);
		}
		return friends;
	}

}
