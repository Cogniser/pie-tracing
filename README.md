# pie-tracing

This project requires jaeger

install docker and then execute below command for locally running jaeger

```

docker run --rm -it --network=host jaegertracing/all-in-one

```
Jaeger ui is available at http://localhost:16686

1. run JaegerTracingApplication which will run at port 8080
2. run DataSourceApp
3. run FormatterApp

Now hit below GET api in browser/postman
http://localhost:8080/sayHello/name

You can Observe the traces in jager ui for the sayHello call.
