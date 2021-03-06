package com.pie.jaegertracing;

import com.pie.jaegertracing.vo.Person;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class JaegerTracingController extends TracedController{

/*    @Autowired
    Tracer tracer;*/

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/sayHello/{name}")
    public String sayHello(@PathVariable String name, HttpServletRequest request){
        //Span span = tracer.buildSpan("say-hello").start();
       Span span = startServerSpan("/sayHello",request);
        try(Scope scope = tracer.scopeManager().activate(span)){
            Person person = getPerson(name);
            Map<String, String> fields = new LinkedHashMap<>();
            fields.put("name", person.getName());
            fields.put("title", person.getTitle());
            fields.put("description", person.getDetails());
            span.log(fields);

            String response = formatGreeting(person);
            span.setTag("response", response);

            return response;
        } finally {
            span.finish();
        }
    }

    /*private Person getPerson(String name) {
        Span span = tracer.buildSpan("get-person").start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            String url = "http://localhost:8081/getPerson/" + name;
            ResponseEntity<Person> response = restTemplate.getForEntity(url, Person.class);
            return response.getBody();
        } finally {
            span.finish();
        }
    }

    private String formatGreeting(Person person) {
        Span span = tracer.buildSpan("format-greeting").start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            URI uri = UriComponentsBuilder //
                    .fromHttpUrl("http://localhost:8082/formatGreeting") //
                    .queryParam("name", person.getName()) //
                    .queryParam("title", person.getTitle()) //
                    .queryParam("description", person.getDetails()) //
                    .build(Collections.emptyMap());
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
            return response.getBody();
        } finally {
            span.finish();
        }
    }*/

    private Person getPerson(String name) {
        String url = "http://localhost:8081/getPerson/" + name;
        URI uri = UriComponentsBuilder.fromHttpUrl(url).build(Collections.emptyMap());
        return get("get-person", uri, Person.class, restTemplate);
    }

    private String formatGreeting(Person person) {
        URI uri = UriComponentsBuilder //
                .fromHttpUrl("http://localhost:8082/formatGreeting") //
                .queryParam("name", person.getName()) //
                .queryParam("title", person.getTitle()) //
                .queryParam("description", person.getDetails()) //
                .build(Collections.emptyMap());
        return get("format-greeting", uri, String.class, restTemplate);
    }
}
