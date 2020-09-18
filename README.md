# akka-http-avro

[![Scala CI](https://github.com/RustedBones/akka-http-avro/workflows/Scala%20CI/badge.svg)](https://github.com/RustedBones/akka-http-avro/actions?query=branch%3Amaster+workflow%3A"Continuous+Integration")
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/fr.davit/akka-http-avro_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/fr.davit/akka-http-avro_2.13)
[![Software License](https://img.shields.io/badge/license-Apache%202-brightgreen.svg?style=flat)](LICENSE)


akka-http avro  marshalling/unmarshalling for generated java avro specific records


## Versions

| Version | Release date | Akka Http version | Avro version | Scala versions                 |
| ------- | ------------ | ----------------- | ------------ | ------------------------------ |
| `0.1.2` | 2020-09-18   | `10.2.0`          | `1.10.0`     | `2.13.3`, `2.12.12`            | 
| `0.1.1` | 2020-07-03   | `10.1.12`         | `1.10.0`     | `2.13.3`, `2.12.11`, `2.11.12` | 
| `0.1.0` | 2020-03-01   | `10.1.11`         | `1.9.2`      | `2.13.1`, `2.12.10`, `2.11.12` | 

The complete list can be found in the [CHANGELOG](CHANGELOG.md) file.

## Getting akka-http-avro

Libraries are published to Maven Central. Add to your `build.sbt`:

```scala
libraryDependencies += "fr.davit" %% "akka-http-avro" % <version>
```

## Quick start

For the examples, we are using the following avro domain model 

```json
{
  "namespace": "com.example",
  "type": "record",
  "name": "Item",
  "fields": [
    {
      "name": "name",
      "type": "string"
    },
    {
      "name": "id",
      "type": "long"
    }
  ]
}
```
and
```json
{
  "namespace": "com.example",
  "type": "record",
  "name": "Order",
  "fields": [
    {
      "name": "items",
      "type": {
        "type" : "array",
        "items": "com.example.Item"
      }
    }
  ]
}
```

Marshalling/Unmarshalling of the generated classes depends on the `Accept`/`Content-Type` header sent by the client:
- `Content-Type: application/json`: json
- `Content-Type: avro/binary`: binary

-No `Accept` header or matching several (eg `Accept: application/*`) will take the 1st matching type from the above list.

### Avro

The implicit marshallers and unmarshallers for your generated avro classes are defined in 
`AvroSupport`. Specific (un)marshallers can be imported from `AvroBinarySupport`, `AvroJsonSupport`. 
You simply need to have them in scope.

```scala
import akka.http.scaladsl.server.Directives._
import fr.davit.akka.http.scaladsl.marshallers.avro.AvroSupport._


object MyAvroService {

  val route =
    get {
      pathSingleSlash {
        complete(Item.newBuilder().setName("thing").setId(42).build())
      }
    } ~ post {
      entity(as[Order]) { order =>
        val itemsCount = order.getItems.size
        val itemNames = order.getItems.asScala.map(_.getName).mkString(", ")
        complete(s"Ordered $itemsCount items: $itemNames")
      }
    }
}
```

## Limitation

Entity streaming (http chunked transfer) is at the moment not supported by the library.


