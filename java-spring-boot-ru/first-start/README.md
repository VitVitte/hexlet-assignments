## Знакомство со Spring Boot

#### Полезные ссылки

* [Начало работы со Spring Boot](https://spring.io/quickstart)

### build.gradle.kts

* Добавьте в файл *build.gradle.kts* плагины и зависимости, необходимые для работы приложения

#### src/main/java/exercise/Application.java

* В файле создайте класс `Application` и реализуйте в нем приложение на Spring Boot , в котором по адресу */about* отдается строчка текста *Welcome to Hexlet!*

* Запустите приложение с помощью команды *./gradlew bootRun*, откройте браузер и убедитесь, что по адресу *http://localhost:8080/about* отдается нужный текст

* Проверьте, как происходит автоматический рестарт приложения при изменении кода

* Добавьте к выводу еще какой-нибудь текст и обновите страницу браузера

### Подсказки

* В зависимости от вашей IDE для работы автоматического рестарта могут потребоваться [дополнительные действия](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.devtools.restart)
