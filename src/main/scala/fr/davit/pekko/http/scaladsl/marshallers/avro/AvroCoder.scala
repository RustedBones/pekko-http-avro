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
import org.apache.avro.generic.{GenericData, GenericDatumReader, GenericDatumWriter, GenericRecord}
import org.apache.avro.io.{DatumReader, DatumWriter, DecoderFactory, EncoderFactory}
import org.apache.avro.message.{BinaryMessageDecoder, BinaryMessageEncoder, RawMessageDecoder, RawMessageEncoder}
import org.apache.avro.reflect.{ReflectData, ReflectDatumReader, ReflectDatumWriter}
import org.apache.avro.specific.{SpecificData, SpecificDatumReader, SpecificDatumWriter, SpecificRecord}
import org.apache.pekko.util.ByteString

import scala.reflect.{classTag, ClassTag}

trait AvroEncoder[T] {
  def encode(record: T): ByteString
}

trait AvroDecoder[T] {
  def decode(bytes: ByteString): T
}

///////////////////////////////////////////////////////////////////////////////
// Binary
///////////////////////////////////////////////////////////////////////////////
class AvroBinaryEncoder[T](model: GenericData, schema: Schema) extends AvroEncoder[T] {
  // set shouldCopy to false since ByteString.apply will make the copy
  private lazy val messageEncoder = new BinaryMessageEncoder[T](model, schema, false)

  override def encode(record: T): ByteString = ByteString(messageEncoder.encode(record))
}

object AvroBinaryEncoder {
  def generic(schema: Schema): AvroBinaryEncoder[GenericRecord] = {
    new AvroBinaryEncoder(GenericData.get(), schema)
  }

  def specific[T <: SpecificRecord: ClassTag]: AvroBinaryEncoder[T] = {
    val clazz  = classTag[T].runtimeClass.asInstanceOf[Class[T]]
    val model  = SpecificData.getForClass(clazz)
    val schema = model.getSchema(clazz)
    new AvroBinaryEncoder(model, schema)
  }

  def reflect[T: ClassTag]: AvroBinaryEncoder[T] = {
    val clazz  = classTag[T].runtimeClass.asInstanceOf[Class[T]]
    val model  = ReflectData.get()
    val schema = model.getSchema(clazz)
    new AvroBinaryEncoder(model, schema)
  }
}

class AvroBinaryDecoder[T](model: GenericData, schema: Schema) extends AvroDecoder[T] {
  private lazy val messageDecoder = new BinaryMessageDecoder[T](model, schema)

  override def decode(bytes: ByteString): T = messageDecoder.decode(bytes.asByteBuffer)
}

object AvroBinaryDecoder {

  // writer schema will be used
  def generic: AvroBinaryDecoder[GenericRecord] = generic(null)

  def generic(schema: Schema): AvroBinaryDecoder[GenericRecord] = {
    new AvroBinaryDecoder(GenericData.get(), schema)
  }

  def specific[T <: SpecificRecord: ClassTag]: AvroBinaryDecoder[T] = {
    val clazz  = classTag[T].runtimeClass.asInstanceOf[Class[T]]
    val model  = SpecificData.getForClass(clazz)
    val schema = model.getSchema(clazz)
    new AvroBinaryDecoder(model, schema)
  }

  def reflect[T: ClassTag]: AvroBinaryDecoder[T] = {
    val clazz  = classTag[T].runtimeClass.asInstanceOf[Class[T]]
    val model  = ReflectData.get()
    val schema = model.getSchema(clazz)
    new AvroBinaryDecoder(model, schema)
  }
}

///////////////////////////////////////////////////////////////////////////////
// Raw
/////////////////////////////////////////////////////////////////////////////
class AvroRawEncoder[T](model: GenericData, schema: Schema) extends AvroEncoder[T] {
  // set shouldCopy to false since ByteString.apply will make the copy
  private lazy val messageEncoder = new RawMessageEncoder[T](model, schema, false)

  override def encode(record: T): ByteString = ByteString(messageEncoder.encode(record))
}

object AvroRawEncoder {
  def generic(schema: Schema): AvroRawEncoder[GenericRecord] = {
    new AvroRawEncoder(GenericData.get(), schema)
  }

