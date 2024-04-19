package toyproject.studyscheduler.dailystudy.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import toyproject.studyscheduler.dailystudy.domain.entity.DailyStudy;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class DailyStudiesTest {

    @DisplayName("특정 학습에 대한 모든 일일 학습 시간을 더한다.")
    @Test
    void calculateTotalStudyMinutes() {
        // given
        DailyStudy argument1 = createDailyStudy(10);
        DailyStudy argument2 = createDailyStudy(20);
        DailyStudy argument3 = createDailyStudy(15);
        DailyStudy argument4 = createDailyStudy(18);

        DailyStudies dailyStudies = new DailyStudies(List.of(argument1, argument2, argument3, argument4));

        // when
        int result = dailyStudies.calculateTotalStudyMinutes();

        // then
        assertThat(result).isEqualTo(63);
    }

    private DailyStudy createDailyStudy(int completeMinutesToday) {
        return DailyStudy.builder()
                .content("any")
                .studyDate(LocalDate.now())
                .completeMinutesToday(completeMinutesToday)
                .build();
    }
}