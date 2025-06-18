## DTO

### src/main/java/exercise/controller/PostController.java

Добавьте контроллер и реализуйте в нем два маршрута для сущности `Post`. Необходимо реализовать следующие маршруты:

* *GET /posts* — cписок всех постов
* *GET /posts/{id}* — просмотр конкретного поста

Каждый пост содержит данные о привязанных к нему комментариях. Пример возвращаемого поста:

```json
{
    "id": 3,
    "title": "Post Title 3",
    "body": "Post Body 3",
    "comments": [
        {
            "id": 6,
            "body": "Comment Body 6"
        }
    ]
}
```

### src/main/java/exercise/dto/CommentDTO.java

Добавьте класс DTO для комментария с полями `id` и `body`.

### src/main/java/exercise/dto/PostDTO.java

Добавьте класс DTO для поста с полями `id`, `title`, `body` и `comments`.
