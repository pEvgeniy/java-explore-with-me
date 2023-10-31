# Explore with me
Проект для размещения событий. \
Предусмотрено разделение на роли (admin, private, public) \
Авторизованные пользователи могут подавать заявки на участие в событиях, оставлять комментарии, получать подборки событий, есть сервис статистики для эндпоинтов.
## Спецификации API:
- [Сервис статистики](https://raw.githubusercontent.com/yandex-praktikum/java-explore-with-me/main/ewm-stats-service-spec.json)
- [Основной сервис](https://raw.githubusercontent.com/yandex-praktikum/java-explore-with-me/main/ewm-main-service-spec.json)

## Технологии:
- Java 11
- Maven
- Spring boot
- Spring Data JPA
- PostgreSQL, H2
- Docker
- MapStruct
- Lombok

Entity-relationship diagram: \
<img width="729" alt="ytfuft" src="https://github.com/pEvgeniy/java-explore-with-me/assets/113806896/20ab6675-3366-44bc-bcbe-f7635c90327c">

## Установка:

Пошаговая инструкция о том, как установить и настроить проект:
```bash
git clone https://github.com/java-explore-with-me.git
cd ваш-проект
mvn clean package
docker-compose up
