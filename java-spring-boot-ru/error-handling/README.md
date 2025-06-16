## Обработка ошибок

### src/main/java/exercise/exception/ResourceNotFoundException.java

Создайте исключение `ResourceNotFoundException` для обработки ошибки `404`.

### src/main/java/exercise/handler/GlobalExceptionHandler.java

Создайте глобальный обработчик ошибок `GlobalExceptionHandler` для нашего приложения. Реализуйте в нем возврат ответа с кодом `404` в случае, если запрашиваемый товар не найден в базе данных.

### src/main/java/exercise/controller/ProductsController.java

Добавьте в контроллер обработчики для следующих маршрутов:

* *GET /products/{id}* — просмотр конкретного товара
* *PUT /products/{id}* — обновление данных товара

Реализуйте в этих методах обработку ошибок. Если запрашиваемый товар не найден в базе данных, код должен вернуться ответ с кодом `404` и сообщением типа *Product with id 3 not found*.
