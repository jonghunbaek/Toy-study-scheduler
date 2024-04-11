package toyproject.studyscheduler.study.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import toyproject.studyscheduler.common.jwt.JwtAuthenticationFilter;
import toyproject.studyscheduler.common.response.ResponseForm;
import toyproject.studyscheduler.study.application.StudyService;
import toyproject.studyscheduler.study.application.dto.LectureSave;
import toyproject.studyscheduler.study.application.dto.Period;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static toyproject.studyscheduler.common.response.ResponseCode.E30001;
import static toyproject.studyscheduler.common.response.ResponseCode.E90000;

@WebMvcTest(
    controllers = StudyController.class,
    excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)}
)
class StudyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    StudyService studyService;

    @WithMockUser
    @DisplayName("특정 기간동안 수행한 모든 학습을 조회시 입력에 대한 검증을 수행한다.")
    @ParameterizedTest
    @MethodSource("argumentsWhenStudiesDuringPeriod")
    void getStudiesByPeriod(String startDate, String endDate, ResponseForm response) throws Exception {
        // given
        String jsonResponse = objectMapper.writeValueAsString(response);

        // when & then
        mockMvc.perform(get("/studies/period")
                .queryParam("startDate", startDate)
                .queryParam("endDate", endDate)
                .with(csrf())
        )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(content().json(jsonResponse));
    }

    private static Stream<Arguments> argumentsWhenStudiesDuringPeriod() {
        return Stream.of(
            Arguments.of(
                "",
                "2024-04-01",
                ResponseForm.from(E90000, Map.of("startDate", "시작일은 필수 값입니다."))
            ),
            Arguments.of(
                "2024-04-01",
                "",
                ResponseForm.from(E90000, Map.of("endDate", "종료일은 필수 값입니다."))
            ),
            Arguments.of(
                "2024-04-11",
                "2024-04-10",
                ResponseForm.of(E30001)
            )
        );
    }

    @WithMockUser
    @DisplayName("학습 저장 요청시 입력에 대한 검증을 수행한다.")
    @ParameterizedTest
    @MethodSource("argumentsWhenStudySave")
    void createStudyTerminated(LectureSave lectureSave, ResponseForm response) throws Exception {
        // given
        String jsonResponse = objectMapper.writeValueAsString(response);

        // when & then
        mockMvc.perform(post("/studies")
                .content(objectMapper.writeValueAsString(lectureSave))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(content().json(jsonResponse));
    }

    private static Stream<Arguments> argumentsWhenStudySave() {
        return Stream.of(
            Arguments.of(
                LectureSave.builder()
                    .studyType("lecture")
                    .title("") // title 빈 값에 대한 검증
                    .description("Spring강의")
                    .isTermination(true)
                    .startDate(LocalDate.of(2024, 4, 1))
                    .endDate(LocalDate.of(2024, 4, 21))
                    .planMinutesInWeekday(30)
                    .planMinutesInWeekend(60)
                    .teacherName("김영한")
                    .totalRuntime(500)
                    .build(),
                ResponseForm.from(E90000, Map.of("title", "제목은 필수 값입니다."))
            ),
            Arguments.of(
                LectureSave.builder()
                    .studyType("lecture")
                    .title("김영한의 Spring")
                    .description("Spring강의")
                    .isTermination(true)  // startDate null값에 대한 검증
                    .endDate(LocalDate.of(2024, 4, 21))
                    .planMinutesInWeekday(30)
                    .planMinutesInWeekend(60)
                    .teacherName("김영한")
                    .totalRuntime(500)
                    .build(),
                ResponseForm.from(E90000, Map.of("startDate", "시작일은 필수 값입니다."))
            ),
            Arguments.of(
                LectureSave.builder()
                    .studyType("lecture")
                    .title("김영한의 Spring")
                    .description("Spring강의")
                    .isTermination(true)
                    .startDate(LocalDate.of(2024, 4, 1))
                    .endDate(LocalDate.of(2024, 4, 21))
                    .planMinutesInWeekday(30)
                    .planMinutesInWeekend(1000) // planMinutesInWeekend의 max값에 대한 검증
                    .teacherName("김영한")
                    .totalRuntime(500)
                    .build(),
                ResponseForm.from(E90000, Map.of("planMinutesInWeekend", "학습 계획 시간은 최대 720분 이하여야 합니다."))
            ),
            Arguments.of(
                LectureSave.builder()
                    .studyType("lecture")
                    .title("김영한의 Spring")
                    .description("Spring강의")
                    .isTermination(true)
                    .startDate(LocalDate.of(2024, 4, 1))
                    .endDate(LocalDate.of(2024, 4, 21))
                    .planMinutesInWeekday(30)
                    .planMinutesInWeekend(-1) // planMinutesInWeekend의 min값에 대한 검증
                    .teacherName("김영한")
                    .totalRuntime(500)
                    .build(),
                ResponseForm.from(E90000, Map.of("planMinutesInWeekend", "학습 계획 시간은 최소 1분 이상이어야 합니다."))
            )
        );
    }
}