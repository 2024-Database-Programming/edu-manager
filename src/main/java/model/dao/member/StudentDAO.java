package model.dao.member;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.dao.JDBCUtil;
import model.domain.member.Student;
import model.domain.member.Teacher;

public class StudentDAO {
	private JDBCUtil jdbcUtil = null;

	public StudentDAO() {
		jdbcUtil = new JDBCUtil(); // JDBCUtil 객체 생성
	}

	/**
	 * 사용자 관리 테이블에 새로운 사용자 생성.
	 */
	public int create(Student student) throws SQLException {
		String sql = "INSERT INTO STUDENT VALUES (?, ?, ?, ?, ?, ?)";
		Object[] param = new Object[] { student.getId(), student.getPwd(), student.getName(), student.getEmail(),
				student.getPhone(), student.getAgeRange() };
		jdbcUtil.setSqlAndParameters(sql, param); // JDBCUtil 에 insert문과 매개 변수 설정

		try {
			int result = jdbcUtil.executeUpdate(); // insert 문 실행
			return result;
		} catch (Exception ex) {
			jdbcUtil.rollback();
			ex.printStackTrace();
		} finally {
			jdbcUtil.commit();
			jdbcUtil.close(); // resource 반환
		}
		return 0;
	}

	/**
	 * 기존의 사용자 정보를 수정. (이름, 전화번호)
	 */
	public int update(Student student) throws SQLException {
		String sql = "UPDATE STUDENT " + "SET pwd=?, email=?, phone=? " + "WHERE id=?";
		Object[] param = new Object[] { student.getPwd(), student.getEmail(), student.getPhone(), student.getId() };

		jdbcUtil.setSqlAndParameters(sql, param); // JDBCUtil에 update문과 매개 변수 설정

		try {
			int result = jdbcUtil.executeUpdate(); // update 문 실행
			return result;
		} catch (Exception ex) {
			jdbcUtil.rollback();
			ex.printStackTrace();
		} finally {
			jdbcUtil.commit();
			jdbcUtil.close(); // resource 반환
		}
		return 0;
	}

	/**
	 * 사용자 ID에 해당하는 사용자를 삭제.
	 */
	public int remove(String id) throws SQLException {
		String sql = "DELETE FROM STUDENT WHERE id=?";
		jdbcUtil.setSqlAndParameters(sql, new Object[] { id }); // JDBCUtil에 delete문과 매개 변수 설정

		try {
			int result = jdbcUtil.executeUpdate(); // delete 문 실행
			return result;
		} catch (Exception ex) {
			jdbcUtil.rollback();
			ex.printStackTrace();
		} finally {
			jdbcUtil.commit();
			jdbcUtil.close(); // resource 반환
		}
		return 0;
	}

	/**
	 * 주어진 사용자 ID에 해당하는 사용자 정보를 데이터베이스에서 찾아 Member 도메인 클래스에 저장하여 반환.
	 */
	public Student findStudent(String id) throws SQLException {
		String sql = "SELECT pwd, name, email, phone, ageRange " + "FROM STUDENT " + "WHERE id = ? ";
		jdbcUtil.setSqlAndParameters(sql, new Object[] { id }); // JDBCUtil에 query문과 매개 변수 설정

		try {
			ResultSet rs = jdbcUtil.executeQuery(); // query 실행
			if (rs.next()) { // 학생 정보 발견
				
				Student student = new Student( // User 객체를 생성하여 학생 정보를 저장
						id, rs.getString("pwd"), rs.getString("name"), rs.getString("email"), rs.getString("phone"),
						rs.getString("ageRange"));
				return student;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.close(); // resource 반환
		}
		return null;
	}

	/**
	 * 전체 사용자 정보를 검색하여 List에 저장 및 반환
	 */
	public List<Student> findStudentList() throws SQLException {
		String sql = "SELECT id, name, email, phone, ageRange ";
		jdbcUtil.setSqlAndParameters(sql, null); // JDBCUtil에 query문 설정

		try {
			ResultSet rs = jdbcUtil.executeQuery(); // query 실행
			List<Student> memberList = new ArrayList<Student>(); // User들의 리스트 생성
			while (rs.next()) {
				Student student = new Student( // User 객체를 생성하여 현재 행의 정보를 저장
						rs.getString("id"), null, rs.getString("name"), rs.getString("email"), rs.getString("phone"),
						rs.getString("ageRange"));
				memberList.add(student); // List에 User 객체 저장
			}
			return memberList;

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.close(); // resource 반환
		}
		return null;
	}

	/**
	 * 전체 사용자 정보를 검색한 후 현재 페이지와 페이지당 출력할 사용자 수를 이용하여 해당하는 사용자 정보만을 List에 저장하여 반환.
	 */
	public List<Student> findStudentList(int currentPage, int countPerPage) throws SQLException {
		String sql = "SELECT id, name, email, phone, ageRange" + "FROM STUDENT " + "ORDER BY id";
		jdbcUtil.setSqlAndParameters(sql, null, // JDBCUtil에 query문 설정
				ResultSet.TYPE_SCROLL_INSENSITIVE, // cursor scroll 가능
				ResultSet.CONCUR_READ_ONLY);

		try {
			ResultSet rs = jdbcUtil.executeQuery(); // query 실행
			int start = ((currentPage - 1) * countPerPage) + 1; // 출력을 시작할 행 번호 계산
			if ((start >= 0) && rs.absolute(start)) { // 커서를 시작 행으로 이동
				List<Student> studentList = new ArrayList<Student>(); // User들의 리스트 생성
				do {
					
					Student student = new Student( // User 객체를 생성하여 현재 행의 정보를 저장
							rs.getString("id"), null, rs.getString("name"), rs.getString("email"),
							rs.getString("phone"), rs.getString("ageRange"));
					studentList.add(student); // 리스트에 User 객체 저장
				} while ((rs.next()) && (--countPerPage > 0));
				return studentList;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.close(); // resource 반환
		}
		return null;
	}

	/**
	 * 주어진 사용자 ID에 해당하는 사용자가 존재하는지 검사
	 */
	public boolean existingStudent(String id) throws SQLException {
		String sql = "SELECT count(*) FROM STUDENT WHERE id=?";
		jdbcUtil.setSqlAndParameters(sql, new Object[] { id }); // JDBCUtil에 query문과 매개 변수 설정

		try {
			ResultSet rs = jdbcUtil.executeQuery(); // query 실행
			if (rs.next()) {
				int count = rs.getInt(1);
				return (count == 1 ? true : false);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			jdbcUtil.close(); // resource 반환
		}
		return false;
	}
}
