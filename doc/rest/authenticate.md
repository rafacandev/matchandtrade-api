Authenticate
============
This endpoint (endpoints do not necessarily operate on resources. See ["Web Services - Terminology](https://www.w3.org/TR/2011/REC-ws-metadata-exchange-20111213/#terms)) is going to redirect the client to an oAuth server and later redirected back to the consumer with an header called `Authorization`.

The `Authorization` header needs to be included to any secured REST resources.

```
-----  Request  -----
GET http://localhost:8081/authenticate


-----  Response  -----
Status:  HTTP/1.1 200 OK
Headers:	{Authorization: 8139-5766-8048}

{"userId": "1", "email": "testing.email@test.com", "name": "AuthenticationOAuthExistingUserMock", "authorizationHeader": "8139-5766-8048"}
```
