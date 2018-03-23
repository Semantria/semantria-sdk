# Semantria SDK for .NET (statically typed)

The [Semantria](https://www.lexalytics.com/semantria) SDKs are the most convenient way to integrate with the Semantria API to build a continuous or high-volume application. The SDKs implement all available Semantria features and include some simple examples of their use. However, the examples are not intended to demonstrate the best practices for processing large volumes of data. Please contact Lexalytics for guidance if you plan to build your own application.

For small volume, or ad-hoc, interactive data exploration check out [Semantria for Excel](https://www.lexalytics.com/semantria/excel).

Signup for a free trial [here](https://www.lexalytics.com/signup).

See [semantria.readme.io](https://semantria.readme.io/docs/) for complete API documentation.

If you find a bug or have suggestions let us know: support@lexaltyics.com. Or fork this repo, make your changes, and submit a pull request.


## Installation

These instructions show how to build and utilize the SDK using Microsoft Visual Studio. You are able to use the SDK in any IDE you choose.

First, clone or download the SDK from https://github.com/Semantria/semantria-sdk.

Then, open semantria-sdks/dotNet45/semantria.sln in Visual Studio.

### Testing

To test, first set the environment variables SEMANTRIA\_KEY and SEMANTRIA\_SECRET with your key and secret.

Then run the tests:

    Open the Test dropdown in the menu bar and click Test > Run > All Tests.
    
    This can also be done using the keyboard shortcut ctrl + R, A.

## Running the examples

There are several example programs in the examples subdirectory.

First, ensure the environment variables SEMANTRIA\_KEY and SEMANTRIA\_SECRET are set.

Select the desired example in the solutions window. If you don't see the solutions window you can open it by clicking View > Solution Explorer in the dropdown menu. Once you have selected desired example, right click and select Debug > Run new Instance. Alternatively, select the desired example project in the Startup Projects dropdown, then click the start button to the right.

Remember, these examples are coded in a simple style to illustrate the use of some of Semantria features. They do not represent the best practices for processing large volumes of data.

