# PWD project

## How to build and run me

Clone our git repository:

```
git clone https://github.com/pwd-project/pwd.git
cd pwd
```

Build project and fire tests:

```
gradlew test
```

Run PWD service at `http://localhost:8080` (requires local Postgres, see [Databases](#Databases))

```
gradlew run
```

Health check:

try `http://localhost:8080/` to see if service is up & running.

## Environments

Environments are configured with `application-*.yml`. 
For now, there are 3 envs:

<table style='word-wrap: break-word; border: 1px solid gray;'>
<tr>
  <td></td>
  <th>local (default)</th>
  <th>integration</th>
  <th>prod</th>
</tr>

<tr>
  <th>What for</th>
  <td>development</td>
  <td>integration tests</td>
  <td>production</td>
</tr>

<tr>
  <th>Database</th>
  <td>Postgres at `localhost:5432/pwd`</td>
  <td>Postgres at `localhost:5432/pwd_integration`</td>
  <td>Postgres at PaaS</td>
</tr>

<tr>
  <th>Heroku deploy</th>
  <td></td>
  <td></td>
  <td>from `master` branch</td>
</tr>

<tr>
  <th>URL</th>
  <td></td>
  <td></td>
  <td><a href="http://pwd-prod.herokuapp.com">pwd-prod.herokuapp.com</a></td>
</tr>


</table>

### Databases
PWD service uses PostgreSQL, as usual, for integration test we are using in-memory database (H2).

Depending on selected ENV, the service connects to an SQL database configured in `/src/resources/application-ENV.yml`.

By default it's a PostgreSQL running at `localhost:5432`, dbname, user and password:  `pwd`. 

### Travis CI
As usual, Travis is doing hard Continuous Integration job
and checks if all of our commits are sound.

See latest build and build history:
(https://travis-ci.org/pwd-project/pwd)

### Heroku CD

PWD production service runs on [Heroku](https://www.heroku.com) PaaS,
which means that Heroku is doing Continous Delivery job.
See [Deployment reference](https://devcenter.heroku.com/categories/reference#deployment).

Deployment to Heroku container is done with `git push` to proper Heroku repository.

Travis is configured to run automatic deploys from our GitHub repository after each successful
build (currently, master branch is connected with pwd-prod application).

### Heroku toolbelt
Heroku toolbelt is a CLI tool for creating and managing Heroku apps.
You'll need this for browsing application logs when running them on Heroku PaaS. 
[Install CLI](https://toolbelt.heroku.com/).

Browse application logs for given app (environment):

```
heroku logs --app pwd-prod --source app --tail
```
### Credentials
**Heroku API-Keys** committed to GitHub repository **have to be encrypted** with Travis public key
and then, stored in `.travis.yml`. 

Travis decrypts them and passed as build environment variables,
see [encryption-keys](http://docs.travis-ci.com/user/encryption-keys/).

Encrypt Heroku API-Key and add it to `.travis.yml` using:

```
travis encrypt ****** --add deploy.api_key
```

**Database passwords** should be managed using Heroku [Config Variables](https://devcenter.heroku.com/articles/config-vars).

- `spring.datasource.password` - production database password


