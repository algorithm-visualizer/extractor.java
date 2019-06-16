# extractor.java

> This repository is part of the project [Algorithm Visualizer](https://github.com/algorithm-visualizer).

`extractor.java` is an AWS Lambda function that compiles/runs Java code and extracts visualizing commands from it.

## Deploy

1. Create an AWS Lambda function with the following properties:

    - Function name: `extractor-java`

    - Runtime: `Java 8`

    - Handler: `Extractor::handleRequest` 
    
    - Memory: `512 MB`
    
    - Timeout: `10 sec`
    
2. Build this project and update the function.

```bash
gradle build

aws lambda update-function-code --function-name extractor-java --zip-file fileb://build/distributions/extractor.java.zip
```
