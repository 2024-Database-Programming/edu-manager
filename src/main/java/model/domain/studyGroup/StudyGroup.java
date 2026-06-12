package model.domain.studyGroup;

import java.sql.Date;

public class StudyGroup {

    private long studyGroupId;
    private String name;
    private String img;
    private String description;
    private long capacity;
    private String category;
    private Date createAt;
    private String leaderId;
    private String leaderName;
    private String place;
    private String categoryName;
    private String categoryColor;
    private String dayOfWeek;

    public StudyGroup() {
    }

    public StudyGroup(long studyGroupId, String name, String img, String description, long capacity, String category,
            Date createAt, String leaderId) {
        super();
        this.studyGroupId = studyGroupId;
        this.name = name;
        this.img = img;
        this.description = description;
        this.capacity = capacity;
        this.category = category;
        this.createAt = createAt;
        this.leaderId = leaderId;
    }

    public long getStudyGroupId() {
        return studyGroupId;
    }

    public void setStudyGroupId(long studyGroupId) {
        this.studyGroupId = studyGroupId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCapacity() {
        return capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    @Override
    public String toString() {
        return "StudyGroup [studyGroupId=" + studyGroupId + ", name=" + name + ", img=" + img + ", description="
                + description + ", capacity=" + capacity + ", category=" + category + ", createAt=" + createAt
                + ", leaderId=" + leaderId + "]";
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
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
    
    public String getDayOfWeek() {
        return dayOfWeek;
    }

    
    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

}