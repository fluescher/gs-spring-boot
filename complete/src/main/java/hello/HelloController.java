package hello;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {

    private final Counter greetingsServed;

    public HelloController(MeterRegistry registry) {
        this.greetingsServed = registry.counter("greetings.served");
    }

    @RequestMapping("/")
    @Timed(value="time.greetings")
    public String index() {

        this.greetingsServed.increment();

        return "Greetings from Spring Boot, Sunrise!";
    }
    
}
