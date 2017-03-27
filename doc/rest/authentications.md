Authentications
===============
Information about the current authenticated session. In many cases, this resource is typically used the used to get the `userId` associated with the current session.

```
-----  Request  -----
GET http://localhost:8081/rest/v1/authentications/
Headers:  {Authorization: 1961-9761-4144}

-----  Response  -----
Status:  HTTP/1.1 200 OK

{"authenticationId":3,"userId":1,"token":"1961-9761-4144"}
```
