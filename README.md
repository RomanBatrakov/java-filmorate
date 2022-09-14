# java-filmorate
Проект в рамках обучения Яндекс Практикума по программе - java разработчик.
## ER диаграмма базы данных проекта
![DB structure.png](https://github.com/RomanBatrakov/java-filmorate/blob/main/ER%20diagram.png)
## Примеры запросов для основных операций:
- Показать список всех пользователей:
```
SELECT * FROM user;
```
- Показать общий список друзей:
```
SELECT * FROM users u
JOIN (SELECT friend_id FROM friends WHERE user_id = ?) f 
ON u.user_id = f.friend_id
JOIN (SELECT friend_id FROM friends WHERE user_id = ?) l 
ON u.user_id = l.friend_id;
```
- Показать список популярных фильмов:
```
SELECT * FROM films f
LEFT JOIN (SELECT film_id, COUNT(*) likes_count
FROM likes GROUP BY film_id) l ON f.film_id = l.film_id
ORDER BY l.likes_count DESC LIMIT ?;
```
- Показать жанры фильма:
```
SELECT f.id, name FROM film_genres f
LEFT JOIN (SELECT * FROM genres) g ON f.id = g.id 
WHERE film_id = ?;
```
