package toyproject.studyscheduler.domain.study.requiredfunction;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.domain.study.Study;
import toyproject.studyscheduler.domain.toyproject.ToyProject;
import toyproject.studyscheduler.domain.user.User;

import java.time.LocalDate;

@DiscriminatorValue("PlannedFunction")
@Getter
@NoArgsConstructor
@Entity
public class RequiredFunction extends Study {

    @Enumerated
    private FunctionType functionType;

    @ManyToOne(fetch = FetchType.LAZY)
    ToyProject toyProject;

    @Builder
    private RequiredFunction(String title, String description, int totalExpectedTime, int planTimeInWeekDay, int planTimeInWeekend,
                            LocalDate startDate, LocalDate endDate, User user, FunctionType functionType, ToyProject toyProject) {
        super(title, description, totalExpectedTime, planTimeInWeekDay, planTimeInWeekend, startDate, endDate, user);
        this.functionType = functionType;
        this.toyProject = toyProject;
    }
}
