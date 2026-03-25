FROM amazoncorretto:21

COPY . .

# todo : installer bruno et 

RUN cd tomcat/bin && bash catalina.sh run &

RUN test-bruno.sh



# docker build -t blavogiez/ecodrop-tester
# docker run --rm -it -p 8080:8080 blavogiez/ecodrop-tester