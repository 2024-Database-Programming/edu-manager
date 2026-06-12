package model.domain.lecture;

public class LectureSchedule {
    private long lectureScheduleId;
    private String dayOfWeek;
    private int frequency;
    private Lecture lectureId;

    public long getLectureScheduleId() {
        return lectureScheduleId;
    }

    public void setLectureScheduleId(long lectureScheduleId) {
        this.lectureScheduleId = lectureScheduleId;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public Lecture getLectureId() {
        return lectureId;
    }

    public void setLectureId(Lecture lectureId) {
        this.lectureId = lectureId;
    }

    @Override
    public String toString() {
        return "LectureSchedule [lectureScheduleId=" + lectureScheduleId + ", dayOfWeek=" + dayOfWeek + ", frequency="
                + frequency + ", lectureId=" + lectureId + "]";
    }

}
