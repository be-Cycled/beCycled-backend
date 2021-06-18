# 🏃🏻 beCycled Backend ⚙️

Сервер API для работы с сервисом.

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
$ ./gradlew bootRun
```

## Сборка Docker образа

```bash
$ ./gradlew bootJar
$ docker build -t becycled-backend .
```
