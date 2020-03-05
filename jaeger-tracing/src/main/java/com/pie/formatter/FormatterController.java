package com.pie.formatter;

import com.pie.jaegertracing.TracedController;
import io.opentracing.Scope;
import io.opentracing.Span;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class FormatterController extends TracedController {


    @GetMapping("/formatGreeting")
    public String formatGreeting(@RequestParam String name, @RequestParam String title,
                                 @RequestParam String description, HttpServletRequest request) {
        Span span = startServerSpan("/formatGreeting",request);
        //Span span = tracer.buildSpan("/formatGreeting").start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            String response = "Hello, ";
            if (!title.isEmpty()) {
                response += title + " ";
            }
            response += name + "!";
            if (!description.isEmpty()) {
                response += " " + description;
            }
            return response;
        } finally {
            span.finish();
        }
    }

}
