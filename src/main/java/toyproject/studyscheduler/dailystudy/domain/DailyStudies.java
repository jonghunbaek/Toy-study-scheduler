package toyproject.studyscheduler.dailystudy.domain;

import lombok.Getter;
import toyproject.studyscheduler.dailystudy.domain.entity.DailyStudy;

import java.util.List;

@Getter
public class DailyStudies {

    private final List<DailyStudy> dailyStudies;

    public DailyStudies(List<DailyStudy> dailyStudies) {
        this.dailyStudies = dailyStudies;
    }

    public int calculateTotalStudyMinutes() {
        return dailyStudies.stream()
                .mapToInt(DailyStudy::getCompleteMinutesToday)
                .sum();
    }
}
