# Java DIY HTTP Server
This project is inspired by [The Programmers Hangout](https://discord.gg/programming). This was created using [this](https://theprogrammershangout.com/resources/projects/http-project-guide/intro.md) guide.

## Features
This is a basic http-server that can handle basic HTTP Methods and
can prevent people from escaping the webroot. It is also multithreaded
and can handle multiple requests at once.
The response in my tests (Postman) were around 7ms.

## TODO

- Automatic Content-Type
- Better (Method) Access control
- Rate Limits
- eventually MIME-Types
