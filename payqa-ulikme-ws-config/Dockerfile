FROM adoptopenjdk/openjdk11

MAINTAINER Fabian Perez <fperezv@payqa.dev>

ADD payqa-ulikme-ws-config-api/target/payqa-ulikme-ws-config.jar payqa-ulikme-ws-config.jar

ENTRYPOINT ["java", "-jar", "payqa-ulikme-ws-config.jar"]