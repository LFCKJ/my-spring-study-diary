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

@Repository // Spring Bean 으로 등록
public class StudyLogRepository {
    /*학습 일지 저장소
    *
    * @Repository 어노테이션 설명
    * -이 클래스를 Spring Bean으로 등록
    * - 데이터 접근 계층임을 명시한다.
    * - 데이터 접근 관련 예외를 Spring의 DataAccessException으로 변환해줍니다.
    *
    * 실제 프로젝트에서는JPA, MyBatis등을 사용하지만,
    * 이번엔 MAP을 사용해 데이터를 저장한다,
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
        //@param studyLog 저장할 학습 일지
        //@return 저장된 학습 일지(ID포함)
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

    //====DELETE
    //ID로 학습 일지를 삭제한다.
    //@param id삭제할 학습 일지ID
    //@return 삭제 성공 여부(true: 삭제됨, false:해당 Id없음
    public boolean deleteById(Long id){
        //Map.remove()는 삭제된 값을 반환, 없으면 Null반환
        StudyLog removed = database.remove(id);
        return removed != null;
    }



}
