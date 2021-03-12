# Typicalc

The project is a standard Maven project, so you can import it to your IDE of choice.
[Read more how to set up a development environment](https://vaadin.com/docs/v18/flow/installing/installing-overview.html) for Vaadin projects (Windows, Linux, macOS).

[To Vaadin documentation](https://vaadin.com/docs-beta/latest/flow/overview/)

## Running and debugging the application

### Running the application from the command line.
To run from the command line, use `mvn` and open http://localhost:8080 in your browser.

### Running and debugging the application in Intellij IDEA
- Locate the Application.java class in the Project view. It is in the src folder, under the main package's root.
- Right click on the Application class
- Select "Debug 'Application.main()'" from the list

After the application has started, you can view it at http://localhost:8080/ in your browser.
You can now also attach break points in code for debugging purposes, by clicking next to a line number in any source file.

### Running and debugging the application in Eclipse
- Locate the Application.java class in the Package explorer. It is in `src/main/java`, under the main package.
- Right click on the file and select `Debug As` --> `Java Application`.

Do not worry if the debugger breaks at a `SilentExitException`. This is a Spring Boot feature and happens on every startup.

After the application has started, you can view your it at http://localhost:8080/ in your browser.
You can now also attach break points in code for debugging purposes, by clicking next to a line number in any source file.

## Fuzzing with [JQF](https://github.com/rohanpadhye/JQF)

### [Zest](https://github.com/rohanpadhye/JQF/wiki/Fuzzing-with-Zest)

Run:
```
mvn test-compile jqf:fuzz -Dclass=edu.kit.typicalc.model.parser.LambdaParserFuzzTest -Dmethod=testInference
```

This will use the `LambdaTermGenerator` to create random lambda terms that are then passed to the `ModelImpl`.

### [AFL](https://lcamtuf.coredump.cx/afl/)

First install the necessary JQF tools: https://github.com/rohanpadhye/jqf/wiki/Fuzzing-with-AFL

Remove the `@Ignore` annotation in `LambdaParserFuzzTest` and run:
```
mvn test-compile
jqf-afl-fuzz -c target/test-classes:target/classes -i src/test/resources/terms/ edu.kit.typicalc.model.parser.LambdaParserFuzzTest testLambdaParserAFL
```

Generated inputs are stored in `fuzz-results/queue/`.
More samples can be added to `src/test/resources/terms/` to speed up the process.

## Running tests + calculating test coverage

First run the unit tests:
```
mvn jacoco:prepare-agent test jacoco:report
```

Then run the application with the JaCoCo agent: (adjust the path to the agent jar)
```
cp target/typicalc-1.0-SNAPSHOT.jar /tmp
java  -javaagent:$HOME/.m2/repository/org/jacoco/org.jacoco.agent/0.8.6/org.jacoco.agent-0.8.6-runtime.jar=port=36320,destfile=jacoco-it.exec,output=tcpserver -jar /tmp/typicalc-1.0-SNAPSHOT.jar
```

To run the integration tests:
```
mvn integration-test
mvn jacoco:dump@pull-test-data -Dapp.host=localhost -Dapp.port=36320 -Dskip.dump=false
```

Finally, a code coverage report can be generated:
```
mvn antrun:run@generate-report -Dskip.int.tests.report=false
```

JaCoCo execution data of manual tests should be exported to `target/jacoco-manual.exec`.

## Deploying using Docker

To build the Dockerized version of the project, run:

```
docker build . -t typicalc:latest
```

Once the Docker image is correctly built, you can run it:

```
docker run -p 80:8080 typicalc:latest
```

## Deploying using a JAR

First build the application:

```
mvn clean package -Pproduction
```

Then run the server:
```
PORT=80 java -jar target/typicalc-1.0-SNAPSHOT.jar
```
