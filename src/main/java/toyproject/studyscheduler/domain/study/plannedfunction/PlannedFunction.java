package toyproject.studyscheduler.domain.study.plannedfunction;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.domain.study.Study;
import toyproject.studyscheduler.domain.toyproject.ToyProject;

@DiscriminatorValue("P")
@Getter
@NoArgsConstructor
@Entity
public class PlannedFunction extends Study {

    @Enumerated
    private FunctionType functionType;

    @ManyToOne(fetch = FetchType.LAZY)
    ToyProject toyProject;
}
