# Comments feature:

### Общие функции:
- только у PUBLISHED события могут быть комментарии
- любой пользовать должен видеть комментарии
- только пользователи с private доступом(авторизованные) могут оставлять комментарии
- пользователь может изменять только собственный комментарий
- админ может изменять / удалять комментарии кого угодно

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
get = baseUrl + /{comId}/event/{eventId} \
update = baseUri + /{comId}/event/{eventId} \
delete = baseUri + /{comId}/event/{eventId}

### Функции:
- получение всех комментариев для события
- редактирование комментария
- удаление комментария