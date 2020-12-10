# Java DIY HTTP Server
This project is inspired by [The Programmers Hangout](https://discord.gg/programming).

## Features
This is a basic http-server that can handle basic HTTP Methods and
can prevent people from escaping the webroot. It is also multithreaded
and can handle multiple requests at once.
The response in my tests (Postman) were around 7ms.

## TODO

- ~~Automatic Content-Type~~ _(Done)_
- Better (Method) Access control
- Rate Limits
- ~~eventually MIME-Types~~ _(Done)_