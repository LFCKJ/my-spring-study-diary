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
    * @Repository
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
        //학습일지 저장(Create)
        if(studyLog.getId() == null){
            studyLog.setId(sequence.getAndIncrement());
        }
        //map에 저장
        database.put(studyLog.getId(), studyLog);
        return studyLog;
    }
/*
학습 일지 수정(Update)
MAP은 같은 키로 put하면 덮어쓰므로 save와 동일하게 동작
하지만 의미를 명확히 하기 위해 별도 메서드로 분리
 */
    public StudyLog update(StudyLog studyLog){
        if(studyLog.getId() == null){
            throw new IllegalArgumentException("수정할 학습 일지의 Id 가 없습니다.");
        }
        if(!database.containsKey(studyLog.getId())){
            throw new IllegalArgumentException("해당 학습 일지를 찾을 수 없습니다.(id:"+studyLog.getId()+")");
        }
        database.put(studyLog.getId(), studyLog);
        return studyLog;
    }
    /*
    ID로 존재 여부 확인
     */
    public boolean existsById(Long id){
        return database.containsKey(id);
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
