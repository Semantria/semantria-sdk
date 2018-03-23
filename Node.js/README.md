# Semantria SDK for Node.js

The [Semantria](https://www.lexalytics.com/semantria) SDKs are the most convenient way to integrate with the Semantria API to build a continuous or high-volume application. The SDKs implement all available Semantria features and include some simple examples of their use. However, the examples are not intended to demonstrate the best practices for processing large volumes of data. Please contact Lexalytics for guidance if you plan to build your own application.

For small volume, or ad-hoc, interactive data exploration check out [Semantria for Excel](https://www.lexalytics.com/semantria/excel).

Signup for a free trial [here](https://www.lexalytics.com/signup).

See [semantria.readme.io](https://semantria.readme.io/docs/) for complete API documentation.

If you find a bug or have suggestions let us know: support@lexaltyics.com. Or fork this repo, make your changes, and submit a pull request.

## Installation

If you already have a project `package.json` you can simply add to your list of dependencies.

If this is a new project you can start in a new directory and create a file called `package.json` with the following:

    {
      "name": "semantria-node-project",
      "version": "1.0.0",
      "description": "Sample Semantria Project",
      "main": "index.js",
      "scripts": {
        "test": "echo \"Error: no test specified\" && exit 1"
      },
      "author": "",
      "license": "ISC",
      "dependencies": {
        "semantria-sdk": "http://www.semantria.com/download/SDK/SemantriaNodejsSDK.tar.gz"
      }
    }

To install:

    npm install

The sdk will now be usable from your Node.js code.  The api code and examples will be found in `node_modules/semantria-sdk/`

### Testing

To test, first add your key and secret to test-config.json (or set the environment variables SEMANTRIA_KEY and SEMANTRIA_SECRET). Then:

    npm test

## Running

The following is an example that will look up the configurations in your account and confirm that your credentials are correct:

    var semantria = require('semantria-sdk');
    
    // OR you can specify consumerKey and consumerSecret
    var config = {
        "username": "YOUR USERNAME",
        "password": "YOUR PASSWORD"
    }
    
    var session = new semantria.Session(config);
    session.getConfigurations().then(function(configs) {
        console.log(JSON.stringify(configs, null, 4));
    }).catch(function(err) {
        console.log("ERROR: " + err);
    });


For complete examples see the scripts located in `node_modules/semantria-sdk/examples`

Remember, these examples are coded in a simple style to illustrate the use of some of Semantria features. They do not represent the best practices for processing large volumes of data.
