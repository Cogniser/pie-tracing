package com.pie.formatter;

import io.jaegertracing.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FormatterApp {
    @Bean
    public io.opentracing.Tracer initTracer() {
        Configuration.SamplerConfiguration samplerConfig = new Configuration.SamplerConfiguration().withType("const").withParam(1);
        Configuration.ReporterConfiguration reporterConfig = new Configuration.ReporterConfiguration().withLogSpans(true);
        return new Configuration("formatter-app").withSampler(samplerConfig).withReporter(reporterConfig).getTracer();
    }
    public static void main(String[] args) {
        System.setProperty("server.port","8082");
        SpringApplication.run(FormatterApp.class);
    }
}
