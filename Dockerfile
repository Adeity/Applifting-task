# --- Build ---
FROM openjdk:17-slim AS build

#Install gradle
RUN apt-get update && \
	apt-get install -y wget unzip

RUN wget https://downloads.gradle.org/distributions/gradle-7.3-bin.zip -qO /usr/lib/gradle-bin.zip && \
	unzip "/usr/lib/gradle-bin.zip" -d /tmp && \
	ln -s "/tmp/gradle-7.3/bin/gradle" /usr/bin/gradle

# Copy files
WORKDIR /app

COPY ./settings.gradle /app
COPY ./build.gradle /app
COPY ./src /app/src

# Build
RUN gradle assemble

# -- Create runtime image ---
FROM openjdk:17-slim AS runtime

WORKDIR /usr/app/

COPY --from=build /app/build/libs/*.jar ./
EXPOSE 8080/tcp
CMD ["java", "-jar", "./applifting-task-0.0.1-SNAPSHOT-boot.jar"]
