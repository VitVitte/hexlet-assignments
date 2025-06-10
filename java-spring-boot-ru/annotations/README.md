## Аннотации

В этом упражнении мы создадим аннотацию, с помощью которой на экран будет печататься информация о методах, которые ею помечены.

### Полезные ссылки

* Класс [Method](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Method.html). Он представляет информацию о методе
* Класс [Class](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html). Он представляет классы в приложении

### src/main/java/exercise/annotation/Inspect.java

* Создайте аннотацию `Inspect`. С ее помощью мы будем отмечать методы, о которых мы хотим получить информацию в рантайме.

### src/main/java/exercise/model/Address.java

У нас есть простой класс `Address`, который представляет почтовый адрес города. Отметьте аннотацией `Inspect` методы класса `getCity()` и `getPostalCode()`.

### src/main/java/exercise/Application.java

Просто создать аннотацию мало, поэтому нужно написать обработчик аннотации.

Допишите код в методе `main()`. Создайте обработчик, который будет выводить на экран информацию о методах класса `Address`, помеченных аннотацией `Inspect`. Обработчик должен печатать название метода и тип возвращаемого значения — например, *Method getCity returns a value of type String*.
