# Clustered Data Warehouse

# About
Warehouse cluster is a solution that reads large logs of csv files and persist to either a mongo or a mysql database. 
In this solution the database in use is MongoDB. Please see docs on [MONGODB](http://https://docs.mongodb.com/)
 for setting up.

Large files are broken to chuncks in a very short time and a parallel processing is initiated to validate and persist them.
This application leverages on the system resources of the machine. If the the processor is more than eight cores it will process 
faster being that the test has just being performed on a system with an 8 CPU.

# Achievement
The major to this solution is the mode of processing that was enhanced through parallel processing and the way 
the batch file is deserialised which gained performance. 

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

  [Node.js][https://nodejs.org/en/docs/]: We use Node to run a development web server and build the project.
    Depending on your system, you can install Node either from source or as a pre-packaged bundle.

    npm install

 [Webpack][] is used as the client build tool. To start the application in dev mode run the two must be 
 run in the sequence as below.

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

To launch server tests, run:

    ./mvnw clean test

The result can be found on [./target/test-results/coverage/jacoco/index.html] target/test-results/coverage/jacoco/index.html
### Client tests

Unit tests are run by [Jest][https://jestjs.io/docs/en/getting-started] and written with [Jasmine][https://jasmine.github.io/pages/getting_started.html]:

    npm test 


## Using Docker

To Dockerize this application. Jib an open source Java tool by Google for building Docker images of Java applications
is used. This does not require prior installation of docker. This catches changes in the application each time it builds.
With Jib docker build is added on the build pipeline. 

To build fully dockerize application and all the services that it depends on.
To achieve this, docker image of the app by running:

    ./mvnw package -Pprod verify jib:dockerBuild

Then run:

    docker-compose -f src/main/docker/app.yml up -d


## How it works

Fire up any of the environment. 

The application will land you on the home page where you can upload files. The test file for this application can be 
found here [./deal/init-csv](./deals/init-csv).

After uploaded is shows a summary alert and a redirect to the summary page. From the summary page you can 
have links to view reports and deals that were uploaded.

Duplicate files are not allowed and each duplicate is saved as a patch.

