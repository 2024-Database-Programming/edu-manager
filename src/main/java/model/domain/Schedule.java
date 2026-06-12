package model.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class Schedule {
	private int scheduleId;
	private String dayOfWeek;
	private LocalTime startTime;
	private LocalTime endTime;
	private String frequency;
	private long studyGroupId;
	private long lectureId;
	private LocalDate startDate;
	private String type;// special:특수 , regular: 정규
	private String title;

	private String lectureName;

	public String getLectureName() {
		return lectureName;
	}

	public void setLectureName(String lectureName) {
		this.lectureName = lectureName;
	}

	// 생성자
	public Schedule() {
	}

	public Schedule(int scheduleId, String dayOfWeek, LocalTime startTime, LocalTime endTime, String frequency,
			long studyGroupId, long lectureId, LocalDate startDate, String type, String title, String lectureName) {
		super();
		this.scheduleId = scheduleId;
		this.dayOfWeek = dayOfWeek;
		this.startTime = startTime;
		this.endTime = endTime;
		this.frequency = frequency;
		this.studyGroupId = studyGroupId;
		this.lectureId = lectureId;
		this.startDate = startDate;
		this.title = title;
		this.type = type;
		this.lectureName = lectureName;
	}

	public Schedule(String dayOfWeek, LocalTime startTime, LocalTime endTime, String frequency, long lectureId,
			String type, String title) {
		super();
		this.dayOfWeek = dayOfWeek;
		this.startTime = startTime;
		this.endTime = endTime;
		this.frequency = frequency;
		this.lectureId = lectureId;
		this.title = title;
		this.type = type;
	}

	public int getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public long getStudyGroupId() {
		return studyGroupId;
	}

	public void setStudyGroupId(long studyGroupId) {
		this.studyGroupId = studyGroupId;
	}

	public long getLectureId() {
		return lectureId;
	}

	public void setLectureId(long lectureId) {
		this.lectureId = lectureId;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Schedule [scheduleId=" + scheduleId + ", dayOfWeek=" + dayOfWeek + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", frequency=" + frequency + ", studyGroupId=" + studyGroupId
				+ ", lectureId=" + lectureId + ", startDate=" + startDate + ", type=" + type + ", title=" + title + "]";
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
