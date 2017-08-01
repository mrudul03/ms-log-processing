#### Security Module

##### Security module provides helper classes for parsing JWT tokens and authorizing a service consumer.

A typical workflow contains a user authentication with a user name and password.

On a successful user authentication, the auth server sends a JSON Web Token (JWT) to the browser. All further requests come with an HTTP header that contains this 
JWT token in the form of Authorization: xxxxx.yyyyy.zzzzz.

The JWT is passed for any service-to-service communication so that any of the services can apply authorization along the way

This framework module provides helper classes to parse the JWT and authorize a service consumer.
