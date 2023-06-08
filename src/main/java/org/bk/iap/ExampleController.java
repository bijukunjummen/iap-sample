package org.bk.iap;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.util.Map;

@RestController
public class ExampleController {

    @GetMapping("/")
    public Mono<String> unsecured() {
        return Mono.just("No secrets here!\n");
    }

    @GetMapping("/topsecret")
    public String secured() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return String.format(
                    "You are [%s] with e-mail address [%s].%n",
                    jwt.getSubject(), jwt.getClaimAsString("email"));
        } else {
            return "Something went wrong; authentication is not provided by IAP/JWT.\n";
        }
    }

    @GetMapping("/headers")
    public Mono<Map<String, String>> headers(WebRequest request) {
        Iterable<String> headerNamesIterable = () -> request.getHeaderNames();
        return Flux
                .fromIterable(headerNamesIterable).map(name -> Tuples.of(name, request.getHeader(name)))
                .collectMap(tup -> tup.getT1(), tup -> tup.getT2());
    }
}