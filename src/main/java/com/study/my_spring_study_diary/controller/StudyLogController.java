package com.study.my_spring_study_diary.controller;


import com.study.my_spring_study_diary.dto.request.StudyLogCreateRequest;
import com.study.my_spring_study_diary.dto.response.StudyLogResponse;
import com.study.my_spring_study_diary.global.common.ApiResponse;
import com.study.my_spring_study_diary.service.StudyLogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/logs")
public class StudyLogController {
//의존성 주입: service를 주입받음
    private final StudyLogService studyLogService;
    public StudyLogController(StudyLogService studyLogService){
        this.studyLogService = studyLogService;
    }

    @PostMapping
    public StudyLogResponse createStudyLog(@RequestBody StudyLogCreateRequest request){
        //service 호출하여 학습 일지 생성
        return studyLogService.createStudyLog(request);
    }

      //모든 학습 일지 조회(read - ALL)
    @GetMapping
    public List<StudyLogResponse> getAllStudyLogs(){
        //service호출하여 모든 학습 일지 조회
        return studyLogService.getAllStudyLogs();
    }

    //특정 학습 일지 조회(read -single)
    @GetMapping("/{id}")
    public StudyLogResponse getStudyLog(@PathVariable Long id){
        //service 호출하여 Id로 학습 일지 조회
        return studyLogService.getStudyLogById(id);
    }
//날짜별 학습 일지 조회(Read - By Date)
    @GetMapping("/date/{date}")
    public List<StudyLogResponse> getStudyLogsByDate(@PathVariable String date){
        //Service 호출하여 날짜로 학습 일지 조회
        return studyLogService.getStudyLogsByDate(LocalDate.parse(date));
    }
    // 카테고리별 학습 일지 조회(read - By category)
    @GetMapping("/category/{category}")
    public List<StudyLogResponse> getStudyLogsByCategory(@PathVariable String category){
        //service 호출
        return studyLogService.getStudyLogsByCategory(category);
    }
}
