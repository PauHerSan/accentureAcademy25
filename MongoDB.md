**MongoDB con Docker**



\#Docker(CMD)



docker run --name mongodb-container -e MONGO\_INITDB\_ROOT\_USERNAME=admin -e MONGO\_INITDB\_ROOT\_PASSWORD=xideral4321 -p 27017:27017 -d mongo:8



\#MongoCompass

mongodb://admin:xideral4321@localhost:27017

-----------------------------------------------------------------------------------------------------------------

#**MySQL con Docker-Gradle**

##**1. Configurar el contenedor MySQL**

Primero, ejecuta tu comando Docker:

bashdocker run --name novels_data -e MYSQL_ROOT_PASSWORD=novels1234 -p 3307:3306 -d mysql:latest

Verifica que el contenedor esté corriendo:

*bashdocker ps*

##**2. Configurar dependencias en Gradle**

Agrega las dependencias necesarias en tu archivo build.gradle:

gradledependencies {
    // Dependencias para Spring Boot (si lo usas)
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    
    // Driver MySQL
    implementation 'mysql:mysql-connector-java:8.0.33'
    
    // O si prefieres el driver más moderno
    // implementation 'com.mysql:mysql-connector-j:8.0.33'
    
    // Otras dependencias que ya tengas...
}

##**3. Configurar la conexión en application.properties**

Crea o edita el archivo src/main/resources/application.properties:
properties

###Configuración de base de datos

spring.datasource.url=jdbc:mysql://localhost:3307/novels_db
spring.datasource.username=root
spring.datasource.password=novels1234
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

### Configuración JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true

##**4. Crear la base de datos**
Conéctate al contenedor MySQL y crea la base de datos:
bash

### Conectar al contenedor
docker exec -it novels_data mysql -u root -p

### Dentro de MySQL, crear la base de datos

CREATE DATABASE novels_db;
SHOW DATABASES;
EXIT;

##**5. Conectar desde MySQL Workbench**

En MySQL Workbench, crea una nueva conexión con estos parámetros:

Host: localhost o 127.0.0.1
Puerto: 3307
Usuario: root
Contraseña: novels1234

##**6. Ejemplo de configuración con código Java**

Si usas Spring Boot, puedes crear una entidad de ejemplo:

java@Entity
@Table(name = "novels")
public class Novel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String author;
    
    // Constructores, getters y setters
}

##**7. Comandos útiles para gestionar el contenedor**
bash
### Ver contenedores corriendo
docker ps

### Parar el contenedor
docker stop novels_data

### Iniciar el contenedor
docker start novels_data

### Ver logs del contenedor
docker logs novels_data

### Eliminar el contenedor (cuidado, perderás los datos)
docker rm novels_data

##**8. Dockerfile opcional para tu aplicación**
Si quieres containerizar también tu aplicación Java:

dockerfileFROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]






