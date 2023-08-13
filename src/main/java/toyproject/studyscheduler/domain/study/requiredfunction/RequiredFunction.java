package toyproject.studyscheduler.domain.study.requiredfunction;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.domain.study.Study;
import toyproject.studyscheduler.domain.toyproject.ToyProject;

@DiscriminatorValue("PlannedFunction")
@Getter
@NoArgsConstructor
@Entity
public class RequiredFunction extends Study {

    @Enumerated
    private FunctionType functionType;

    @ManyToOne(fetch = FetchType.LAZY)
    ToyProject toyProject;
}
