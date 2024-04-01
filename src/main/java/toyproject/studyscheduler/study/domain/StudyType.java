package toyproject.studyscheduler.study.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum StudyType {

    LECTURE("lecture"),
    READING("reading"),
    TOY("toy");

    private String dType;

    public static StudyType toEnum(String studyType) {
        return Arrays.stream(StudyType.values())
            .filter(val -> val.dType.equals(studyType))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("일치하는 학습 종류가 없습니다."));
    }
}
