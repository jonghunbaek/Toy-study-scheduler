package toyproject.studyscheduler.dailystudy.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.common.domain.BaseEntity;
import toyproject.studyscheduler.study.domain.entity.Study;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class DailyStudy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private int completeMinutesToday;

    private LocalDate studyDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Study study;

    @Builder
    private DailyStudy(String content, int completeMinutesToday, LocalDate studyDate, Study study) {
        this.content = content;
        this.completeMinutesToday = completeMinutesToday;
        this.studyDate = studyDate;
        this.study = study;
    }
}
