![images/sky-logo.png](images/sky-logo.png)

# Sky Content Discovery Coding Challenge

Providing customised recommendations is a very important feature in our streaming platforms. We would like you to create a simple recommendation service, which when given a movie, recommends a similar movie. The service should be applicable in an "If you like X, you may like Y" context.

We would like you to demonstrate your capabilities to write software when given a set of requirements, including necessary documentation and tests.

Please provide your solution in Scala. If this is not possible, we will also accept submissions in Java or Kotlin. Please feel free to include any notes on how this recommender could be implemented or any rationale behind design decisions.


## Task

In the resources folder, you will find a [json file](src/main/resources/metadatas.json) containing a full catalogue of movies, along with tags associated to them. 
Write a REST service, which takes in a movie id and returns the id of a similar movie, making use of the tags provided in the json file.
Feel free to use any method you see fit to determine similarity using the tags and/or other information in the json.
