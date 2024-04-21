package toyproject.studyscheduler.dailystudy.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import toyproject.studyscheduler.dailystudy.application.DailyStudyService;
import toyproject.studyscheduler.dailystudy.application.dto.DailyStudySave;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static toyproject.studyscheduler.common.response.ResponseCode.*;

@WebMvcTest(
        controllers = DailyStudyController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)}
)
class DailyStudyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    DailyStudyService dailyStudyService;

    @WithMockUser
    @DisplayName("일일 학습을 등록할 때 body에 전달되는 값에 대한 입력 검증을 한다.")
    @ParameterizedTest
    @MethodSource("argumentsWhenDailyStudyCreate")
    void getStudiesByPeriod(DailyStudySave dailyStudySave, ResponseForm response) throws Exception {
        // given
        String jsonResponse = objectMapper.writeValueAsString(response);

        // when & then
        mockMvc.perform(post("/daily-studies")
                        .content(objectMapper.writeValueAsString(dailyStudySave))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonResponse));
    }

    private static Stream<Arguments> argumentsWhenDailyStudyCreate() {
        return Stream.of(
                Arguments.of(
                        DailyStudySave.builder()
                                .studyId(-1L)
                                .content("테스트 내용입니다.")
                                .completeMinutesToday(1)
                                .studyDate(LocalDate.of(2024, 4, 1))
                                .build(),
                        ResponseForm.from(E90000, Map.of("studyId", "학습 id의 최소 값은 1입니다."))
                ),
                Arguments.of(
                        DailyStudySave.builder()
                                .studyId(null)
                                .content(createStringOver1000Characters())
                                .completeMinutesToday(-1)
                                .studyDate(null)
                                .build(),
                        ResponseForm.from(E90000, Map.of(
                                "studyId", "학습 id는 필수 값입니다.",
                                "content", "학습 내용은 1000자 이하이어야 합니다.",
                                "completeMinutesToday", "최소 학습 시간은 1분입니다.",
                                "studyDate", "학습일은 필수 입력 값입니다."
                                )
                        )
                )
        );
    }

    private static String createStringOver1000Characters() {
        StringBuilder sb = new StringBuilder("한글로");

        while (sb.length() < 1000) {
            sb.append(sb);
        }

        System.out.println(sb);

        return sb.toString();
    }

    @WithMockUser
    @DisplayName("예상 종료일을 조회할 때 요청 파라미터의 값을 검증한다.")
    @ParameterizedTest
    @MethodSource("argumentsWhengetExpectedEndDate")
    void getExpectedEndDate(String studyId, ResponseForm response) throws Exception {
        // given
        String jsonResponse = objectMapper.writeValueAsString(response);

        // when & then
        mockMvc.perform(get("/daily-studies/remaining-days")
                        .queryParam("studyId", studyId)
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonResponse));
    }

    private static Stream<Arguments> argumentsWhengetExpectedEndDate() {
        return Stream.of(
                Arguments.of(
                        "",
                        ResponseForm.of(E90002)
                ),
                Arguments.of(
                        "-1",
                        ResponseForm.from(E90000, List.of("id 값은 양의 정수이어야 합니다."))
                ),
                Arguments.of(
                        "TEST",
                        ResponseForm.of(E90001)
                )
        );
    }
}