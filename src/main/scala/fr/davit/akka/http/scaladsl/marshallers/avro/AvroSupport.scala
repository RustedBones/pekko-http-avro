/*
 * Copyright 2020 Michel Davit
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

package fr.davit.akka.http.scaladsl.marshallers.avro

import akka.http.scaladsl.marshalling.{Marshaller, ToEntityMarshaller}
import akka.http.scaladsl.model.MediaType.NotCompressible
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, Unmarshaller}
import org.apache.avro.specific.SpecificRecordBase

import scala.reflect.ClassTag

object AvroProtocol {
  val `avro/binary`: ContentType = MediaType.customBinary("avro", "binary", NotCompressible)
}

trait AvroAbstractSupport {

  def contentTypes: Seq[ContentType]

  protected def coderFactory: AvroCoderFactory

  // --------------------------------------------------------------------------------------------------------------------
  // Unmarshallers
  // --------------------------------------------------------------------------------------------------------------------
  implicit def avroUnmarshaller[T <: SpecificRecordBase: ClassTag]: FromEntityUnmarshaller[T] = {
    val coder = coderFactory.newCoder[T]
    Unmarshaller.byteStringUnmarshaller
      .forContentTypes(contentTypes.map(ContentTypeRange.apply): _*)
      .map(coder.decode)
  }

  // --------------------------------------------------------------------------------------------------------------------
  // Marshallers
  // --------------------------------------------------------------------------------------------------------------------
  implicit def avroMarshaller[T <: SpecificRecordBase: ClassTag]: ToEntityMarshaller[T] = {
    val coder = coderFactory.newCoder[T]
    Marshaller.oneOf(contentTypes.map(ct => Marshaller.ByteStringMarshaller.wrap(ct.mediaType)(coder.encode)): _*)
  }
}

trait AvroBinarySupport extends AvroAbstractSupport {
  override lazy val contentTypes: Seq[ContentType]           = Seq(AvroProtocol.`avro/binary`)
  override protected lazy val coderFactory: AvroCoderFactory = AvroBinaryCoderFactory
}

object AvroBinarySupport extends AvroBinarySupport

trait AvroJsonSupport extends AvroAbstractSupport {
  override lazy val contentTypes: Seq[ContentType]           = Seq(ContentTypes.`application/json`)
  override protected lazy val coderFactory: AvroCoderFactory = AvroJsonCoderFactory
}

object AvroJsonSupport extends AvroJsonSupport

trait AvroSupport extends AvroAbstractSupport {

  override protected def coderFactory: AvroCoderFactory =
    throw new Exception("No avro coder factory defined for AvroSupport")

  private val avroSupports = Seq(AvroJsonSupport, AvroBinarySupport)

  override val contentTypes: Seq[ContentType] = avroSupports.flatMap(_.contentTypes)

  implicit override def avroUnmarshaller[T <: SpecificRecordBase: ClassTag]: FromEntityUnmarshaller[T] = {
    Unmarshaller.firstOf(avroSupports.map(_.avroUnmarshaller[T]): _*)
  }

  implicit override def avroMarshaller[T <: SpecificRecordBase: ClassTag]: ToEntityMarshaller[T] = {
    Marshaller.oneOf(avroSupports.map(_.avroMarshaller[T]): _*)
  }

}

object AvroSupport extends AvroSupport
