# Filmorate project
Filmorate project - social network with rating of films based on marks from users. Users can add each other to friendlist.
## Database ER diagram

## Database description: 
<details>
    <summary><h3>ER diagram:</h3></summary>

![DB structure.png](https://github.com/RomanBatrakov/java-filmorate/blob/main/ER%20diagram.png)
</details>
<details>
    <summary><h3>DB request examples:</h3></summary>

- Get all users:
```
SELECT * FROM user;
```
- Get common friends:
```
SELECT * FROM users u
JOIN (SELECT friend_id FROM friends WHERE user_id = ?) f 
ON u.user_id = f.friend_id
JOIN (SELECT friend_id FROM friends WHERE user_id = ?) l 
ON u.user_id = l.friend_id;
```
- Get popular films:
```
SELECT * FROM films f
LEFT JOIN (SELECT film_id, COUNT(*) likes_count
FROM likes GROUP BY film_id) l ON f.film_id = l.film_id
ORDER BY l.likes_count DESC LIMIT ?;
```
- Get genres:
```
SELECT f.id, name FROM film_genres f
LEFT JOIN (SELECT * FROM genres) g ON f.id = g.id 
WHERE film_id = ?;
```
</details>