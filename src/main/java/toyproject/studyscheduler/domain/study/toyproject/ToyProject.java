package toyproject.studyscheduler.domain.study.toyproject;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.study.Study;
import toyproject.studyscheduler.domain.study.toyproject.requiredfunction.RequiredFunction;
import toyproject.studyscheduler.domain.study.toyproject.TechStack.TechStack;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@DiscriminatorValue("ToyProject")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ToyProject extends Study {

    @OneToMany(mappedBy = "toyProject", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<RequiredFunction> functions = new ArrayList<>();

    @OneToMany(mappedBy = "toyProject", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<TechStack> stack = new ArrayList<>();

    @Builder
    private ToyProject(String title, String description, int totalExpectedTime, int planTimeInWeekDay, int planTimeInWeekend,
                   LocalDate startDate, LocalDate endDate, Member member, List<RequiredFunction> functions, List<TechStack> stack) {

        super(title, description, totalExpectedTime, planTimeInWeekDay, planTimeInWeekend, startDate, endDate, member);
        this.functions = functions;
        this.stack = stack;
    }
}
