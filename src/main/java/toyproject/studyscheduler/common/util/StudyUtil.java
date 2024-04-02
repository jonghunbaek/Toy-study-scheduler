package toyproject.studyscheduler.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import toyproject.studyscheduler.study.domain.PeriodCalculator;

import java.time.LocalDate;

/**
 *  Study와 관련된 모든 유틸 작업의 셋팅을 담당하는 클래스
 */

@RequiredArgsConstructor
@Component
public class StudyUtil {

    // TODO : 인수가 3개인 점, 첫 번째, 두 번째 인수의 순서가 바뀔 경우 계산 오류가 발생하니 추후 리팩토링하기.
    public PeriodCalculator setUpPeriodCalCulatorBy(int planTimeInWeekday, int planTimeInWeekend, LocalDate startDate) {
        return PeriodCalculator.builder()
            .planQuantityInWeekday(planTimeInWeekday)
            .planQuantityInWeekend(planTimeInWeekend)
            .startDate(startDate)
            .build();
    }
}
