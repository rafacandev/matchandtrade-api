Trades
======
Trades are the central components of Match and Trade. The user who creates a trade becomes the _organizer_ and people can join the trade and became _members_. _Members_ submit their `Trade Items`. Later the _organizer_ closes the trade and the application generates the results of the trade.

```
-----  Request  -----
POST http://localhost:8081/rest/v1/trades/
Headers:  {Authorization: 1961-9761-4144}{Content-Type: application/json}

{"name": "Testing Trade Name"}

-----  Response  -----
Status:  HTTP/1.1 200 OK

{"name":"Testing Trade Name","tradeId":1,"_links":[]}
```


```
-----  Request  -----
GET http://localhost:8081/rest/v1/trades/1
Headers:  {Authorization: 1961-9761-4144}{Content-Type: application/json}

-----  Response  -----
Status:  HTTP/1.1 200 OK

{"name":"Testing Trade Name","tradeId":1,"_links":[]}
```
