package com.study.my_spring_study_diary.controller;

import com.study.my_spring_study_diary.dto.request.StudyLogCreateRequest;
import com.study.my_spring_study_diary.dto.request.StudyLogUpdateRequest; // 추가됨
import com.study.my_spring_study_diary.dto.response.StudyLogDeleteResponse;
import com.study.my_spring_study_diary.dto.response.StudyLogResponse;
import com.study.my_spring_study_diary.global.common.ApiResponse;
import com.study.my_spring_study_diary.service.StudyLogService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/logs")
public class StudyLogController {

    private final StudyLogService studyLogService;

    public StudyLogController(StudyLogService studyLogService) {
        this.studyLogService = studyLogService;
    }

    // ============================== CREATE ==============================
    @PostMapping
    public ResponseEntity<ApiResponse<StudyLogResponse>> createStudyLog(
            @RequestBody StudyLogCreateRequest request) {
        StudyLogResponse response = studyLogService.createStudyLog(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    // ============================== READ ==============================

    // 모든 학습 일지 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudyLogResponse>>> getAllStudyLogs() {
        List<StudyLogResponse> responses = studyLogService.getAllStudyLogs();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    // 특정 ID로 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudyLogResponse>> getStudyLog(
            @PathVariable Long id) {
        StudyLogResponse response = studyLogService.getStudyLogById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 날짜별 조회
    @GetMapping("/date/{date}")
    public ResponseEntity<ApiResponse<List<StudyLogResponse>>> getStudyLogsByDate(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        List<StudyLogResponse> responses = studyLogService.getStudyLogsByDate(date);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    // 카테고리별 조회
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<StudyLogResponse>>> getStudyLogsByCategory(
            @PathVariable String category) {
        List<StudyLogResponse> responses = studyLogService.getStudyLogsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    // ============================== UPDATE ==============================
    /**
     * 학습 일지 수정
     * PUT /api/v1/logs/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudyLogResponse>> updateStudyLog(
            @PathVariable Long id,
            @RequestBody StudyLogUpdateRequest request) { // 수정 전용 DTO 사용 권장

        StudyLogResponse response = studyLogService.updateStudyLog(id, request);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    //=================DELETE
    /*
    학습 일지 삭제 API
    DELETE /api/v1/logs{id}
    @param id 삭제할 학습 일지ID
    @return 삭제 결과
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<StudyLogDeleteResponse>>deleteStudyLog(
            @PathVariable Long id){
        StudyLogDeleteResponse response = studyLogService.deleteStudyLog(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}