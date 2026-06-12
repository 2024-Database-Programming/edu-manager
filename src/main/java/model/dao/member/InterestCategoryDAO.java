package model.dao.member;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.dao.JDBCUtil;

public class InterestCategoryDAO {
    private JDBCUtil jdbcUtil = null;

    public InterestCategoryDAO() {
        jdbcUtil = new JDBCUtil(); // JDBCUtil 객체 생성
    }

    public List<Map<String, Object>> getCategories() {
        List<Map<String, Object>> categories = new ArrayList<>();
        String query = "SELECT id, name FROM interestcategory";

        try {
            jdbcUtil.setSqlAndParameters(query, null); // 쿼리와 매개변수 설정
            ResultSet rs = jdbcUtil.executeQuery(); // 쿼리 실행

            while (rs.next()) {
                Map<String, Object> category = new HashMap<>();
                category.put("id", rs.getInt("id")); // ID
                category.put("name", rs.getString("name")); // 이름
                categories.add(category);
            }
            System.out.println("Categories retrieved: " + categories); // 디버깅 출력
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            jdbcUtil.close(); // 자원 정리
        }

        return categories;
    }

}
