simple-search
==============

Build and execution
-------------

simple-search is a small command line search engine. It accepts a number of JSON files as input as well as one config
file.

It is written in Java 11 and packaged with maven. To build and use:

```
mvn package
java -jar target/simple-search-1.0-SNAPSHOT-jar-with-dependencies.jar src/test/resources/integration/organizations.json src/test/resources/integration/users.json src/test/resources/integration/tickets.json src/test/resources/integration/CONFIG.json
```

Tests can be run with:

`mvn test`

Usage
-----------
The command line accepts a number of JSON files as input. These JSON files must be a valid JSON array, containing
objects. This is shown as an example above.

The fields of these objects must not be objects. Such fields will be stored but cannot be searched on.

Once successfully built, the application will present a REPL where the user can make searches. Searching is performed
with the following syntax:

`index.field=value`

index, field, and value are alphanumeric sequences. The index is the name of the file (without the file extension) to be
searched. The field is the actual fied th at will be search. The value is the value that must be matched.

They can contain any character (except double quotes) when surrounded by double quotes, for example:

`users."_id"=1`

This query will search the `users` file (users.json) for any documents with the field `_id` with the value 1.

Note that escaping double quotes inside a double quotes is not support.

The following are both valid queries:

```
"users".locale="en-AU"
"tickets"."status"=closed
```

The REPL can be ended by issuing the `exit` command instead of a query.

Configuration
----------

The application must be provided with a config file; this must be the last argument provided in the command line. In the
above example, it is CONFIG.json.

The configuration file must be of the following format:

```
{
  "idField": string,
  "foreignKeys": {
  }
}
```

The `idField` indicates which field in the input files will be treated as a unique identifier for the purpose of
querying. This is the same for every file. Any documents without a matching ID field will not be indexed.

The `foreignKeys` object allows the user to configure which fields will be treated as foreign keys when looking for
related documents.

For example, if there are two files `users.json` and `websites.json`, it can be configured like:

```
"foreignKeys": {
    "users": "user_id",
    "websites": "website_id"
}
```

When searching in the `users` file, the results from the search will be used to search other files for any documents
that have the field `user_id` and, if any of those documents' `user_id` matches any IDs in the result set, those
documents will be displayed too as related results.

The same applies for when searching in the `websites` file; any documents with a matching `website_id`
will be returned as a related result.

Assumptions
---------

- All data can be reasonably stored in memory. Very large files will not work.
- Searches cannot be performed on nested objects. Support for this could reasonably be added however.
- A valid configuration file is necessary. The application will not start without one.
- Index names, field names, and values cannot contain double quotes. Support for escape sequences however is possible to add.