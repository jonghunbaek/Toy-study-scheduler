package toyproject.studyscheduler.domain.study.toyproject;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.study.Study;
import toyproject.studyscheduler.domain.function.RequiredFunction;
import toyproject.studyscheduler.domain.techstack.TechStack;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@DiscriminatorValue("toy")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ToyProject extends Study {

    @OneToMany(mappedBy = "toyProject", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<RequiredFunction> functions = new ArrayList<>();

    @OneToMany(mappedBy = "toyProject", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<TechStack> stacks = new ArrayList<>();

    public List<RequiredFunction> getRequiredFunctions() {
        return functions.stream().toList();
    }
    
    public List<TechStack> getTechStacks() {
        return stacks.stream().toList();
    }

    @Builder
    private ToyProject(String title, String description, int totalExpectedPeriod, int totalExpectedMin, int planTimeInWeekday, int planTimeInWeekend,
                   LocalDate startDate, boolean isTermination, LocalDate realEndDate, Member member, List<RequiredFunction> functions, List<TechStack> stacks) {
        super(title, description, totalExpectedPeriod, totalExpectedMin, planTimeInWeekday, planTimeInWeekend,
            startDate, isTermination, realEndDate, member);

        if (functions != null) {
            this.functions = functions.stream()
                .map(function -> RequiredFunction.builder()
                    .title(function.getTitle())
                    .description(function.getDescription())
                    .toyProject(this)
                    .functionType(function.getFunctionType())
                    .expectedTime(function.getExpectedTime())
                    .build())
                .toList();
        }

        if (stacks != null) {
            this.stacks = stacks.stream()
                .map(stack -> TechStack.builder()
                    .toyProject(this)
                    .title(stack.getTitle())
                    .techCategory(stack.getTechCategory())
                    .build())
                .toList();
        }
    }

    public void addTechStack(TechStack techStack) {
        stacks.add(techStack);
    }

    public void addRequiredFunction(RequiredFunction function) {
        functions.add(function);
        calculateTotalExpectedMin();
    }

    private void calculateTotalExpectedMin() {
        int totalExpectedMin = functions.stream()
            .mapToInt(RequiredFunction::getExpectedTime)
            .sum();

        super.updateTotalExpectedMin(totalExpectedMin);
    }
}
