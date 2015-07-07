## PWD project

### How to build and run me

Clone our git repository:

```
git clone https://github.com/pwd-project/pwd.git
cd pwd
```

Build project and fire tests:

```
gradlew test
```

Run PWD service at `http://localhost:8080`

```
gradlew run
```

### Database
Depending on selected ENV, service connects to database configured in `/src/resources/application-ENV.yml`.
By default it's PostgresSQL at `localhost:5432`, dbname, user and password = `pwd`. 


### Health check

Try `http://localhost:8080/` to see if service is up & running.

### Project version
Based on GIT tags. 
Check the `currentVersion` number

```
gradlew cV
```
