# Semantria SDK for PHP

The [Semantria](https://www.lexalytics.com/semantria) SDKs are the most convenient way to integrate with the Semantria API to build a continuous or high-volume application. The SDKs implement all available Semantria features and include some simple examples of their use. However, the examples are not intended to demonstrate the best practices for processing large volumes of data. Please contact Lexalytics for guidance if you plan to build your own application.

For small volume, or ad-hoc, interactive data exploration check out [Semantria for Excel](https://www.lexalytics.com/semantria/excel).

See [semantria.readme.io](https://semantria.readme.io/docs/) for complete API documentation.

If you find a bug or have suggestions let us know: support@lexaltyics.com. Or fork this repo, make your changes, and submit a pull request.

## Installation

To install simply clone or download the SDK from https://github.com/Semantria/semantria-sdk.

### Testing

We use the PHPUnit testing framework. Find full installation instructions [here](https://phpunit.de/index.html). In brief:

    $ wget -O phpunit https://phar.phpunit.de/phpunit-7.phar
    $ chmod +x phpunit

To run the SDK tests, first set the environment variables SEMANTRIA\_KEY and SEMANTRIA\_SECRET with your key and secret.

Then run the tests:

    $ phpunit test/unittests.php

## Running the examples

There are several example programs in the examples subdirectory.

First, ensure the environment variables SEMANTRIA\_KEY and SEMANTRIA\_SECRET are set.

Change directory into the examples subdirectory. Then run an example (the detailed example in this case):

    $ php DetailedModeTestApp.php

Remember, these examples are coded in a simple style to illustrate the use of some of Semantria features. They do not represent the best practices for processing large volumes of data.

