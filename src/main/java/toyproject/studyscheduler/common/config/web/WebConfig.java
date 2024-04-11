package toyproject.studyscheduler.common.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import toyproject.studyscheduler.common.resolver.LoginMemberInfoResolver;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 인증된 유저의 아이디를 가져오는 Resolver추가
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberInfoResolver());
    }
}
