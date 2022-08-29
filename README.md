# java-filmorate
Проект в рамках обучения Яндекс Практикума по программе - java разработчик.
## Схема структуры базы данных
![DB structure.png](https://github.com/RomanBatrakov/java-filmorate/blob/main/DB%20structure.png)
## Примеры запросов для основных операций:
- Показать список всех пользователей:
```
SELECT * FROM user;
```
- Показать список популярных фильмов:
```
SELECT f.film_id, 
        COUNT(l.user_id) likes_count
FROM films AS f
LEFT JOIN likes AS l ON f.film_id = l.film_id
GROUP BY f.film_id
ORDER BY likes_count DESC
```
