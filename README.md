# hello-world-challenge-tests

Test for [hello-world-challenge](https://github.com/letsrokk/hello-world-challenge) application.

## Run App and Run Tests with [Docker-Compose.yaml](https://docs.docker.com/compose/install/)

```
version: '3.1'

services:

  challenge:
    image: letsrokk/hello-world-challenge:latest
    restart: always
    ports:
      - 8080:8080
    network_mode: host

  tests:
    image: ubuntu
    command: bash -c "
        apt-get update && 
        apt-get -y install git &&
        apt-get -y install maven &&
        git clone https://github.com/PavelSakharchuk/hello-world-challenge-tests.git &&
        cd hello-world-challenge-tests &&
        mvn test surefire-report:report &&
        cp /hello-world-challenge-tests/target/site/surefire-report.html /report"
    depends_on:
      - challenge
    volumes:
      - .:/report
    network_mode: host
```

## Building Documentation For Docker-Compose.yaml

1. Running App with Docker;
2. Ubuntu image Building with the following commands for running autotest with copying report near the docker-compose file:
    - apt-get update: Packages update;
    - apt-get -y install git: Git installation for repository clone;
    - apt-get -y install maven: Maven installation for build and run autotests;
    - git clone https://github.com/PavelSakharchuk/hello-world-challenge-tests.git: Autotests repository cloning for run getting;
    - cd hello-world-challenge-tests: Jump to project folder for run maven commands;
    - mvn test surefire-report:report: Running tests and generate report;
    - cp /hello-world-challenge-tests/target/site/surefire-report.html /report: Copying report to report path;
    
## Run App and Tests with Commands
Run App with Java
Latest version of JAR-file can be found in [Releases](https://github.com/letsrokk/hello-world-challenge/releases/latest) on GitHub
```
% java -jar hello-world-challenge-runner.jar 
```

Run Autotests (report is in the 'target/site' folder after run autotest)
```
git clone https://github.com/PavelSakharchuk/hello-world-challenge-tests.git
cd hello-world-challenge-tests
mvn clean test surefire-report:report
```