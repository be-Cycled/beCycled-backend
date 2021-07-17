# 🏃🏻 beCycled Backend ⚙️

Сервер API для работы с сервисом.

![beCycled Deploy Status](https://img.shields.io/github/workflow/status/be-Cycled/beCycled-devops/Deploy?label=deploy)

## Требования

* Java 11 и выше

* Docker

## Локальная сборка и запуск

Сборка и тесты:

```bash
$ ./gradlew clean build test check javadoc
```

Локальный запуск:

```bash
docker run \
    --name postgres \
    -e "POSTGRES_PASSWORD=postgres" \
    -p 5432:5432 \
    -d postgres:13

docker run \
    --name pgadmin4 \
    -e "PGADMIN_DEFAULT_EMAIL=admin@mail.com" \
    -e "PGADMIN_DEFAULT_PASSWORD=admin" \
    -p 5555:80 \
    -d dpage/pgadmin4
```

```bash
$ ./gradlew bootRun
```

## Сборка Docker образа

```bash
$ ./gradlew bootJar
$ docker build -t becycled-backend .
```

## Запуск Docker образа локально

```bash
$ docker run \
    --name becycled-backend \
    -p 8080:8080 \
    -d becycled-backend
```
