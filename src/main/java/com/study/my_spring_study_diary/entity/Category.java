package com.study.my_spring_study_diary.entity;

import org.apache.catalina.startup.Tomcat;

public enum Category {
    JAVA("☕","Java 기초/심화"),
    SPRING("🌱","Spring Framework/Boot"),
    JPA("🧳","JPA/Hibernate"),
    DATABASE("💾","SQL/데이터베이스"),
    ALGORITHM("🕹️","알고리즘/자료구조"),
    CS("💻","컴퓨터 과학 기초"),
    NETWORK("🌐","네트워크/HTTP"),
    GIT("📂","Git/버전 관리"),
    ETC("📝","기타");


    private final String icon;
    private final String description;

    Category(String icon, String description){
        this.icon = icon;
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
      return description;
    }
}
