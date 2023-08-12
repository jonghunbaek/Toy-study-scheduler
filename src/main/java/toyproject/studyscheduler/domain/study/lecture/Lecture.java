package toyproject.studyscheduler.domain.study.lecture;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.domain.BaseEntity;
import toyproject.studyscheduler.domain.study.Study;

@DiscriminatorValue("Lecture")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Lecture extends Study {

    private String teacherName;

}
