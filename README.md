# hello-world-challenge-tests

Test for [hello-world-challenge](https://github.com/letsrokk/hello-world-challenge) application.

## Run App and Run Tests with Docker-Compose.yaml

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
    command: bash -c "apt-get update && apt-get -y install git && apt-get -y install maven && git clone https://github.com/PavelSakharchuk/hello-world-challenge-tests.git && cd hello-world-challenge-tests && mvn clean test"
    depends_on:
      - challenge
    network_mode: host
```

## Building Documentation

1. Running App with Docker;
2. Ubuntu image Building with following commands:
    - apt-get update: Packages update;
    - apt-get -y install git: Git installation for repository clone;
    - apt-get -y install maven: Maven installation for build and run autotests;
    - git clone https://github.com/PavelSakharchuk/hello-world-challenge-tests.git: Autotests repository cloning for run getting;
    - cd hello-world-challenge-tests: Jump to project folder for run maven commands;
    - mvn clean test: Running tests.