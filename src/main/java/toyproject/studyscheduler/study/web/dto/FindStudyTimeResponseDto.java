package toyproject.studyscheduler.study.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.dailystudy.entity.StudyTime;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class FindStudyTimeResponseDto {

    private Long studyId;
    private String title;
    private String description;
    private boolean isTermination;
    private int totalCompleteTime;
    private double totalLearningRate;
    private int completeTimeToday;
    private LocalDate date;

    @Builder
    private FindStudyTimeResponseDto(Long studyId, String title, String description, boolean isTermination, int totalCompleteTime, double totalLearningRate, int completeTimeToday, LocalDate date) {
        this.studyId = studyId;
        this.title = title;
        this.description = description;
        this.isTermination = isTermination;
        this.totalCompleteTime = totalCompleteTime;
        this.totalLearningRate = totalLearningRate;
        this.completeTimeToday = completeTimeToday;
        this.date = date;
    }

    public static FindStudyTimeResponseDto of(StudyTime studyTime) {
        return FindStudyTimeResponseDto.builder()
            .studyId(studyTime.getStudy().getId())
            .title(studyTime.getStudy().getStudyInformation().getTitle())
            .description(studyTime.getStudy().getStudyInformation().getDescription())
            .isTermination(studyTime.getStudy().getStudyInformation().isTermination())
            .totalCompleteTime(studyTime.getTotalCompleteTime())
            .totalLearningRate(studyTime.getTotalLearningRate())
            .completeTimeToday(studyTime.getCompleteTimeToday())
            .date(studyTime.getDate())
            .build();
    }
}
