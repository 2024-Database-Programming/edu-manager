package controller;

import java.util.Collection;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import model.dao.AttachmentDao;
import model.domain.Attachment;

public final class MultipartUploadUtils {
	private static final long MAX_FILE_SIZE = 10L * 1024 * 1024;
	private static final int MAX_ATTACHMENT_COUNT = 5;
	private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
			"pdf", "txt", "md", "csv", "zip",
			"doc", "docx", "ppt", "pptx", "xls", "xlsx",
			"hwp", "hwpx", "fig",
			"png", "jpg", "jpeg", "gif", "webp");

	private MultipartUploadUtils() {
	}

	public static void saveAttachments(HttpServletRequest request, String ownerType, String itemType, int itemId)
			throws Exception {
		Collection<Part> parts = request.getParts();
		AttachmentDao attachmentDao = new AttachmentDao();
		int savedCount = 0;
		for (Part part : parts) {
			if (!"attachments".equals(part.getName()) || part.getSize() <= 0) {
				continue;
			}
			savedCount++;
			if (savedCount > MAX_ATTACHMENT_COUNT) {
				throw new IllegalArgumentException("첨부파일은 한 번에 최대 " + MAX_ATTACHMENT_COUNT + "개까지 업로드할 수 있습니다.");
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
		if (part.getSize() > MAX_FILE_SIZE) {
			throw new IllegalArgumentException("파일 1개당 최대 10MB까지 업로드할 수 있습니다.");
		}
		String fileName = submittedName.substring(Math.max(submittedName.lastIndexOf('/'),
				submittedName.lastIndexOf('\\')) + 1);
		validateFileName(fileName);
		return new UploadedFile(fileName, part.getContentType(), part.getInputStream().readAllBytes());
	}

	private static void validateFileName(String fileName) {
		if (fileName == null || fileName.isBlank()) {
			throw new IllegalArgumentException("파일명을 확인할 수 없습니다.");
		}
		if (fileName.length() > 255) {
			throw new IllegalArgumentException("파일명이 너무 깁니다. 255자 이하의 파일을 업로드해 주세요.");
		}
		int dotIndex = fileName.lastIndexOf('.');
		if (dotIndex < 0 || dotIndex == fileName.length() - 1) {
			throw new IllegalArgumentException("확장자가 있는 파일만 업로드할 수 있습니다.");
		}
		String extension = fileName.substring(dotIndex + 1).toLowerCase(Locale.ROOT);
		if (!ALLOWED_EXTENSIONS.contains(extension)) {
			throw new IllegalArgumentException("지원하지 않는 파일 형식입니다. PDF, 문서, 이미지, 압축 파일만 업로드해 주세요.");
		}
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
