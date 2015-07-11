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

### Travis CI

(https://travis-ci.org/pwd-project/pwd)

### Heroku CLI
Heroku toolbelt is a CLI tool for creating and managing Heroku apps.
You'll need this for browsing application logs when running them on Heroku PaaS. 
Install CLI from here: (https://toolbelt.heroku.com/).

Browse logs for given app (environment):

```
heroku logs --app pwd-test
```
