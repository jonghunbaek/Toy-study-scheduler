package toyproject.studyscheduler.domain.study.toyproject;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.domain.BaseEntity;
import toyproject.studyscheduler.domain.study.Study;
import toyproject.studyscheduler.domain.study.plannedfunction.PlannedFunction;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ToyProject extends BaseEntity implements Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private int completeTime;

    private int planTimeInWeekDay;

    private int planTimeInWeekend;

    private String progress;
    //todo : 이너클래스와의 차이를 생각해보자.
    @OneToMany(fetch = FetchType.LAZY)
    private List<PlannedFunction> functions;
}
