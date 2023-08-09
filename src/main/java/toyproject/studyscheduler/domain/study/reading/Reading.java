package toyproject.studyscheduler.domain.study.reading;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.domain.BaseEntity;
import toyproject.studyscheduler.domain.study.Study;

@NoArgsConstructor
@Getter
@Entity
public class Reading extends BaseEntity implements Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private int completeTime;

    private int planTimeInWeekDay;

    private int planTimeInWeekend;


}
