# Utiliser une image officielle de Java comme base
FROM openjdk:17-jdk-slim

# Ajouter le jar compilé dans le conteneur
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Exposer le port sur lequel l'application écoute
EXPOSE 8080

# Commande pour exécuter l'application Spring Boot
ENTRYPOINT ["java", "-jar", "/app.jar"]
