package model.dao;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 이미지(BLOB) 저장/조회 전용 DAO.
 * 기존 테이블을 건드리지 않도록 별도 IMAGE 테이블에 보관한다.
 *   IMAGE(OWNER_TYPE, OWNER_ID, IMG_DATA BLOB, IMG_TYPE, PK(OWNER_TYPE, OWNER_ID))
 *   - OWNER_TYPE : 'member' | 'lecture' | 'study'
 *   - OWNER_ID   : 회원 id 또는 강의/스터디 id(문자열)
 *
 * BLOB는 setBinaryStream / getBlob 로 처리한다(큰 이미지도 안전).
 */
public class ImageDAO {

	/** 업로드한 이미지 바이트를 저장(있으면 교체). */
	public void save(String ownerType, String ownerId, byte[] data, String contentType) {
		ConnectionManager cm = new ConnectionManager();
		Connection conn = cm.getConnection();
		try {
			conn.setAutoCommit(false);

			// 업서트: 기존 행 삭제 후 삽입
			try (PreparedStatement del = conn.prepareStatement(
					"DELETE FROM IMAGE WHERE OWNER_TYPE = ? AND OWNER_ID = ?")) {
				del.setString(1, ownerType);
				del.setString(2, ownerId);
				del.executeUpdate();
			}

			try (PreparedStatement ins = conn.prepareStatement(
					"INSERT INTO IMAGE (OWNER_TYPE, OWNER_ID, IMG_DATA, IMG_TYPE) VALUES (?, ?, ?, ?)")) {
				ins.setString(1, ownerType);
				ins.setString(2, ownerId);
				ins.setBinaryStream(3, new ByteArrayInputStream(data), data.length); // BLOB
				ins.setString(4, contentType);
				ins.executeUpdate();
			}

			conn.commit();
		} catch (Exception ex) {
			try { conn.rollback(); } catch (Exception ignore) {}
			ex.printStackTrace();
		} finally {
			// conn.close()는 커넥션을 풀에 '반납'할 뿐이다.
			// cm.close()는 앱 전체가 공유하는 static 풀 자체를 닫아버리므로 절대 호출하면 안 된다.
			try { if (conn != null) conn.close(); } catch (Exception ignore) {}
		}
	}

	/** 이미지 바이트 + content-type 조회. 없으면 null. */
	public ImageData get(String ownerType, String ownerId) {
		ConnectionManager cm = new ConnectionManager();
		Connection conn = cm.getConnection();
		try (PreparedStatement ps = conn.prepareStatement(
				"SELECT IMG_DATA, IMG_TYPE FROM IMAGE WHERE OWNER_TYPE = ? AND OWNER_ID = ?")) {
			ps.setString(1, ownerType);
			ps.setString(2, ownerId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					byte[] data = null;
					Blob blob = rs.getBlob("IMG_DATA");
					if (blob != null) {
						data = blob.getBytes(1, (int) blob.length());
					}
					String type = rs.getString("IMG_TYPE");
					if (data != null && data.length > 0) {
						return new ImageData(data, type);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// conn.close()는 커넥션을 풀에 '반납'할 뿐이다.
			// cm.close()는 앱 전체가 공유하는 static 풀 자체를 닫아버리므로 절대 호출하면 안 된다.
			try { if (conn != null) conn.close(); } catch (Exception ignore) {}
		}
		return null;
	}

	/** 조회 결과(바이트 + content-type) 홀더. */
	public static class ImageData {
		public final byte[] data;
		public final String contentType;

		public ImageData(byte[] data, String contentType) {
			this.data = data;
			this.contentType = contentType;
		}
	}
}
