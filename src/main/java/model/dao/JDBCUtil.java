// Java Project 용 JDBCUtil
// DBCP2 관련 jar 파일을 프로젝트에 포함해야 동작함
// commons-dbcp2-X.X.X.jar, commons-pool2-X.X.X.jar, commons-logging-X.X.jar
package model.dao;

import java.sql.*;

/**
 * JDBCUtil — 싱글톤 매니저가 DAO(따라서 이 JDBCUtil 인스턴스)를 공유하므로,
 * conn/pstmt/rs 등 모든 가변 상태를 ThreadLocal로 두어 요청 스레드 간 간섭을 차단한다.
 * 단일 스레드 동작은 기존(인스턴스 필드) 방식과 동일하다.
 */
public class JDBCUtil {
	private final ConnectionManager connMan = new ConnectionManager(); // DBCP 풀 래퍼(공유 안전)

	// 모든 가변 상태를 스레드별로 분리
	private final ThreadLocal<String> sql = new ThreadLocal<>();
	private final ThreadLocal<Object[]> parameters = new ThreadLocal<>();
	private final ThreadLocal<Connection> conn = new ThreadLocal<>();
	private final ThreadLocal<PreparedStatement> pstmt = new ThreadLocal<>();
	private final ThreadLocal<CallableStatement> cstmt = new ThreadLocal<>();
	private final ThreadLocal<ResultSet> rs = new ThreadLocal<>();
	private final ThreadLocal<Integer> resultSetType = ThreadLocal.withInitial(() -> ResultSet.TYPE_FORWARD_ONLY);
	private final ThreadLocal<Integer> resultSetConcurrency = ThreadLocal.withInitial(() -> ResultSet.CONCUR_READ_ONLY);

	// 기본 생성자
	public JDBCUtil() {
	}

	public void setSql(String sql) {
		this.sql.set(sql);
	}

	public String getSql() {
		return this.sql.get();
	}

	// 매개변수 배열에서 특정위치의 매개변수를 반환
	private Object getParameter(int index) throws Exception {
		if (index >= getParameterSize())
			throw new Exception("INDEX 값이 파라미터의 갯수보다 많습니다.");
		return parameters.get()[index];
	}

	// 매개변수의 개수를 반환
	private int getParameterSize() {
		Object[] p = parameters.get();
		return p == null ? 0 : p.length;
	}

	// sql 및 Object[] 변수 setter
	public void setSqlAndParameters(String sql, Object[] parameters) {
		this.sql.set(sql);
		this.parameters.set(parameters);
		this.resultSetType.set(ResultSet.TYPE_FORWARD_ONLY);
		this.resultSetConcurrency.set(ResultSet.CONCUR_READ_ONLY);
	}

	// sql 및 Object[], resultSetType, resultSetConcurrency 변수 setter
	public void setSqlAndParameters(String sql, Object[] parameters, int resultSetType, int resultSetConcurrency) {
		this.sql.set(sql);
		this.parameters.set(parameters);
		this.resultSetType.set(resultSetType);
		this.resultSetConcurrency.set(resultSetConcurrency);
	}

	// 현재 스레드의 Connection 확보(없으면 풀에서 획득)
	private Connection currentConnection() throws SQLException {
		if (conn.get() == null) {
			Connection c = connMan.getConnection();
			c.setAutoCommit(false);
			conn.set(c);
		}
		return conn.get();
	}

	// 현재의 PreparedStatement를 반환
	private PreparedStatement getPreparedStatement() throws SQLException {
		Connection c = currentConnection();
		if (pstmt.get() != null)
			pstmt.get().close();
		PreparedStatement ps = c.prepareStatement(sql.get(), resultSetType.get(), resultSetConcurrency.get());
		pstmt.set(ps);
		return ps;
	}

	// executeQuery 수행
	public ResultSet executeQuery() {
		try {
			PreparedStatement ps = getPreparedStatement();
			for (int i = 0; i < getParameterSize(); i++) {
				ps.setObject(i + 1, getParameter(i));
			}
			ResultSet r = ps.executeQuery();
			rs.set(r);
			return r;
		} catch (Exception ex) {
			// 예외를 삼키면 호출부에서 rs.next() 시 엉뚱한 NPE가 난다 → 원인 그대로 전파
			throw new RuntimeException("executeQuery 실패: " + sql.get(), ex);
		}
	}

	// executeUpdate 수행
	public int executeUpdate() throws SQLException, Exception {
		PreparedStatement ps = getPreparedStatement();
		int parameterSize = getParameterSize();
		for (int i = 0; i < parameterSize; i++) {
			if (getParameter(i) == null) {
				ps.setString(i + 1, null);
			} else {
				ps.setObject(i + 1, getParameter(i));
			}
		}
		return ps.executeUpdate();
	}

	// 현재의 CallableStatement를 반환
	private CallableStatement getCallableStatement() throws SQLException {
		Connection c = currentConnection();
		if (cstmt.get() != null)
			cstmt.get().close();
		CallableStatement cs = c.prepareCall(sql.get());
		cstmt.set(cs);
		return cs;
	}

	// CallableStatement의 execute를 수행
	public boolean execute(JDBCUtil source) throws SQLException, Exception {
		CallableStatement cs = getCallableStatement();
		for (int i = 0; i < source.getParameterSize(); i++) {
			cs.setObject(i + 1, source.getParameter(i));
		}
		return cs.execute();
	}

	// PK 컬럼 이름 배열을 이용하여 PreparedStatement를 생성 (Sequence로 PK 생성하는 INSERT용)
	private PreparedStatement getPreparedStatement(String[] columnNames) throws SQLException {
		Connection c = currentConnection();
		if (pstmt.get() != null)
			pstmt.get().close();
		PreparedStatement ps = c.prepareStatement(sql.get(), columnNames);
		pstmt.set(ps);
		return ps;
	}

	// 위 메소드를 이용하여 PreparedStatement를 생성한 후 executeUpdate 실행
	public int executeUpdate(String[] columnNames) throws SQLException, Exception {
		PreparedStatement ps = getPreparedStatement(columnNames);
		int parameterSize = getParameterSize();
		for (int i = 0; i < parameterSize; i++) {
			if (getParameter(i) == null) {
				ps.setString(i + 1, null);
			} else {
				ps.setObject(i + 1, getParameter(i));
			}
		}
		return ps.executeUpdate();
	}

	// PK 컬럼의 값(들)을 포함하는 ResultSet 객체 구하기
	public ResultSet getGeneratedKeys() {
		try {
			return pstmt.get().getGeneratedKeys();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 자원 반환 (현재 스레드의 자원만 닫고 ThreadLocal 슬롯 제거)
	public void close() {
		try {
			if (rs.get() != null) rs.get().close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			rs.remove();
		}
		try {
			if (pstmt.get() != null) pstmt.get().close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			pstmt.remove();
		}
		try {
			if (cstmt.get() != null) cstmt.get().close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			cstmt.remove();
		}
		try {
			if (conn.get() != null) conn.get().close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			conn.remove();
		}
		sql.remove();
		parameters.remove();
	}

	public void commit() {
		try {
			if (conn.get() != null) conn.get().commit();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public void rollback() {
		try {
			if (conn.get() != null) conn.get().rollback();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	// DataSource 를 종료 (앱 종료 시점 전용 — 요청 처리 중 호출 금지)
	public void shutdownPool() {
		this.close();
		connMan.close();
	}

	// 활성/비활성 Connection 개수 출력
	public void printDataSourceStats() {
		connMan.printDataSourceStats();
	}
}
