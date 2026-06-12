package model.domain;

import java.time.LocalDate;

public class Notice {
    private int id;
    private String title;
    private String description;
    private LocalDate createat;
    private int lectureId;
    private int studyId;

    
    public Notice(){}

    public Notice(int id, String title, String description, LocalDate createat,
            int lectureId, int studyId) {
        super();
        this.id = id;
        this.title = title;
        this.description = description;
        this.createat = createat;
        this.lectureId = lectureId;
        this.studyId = studyId;
    }



    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
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





    public LocalDate getCreateat() {
        return createat;
    }


    public void setCreateat(LocalDate createat) {
        this.createat = createat;
    }




    public int getLectureId() {
        return lectureId;
    }


    public void setLectureId(int lectureId) {
        this.lectureId = lectureId;
    }

    public int getStudyId() {
        return studyId;
    }

    public void setStudyId(int studyId) {
        this.studyId = studyId;
    }


    @Override
    public String toString() {
        return "Notice [id=" + id + ", title=" + title + ", description=" + description + ", createat=" + createat
                + ", lectureId=" + lectureId + ", studyId=" + studyId + "]";
    }





}
