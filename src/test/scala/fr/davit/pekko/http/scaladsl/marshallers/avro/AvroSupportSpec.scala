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
import org.apache.pekko.http.scaladsl.server.Directives._
import org.apache.pekko.http.scaladsl.testkit.ScalatestRouteTest
import org.apache.pekko.http.scaladsl.unmarshalling.Unmarshal
import org.apache.pekko.http.scaladsl.unmarshalling.Unmarshaller.UnsupportedContentTypeException
import fr.davit.avro.TestMessage
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.annotation.nowarn

class AvroSupportSpec extends AnyFlatSpec with Matchers with ScalaFutures with ScalatestRouteTest {

  val avro = TestMessage
    .newBuilder()
    .setStringField("test")
    .setNumberField(42)
    .build()

  val dataForContentType = Map[ContentType, Array[Byte]](
    ContentTypes.`application/json` -> """{"string_field":"test","number_field":42}""".getBytes,
    AvroProtocol.`avro/binary`      -> avro.toByteBuffer.array()
  )

  class AvroTestSuite(avroSupport: AvroAbstractSupport) {

    import avroSupport.{avroMarshaller, avroUnmarshaller}

    it should "marshall avro message with default content type" in {
      Get() ~> get(complete(avro)) ~> check {
        contentType shouldBe avroSupport.contentTypes.head
        responseAs[Array[Byte]] shouldBe dataForContentType(avroSupport.contentTypes.head)
      }
    }

    it should "marshall avro message with requested content type" in {
      avroSupport.contentTypes.foreach { ct =>
        Get().withHeaders(Accept(ct.mediaType)) ~> get(complete(avro)) ~> check {
          contentType shouldBe ct
          responseAs[Array[Byte]] shouldBe dataForContentType(ct)
        }
      }
    }

    it should "unmarshall to avro message with default content type" in {
      avroSupport.contentTypes.foreach { ct =>
        val entity = HttpEntity(ct, dataForContentType(ct))
        Unmarshal(entity).to[TestMessage].futureValue shouldBe avro
      }
    }

    it should "fail unmarshalling if the content type is not valid" in {
      val entity = HttpEntity(ContentTypes.`text/plain(UTF-8)`, "")
      Unmarshal(entity).to[TestMessage].failed.futureValue shouldBe an[UnsupportedContentTypeException]
    }
  }

  "AvroBinarySupport" should behave like new AvroTestSuite(AvroBinarySupport): @nowarn
  "AvroJsonSupport" should behave like new AvroTestSuite(AvroJsonSupport): @nowarn
  "AvroSupport" should behave like new AvroTestSuite(AvroSupport): @nowarn
}
