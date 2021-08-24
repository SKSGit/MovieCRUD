FROM ubuntu:20.04
RUN apt-get update
#skips region picking?
ARG DEBIAN_FRONTEND=noninteractive 
RUN apt-get -y install postgresql
# adding: database dump file
COPY database_backup.sql /tmp/database_backup.sql
# replace existing file with modified: listen_addresses = '*'
COPY postgresql.conf /etc/postgresql/12/main/
# changing user from root to postgres
USER postgres
# start service, create fresh database, load dump, stop service
RUN whoami && \ 
	service postgresql start && \ 
	createdb -T template0 MC_DB && \
	psql --username=postgres MC_DB < /tmp/database_backup.sql && \
	service postgresql stop
#Start service, and CMD something that never quits in the foreground. Like tail -f /dev/null.
CMD service postgresql start && tail -f /dev/null
EXPOSE 80

#Try using database "MC_DB":
# docker container run -d <image_name>
# winpty docker exec -it <container_id> bash
# psql
# \c MC_DB;
# SELECT * FROM movies;