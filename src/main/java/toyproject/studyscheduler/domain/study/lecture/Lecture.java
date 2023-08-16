package toyproject.studyscheduler.domain.study.lecture;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import toyproject.studyscheduler.domain.study.Study;

@SuperBuilder
@DiscriminatorValue("Lecture")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Lecture extends Study {

    private String teacherName;
}
