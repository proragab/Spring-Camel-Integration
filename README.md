# Spring-Camel-Integration

# simple demo using spring camel integration with google Geocoding API 

https://developers.google.com/maps/documentation/geocoding/intro

build and run project with maven. 

use command  spring-boot:run 

Attempt to access resources [REST API] without any authorization will fail of-course.
GET http://localhost:9090/camel/api/location/{place}
Replace {place} with place to search for 

to access to the api you should authenticate with oauth2  

Ask for token using HTTP POST on /oauth/token

POST http://localhost:9090/oauth/token?grant_type=password&username=user1&password=password1

send client credentials in Authorization header username=> root and password => root


Then use the generated token to access the API

 http://localhost:9090/camel/api/location/{place}?access_token={yourToken}

Replace {place} with place to search for and {yourtoken} with generated token
 
 The JSON response should contain: the formatted address, as well as latitude and longitude of the
address.


Ask for a new access token via valid refresh-token, using HTTP POST on /oauth/token, with grant_type=refresh_token,and sending actual refresh token. Additionally, send client credentials in Authorization header username=> root and password => root

POST http://localhost:9090/oauth/token?grant_type=refresh_token&refresh_token={refresh_token}



