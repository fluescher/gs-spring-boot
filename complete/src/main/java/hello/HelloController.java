package hello;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.opentracing.Tracer;
import io.opentracing.contrib.spring.web.client.TracingRestTemplateInterceptor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@RestController
public class HelloController {

    private final Counter greetingsServed;
    private final Tracer tracer;

    public HelloController(MeterRegistry registry, Tracer tracer) {
        this.greetingsServed = registry.counter("greetings.served");
        this.tracer = tracer;
    }

    @RequestMapping("/")
    @Timed(value="time.greetings")
    public String index() {

        this.greetingsServed.increment();

        return "Greetings from Spring Boot, Sunrise!";
    }

    @RequestMapping("/caller")
    @Timed(value="time.caller")
    public String caller() {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(new TracingRestTemplateInterceptor()));

        restTemplate.getForObject("http://localhost:8080/slow", String.class);

        return restTemplate.getForObject("http://localhost:8080/slow", String.class);
    }

    @RequestMapping("/slow")
    @Timed(value="time.slow-greetings")
    public String slowGreeting() {

        try {
            Thread.sleep((long)(Math.random() * 5000 + 5000));
        } catch (InterruptedException ignored) {
        }

        return "This is a slow greeting";
    }
    
}
