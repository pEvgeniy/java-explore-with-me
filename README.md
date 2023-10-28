# Comments feature:
https://github.com/pEvgeniy/java-explore-with-me/pull/5
### Общие функции:
- только у PUBLISHED события могут быть комментарии
- любой пользовать должен видеть комментарии
- только пользователи с private доступом(авторизованные) могут оставлять комментарии
- пользователь может изменять/удалять только собственный комментарий
- админ может изменять/удалять комментарии кого угодно
- админ может получать выгрузку всех комментариев для выбранного события

## Private:

baseUri = /users/{userId}/comments \
post = baseUri + /event/{eventId} \
update = baseUri + /{comId}/event/{eventId} \
delete = baseUri + /{comId}/event/{eventId} 

### Функции:
- добавление комментарии для определенного события
- редактирование собственного комментария
- удаление собственного комментария

## Admin:

baseUri = /admin/comments \
get = baseUrl + event/{eventId} \
update = baseUri + /{comId}/event/{eventId} \
delete = baseUri + /{comId}/event/{eventId}

### Функции:
- получение всех комментариев для события
- редактирование комментария
- удаление комментария