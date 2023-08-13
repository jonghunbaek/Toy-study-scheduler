package toyproject.studyscheduler.domain.study.requiredfunction;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum FunctionType {
    CREATE("생성"),
    READ("조회"),
    UPDATE("수정"),
    DELETE("삭제"),
    ETC("기타");

    private String description;

    FunctionType(String description) {
        this.description = description;
    }
}
