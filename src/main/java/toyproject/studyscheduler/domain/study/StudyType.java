package toyproject.studyscheduler.domain.study;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum StudyType {

    LECTURE("강의"),
    READING("독서"),
    TOY("토이 프로젝트");

    private String description;
}
