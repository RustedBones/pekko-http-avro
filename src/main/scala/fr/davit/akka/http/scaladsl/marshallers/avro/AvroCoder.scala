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

import akka.util.ByteString
import org.apache.avro.Schema
import org.apache.avro.io.{DecoderFactory, EncoderFactory}
import org.apache.avro.message.{BinaryMessageDecoder, BinaryMessageEncoder}
import org.apache.avro.reflect.ReflectData
import org.apache.avro.specific.{SpecificData, SpecificDatumReader, SpecificDatumWriter}

import scala.reflect.ClassTag

abstract class AvroCoder[T: ClassTag] {

  protected lazy val schema: Schema = ReflectData.get().getSchema(implicitly[ClassTag[T]].runtimeClass)

  def encode(model: T): ByteString

  def decode(data: ByteString): T

}

class AvroBinaryCoder[T: ClassTag] extends AvroCoder[T] {

  // set shouldCopy to false since ByteString.apply will make the copy
  private lazy val messageEncoder = new BinaryMessageEncoder[T](new SpecificData(), schema, false)

  private lazy val messageDecoder = new BinaryMessageDecoder[T](new SpecificData(), schema)

  override def encode(model: T): ByteString = ByteString(messageEncoder.encode(model))

  override def decode(data: ByteString): T = messageDecoder.decode(data.asByteBuffer)
}

class AvroJsonCoder[T: ClassTag] extends AvroCoder[T] {

  private lazy val jsonWriter = new SpecificDatumWriter[T](schema)

  private lazy val jsonReader = new SpecificDatumReader[T](schema)

  override def encode(avro: T): ByteString = {
    val builder = ByteString.newBuilder
    val encoder = EncoderFactory.get.jsonEncoder(schema, builder.asOutputStream)
    jsonWriter.write(avro, encoder)
    encoder.flush()
    builder.result()
  }

  override def decode(data: ByteString): T = {
    val decoder = DecoderFactory.get.jsonDecoder(schema, data.iterator.asInputStream)
    jsonReader.read(null.asInstanceOf[T], decoder)
  }
}

trait AvroCoderFactory {
  def newCoder[T: ClassTag]: AvroCoder[T]
}

object AvroBinaryCoderFactory extends AvroCoderFactory {
  override def newCoder[T: ClassTag]: AvroCoder[T] = new AvroBinaryCoder[T]()
}

object AvroJsonCoderFactory extends AvroCoderFactory {
  override def newCoder[T: ClassTag]: AvroCoder[T] = new AvroJsonCoder[T]()
}
