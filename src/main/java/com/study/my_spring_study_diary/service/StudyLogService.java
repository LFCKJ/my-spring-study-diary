package com.study.my_spring_study_diary.service;

import com.study.my_spring_study_diary.dto.request.StudyLogCreateRequest;
import com.study.my_spring_study_diary.dto.request.StudyLogUpdateRequest;
import com.study.my_spring_study_diary.dto.response.StudyLogDeleteResponse;
import com.study.my_spring_study_diary.dto.response.StudyLogResponse;
import com.study.my_spring_study_diary.entity.Category;
import com.study.my_spring_study_diary.entity.StudyLog;
import com.study.my_spring_study_diary.entity.Understanding;
import com.study.my_spring_study_diary.exception.StudyLogNotFoundException;
import com.study.my_spring_study_diary.repository.StudyLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class StudyLogService {
    //의존성 주입
    private final StudyLogRepository studyLogRepository;

    //생성자 주입
    public StudyLogService(StudyLogRepository studyLogRepository) {
        this.studyLogRepository = studyLogRepository;
    }


    public StudyLogResponse createStudyLog(StudyLogCreateRequest request) {

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
    public List<StudyLogResponse> getAllStudyLogs() {
        List<StudyLog> studyLogs = studyLogRepository.findAll();
        //Entity 리스트 -> Response DTO 리스트로 변환
        return studyLogs.stream()
                .map(StudyLogResponse::from)
                .collect(Collectors.toList());
    }

    //ID로 학습 일지 단건 조회
    public StudyLogResponse getStudyLogById(Long id) {
        StudyLog studyLog = studyLogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 학습 일지를 찾을 수 없습니다(id:" +
                        id + ")"));
        return StudyLogResponse.from(studyLog);
    }

    //날짜별 학습일지 조회
    public List<StudyLogResponse> getStudyLogsByDate(LocalDate date) {
        List<StudyLog> studyLogs = studyLogRepository.findByStudyDate(date);
        return studyLogs.stream()
                .map(StudyLogResponse::from)
                .collect(Collectors.toList());
    }

    //카테고리별 학습 일지 조회
    public List<StudyLogResponse> getStudyLogsByCategory(String categoryName) {
        //문자열 ->  Enum변환(유효성 검증 포함)
        Category category;
        try {
            category = Category.valueOf(categoryName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 카테고리입니다: " + categoryName);
        }
        List<StudyLog> studyLogs = studyLogRepository.findByCategory(category);
        return studyLogs.stream()
                .map(StudyLogResponse::from)
                .collect(Collectors.toList());

    }

    /*
    학습 일지 수정
    @param id 수정할 학습 일지 id
    @param request 수정 요청 데이터
    @return 수정된 학습 일지 응답
     */
    public StudyLogResponse updateStudyLog(Long id, StudyLogUpdateRequest request) {
        //1.기존 학습 일지 조회
        StudyLog studyLog = studyLogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 학습 일지를 찾을 수 없습니다(id:" + id + ")"));
        //2.수정할 내용이 있는지 확인
        if (request.hasNoUpdates()) {
            throw new IllegalArgumentException("수정할 내용이 없습니다.");
        }
        //3.수정할 값들의 유효성 검증
        validateUpdateRequest(request);
        //4.카테고리와 이해도 변환(NULL이 아닌 경우에만)
        Category category = null;
        if (request.getCategory() != null) {
            try {
                category = Category.valueOf(request.getCategory().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 카테고리입니다." + request.getCategory());
            }
        }
        Understanding understanding = null;
        if (request.getUnderstanding() != null) {
            try {
                understanding = Understanding.valueOf(request.getUnderstanding().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 이해도입니다." + request.getUnderstanding());
            }
        }
        //5.Entity 업데이트(NULL이 아닌 값만 반영)
        studyLog.update(
                request.getTitle(),
                request.getContent(),
                category,
                understanding,
                request.getStudyTime(),
                request.getStudyDate()
        );
        //6.저장 및 응답 반환'
        StudyLog updatedStudyLog = studyLogRepository.update(studyLog);
        return StudyLogResponse.from(updatedStudyLog);
    }

    //====Validation====
    private void validateCreateRequest(StudyLogCreateRequest request) {
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("학습 주제는 필수입니다");
        }
        if (request.getTitle().length() > 100) {
            throw new IllegalArgumentException("학습 주제는 100자를 초과할 수 없습니다.");
        }
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("학습 내용은 필수입니다.");
        }
        if (request.getContent().length() > 1000) {
            throw new IllegalArgumentException("학습 내용은 1000자를 초과할 수 없습니다.");
        }
        if (request.getStudyTime() == null || request.getStudyTime() < 1) {
            throw new IllegalArgumentException("학습 시간은 1분 이상이어야합니다.");
        }
    }

    /*
    수정 요청 유효성 검증
    Null이 아닌 값만 검증합니다.
     */
    private void validateUpdateRequest(StudyLogUpdateRequest request) {
        if (request.getTitle() != null) {
            if (request.getTitle().trim().isEmpty()) {
                throw new IllegalArgumentException("학습 주제는 빈 값일 수 없습니다.");
            }
            if (request.getTitle().length() > 100) {
                throw new IllegalArgumentException("학습 주제는 100자를 초과할 수 없습니다.");
            }
        }
        if (request.getContent() != null) {
            if (request.getContent().trim().isEmpty()) {
                throw new IllegalArgumentException("학습 내용는 빈 값일 수 없습니다.");
            }
            if (request.getContent().length() > 1000) {
                throw new IllegalArgumentException("학습 주제는 1000자를 초과할 수 없습니다.");
            }
        }
        if (request.getStudyTime() != null && request.getStudyTime() < 1) {
            throw new IllegalArgumentException("학습 시간은 1분 이상이어야합니다.");
        }
        if (request.getStudyDate() == null && request.getStudyDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("학습 날짜는 미래일 수 없습니다.");
        }
    }
    //==============DELETE
    /*
    학습 일지를 삭제한다.
    @param id삭제할 학습 일지 ID
    @return 삭제 결과 응답
    @throws StudyLogNotFoundException해당 Id의 학습 일지가 없는 경우
     */
    public StudyLogDeleteResponse deleteStudyLog(Long id){
        //1.존재 여부 확인
        if(!studyLogRepository.existsById(id)){
            throw new StudyLogNotFoundException(id);
        }
        //2.삭제 수행
        studyLogRepository.deleteById(id);
        //3.삭제 결과 반환
        return StudyLogDeleteResponse.of(id);
    }
}
