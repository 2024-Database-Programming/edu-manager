package controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

import model.domain.Assignment;
import model.domain.Schedule;

public final class CalendarEventJsonUtils {
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
	private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

	private CalendarEventJsonUtils() {
	}

	public static String build(List<Schedule> schedules, List<Assignment> assignments, long targetId, YearMonth month,
			boolean study) {
		StringBuilder json = new StringBuilder("[");
		boolean[] needsComma = new boolean[] { false };

		if (schedules != null) {
			for (Schedule schedule : schedules) {
				long ownerId = study ? schedule.getStudyGroupId() : schedule.getLectureId();
				if (ownerId != targetId || schedule.getStartDate() == null || !YearMonth.from(schedule.getStartDate()).equals(month)) {
					continue;
				}

				String category = scheduleCategory(schedule);
				String label = categoryLabel(category);
				String title = scheduleTitle(schedule, study);
				appendEvent(json, needsComma, schedule.getStartDate(), category, schedule.getScheduleId(), label, title,
						timeRange(schedule.getStartTime(), schedule.getEndTime()));
			}
		}

		if (assignments != null) {
			for (Assignment assignment : assignments) {
				long ownerId = study ? assignment.getStudyId() : assignment.getLectureId();
				if (ownerId != targetId) {
					continue;
				}

				LocalDate start = assignment.getCreateat() != null ? assignment.getCreateat() : assignment.getDueDate();
				LocalDate end = assignment.getDueDate() != null ? assignment.getDueDate() : start;
				if (start == null || end == null) {
					continue;
				}

				LocalDate cursor = start.isBefore(month.atDay(1)) ? month.atDay(1) : start;
				LocalDate rangeEnd = end.isAfter(month.atEndOfMonth()) ? month.atEndOfMonth() : end;
				while (!cursor.isAfter(rangeEnd)) {
					appendEvent(json, needsComma, cursor, "assignment", assignment.getId(), "과제", safeTitle(assignment.getTitle(), "과제"),
							assignmentPeriod(assignment));
					cursor = cursor.plusDays(1);
				}
			}
		}

		json.append("]");
		return json.toString();
	}

	private static void appendEvent(StringBuilder json, boolean[] needsComma, LocalDate date, String category,
			int id, String label, String title, String meta) {
		if (needsComma[0]) {
			json.append(',');
		}
		needsComma[0] = true;
		json.append('{');
		appendField(json, "date", DATE_FORMAT.format(date));
		json.append(',');
		appendField(json, "category", category);
		json.append(',');
		json.append("\"id\":").append(id);
		json.append(',');
		appendField(json, "label", label);
		json.append(',');
		appendField(json, "title", title);
		json.append(',');
		appendField(json, "meta", meta);
		json.append('}');
	}

	private static void appendField(StringBuilder json, String key, String value) {
		json.append('"').append(key).append("\":\"").append(escapeJson(value)).append('"');
	}

	private static String scheduleCategory(Schedule schedule) {
		String type = schedule.getType();
		String title = schedule.getTitle();
		if ("regular".equals(type) || "class".equals(type)) {
			return "class";
		}
		if ("exam".equals(type) || containsExamKeyword(title)) {
			return "exam";
		}
		return "event";
	}

	private static boolean containsExamKeyword(String title) {
		return title != null && (title.contains("시험") || title.contains("중간") || title.contains("기말") || title.contains("평가"));
	}

	private static String categoryLabel(String category) {
		if ("class".equals(category)) {
			return "수업";
		}
		if ("exam".equals(category)) {
			return "시험";
		}
		if ("assignment".equals(category)) {
			return "과제";
		}
		return "일정";
	}

	private static String scheduleTitle(Schedule schedule, boolean study) {
		if (schedule.getTitle() != null && !schedule.getTitle().trim().isEmpty()) {
			return schedule.getTitle();
		}
		if ("regular".equals(schedule.getType())) {
			return study ? "정기모임" : "정기 수업";
		}
		if ("class".equals(schedule.getType())) {
			return "단기 수업";
		}
		if ("exam".equals(schedule.getType())) {
			return "시험";
		}
		return "일정";
	}

	private static String safeTitle(String title, String fallback) {
		return title != null && !title.trim().isEmpty() ? title : fallback;
	}

	private static String timeRange(LocalTime start, LocalTime end) {
		if (start == null && end == null) {
			return "";
		}
		String startText = start != null ? TIME_FORMAT.format(start) : "";
		String endText = end != null ? TIME_FORMAT.format(end) : "";
		if (!startText.isEmpty() && !endText.isEmpty()) {
			return startText + "~" + endText;
		}
		return startText + endText;
	}

	private static String assignmentPeriod(Assignment assignment) {
		StringBuilder period = new StringBuilder();
		if (assignment.getCreateat() != null) {
			period.append(DATE_FORMAT.format(assignment.getCreateat()));
			if (assignment.getStartTime() != null) {
				period.append(' ').append(TIME_FORMAT.format(assignment.getStartTime()));
			}
		}
		if (assignment.getDueDate() != null) {
			if (period.length() > 0) {
				period.append(" ~ ");
			}
			period.append(DATE_FORMAT.format(assignment.getDueDate()));
			if (assignment.getDueTime() != null) {
				period.append(' ').append(TIME_FORMAT.format(assignment.getDueTime()));
			}
		}
		return period.toString();
	}

	private static String escapeJson(String value) {
		if (value == null) {
			return "";
		}
		StringBuilder escaped = new StringBuilder();
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			switch (c) {
			case '\\':
				escaped.append("\\\\");
				break;
			case '"':
				escaped.append("\\\"");
				break;
			case '\n':
				escaped.append("\\n");
				break;
			case '\r':
				escaped.append("\\r");
				break;
			case '\t':
				escaped.append("\\t");
				break;
			case '<':
				escaped.append("\\u003c");
				break;
			case '>':
				escaped.append("\\u003e");
				break;
			case '&':
				escaped.append("\\u0026");
				break;
			default:
				if (c < 0x20) {
					escaped.append(String.format("\\u%04x", (int) c));
				} else {
					escaped.append(c);
				}
			}
		}
		return escaped.toString();
	}
}
