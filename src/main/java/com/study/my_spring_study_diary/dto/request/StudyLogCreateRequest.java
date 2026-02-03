package com.study.my_spring_study_diary.dto.request;

import java.time.LocalDateTime;

public class StudyLogCreateRequest {

    private String title;
    private String content;
    private String category;
    private String understanding;
    private Integer studyTime;
    private LocalDateTime studyDate;

    //기본 생성자(JSON 역직렬화를 위해 필요)
     public StudyLogCreateRequest(){
     }

     //Getter메서드들
    public String getTitle(){return title;}
    public String getContent(){return content;}
    public String getCategory(){return category;}
    public String getUnderstanding(){return understanding;}
    public Integer getStudyTime(){return studyTime;}
    public LocalDateTime getStudyDate(){return studyDate;}

    //setter 메서드들
    public void setTitle(String title){this.title = title;}
    public void setContent(String content){this.content = content;}
    public void setCategory(String category){this.category = category;}
    public void setUnderstanding(String understanding){this.understanding = understanding;}
    public void setStudyTime(Integer studyTime){this.studyTime = studyTime;}
    public void setStudyDate(LocalDateTime studyDate){this.studyDate = studyDate;}

}
