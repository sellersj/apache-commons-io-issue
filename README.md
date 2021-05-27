# apache-commons-io-issue

This is to demo some change in behaviour when upgrading to 2.9.0.

The code that broke looked for files that do not match the surefire testcase pattern, but have the word
Test in them. This was a common problem of people adding tests that were not properly picked up by surefire
and therefore would not be run by CI.

## How to run
To run with the default, 2.8.0 version
`mvn test`
to run with 2.9.0
`mvn test -P version290`
