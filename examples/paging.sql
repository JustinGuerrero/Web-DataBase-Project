

SELECT * FROM tracks
LIMIT 10
OFFSET 20;

SELECT * FROM tracks
ORDER BY tracks.Milliseconds
LIMIT 10
OFFSET 20;

SELECT * FROM tracks
ORDER BY
         tracks.Milliseconds DESC,
         tracks.Name
LIMIT 10
OFFSET 20;

SELECT TrackId,Name, Composer
FROM
    tracks
ORDER BY
    Composer;

SELECT TrackId,Name, Composer
FROM
    tracks
ORDER BY
    Composer NULLS LAST;

EXPLAIN QUERY PLAN
    SELECT TrackId,Name, Composer
FROM
    tracks
ORDER BY
    Composer DESC NULLS LAST;

CREATE INDEX
    track_composers
    ON tracks (Composer);

EXPLAIN QUERY PLAN
SELECT EmployeeId, LastName, FirstName, HireDate
FROM employees
WHERE julianday(HireDate) > (SELECT AVG(julianday(HireDate)) FROM employees);

