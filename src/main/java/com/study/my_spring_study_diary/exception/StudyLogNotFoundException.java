package com.study.my_spring_study_diary.exception;

// RuntimeException을 상속받아야 예외로 던질 수 있습니다.
public class StudyLogNotFoundException extends RuntimeException {

    // 1. ID 없이 메시지만 사용하는 경우 (선택 사항)
    public StudyLogNotFoundException() {
        super("학습 일지를 찾을 수 없습니다.");
    }

    // 2. ID를 받아서 메시지를 동적으로 구성하는 생성자 (필요한 부분!)
    public StudyLogNotFoundException(Long id) {
        super("해당 학습 일지를 찾을 수 없습니다. (id: " + id + ")");
    }
}