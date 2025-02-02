# Stage that builds the application, a prerequisite for the running stage
FROM maven:3-jdk-11 as build
RUN curl -sL https://deb.nodesource.com/setup_12.x | bash -
RUN apt-get update -qq && apt-get install -qq --no-install-recommends nodejs

# Stop running as root at this point
RUN useradd -m typicalc
WORKDIR /usr/src/app/
RUN chown typicalc:typicalc /usr/src/app/
USER typicalc

# Copy pom.xml and prefetch dependencies so a repeated build can continue from the next step with existing dependencies
COPY --chown=typicalc pom.xml ./
RUN mvn dependency:go-offline -Pproduction

# Copy all needed project files to a folder
COPY --chown=typicalc:typicalc src src
COPY --chown=typicalc:typicalc frontend frontend
COPY --chown=typicalc:typicalc package.json pnpm-lock.yaml webpack.config.js ./


# Build the production package, assuming that we validated the version before so no need for running tests again
RUN mvn clean package -DskipTests -Pproduction

# Running stage: the part that is used for running the application
FROM openjdk:11
COPY --from=build /usr/src/app/target/*.jar /usr/app/app.jar
RUN useradd -m typicalc
USER typicalc
EXPOSE 8080
CMD java -jar /usr/app/app.jar
