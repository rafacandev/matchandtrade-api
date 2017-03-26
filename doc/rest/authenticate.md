Authenticate
============
Information about the current authenticated session. In many cases, this resource is the used to get the `userId` associated with the current session.

This authentication request is going to redirect the client to a oAuth server and later redirected back with an header called `Authorization` which needs to be passed to secured REST resources.

```
-----  Request  -----
GET http://localhost:8081/authenticate

-----  Response  -----
HTTP/1.1 200 OK
Headers: 
	Authorization: 4936-9598-3514

{"userId": "1", "email": "testing.email@test.com", "name": "AuthenticationOAuthExistingUserMock", "authorizationHeader": "4936-9598-3514"}
```
