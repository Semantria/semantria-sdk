# Semantria SDK for Go

The [Semantria](https://www.lexalytics.com/semantria) SDKs are the most convenient way to integrate with the Semantria API to build a continuous or high-volume application. The SDKs implement all available Semantria features and include some simple examples of their use. However, the examples are not intended to demonstrate the best practices for processing large volumes of data. Please contact Lexalytics for guidance if you plan to build your own application.

For small volume, or ad-hoc, interactive data exploration check out [Semantria for Excel](https://www.lexalytics.com/semantria/excel).

See [semantria.readme.io](https://semantria.readme.io/docs/) for complete API documentation.

If you find a bug or have suggestions let us know: support@lexaltyics.com. Or fork this repo, make your changes, and submit a pull request.

## Installation

These instructions show how to build the SDK using Go command line tools. 

First, clone or download the SDK from [https://github.com/Semantria/semantria-sdk](https://github.com/Semantria/semantria-sdk).

Change directory into the Go directory. Then build the cli and run the unit tests:

    $ make build

### Using the cli

The `sem4cli` command line program can be used to interact with Semantria including both sending documents and polling for completed analyses.

To test connectivity, first set the environment variables `SEMANTRIA_KEY` and `SEMANTRIA_SECRET` with your key and secret.

Then you can check Semantria status:

    $ ./sem4cli status
    {"api_version":"4.2","service_status":"available","service_version":"4.2.25",...}

When sending documents, two formats are supported.  

**JSON** -- If the input files end with the extension ".json" the files are expected to contain one json document per line.   By default each document is supposed to have `id` and `text` fields.  For example:

    $ ./sem4cli send testdata/defaults.json

If your json has id and text elsewhere you can use the `--id-path` and `--text-path` flags:

    $ ./sem4cli send testdata/custom.json --id-path docid --text-path nested.text

**TEXT** -- Otherwise the input files are expected to have one document text per line.

    $ ./sem4cli send testdata/en-data.utf8

To get documents back use the `poll` command which by default will download and write to standard output any documents that are ready to pick up:

    $ ./sem4cli poll

Adding the `forever` keyword will cause the polling to continue indefinitely (control-c to gracefully exit)

    $ ./sem4cli poll forever

To see the full command line syntax use the `--help` switch.

## Further development

Have a look at the source `main.go` for inspiration.


