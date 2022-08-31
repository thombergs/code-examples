# A simple Spring Boot library application
This application manages a database of books and authors 

## Details
 * This application uses the default demo HSQLDB schema to maintain book and authors.
 * It uses Spring Boot to create REST endpoints
 * It uses Maven and Java11 to build and run.

## How to run
 * Clone this project
 * Use maven command: `mvn clean verify spring-boot:run`
 * The application should run at `http://localhost:8091`
 * Uses Basic auth for authentication.

## Sample request JSON body
# POST Sample
````json
{
"bookName": "The Da Vinci Code",
"publisher": "Corgie Adult",
"publicationYear": "2009",
"isCopyrighted": true,
"authors": [{
"name": "Dan Brown",
"dob" : "22/06/1964"
}]
}
````