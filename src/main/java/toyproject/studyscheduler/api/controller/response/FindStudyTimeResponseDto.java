package toyproject.studyscheduler.api.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class FindStudyTimeResponseDto {

    private String title;
    private String description;
    private boolean isTermination;
    private int totalCompleteTime;
    private double totalLearningRate;
    private int completeTimeToday;
    private LocalDate date;

    @Builder
    private FindStudyTimeResponseDto(String title, String description, boolean isTermination, int totalCompleteTime, double totalLearningRate, int completeTimeToday, LocalDate date) {
        this.title = title;
        this.description = description;
        this.isTermination = isTermination;
        this.totalCompleteTime = totalCompleteTime;
        this.totalLearningRate = totalLearningRate;
        this.completeTimeToday = completeTimeToday;
        this.date = date;
    }

    public static FindStudyTimeResponseDto of(String title, String description, boolean termination,
                                              int totalCompleteTime, double totalLearningRate, int completeTimeToday, LocalDate date) {
        return FindStudyTimeResponseDto.builder()
            .title(title)
            .description(description)
            .isTermination(termination)
            .totalCompleteTime(totalCompleteTime)
            .totalLearningRate(totalLearningRate)
            .completeTimeToday(completeTimeToday)
            .date(date)
            .build();
    }
}
