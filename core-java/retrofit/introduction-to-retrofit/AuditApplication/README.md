# A simple Spring Boot Audit application
This application manages auditing of the Library application 

## Details
 * This application uses the in memory HSQLDB to call the Library application maintain audit logs of the calls made.
 * It uses Spring Boot to create REST endpoints
 * It uses Retrofit to make calls to the Library application
 * It uses Maven and Java11 to build and run.

## How to run
 * Clone this project
 * Use maven command: `mvn clean verify spring-boot:run`
 * The application should run at `http://localhost:8081`
 * Use POSTMAN to make REST calls

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

# PUT Sample
````json
{
    "bookName": "The Da Vinci Code",
    "publisher": "Corgie Adult - 01",
    "publicationYear": "2010"
}
````