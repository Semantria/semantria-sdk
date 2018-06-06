# Semantria SDK for Java

The [Semantria](https://www.lexalytics.com/semantria) SDKs are the most convenient way to integrate with the Semantria API to build a continuous or high-volume application. The SDKs implement all available Semantria features and include some simple examples of their use. However, the examples are not intended to demonstrate the best practices for processing large volumes of data. Please contact Lexalytics for guidance if you plan to build your own application.

For small volume, or ad-hoc, interactive data exploration check out [Semantria for Excel](https://www.lexalytics.com/semantria/excel).

See [semantria.readme.io](https://semantria.readme.io/docs/) for complete API documentation.

If you find a bug or have suggestions let us know: support@lexaltyics.com. Or fork this repo, make your changes, and submit a pull request.

## Installation

These instructions show how to build the SDK using maven. You should be able to build and run the eamples in your Java IDE as well.

First, clone or download the SDK from https://github.com/Semantria/semantria-sdk.

Change directory into the Java directory. Then compile and install the SDK and examples:

    $ mvn package install

### Testing

To test, first set the environment variables SEMANTRIA\_KEY and SEMANTRIA\_SECRET with your key and secret.

Then run the tests:

    $ mvn test -DskipTests=false

(For those wondering why the tests don't run by default, they will fail if the key/secret are not set, thus preventing the SDK from building.)

## Running the examples

First, ensure the environment variables SEMANTRIA\_KEY and SEMANTRIA\_SECRET are set.

Then run the detailed example:

    $ java -cp target/semantria-java-sdk-4.2.104-jar-with-dependencies.jar com.semantria.example.DetailedModeTestApp

Several other examples are in com.semantria.example.

Remember, these examples are coded in a simple style to illustrate the use of some of Semantria features. They do not represent the best practices for processing large volumes of data.

