MERGE INTO rating (rating_id, name)
    VALUES (1, 'G'),
           (2, 'PG'),
           (3, 'PG-13'),
           (4, 'R'),
           (5, 'NC-17');

MERGE INTO genres (genre_id, name)
    VALUES (1, 'Боевик'),
           (2, 'Триллер'),
           (3, 'Приключения'),
           (4, 'Детектив'),
           (5, 'Фантастика'),
           (6, 'Комедия'),
           (7, 'Драма'),
           (8, 'Мультфильм');