package model.dao;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.domain.Attachment;

public class AttachmentDao {
	public int save(Attachment attachment) {
		ConnectionManager cm = new ConnectionManager();
		Connection conn = cm.getConnection();
		try {
			conn.setAutoCommit(false);
			int id = nextId(conn);
			try (PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO ITEMATTACHMENT "
							+ "(ATTACHMENTID, OWNERTYPE, ITEMTYPE, ITEMID, FILENAME, CONTENTTYPE, FILEDATA, CREATEDAT) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, SYSDATE)")) {
				ps.setInt(1, id);
				ps.setString(2, attachment.getOwnerType());
				ps.setString(3, attachment.getItemType());
				ps.setInt(4, attachment.getItemId());
				ps.setString(5, attachment.getFileName());
				ps.setString(6, attachment.getContentType());
				ps.setBinaryStream(7, new ByteArrayInputStream(attachment.getData()), attachment.getData().length);
				ps.executeUpdate();
			}
			conn.commit();
			return id;
		} catch (Exception ex) {
			try {
				conn.rollback();
			} catch (Exception ignore) {
			}
			throw new RuntimeException("첨부파일 저장에 실패했습니다.", ex);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception ignore) {
			}
		}
	}

	public List<Attachment> findByItem(String ownerType, String itemType, int itemId) {
		ConnectionManager cm = new ConnectionManager();
		Connection conn = cm.getConnection();
		List<Attachment> attachments = new ArrayList<>();
		try (PreparedStatement ps = conn.prepareStatement(
				"SELECT ATTACHMENTID, OWNERTYPE, ITEMTYPE, ITEMID, FILENAME, CONTENTTYPE, CREATEDAT "
						+ "FROM ITEMATTACHMENT "
						+ "WHERE OWNERTYPE = ? AND ITEMTYPE = ? AND ITEMID = ? "
						+ "ORDER BY ATTACHMENTID")) {
			ps.setString(1, ownerType);
			ps.setString(2, itemType);
			ps.setInt(3, itemId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					attachments.add(map(rs, false));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception ignore) {
			}
		}
		return attachments;
	}

	public Attachment findById(int id) {
		ConnectionManager cm = new ConnectionManager();
		Connection conn = cm.getConnection();
		try (PreparedStatement ps = conn.prepareStatement(
				"SELECT ATTACHMENTID, OWNERTYPE, ITEMTYPE, ITEMID, FILENAME, CONTENTTYPE, FILEDATA, CREATEDAT "
						+ "FROM ITEMATTACHMENT WHERE ATTACHMENTID = ?")) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return map(rs, true);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception ignore) {
			}
		}
		return null;
	}

	public void deleteByItem(String ownerType, String itemType, int itemId) {
		ConnectionManager cm = new ConnectionManager();
		Connection conn = cm.getConnection();
		try (PreparedStatement ps = conn.prepareStatement(
				"DELETE FROM ITEMATTACHMENT WHERE OWNERTYPE = ? AND ITEMTYPE = ? AND ITEMID = ?")) {
			conn.setAutoCommit(false);
			ps.setString(1, ownerType);
			ps.setString(2, itemType);
			ps.setInt(3, itemId);
			ps.executeUpdate();
			conn.commit();
		} catch (Exception ex) {
			try {
				conn.rollback();
			} catch (Exception ignore) {
			}
			throw new RuntimeException("첨부파일 삭제에 실패했습니다.", ex);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception ignore) {
			}
		}
	}

	private int nextId(Connection conn) throws Exception {
		try (PreparedStatement ps = conn.prepareStatement("SELECT SEQ_ITEM_ATTACHMENT_ID.nextval FROM dual");
				ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				return rs.getInt(1);
			}
		}
		throw new IllegalStateException("첨부파일 식별자를 생성하지 못했습니다.");
	}

	private Attachment map(ResultSet rs, boolean includeData) throws Exception {
		Attachment attachment = new Attachment();
		attachment.setId(rs.getInt("ATTACHMENTID"));
		attachment.setOwnerType(rs.getString("OWNERTYPE"));
		attachment.setItemType(rs.getString("ITEMTYPE"));
		attachment.setItemId(rs.getInt("ITEMID"));
		attachment.setFileName(rs.getString("FILENAME"));
		attachment.setContentType(rs.getString("CONTENTTYPE"));
		Timestamp createdAt = rs.getTimestamp("CREATEDAT");
		if (createdAt != null) {
			attachment.setCreatedAt(createdAt.toLocalDateTime());
		}
		if (includeData) {
			Blob blob = rs.getBlob("FILEDATA");
			if (blob != null) {
				attachment.setData(blob.getBytes(1, (int) blob.length()));
			}
		}
		return attachment;
	}
}
