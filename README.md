# PGR301 - Application

Application for exam in class PGR301 at Kristiania University College as seen at [https://github.com/pgr301-2019/labguide/blob/master/eksamen.md].

## Getting started 

To get started with this application, first set up the infrastructure following the guidelines on this repo: 
[TODO: Insert link to repo]

To run locally, set the variables under _local_, and 
run the script called `./run_influx.sh` if you don't already have influx running.

After the infrastructure has been set up, you can run this repo using Travis. 
To build this project, it's set up so that Travis will handle it for you. 
Set up Travis to work on your project by navigating to [travis-ci.com] and connecting Travis to your repo. 
For Travis to set up everything correctly, you will have to set a few variables as well.

### Requirements
You need to have two CLI tools for this to work: 
- Travis CLI as found [here](https://github.com/travis-ci/travis.rb).
- Heroku CLI as found [here](https://devcenter.heroku.com/articles/heroku-cli#download-and-install)

### Variables
List of variables to set either for Travis or locally. 
To set a variable for travis you can run the following command: 
>`travis encrypt SOMEVAR="secretvalue" --add`

To set a variable locally you can use one of the following commands: 
> MacOS
>
> `export SOMEVAR=yourvalue`

> Windows
>
> You need to use CMD.exe as administrator for this to work
>
> `SETX SOMEVAR yourvalue`

__Travis__
- DOCKER_USERNAME
- DOCKER_PASSWORD
- LOGZ_URL
- LOGZ_TOKEN 
- HEROKU_APP_NAME (doesn't have to be secret)
- Heroku API key for deployment should be set using the following command
    - `travis encrypt $(heroku auth:token) --add deploy.api_key`

__Local__
- APP_NAME
- LOGZ_URL
- LOGZ_TOKEN


### Deploying application
To deploy the application, push the code to the repository of which Travis is watching, after setting up the infrastructure
and watch as the application is being pushed to the Heroku App.