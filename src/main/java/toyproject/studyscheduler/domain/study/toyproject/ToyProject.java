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
    List<TechStack> stacks = new ArrayList<>();

    @Builder
    private ToyProject(String title, String description, int totalExpectedTime, int planTimeInWeekDay, int planTimeInWeekend,
                   LocalDate startDate, LocalDate endDate, Member member, List<RequiredFunction> functions, List<TechStack> stacks) {

        super(title, description, totalExpectedTime, planTimeInWeekDay, planTimeInWeekend, startDate, endDate, member);
        this.functions = functions.stream()
            .map(function -> RequiredFunction.builder()
                .toyProject(this)
                .functionType(function.getFunctionType())
                .build())
            .toList();

        this.stacks = stacks.stream()
            .map(stack -> TechStack.builder()
                .toyProject(this)
                .title(stack.getTitle())
                .techCategory(stack.getTechCategory())
                .build())
            .toList();
    }
}
