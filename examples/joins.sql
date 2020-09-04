

SELECT employees.FirstName as FirstName,
       employees.EmployeeId as EmployeeId,
       bosses.FirstName as BossFirstName,
       bosses.EmployeeId as BossEmployeeId
FROM employees
JOIN employees AS bosses
WHERE employees.ReportsTo = bosses.EmployeeId;


SELECT *
FROM albums
    CROSS JOIN artists;

SELECT
       tracks.name
FROM
     tracks
    JOIN albums
     ON tracks.AlbumId = albums.AlbumId
    JOIN artists
     ON albums.ArtistId = artists.ArtistId
WHERE artists.name = "AC/DC";


