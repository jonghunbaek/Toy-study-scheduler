package toyproject.studyscheduler.study.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum StudyType {

    LECTURE(Values.LECTURE),
    READING(Values.READING),
    TOY("toy");

    private String studyType;

    public static class Values {
        public static final String LECTURE = "lecture";
        public static final String READING = "reading";
    }
}
