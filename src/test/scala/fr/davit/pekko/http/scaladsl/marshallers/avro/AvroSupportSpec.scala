/*
 * Copyright 2019 Michel Davit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.davit.pekko.http.scaladsl.marshallers.avro

import org.apache.pekko.http.scaladsl.model.headers.Accept
import org.apache.pekko.http.scaladsl.model.{ContentType, ContentTypes, HttpEntity}
import org.apache.pekko.http.scaladsl.server.Directives.*
import org.apache.pekko.http.scaladsl.testkit.ScalatestRouteTest
import org.apache.pekko.http.scaladsl.unmarshalling.Unmarshal
import org.apache.pekko.http.scaladsl.unmarshalling.Unmarshaller.UnsupportedContentTypeException
import fr.davit.avro.TestMessage
import org.apache.avro.message.{BinaryMessageEncoder, RawMessageEncoder}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.nio.charset.StandardCharsets
import java.time.LocalDate

class AvroSupportSpec extends AnyFlatSpec with Matchers with ScalaFutures with ScalatestRouteTest {

  val avro: TestMessage = TestMessage
    .newBuilder()
    .setStringField("test")
    .setNumberField(42)
    .setDateField(LocalDate.of(1970, 1, 1))
    .build()

  val json: String = """{"string_field":"test","number_field":42,"date_field":0}"""

  val raw: Array[Byte] = new RawMessageEncoder[TestMessage](avro.getSpecificData, avro.getSchema)
    .encode(avro)
    .array()

  val binary: Array[Byte] = new BinaryMessageEncoder[TestMessage](avro.getSpecificData, avro.getSchema)
    .encode(avro)
    .array()

  val dataForContentType = Map[ContentType, Array[Byte]](
    ContentTypes.`application/json` -> json.getBytes(StandardCharsets.UTF_8),
    AvroProtocol.`avro/raw`         -> raw,
    AvroProtocol.`avro/binary`      -> binary
  )

  def avroTestSuite(avroSupport: AvroAllSupport, contentTypes: ContentType*): Unit = {
    import avroSupport.*

    it should "marshall avro message with default content type" in {
      Get() ~> get(complete(avro)) ~> check {
        contentType shouldBe contentTypes.head
        responseAs[Array[Byte]] shouldBe dataForContentType(contentTypes.head)
      }
    }

    it should "marshall avro message with requested content type" in {
      contentTypes.foreach { ct =>
        Get().withHeaders(Accept(ct.mediaType)) ~> get(complete(avro)) ~> check {
          contentType shouldBe ct
          responseAs[Array[Byte]] shouldBe dataForContentType(ct)
        }
      }
    }

    it should "unmarshall to avro message with default content type" in {
      contentTypes.foreach { ct =>
        val entity = HttpEntity(ct, dataForContentType(ct))
        Unmarshal(entity).to[TestMessage].futureValue shouldBe avro
      }
    }

    it should "fail unmarshalling if the content type is not valid" in {
      val entity = HttpEntity(ContentTypes.`text/plain(UTF-8)`, "")
      Unmarshal(entity).to[TestMessage].failed.futureValue shouldBe an[UnsupportedContentTypeException]
    }
  }

  "AvroBinarySupport" should behave like avroTestSuite(
    AvroBinarySupport,
    AvroProtocol.`avro/binary`
  )
  "AvroRawSupport" should behave like avroTestSuite(
    AvroRawSupport,
    AvroProtocol.`avro/raw`
  )
  "AvroJsonSupport" should behave like avroTestSuite(
    AvroJsonSupport,
    ContentTypes.`application/json`
  )
  "AvroSupport" should behave like avroTestSuite(
    AvroSupport,
    ContentTypes.`application/json`,
    AvroProtocol.`avro/raw`,
    AvroProtocol.`avro/binary`
  )
}
