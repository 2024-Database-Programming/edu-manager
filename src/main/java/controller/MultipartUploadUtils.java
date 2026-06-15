package controller;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import model.dao.AttachmentDao;
import model.domain.Attachment;

public final class MultipartUploadUtils {
	private MultipartUploadUtils() {
	}

	public static void saveAttachments(HttpServletRequest request, String ownerType, String itemType, int itemId)
			throws Exception {
		Collection<Part> parts = request.getParts();
		AttachmentDao attachmentDao = new AttachmentDao();
		for (Part part : parts) {
			if (!"attachments".equals(part.getName()) || part.getSize() <= 0) {
				continue;
			}
			UploadedFile file = readFile(part);
			if (file == null) {
				continue;
			}
			Attachment attachment = new Attachment();
			attachment.setOwnerType(ownerType);
			attachment.setItemType(itemType);
			attachment.setItemId(itemId);
			attachment.setFileName(file.fileName);
			attachment.setContentType(file.contentType);
			attachment.setData(file.data);
			attachmentDao.save(attachment);
		}
	}

	public static UploadedFile readOptionalFile(HttpServletRequest request, String partName) throws Exception {
		Part part = request.getPart(partName);
		if (part == null || part.getSize() <= 0) {
			return null;
		}
		return readFile(part);
	}

	private static UploadedFile readFile(Part part) throws Exception {
		String submittedName = part.getSubmittedFileName();
		if (submittedName == null || submittedName.isBlank()) {
			return null;
		}
		String fileName = submittedName.substring(Math.max(submittedName.lastIndexOf('/'),
				submittedName.lastIndexOf('\\')) + 1);
		return new UploadedFile(fileName, part.getContentType(), part.getInputStream().readAllBytes());
	}

	public static class UploadedFile {
		public final String fileName;
		public final String contentType;
		public final byte[] data;

		public UploadedFile(String fileName, String contentType, byte[] data) {
			this.fileName = fileName;
			this.contentType = contentType;
			this.data = data;
		}
	}
}
