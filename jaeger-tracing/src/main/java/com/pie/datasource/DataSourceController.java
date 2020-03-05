package com.pie.datasource;

import com.pie.jaegertracing.TracedController;
import com.pie.jaegertracing.vo.Person;
import io.opentracing.Scope;
import io.opentracing.Span;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class DataSourceController extends TracedController {

    @GetMapping("/getPerson/{name}")
    public Person getPerson(@PathVariable String name, HttpServletRequest request) {
        Span span = startServerSpan("/getPerson",request);
        //Span span = tracer.buildSpan("/getPerson").start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            Person person = loadPerson(name);
            Map<String, String> fields = new LinkedHashMap<>();
            fields.put("name", person.getName());
            fields.put("title", person.getTitle());
            fields.put("description", person.getDetails());
            span.log(fields);
            return person;
        } finally {
            span.finish();
        }
    }

    private Person loadPerson(String name) {
        Span span = tracer.buildSpan("get-person").start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            Person p1 = new Person(name);
            p1.setTitle("Super Star");
            p1.setDetails("He is a nice actor.");

            return p1;
        } finally {
            span.finish();
        }
    }
}
