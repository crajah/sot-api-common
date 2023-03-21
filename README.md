SOT API Common
==============

Installation requirements
-------------------------
On Mac:
```
$ brew update && brew install scala && brew install sbt
```

Unit Testing
------------
```
$ sbt test
```

Integration Testing
-------------------
```
$ sbt it:test
```

Gatling
-------
Gatling tests can act as performance tests and acceptance tests (thus providing regression tests).
```
$ sbt gatling-it:test
```

Release
-------
```
$ sbt "release with-defaults"
```
or to "cross" release:
```
$ sbt "release cross with-defaults"
```