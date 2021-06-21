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

Once successfully built, the application will present a REPL where the user can make searches.

The user has three basic commands, `1`, `2`, or `3`.

`1` will let the user progress to the search interface, where the user can choose a file to search in, a field to search
on, and a value to search for, which the user will perform in three stages.

`2` displays a list of searchable fields for each file.

`3` will simply exit the REPL and close the application.

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
querying. This is the same for every file. Any documents without a matching ID field will not be indexed. This is also
the field that foreign keys reference.

The `foreignKeys` object allows the user to configure which fields will be treated as foreign keys when looking for
related documents.

For example, if there are two files `users.json` and `websites.json`, it can be configured like:

```
{
    "idField": "_id",
    "foreignKeys": {
        "users": "user_id",
        "websites": "website_id"
    }
}
```

When searching in the `users` file, the results from the search will be used to search other files for any documents
that have the field `user_id` and, if any of those documents' `user_id` matches any of the `_id` field in the result
set, those documents will be displayed too as related results.

The same applies for when searching in the `websites` file; any documents with a matching `website_id`
will be returned as a related result.

Assumptions and limitations
---------

- All data can be reasonably stored in memory. Very large files will not work.
- Searches cannot be performed on nested objects. Support for this could reasonably be added however.
- A valid configuration file is necessary. The application will not start without one.
- This application can only perform exact match searches.

Design Notes
--------
This is a simple implementation of a search index. It uses a number of inverted indices in order to perform quick
searches; the actual data is stored in another index where it can be referred by ID after the actual search is
performed, just to display the data.

The configuration file allows the application to have significant flexibility in how the users want their data model to
be used, as it allowed customisation of the ID field as well as the foreign key references.