package com.study.my_spring_study_diary.service;

import com.study.my_spring_study_diary.dto.request.StudyLogCreateRequest;
import com.study.my_spring_study_diary.dto.response.StudyLogResponse;
import com.study.my_spring_study_diary.entity.Category;
import com.study.my_spring_study_diary.entity.StudyLog;
import com.study.my_spring_study_diary.entity.Understanding;
import com.study.my_spring_study_diary.repository.StudyLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;


@Service
public class StudyLogService {
    //의존성 주입
    private final StudyLogRepository studyLogRepository;

    //생성자 주입
    public StudyLogService(StudyLogRepository studyLogRepository) {
        this.studyLogRepository = studyLogRepository;
    }


    public StudyLogResponse createStudyLog(StudyLogCreateRequest request){

        //1.요청 데이터 유효성 검증
        validateCreateRequest(request);

        //2.DTO -> Entity 변환
        StudyLog studyLog = new StudyLog(
                null,
                request.getTitle(),
                request.getContent(),
                Category.valueOf(request.getCategory()),
                Understanding.valueOf(request.getUnderstanding()),
                request.getStudyTime(),
                request.getStudyDate() != null ? request.getStudyDate() : LocalDate.now()
        );
        //3.저장
        StudyLog savedStudyLog = studyLogRepository.save(studyLog);
        //4.Entity -> Response DTO 변환 후 반환=
        return StudyLogResponse.from(savedStudyLog);
    }

        //전체 학습 일지 목록 조회
    public List<StudyLogResponse> getAllStudyLogs(){
        List<StudyLog>studyLogs = studyLogRepository.findAll();
        //Entity 리스트 -> Response DTO 리스트로 변환
        return studyLogs.stream()
                .map(StudyLogResponse::from)
                .collect(Collectors.toList());
    }

    //ID로 학습 일지 단건 조회
    public StudyLogResponse getStudyLogById(Long id){
        StudyLog studyLog = studyLogRepository.findById(id)
                .orElseThrow(() ->new IllegalArgumentException("해당 학습 일지를 찾을 수 없습니다(id:"+
                id+")"));
        return StudyLogResponse.from(studyLog);
    }
    //날짜별 학습일지 조회
    public List<StudyLogResponse> getStudyLogsByDate(LocalDate date){
        List<StudyLog> studyLogs = studyLogRepository.findByStudyDate(date);
        return studyLogs.stream()
                .map(StudyLogResponse::from)
                .collect(Collectors.toList());
    }
    //카테고리별 학습 일지 조회
    public List<StudyLogResponse>getStudyLogsByCategory(String categoryname){
        //문자열 ->  Enum변환(유효성 검증 포함)
        Category category;
        try{
            category = Category.valueOf(categoryname.toUpperCase());
            }catch(IllegalArgumentException e){
            throw new IllegalArgumentException("유효하지 않은 카테고리입니다: "+categoryname);
        }
        List<StudyLog>studyLogs = studyLogRepository.findByCategory(category);
        return studyLogs.stream()
                .map(StudyLogResponse::from)
                .collect(Collectors.toList());

    }
    private void validateCreateRequest(StudyLogCreateRequest request){
        if(request.getTitle() == null || request.getTitle().trim().isEmpty()){
            throw new IllegalArgumentException("학습 주제는 필수입니다");
        }
        if(request.getTitle().length() > 100){
            throw new IllegalArgumentException("학습 주제는 100자를 초과할 수 없습니다");
        }
        if(request.getContent() ==null ||request.getContent().trim().isEmpty()){
            throw new IllegalArgumentException("학습 내용은 필수입니다");
        }
        if(request.getContent().length() > 1000){
            throw new IllegalArgumentException("학습 내용은 1000자를 초과할 수 없습니다.");
        }
        if(request.getStudyTime() == null || request.getStudyTime() < 1){
            throw new IllegalArgumentException("학습 시간은 1분 이상이어야합니다.");
        }
    }


}
