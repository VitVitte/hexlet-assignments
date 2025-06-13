## REST API в Spring Boot

### Полезные ссылки

* [Начало работы со Spring Boot](https://spring.io/quickstart)

### src/main/java/exercise/controller/users/PostsController.java

Реализуйте дополнительные обработчики маршрутов для `Post`:

* *GET /api/users/{id}/posts* — список всех постов, написанных пользователем с таким же `userId`, как `id` в маршруте
* *POST /api/users/{id}/posts* – создание нового поста, привязанного к пользователю по `id`. Код должен возвращать статус 201, тело запроса должно содержать `slug`, `title` и `body`. Обратите внимание, что `userId` берется из маршрута
