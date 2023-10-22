package toyproject.studyscheduler.api.service.studytime;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toyproject.studyscheduler.api.controller.response.FindStudyTimeResponseDto;
import toyproject.studyscheduler.domain.study.StudyTime;
import toyproject.studyscheduler.domain.study.repository.StudyTimeRepository;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StudyTimeService {

    private final StudyTimeRepository studyTimeRepository;

    public List<FindStudyTimeResponseDto> findAllBy(LocalDate startDate, LocalDate endDate) {
        List<StudyTime> studyTimes = studyTimeRepository.findAllByPeriod(startDate, endDate);

        return studyTimes.stream()
            .map(studyTime -> FindStudyTimeResponseDto.of(
                studyTime.getStudy().getId(),
                studyTime.getStudy().getTitle(),
                studyTime.getStudy().getDescription(),
                studyTime.getStudy().isTermination(),
                studyTime.getTotalCompleteTime(),
                studyTime.getTotalLearningRate(),
                studyTime.getCompleteTimeToday(),
                studyTime.getDate()
            ))
            .toList();
    }
}
