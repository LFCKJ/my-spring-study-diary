package com.study.my_spring_study_diary.repository;

import com.study.my_spring_study_diary.entity.Category;
import com.study.my_spring_study_diary.entity.StudyLog;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class StudyLogRepository {
    /*학습 일지 저장소
    *
    * @Respository
    *
    *
    *
    *
     */
    //데이터 저장소(실제 DB 대신 Map사용)
    private final Map<Long, StudyLog> database = new HashMap<>();
    //ID자동 증가를 위한 시퀀스
    private final AtomicLong sequence = new AtomicLong(1);

    @PostConstruct
    public void init(){
        System.out.println("🚀 StudyLogRepository 초기화 완료!");
    }
    @PreDestroy
    public void destroy(){
     System.out.println("⬅️StudyLogRepository 종료! 저장된 데이터:"+ database.size()+"개");
    }
    public StudyLog save(StudyLog studyLog){
        //id가 없으면 새로운 id 부여
        if(studyLog.getId() == null){
            studyLog.setId(sequence.getAndIncrement());
        }
        //map에 저장
        database.put(studyLog.getId(), studyLog);
        return studyLog;
    }

    //전체 학습일지 조회
    public List<StudyLog>findAll(){
        return database.values().stream()
                .sorted((a,b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .collect(Collectors.toList());
    }
    //ID로 학습 일지 조회
    public Optional<StudyLog> findById(Long id){
        return Optional.ofNullable(database.get(id));
    }

    //날짜로 학습 일지 조회
    public List<StudyLog>findByStudyDate(LocalDate date){
        return database.values().stream()
                .filter(log -> log.getStudyDate().equals(date))
                .sorted((a,b) ->b.getCreatedAt().compareTo(a.getCreatedAt()))
                .collect(Collectors.toList());
    }

    //카테고리로 학습 일지 조회
    public List<StudyLog>findByCategory(Category category){
        return database.values().stream()
                .filter(log ->log.getCategory().equals(category))
                .sorted((a,b) ->b.getCreatedAt().compareTo(a.getCreatedAt()))
                .collect(Collectors.toList());
    }

    //저장된 데이터 개수 조회
    public long count(){
        return database.size();
    }
}
