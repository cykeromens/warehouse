# Warehouse Cluster

# About
Warehouse cluster is a solution that reads large logs of csv files and persist to either a mongo or a mysql database. 
In this solution the database in use is MongoDB. Please see docs on [MONGODB](http://https://docs.mongodb.com/)
 for setting up.

Large files are files are broken to chuncks in a very short time and a parallel processing is initiated to persist them.
This application leverages on the processors of the machine. If the the processor is more than eight it will process 
faster being that the test has just being performed on a system with an 8 CPU.

# Achievement
The major to this solution is the mode of processing that was enhanced through parallel processing and the way 
the batch file is deserialised which gained performance. 

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

  [Node.js][]: We use Node to run a development web server and build the project.
    Depending on your system, you can install Node either from source or as a pre-packaged bundle.

    npm install

 [Webpack][] is used as the client build tool. To start the application in dev mode run

    ./mvnw
    npm start

## Building for production

To optimize the cluster application for production, run:

    ./mvnw -Pprod clean package

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

    java -jar target/*.war

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

## Testing

To launch your application's tests, run:

    ./mvnw clean test

### Client tests

Unit tests are run by [Jest][] and written with [Jasmine][]:

    npm test


## Using Docker

Docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a mongodb database in a docker container, run:

    docker-compose -f src/main/docker/mongodb.yml up -d

To stop it and remove the container, run:

    docker-compose -f src/main/docker/mongodb.yml down

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

    ./mvnw package -Pprod verify jib:dockerBuild

Then run:

    docker-compose -f src/main/docker/app.yml up -d


##How it works

Fire up any of the environment. 

The application will land you on the home page where you can upload files. The test file for this application can be 
found here [./deal/init-csv](./deals/init-csv).

After uploaded is shows a summary alert and a redirect to the summary page. From the summary page you can 
have links to view reports and deals that were uploaded.

Duplicate files are not allowed and each failed duplicate is saved as an invalid collection.

