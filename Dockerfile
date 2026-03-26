# ecodrop : cette image lance le projet compilé directement, avec bruno de disponible. l'objectif est de lancer les tests API dedans

FROM tomcat:11.0-jdk21-temurin

# installer Node.js et Bruno CLI pour tests API
RUN apt-get update && apt-get install -y nodejs npm && npm install -g @usebruno/cli && rm -rf /var/lib/apt/lists/*

# webapp + drivers nécessaires
COPY tomcat/webapps/ecodrop /usr/local/tomcat/webapps/ecodrop
COPY tomcat/lib/* /usr/local/tomcat/lib/

# recompiler avec les bons jars jakarta de l'image
RUN cd /usr/local/tomcat/webapps/ecodrop/WEB-INF && \
    mkdir -p classes && \
    javac -cp "/usr/local/tomcat/lib/*:lib/*" -d classes $(find src -name "*.java")

# collection bruno
COPY bruno-clean /bruno-clean

EXPOSE 8080

CMD ["catalina.sh", "run"]

# docker build .
# docker run --rm -it -p blavogiez/ecodrop-tester