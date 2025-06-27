## Профили

### Полезные ссылки

* [Use YAML for External Properties](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.properties-and-configuration.yaml)
* [Using application.yml vs application.properties in Spring Boot](https://www.baeldung.com/spring-boot-yaml-vs-properties)

### src/main/java/exercise/resources/application-development.yml

Добавьте конфигурацию подключения к базе данных для профиля `development` со следующими параметрами:

* url: `jdbc:h2:mem:test`
* username: `sa`
* password: `123`

### src/main/java/exercise/resources/application-stage.yml

Добавьте конфигурацию подключения к базе данных для профиля `stage` со следующими параметрами:

* url: `jdbc:h2:file:./test`
* username: `admin`
* password: `admin`

Проверьте работу приложения в обоих профилях. Проверьте, что в профиле `stage` создается файл базы данных, и данные не удаляются после перезапуска приложения.
