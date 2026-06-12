package model.dao.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.dao.JDBCUtil;
import model.domain.member.Student;

public class StudentInterestCategoryDAO {
	private JDBCUtil jdbcUtil = null;

	public StudentInterestCategoryDAO() {
		jdbcUtil = new JDBCUtil(); // JDBCUtil 객체 생성
	}



	public int create(String stuid, int categoryId) throws SQLException {
		String sql = "INSERT INTO studentinterestcategory (stuid, interestid) VALUES (?, ?)";
		
		Object[] param = new Object[] { stuid, categoryId };
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
    
}