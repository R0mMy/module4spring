FROM openjdk
WORKDIR /app
COPY /путь_из_локального /куда_на_удаленном */app/блабла.jar*
ENTRYPOINT ["java", "-jar", "блабла.jar"]