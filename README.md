# pekko-http-avro

[![Continuous Integration](https://github.com/RustedBones/pekko-http-avro/actions/workflows/ci.yml/badge.svg)](https://github.com/RustedBones/pekko-http-avro/actions/workflows/ci.yml)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/fr.davit/pekko-http-avro_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/fr.davit/pekko-http-avro_2.13)
[![Software License](https://img.shields.io/badge/license-Apache%202-brightgreen.svg?style=flat)](LICENSE)
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)


pekko-http avro  marshalling/unmarshalling for generated java avro specific records


## Versions

| Version | Release date | Pekko Http version | Avro version | Scala versions |
|---------|--------------|--------------------|--------------|----------------|
| `x.x.x` | xxxx-xx-xx   | `x.x.x`            | `x.x.x`      | `x.x.x`        |

The complete list can be found in the [CHANGELOG](CHANGELOG.md) file.

## Getting pekko-http-avro

Libraries are published to Maven Central. Add to your `build.sbt`:

```scala
libraryDependencies += "fr.davit" %% "pekko-http-avro" % <version>
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
- `Content-Type: avro/raw`: binary without schema
- `Content-Type: avro/binary`: binary with schema (see [schema resolution](https://avro.apache.org/docs/1.11.1/specification/#schema-resolution))

-No `Accept` header or matching several (eg `Accept: application/*`) will take the 1st matching type from the above list.

### Avro

The implicit marshallers and unmarshallers for your generated avro classes are defined in 
`AvroSupport`. Specific (un)marshallers can be imported from `AvroBinarySupport`, `AvroJsonSupport`. 
You simply need to have them in scope.

```scala
import org.apache.pekko.http.scaladsl.server.Directives._
import fr.davit.pekko.http.scaladsl.marshallers.avro.AvroSupport._


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


