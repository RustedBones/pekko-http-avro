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

import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import org.apache.avro.specific.SpecificRecord
import org.apache.pekko.http.scaladsl.marshalling.{Marshaller, ToEntityMarshaller}
import org.apache.pekko.http.scaladsl.model.*
import org.apache.pekko.http.scaladsl.model.MediaType.NotCompressible
import org.apache.pekko.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, Unmarshaller}

import scala.reflect.ClassTag

object AvroProtocol {
  val `avro/binary`: ContentType = MediaType.customBinary("avro", "binary", NotCompressible)
  val `avro/raw`: ContentType    = MediaType.customBinary("avro", "raw", NotCompressible)
}

trait AvroGenericSupport {
  def avroGenericUnmarshaller(schema: Schema): FromEntityUnmarshaller[GenericRecord]
  def avroGenericMarshaller(schema: Schema): ToEntityMarshaller[GenericRecord]
}

trait AvroSpecificSupport {
  implicit def avroSpecificUnmarshaller[T <: SpecificRecord: ClassTag]: FromEntityUnmarshaller[T]
  implicit def avroSpecificMarshaller[T <: SpecificRecord: ClassTag]: ToEntityMarshaller[T]
}

trait AvroReflectSupport {
  def avroReflectUnmarshaller[T: ClassTag]: FromEntityUnmarshaller[T]
  def avroReflectMarshaller[T: ClassTag]: ToEntityMarshaller[T]
}

trait AvroAllSupport             extends AvroGenericSupport with AvroSpecificSupport with AvroReflectSupport
trait AvroProtocolGenericSupport extends AvroGenericSupport {

  def contentType: ContentType

  def genericDecoder(schema: Schema): AvroDecoder[GenericRecord]

  def genericEncoder(schema: Schema): AvroEncoder[GenericRecord]

  override def avroGenericUnmarshaller(schema: Schema): FromEntityUnmarshaller[GenericRecord] = {
    val decoder = genericDecoder(schema)
    Unmarshaller.byteStringUnmarshaller
      .forContentTypes(contentType)
      .map(decoder.decode)
  }

  override def avroGenericMarshaller(schema: Schema): ToEntityMarshaller[GenericRecord] = {
    val encoder = genericEncoder(schema)
    Marshaller
      .byteStringMarshaller(contentType)
      .compose(encoder.encode)
  }
}

trait AvroProtocolSpecificSupport extends AvroSpecificSupport {

  def contentType: ContentType

  def specificDecoder[T <: SpecificRecord: ClassTag]: AvroDecoder[T]

  def specificEncoder[T <: SpecificRecord: ClassTag]: AvroEncoder[T]

  implicit override def avroSpecificUnmarshaller[T <: SpecificRecord: ClassTag]: FromEntityUnmarshaller[T] = {
    val decoder = specificDecoder[T]
    Unmarshaller.byteStringUnmarshaller
      .forContentTypes(contentType)
      .map(decoder.decode)
  }

  implicit override def avroSpecificMarshaller[T <: SpecificRecord: ClassTag]: ToEntityMarshaller[T] = {
    val encoder = specificEncoder[T]
    Marshaller
      .byteStringMarshaller(contentType)
      .compose(encoder.encode)
  }
}

trait AvroProtocolReflectSupport extends AvroReflectSupport {

  def contentType: ContentType

  def reflectDecoder[T: ClassTag]: AvroDecoder[T]

  def reflectEncoder[T: ClassTag]: AvroEncoder[T]
  override def avroReflectUnmarshaller[T: ClassTag]: FromEntityUnmarshaller[T] = {
    val decoder = reflectDecoder[T]
    Unmarshaller.byteStringUnmarshaller
      .forContentTypes(contentType)
      .map(decoder.decode)
  }

  override def avroReflectMarshaller[T: ClassTag]: ToEntityMarshaller[T] = {
    val encoder = reflectEncoder[T]
    Marshaller
      .byteStringMarshaller(contentType)
      .compose(encoder.encode)
  }
}

trait AvroProtocolAllSupport
    extends AvroAllSupport
    with AvroProtocolGenericSupport
    with AvroProtocolSpecificSupport
    with AvroProtocolReflectSupport

trait AvroBinarySupport extends AvroProtocolAllSupport {
  override def contentType: ContentType                                       = AvroProtocol.`avro/binary`
  override def genericDecoder(schema: Schema): AvroDecoder[GenericRecord]     = AvroBinaryDecoder.generic(schema)
  override def genericEncoder(schema: Schema): AvroEncoder[GenericRecord]     = AvroBinaryEncoder.generic(schema)
  override def specificDecoder[T <: SpecificRecord: ClassTag]: AvroDecoder[T] = AvroBinaryDecoder.specific[T]
  override def specificEncoder[T <: SpecificRecord: ClassTag]: AvroEncoder[T] = AvroBinaryEncoder.specific[T]
  override def reflectDecoder[T: ClassTag]: AvroDecoder[T]                    = AvroBinaryDecoder.reflect[T]
  override def reflectEncoder[T: ClassTag]: AvroEncoder[T]                    = AvroBinaryEncoder.reflect[T]
}

