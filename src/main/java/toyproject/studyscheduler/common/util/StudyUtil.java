package toyproject.studyscheduler.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import toyproject.studyscheduler.study.domain.PeriodCalculator;

import java.time.LocalDate;

/**
 *  Study와 관련된 모든 유틸 작업의 셋팅을 담당하는 클래스
 */
// TODO : 삭제 예정

@RequiredArgsConstructor
@Component
public class StudyUtil {

    public PeriodCalculator setUpPeriodCalCulatorBy(int planTimeInWeekday, int planTimeInWeekend, LocalDate startDate) {
        return PeriodCalculator.builder()
            .planQuantityInWeekday(planTimeInWeekday)
            .planQuantityInWeekend(planTimeInWeekend)
            .startDate(startDate)
            .build();
    }
}
