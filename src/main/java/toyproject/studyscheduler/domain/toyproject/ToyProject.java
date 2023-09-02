package toyproject.studyscheduler.domain.toyproject;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.domain.BaseEntity;
import toyproject.studyscheduler.domain.study.requiredfunction.RequiredFunction;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ToyProject extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @OneToMany(mappedBy = "toyProject", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<RequiredFunction> functions = new ArrayList<>();

    @OneToMany(mappedBy = "toyProject", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<TechStack> stack = new ArrayList<>();

    @Builder
    private ToyProject(String title, String description, List<RequiredFunction> functions, List<TechStack> stack) {
        this.title = title;
        this.description = description;
        this.functions = functions;
        this.stack = stack;
    }
}
