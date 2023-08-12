package toyproject.studyscheduler.domain.study.reading;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.domain.BaseEntity;
import toyproject.studyscheduler.domain.study.Study;

@DiscriminatorValue("R")
@NoArgsConstructor
@Getter
@Entity
public class Reading extends Study {

    private String authorName;

    private int totalPage;

    private int readPagePerMin;
}