object AvroBinarySupport extends AvroBinarySupport

trait AvroRawSupport extends AvroProtocolAllSupport {
  override def contentType: ContentType                                       = AvroProtocol.`avro/raw`
  override def genericDecoder(schema: Schema): AvroDecoder[GenericRecord]     = AvroRawDecoder.generic(schema)
  override def genericEncoder(schema: Schema): AvroEncoder[GenericRecord]     = AvroRawEncoder.generic(schema)
  override def specificDecoder[T <: SpecificRecord: ClassTag]: AvroDecoder[T] = AvroRawDecoder.specific[T]
  override def specificEncoder[T <: SpecificRecord: ClassTag]: AvroEncoder[T] = AvroRawEncoder.specific[T]
  override def reflectDecoder[T: ClassTag]: AvroDecoder[T]                    = AvroRawDecoder.reflect[T]
  override def reflectEncoder[T: ClassTag]: AvroEncoder[T]                    = AvroRawEncoder.reflect[T]
}

object AvroRawSupport extends AvroRawSupport

trait AvroJsonSupport extends AvroProtocolAllSupport {
  override def contentType: ContentType                                       = ContentTypes.`application/json`
  override def genericDecoder(schema: Schema): AvroDecoder[GenericRecord]     = AvroJsonDecoder.generic(schema)
  override def genericEncoder(schema: Schema): AvroEncoder[GenericRecord]     = AvroJsonEncoder.generic(schema)
  override def specificDecoder[T <: SpecificRecord: ClassTag]: AvroDecoder[T] = AvroJsonDecoder.specific[T]
  override def specificEncoder[T <: SpecificRecord: ClassTag]: AvroEncoder[T] = AvroJsonEncoder.specific[T]
  override def reflectDecoder[T: ClassTag]: AvroDecoder[T]                    = AvroJsonDecoder.reflect[T]
  override def reflectEncoder[T: ClassTag]: AvroEncoder[T]                    = AvroJsonEncoder.reflect[T]
}

object AvroJsonSupport extends AvroJsonSupport

trait AvroSupport extends AvroAllSupport {
  override def avroGenericUnmarshaller(schema: Schema): FromEntityUnmarshaller[GenericRecord] =
    Unmarshaller.firstOf(
      AvroJsonSupport.avroGenericUnmarshaller(schema),
      AvroRawSupport.avroGenericUnmarshaller(schema),
      AvroBinarySupport.avroGenericUnmarshaller(schema)
    )

  override def avroGenericMarshaller(schema: Schema): ToEntityMarshaller[GenericRecord] =
    Marshaller.oneOf(
      AvroJsonSupport.avroGenericMarshaller(schema),
      AvroRawSupport.avroGenericMarshaller(schema),
      AvroBinarySupport.avroGenericMarshaller(schema)
    )

  override implicit def avroSpecificUnmarshaller[T <: SpecificRecord: ClassTag]: FromEntityUnmarshaller[T] =
    Unmarshaller.firstOf(
      AvroJsonSupport.avroSpecificUnmarshaller[T],
      AvroRawSupport.avroSpecificUnmarshaller[T],
      AvroBinarySupport.avroSpecificUnmarshaller[T]
    )

  override implicit def avroSpecificMarshaller[T <: SpecificRecord: ClassTag]: ToEntityMarshaller[T] =
    Marshaller.oneOf(
      AvroJsonSupport.avroSpecificMarshaller[T],
      AvroRawSupport.avroSpecificMarshaller[T],
      AvroBinarySupport.avroSpecificMarshaller[T]
    )

  override def avroReflectUnmarshaller[T: ClassTag]: FromEntityUnmarshaller[T] =
    Unmarshaller.firstOf(
      AvroJsonSupport.avroReflectUnmarshaller[T],
      AvroRawSupport.avroReflectUnmarshaller[T],
      AvroBinarySupport.avroReflectUnmarshaller[T]
    )

  override def avroReflectMarshaller[T: ClassTag]: ToEntityMarshaller[T] =
    Marshaller.oneOf(
      AvroJsonSupport.avroReflectMarshaller[T],
      AvroRawSupport.avroReflectMarshaller[T],
      AvroBinarySupport.avroReflectMarshaller[T]
    )
}

object AvroSupport extends AvroSupport
