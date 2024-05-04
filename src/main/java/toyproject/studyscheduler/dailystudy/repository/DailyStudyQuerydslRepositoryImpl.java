package toyproject.studyscheduler.dailystudy.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DailyStudyQuerydslRepositoryImpl {

    private final JPAQueryFactory query;
}
