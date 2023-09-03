package toyproject.studyscheduler.domain.toyproject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum Category {

    IDE("개발 환경"),
    LANGUAGE("언어"),
    FRAMEWORK("프레임워크"),
    DATABASE("데이터 베이스"),
    OS("운영체제"),
    BUILD("빌드 도구"),
    CLOUD("클라우드");

    private String description;
}
