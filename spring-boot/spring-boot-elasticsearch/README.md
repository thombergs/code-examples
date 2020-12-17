# Related Blog Posts

* [How to use Elasticsearch with Spring Data](https://reflectoring.io/spring-boot-elasticsearch/)

This application code demonstrates use of indexing and search capabilities of Spring Data Elasticsearch in a simple search application used for searching products in a product inventory. The main steps for running the application are:

1. Start an Elasticsearch Instance by running the Docker `run` command:

```shell
docker run -p 9200:9200 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.10.0
```

2. Clone the application and change the current directory to application root.
3. Build the application with Maven 

```shell
mvn clean package
```
4. Start the application

```shell
java -jar target/<application>.jar

```
5. The productindex will be built during application start up.
6. Access the application with URL: http://localhost:8080/search
7. Start to input some characters in the search box(examples: toy, white shirt, jacket, etc), which will open an auto-complete box of maximum 5 suggestions.
8. Complete the search text and click search button to see the search results.

