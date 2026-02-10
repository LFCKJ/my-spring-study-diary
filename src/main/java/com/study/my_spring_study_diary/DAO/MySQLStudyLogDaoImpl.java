package com.study.my_spring_study_diary.DAO;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * MySQL based StudyLog DAO Implementation
 *
 * JDBCTemplate usage;
 * -JDBC helper class provied By SPRING
 * -Automatically manages Connection, Statement, etc.
 * -Converts SQL exception to SPRING's DataAccessException
 * -Reduces boilerplate code
 */
@Repository
public class MySQLStudyLogDaoImpl implements StudyLogDao {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor injection
     * JdbcTemplate is automatically registered as Bean by Spring
     */
    public MySQLStudyLogDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
}
