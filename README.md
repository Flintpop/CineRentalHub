# CineRentalHub
10 days developed Web app to rent or buy movies.
Everything works, with some bugs, and workaround needed.

## Start the project

startReset.sh, and docker must be installed.

> You will need, once everything is running, to restart the node container to make it work.

## Notes

In this project, there are as additional features : 

- All apps are dockerized, including databases and used with docker compose
- Automatic database data import
- Https apache server
- Chat
- CRUD on Movies
- Unit tests of APIs
- Permission system in authMiddleware.js
- Detailed error messages (with dates for example)
- When deleting a user, its comments stays and become anonymous
- APIs only use procedures.