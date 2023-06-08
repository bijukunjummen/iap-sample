package org.bk.iap;

import com.google.cloud.spring.security.iap.AudienceProvider;
import com.google.cloud.spring.security.iap.AudienceValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

@Configuration
public class IapConfig {

    @Bean
    public AudienceValidator audienceValidator(AudienceProvider audienceProvider) {
        return new AudienceValidator(audienceProvider) {
            @Override
            public OAuth2TokenValidatorResult validate(Jwt t) {
                return OAuth2TokenValidatorResult.success();
            }
        };
    }

}
