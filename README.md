# Bookstore_Mini_Project
 Language used- Kotlin, Framwoek- Spring
 
## Objective
 This project is about implementing a Bookstore REST API.
## Functionalities
 * This RSET API implementation is going to return xml or json data based on the **Content-Type** header
 * There is a custom user model where each user will have a identification number and a username
 * Here is a Book model where each book is having a title, description, author(The custom user model), cover image and price
 * Every user will be authenticated by the API using their username and password and will get a JWT in return
 * Implementation of REST endpoints for the book resource:
   - No authentication is here
   - Here only GET (List/Detail) operations are allowed for the user
   - The list resource is searchable with the query parameter
 * Implementations of REST endpoints for the authenticated user: 
   - CRUD operation for the book resource
   - User can unpublish only their own book
   - User named **_Darth Vader_** can not publish his work on book by **Wookie Books**
 * implementation of API tests for all endpoints
