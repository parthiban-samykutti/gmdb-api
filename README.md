# gmdb-api
##An application for users to browse all the movies of the world!
As a user, I should see a list of movies when I visit GMDB.

When I visit GMDB
Then I can see a list of all movies.
##API Spec
| URI              | HTTP Method | HTTP Status | DESC                                 |   |
|------------------|-------------|-------------|--------------------------------------|---|
| /api/gmdb/movies | GET         | 200         | List all the available movies titles |   |
| /api/gmdb/movies/title/{title} | GET             |   200          |    Return the movie detail matched with title.                                  |   |
| /api/gmdb/movies/title/{title} | GET           |     404        |      Return a message  'Movie doesn't exist'                 |   |
| /api/gmdb/movies/title/{title}/rating/{rating} | PUT             |   200          |    Update the rating to the movie matched with title and Return the movie detail.                                  |   |
| /api/gmdb/movies/title/{title} | PUT             |   200          |    Update the rating and review to the movie matched with title and Return the movie detail.                                  |   |
