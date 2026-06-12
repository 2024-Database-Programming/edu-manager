package model.domain.lecture;

import java.sql.Date;

public class Lecture {
    private long lectureId;
    private String name;
    private String img;
    private String category;
    private long capacity;
    private int level;
    private String description;
    private Date createAt;
    private String teacherId;
    private String teacherName;
    private Integer lectureRoom;
    private String categoryName;
    private String categoryColor;
    // 전화번호
    private String phone;

    public Lecture() {
    }

    // 변수 다 있는 생성자
    public Lecture(long lectureId, String name, String img, String category, long capacity, int level,
            String description, Date createAt, String teacherId, String teacherName, Integer lectureRoom,
            String phone, String categoryName,String categoryColor) {
        super();
        this.lectureId = lectureId;
        this.name = name;
        this.img = img;
        this.category = category;
        this.capacity = capacity;
        this.level = level;
        this.description = description;
        this.createAt = createAt;
        this.teacherId = teacherId;
        this.setTeacherName(teacherName);
        this.lectureRoom = lectureRoom;
        this.phone = phone;
        this.setCategoryName(categoryName);
        this.setCategoryColor(categoryColor);
       
    }

    // create,update에 사용
    public Lecture(Long lectureId, String name, String img, String category, long capacity, int level,
            String description, String teacherId, Integer lectureRoom) {
        super();
        this.lectureId = lectureId;
        this.name = name;
        this.img = img;
        this.category = category;
        this.capacity = capacity;
        this.level = level;
        this.description = description;
        this.teacherId = teacherId;
        this.lectureRoom = lectureRoom;
    }

    // 위에거에서 phone 추가
    public Lecture(Long lectureId, String name, String img, String category, long capacity, int level,
            String description, String teacherId, Integer lectureRoom, String phone) {
        super();
        this.lectureId = lectureId;
        this.name = name;
        this.img = img;
        this.category = category;
        this.capacity = capacity;
        this.level = level;
        this.description = description;
        this.teacherId = teacherId;
        this.lectureRoom = lectureRoom;
        this.phone = phone;
    }

    public long getLectureId() {
        return lectureId;
    }

    public void setLectureId(long lectureId) {
        this.lectureId = lectureId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getCapacity() {
        return capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLectureRoom() {
        return lectureRoom;
    }

    public void setLectureRoom(Integer lectureRoom) {
        this.lectureRoom = lectureRoom;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    @Override
    public String toString() {
        return "Lecture [lectureId=" + lectureId + ", name=" + name + ", img=" + img + ", category=" + category
                + ", capacity=" + capacity + ", level=" + level + ", description=" + description + ", createAt="
                + createAt + ", teacherId=" + teacherId + ", teacherName=" + teacherName + ", lectureRoom="
                + lectureRoom + "]";
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCategoryName() {
        return categoryName;
    }

    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public String getCategoryColor() {
        return categoryColor;
    }

    
    public void setCategoryColor(String categoryColor) {
        this.categoryColor = categoryColor;
    }
}
