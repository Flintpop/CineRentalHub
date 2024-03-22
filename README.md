# CineRentalHub
10 days developed Web app to rent or buy movies.
Everything works, with some bugs, and workaround needed.

## Start the project

startReset.sh, and docker must be installed.

> You will need, once everything is running, to restart the node container to make it work.

## Features of the project

### Core features
- CRUD on movies
- CRUD on users
- CRUD on comments
- Cart system, with renting and buying movies
- Authentification system
- Viewing history for users
- Embedded YouTube player for movies

### Technologies

- Frontend: Vite, Vue.js
- Backend: Node.js, Apache, Servlet, MariaDB, MongoDB

### Additional features

- All apps are dockerized, including databases and used with docker compose
- Automatic database data import at startReset.sh
- Https apache server, with a self-signed certificate, and the possibility to encrypt all the traffic and api calls (https://localhost/api/...)
- Chat
- CRUD on Movies
- Unit tests of APIs
- Permission system, in authMiddleware.js, on both front and back for both routes and methods depending on the role
- Detailed error messages (with dates for example)
- When deleting a user, its comments stays and become anonymous
- APIs only use procedures.

## Architecture

**Mariadb**
![alt text](cine_rental_hub.png)

**MongoDB**
```json
[
  {
    "_id": 1,
    "user_id": 1,
    "movie_id": 1,
    "view_date": {"$date": "2020-01-01T20:00:00Z"}
  }
]
```

### APIs

- Movies
- Users
- Comments
- Cart
- Stats

See their swagger files in the docs/swagger folder.