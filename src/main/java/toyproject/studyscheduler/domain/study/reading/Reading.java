package toyproject.studyscheduler.domain.study.reading;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import toyproject.studyscheduler.domain.study.Study;

@SuperBuilder
@DiscriminatorValue("Reading")
@NoArgsConstructor
@Getter
@Entity
public class Reading extends Study {

    private String authorName;

    private int totalPage;

    private int readPagePerMin;
}
