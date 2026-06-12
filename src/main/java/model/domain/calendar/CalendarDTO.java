package model.domain.calendar;

import java.util.Date;

public class CalendarDTO {   
	private long CalendarDTOId;
	private Date date;
	private String lectureName;
	private String type; // schedule, assignment, notice
	private String title;
	private String description;
	private String color; // Lecture-specific color

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getLectureName() {
		return lectureName;
	}

	public void setLectureName(String lectureName) {
		this.lectureName = lectureName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public CalendarDTO(Date date, String lectureName, String type, String title, String description, String color) {
		super();
		this.date = date;
		this.lectureName = lectureName;
		this.type = type;
		this.title = title;
		this.description = description;
		this.color = color;
	}

}
