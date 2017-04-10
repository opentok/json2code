json2code
=========

Convert JSON schemas to code - work with JSON using typed objects in C++, Objective-C, Java and generates validation code for JavaScript.

How to get it running
---------------------

Use a version of [node.js][node] - v6 or higher. You may want to use [nvm][nvm]
if you need multiple versions of node installed.

To install dependencies run:

```
$ npm install
```

To generate your first code run

```
node json2code.js $SCHEMA $PREFIX $PACKAGE
```

The options:

* **$SCHEMA** path to JSON schema file
* **$PREFIX** prepended to the class names for Objective-C & Java output
* **$PACAKGE** the Java package to generate this code under

You can then find your generated code in the `output` folder.

Contributing
------------

JavaScript is expected to adhere to the [Airbnb style guide][asg]. Pull
requests will not merge unless:
* [eslint][eslint] checks pass

Please be sure to run `npm test` before submitting your PR.

[asg]: https://github.com/airbnb/javascript
[eslint]: http://eslint.org/
