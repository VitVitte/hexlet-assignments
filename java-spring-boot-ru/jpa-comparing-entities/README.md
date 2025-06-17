## Сравнение сущностей

### src/main/java/exercise/model/Product.java

## Задачи

Создайте модель `Product` со свойствами, которая будет представлять товар. У товара есть уникальный идентификатор, название *title* и цена *price*. Идентификатор должен генерироваться автоматически. Укажите аннотацию `EqualsAndHashCode` так, чтобы объект сравнивался по названию и цене. Если название и цена совпадает — значит товары одинаковые

### src/main/java/exercise/exception/ResourceAlreadyExistsException.java

Создайте исключение `ResourceAlreadyExistsException` для обработки ошибки `409`.

### src/main/java/exercise/handler/GlobalExceptionHandler.java

Добавьте в глобальный обработчик ошибок реализацию возврата ответа с кодом `409` в случае, если создаваемый товар уже существует в базе данных.

### src/main/java/exercise/controller/ProductsController.java

Добавьте в контроллер обработчики для следующего маршрута:

* *POST /products* — добавление нового товара. Если товар уже существует, то должно выбрасываться исключение `ResourceAlreadyExistsException`