  def specific[T <: SpecificRecord: ClassTag]: AvroRawEncoder[T] = {
    val clazz  = classTag[T].runtimeClass.asInstanceOf[Class[T]]
    val model  = SpecificData.getForClass(clazz)
    val schema = model.getSchema(clazz)
    new AvroRawEncoder(model, schema)
  }

  def reflect[T: ClassTag]: AvroRawEncoder[T] = {
    val clazz  = classTag[T].runtimeClass.asInstanceOf[Class[T]]
    val model  = ReflectData.get()
    val schema = model.getSchema(clazz)
    new AvroRawEncoder(model, schema)
  }
}

class AvroRawDecoder[T](model: GenericData, schema: Schema) extends AvroDecoder[T] {
  private lazy val messageDecoder = new RawMessageDecoder[T](model, schema)

  override def decode(bytes: ByteString): T = messageDecoder.decode(bytes.asByteBuffer)
}

object AvroRawDecoder {
  def generic(schema: Schema): AvroRawDecoder[GenericRecord] = {
    new AvroRawDecoder(GenericData.get(), schema)
  }

  def specific[T <: SpecificRecord: ClassTag]: AvroRawDecoder[T] = {
    val clazz  = classTag[T].runtimeClass.asInstanceOf[Class[T]]
    val model  = SpecificData.getForClass(clazz)
    val schema = model.getSchema(clazz)
    new AvroRawDecoder(model, schema)
  }

  def reflect[T: ClassTag]: AvroRawDecoder[T] = {
    val clazz  = classTag[T].runtimeClass.asInstanceOf[Class[T]]
    val model  = ReflectData.get()
    val schema = model.getSchema(clazz)
    new AvroRawDecoder(model, schema)
  }
}

///////////////////////////////////////////////////////////////////////////////
// Json
///////////////////////////////////////////////////////////////////////////////
class AvroJsonEncoder[T](writer: DatumWriter[T], schema: Schema) extends AvroEncoder[T] {

  override def encode(model: T): ByteString = {
    val builder = ByteString.newBuilder
    val encoder = EncoderFactory.get.jsonEncoder(schema, builder.asOutputStream)
    writer.write(model, encoder)
    encoder.flush()
    builder.result()
  }
}

object AvroJsonEncoder {
  def generic(schema: Schema): AvroJsonEncoder[GenericRecord] = {
    val writer = new GenericDatumWriter[GenericRecord]
    new AvroJsonEncoder(writer, schema)
  }

  def specific[T <: SpecificRecord: ClassTag]: AvroJsonEncoder[T] = {
    val clazz  = classTag[T].runtimeClass.asInstanceOf[Class[T]]
    val writer = new SpecificDatumWriter(clazz)
    val schema = SpecificData.get().getSchema(clazz)
    new AvroJsonEncoder(writer, schema)
  }

  def reflect[T: ClassTag]: AvroJsonEncoder[T] = {
    val clazz  = classTag[T].runtimeClass.asInstanceOf[Class[T]]
    val writer = new ReflectDatumWriter(clazz)
    val schema = ReflectData.get().getSchema(clazz)
    new AvroJsonEncoder(writer, schema)
  }

}

class AvroJsonDecoder[T](reader: DatumReader[T], schema: Schema) extends AvroDecoder[T] {
  override def decode(data: ByteString): T = {
    val decoder = DecoderFactory.get.jsonDecoder(schema, data.iterator.asInputStream)
    reader.read(null.asInstanceOf[T], decoder)
  }
}

object AvroJsonDecoder {
  def generic(schema: Schema): AvroJsonDecoder[GenericRecord] = {
    val reader = new GenericDatumReader[GenericRecord]
    new AvroJsonDecoder(reader, schema)
  }

  def specific[T <: SpecificRecord: ClassTag]: AvroJsonDecoder[T] = {
    val clazz  = classTag[T].runtimeClass.asInstanceOf[Class[T]]
    val reader = new SpecificDatumReader(clazz)
    val schema = SpecificData.get().getSchema(clazz)
    new AvroJsonDecoder(reader, schema)
  }

  def reflect[T: ClassTag]: AvroJsonDecoder[T] = {
    val clazz  = classTag[T].runtimeClass.asInstanceOf[Class[T]]
    val reader = new ReflectDatumReader(clazz)
    val schema = ReflectData.get().getSchema(clazz)
    new AvroJsonDecoder(reader, schema)
  }

}
