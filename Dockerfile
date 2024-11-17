# Build application
FROM openjdk:17-alpine AS build

# Install Maven
RUN apk update && apk add wget
RUN wget https://dlcdn.apache.org/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.tar.gz \
	&& tar -xzvf apache-maven-3.9.9-bin.tar.gz -C /opt/ \
	&& rm apache-maven-3.9.9-bin.tar.gz
ENV PATH=$PATH:/opt/apache-maven-3.9.9/bin

WORKDIR /build

COPY . .

RUN mvn clean package -DskipTests


# Run application
FROM openjdk:17-alpine

WORKDIR /app

COPY --from=build /build/target/blog-0.0.1-SNAPSHOT.jar .

CMD ["java", "-jar", "blog-0.0.1-SNAPSHOT.jar"]
