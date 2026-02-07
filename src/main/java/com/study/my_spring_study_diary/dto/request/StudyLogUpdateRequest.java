package com.study.my_spring_study_diary.dto.request;

/*
학습 일지 수정 요청 DTO
CREATE와 달리 모든 필드가 선택적
null 이면 기존 값을 유지한다.

 */


import java.time.LocalDate;

public class StudyLogUpdateRequest {
    private String title;
    private String content;
    private String category;
    private String understanding;
    private Integer studyTime;
    private LocalDate studyDate;

    //기본 생성자
    public StudyLogUpdateRequest(){
            }
    //Getter 메서드들
    public String getTitle(){return title;}
    public String getContent(){return content;}
    public String getCategory(){return category;}
    public String getUnderstanding(){return understanding;}
    public Integer getStudyTime(){return studyTime;}
    public LocalDate getStudyDate(){return studyDate;}

    //Setter메서드들
    public void setTitle(String title){this.title = title;}
    public void setContent(String content){this.content = content;}
    public void setCategory(String category){this.category = category;}
    public void setUnderstanding(String understanding){this.understanding = understanding;}
    public void setStudyTime(Integer studyTime){this.studyTime = studyTime;}
    public void setStudyDate(LocalDate studyDate){this.studyDate = studyDate;}

    /*
    모든 필드가 NUll인지 확인
    아무것도 수정할 내용이 없는 경우 체크용
     */
    public boolean hasNoUpdates(){
        return title == null
                &&content == null
                && category == null
                &&understanding == null
                &&studyTime == null
                &&studyDate == null;
    }
}
