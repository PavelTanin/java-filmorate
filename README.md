# java-filmorate
Template repository for Filmorate project.

DB project:

![Filmorate DB model](https://user-images.githubusercontent.com/102518617/187655741-30d33aeb-2a01-4627-8642-763c5cef9241.png)


1. Таблицы users и films являются основными.
2. Для реализации логики по добавлению в друзья используется таблица friendship.
        Когда user1 добавляет в друзья user2 в таблице появляется связка значения user1-user2. Связка user2-user1 появляется в том случае, если user2 также добавит
        user1 в свой френдлист.
        При запросе списка друзей, поиск ведется по user_id, пользователя запросившего список. Возвращаются данные пользователей, чьи id находятся в поле friended_id в           связке с user_id пользователя, запросившего список. 
3. Для реализации логики по добавлению лайка используется таблица likes.
        Когда user ставит лайк фильму, в таблице появляется значение film_id-user_id. Обновление поля rate у film_id в таблице films производится на основе подсчета
        связок film_id-user_id(n).
4. Возможная многожанровость film реализована через дополнительную таблицу film_genre.
5. Рейтинг фильма также реализован через дополнительную таблицу MPA_rating. id соответствующего рейтинга будет указываться в поле MPA таблицы film. Такой подход позволит значительно проще, в случае, необходимости переименовывать рейтинги.  

