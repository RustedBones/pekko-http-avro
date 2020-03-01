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

package fr.davit.avro;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class TestMessage extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -7487682104369921468L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"TestMessage\",\"namespace\":\"fr.davit.avro\",\"fields\":[{\"name\":\"string_field\",\"type\":\"string\"},{\"name\":\"number_field\",\"type\":\"int\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<TestMessage> ENCODER =
      new BinaryMessageEncoder<TestMessage>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<TestMessage> DECODER =
      new BinaryMessageDecoder<TestMessage>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<TestMessage> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<TestMessage> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<TestMessage> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<TestMessage>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this TestMessage to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a TestMessage from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a TestMessage instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static TestMessage fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  @Deprecated public java.lang.CharSequence string_field;
  @Deprecated public java.lang.Integer number_field;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public TestMessage() {}

  /**
   * All-args constructor.
   * @param string_field The new value for string_field
   * @param number_field The new value for number_field
   */
  public TestMessage(java.lang.CharSequence string_field, java.lang.Integer number_field) {
    this.string_field = string_field;
    this.number_field = number_field;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return string_field;
    case 1: return number_field;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: string_field = (java.lang.CharSequence)value$; break;
    case 1: number_field = (java.lang.Integer)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'string_field' field.
   * @return The value of the 'string_field' field.
   */
  public java.lang.CharSequence getStringField() {
    return string_field;
  }


  /**
   * Sets the value of the 'string_field' field.
   * @param value the value to set.
   */
  public void setStringField(java.lang.CharSequence value) {
    this.string_field = value;
  }

  /**
   * Gets the value of the 'number_field' field.
   * @return The value of the 'number_field' field.
   */
  public java.lang.Integer getNumberField() {
    return number_field;
  }


  /**
   * Sets the value of the 'number_field' field.
   * @param value the value to set.
   */
  public void setNumberField(java.lang.Integer value) {
    this.number_field = value;
  }

  /**
   * Creates a new TestMessage RecordBuilder.
   * @return A new TestMessage RecordBuilder
   */
  public static fr.davit.avro.TestMessage.Builder newBuilder() {
    return new fr.davit.avro.TestMessage.Builder();
  }

  /**
   * Creates a new TestMessage RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new TestMessage RecordBuilder
   */
  public static fr.davit.avro.TestMessage.Builder newBuilder(fr.davit.avro.TestMessage.Builder other) {
    if (other == null) {
      return new fr.davit.avro.TestMessage.Builder();
    } else {
      return new fr.davit.avro.TestMessage.Builder(other);
    }
  }

  /**
   * Creates a new TestMessage RecordBuilder by copying an existing TestMessage instance.
   * @param other The existing instance to copy.
   * @return A new TestMessage RecordBuilder
   */
  public static fr.davit.avro.TestMessage.Builder newBuilder(fr.davit.avro.TestMessage other) {
    if (other == null) {
      return new fr.davit.avro.TestMessage.Builder();
    } else {
      return new fr.davit.avro.TestMessage.Builder(other);
    }
  }

  /**
   * RecordBuilder for TestMessage instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<TestMessage>
    implements org.apache.avro.data.RecordBuilder<TestMessage> {

    private java.lang.CharSequence string_field;
    private java.lang.Integer number_field;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(fr.davit.avro.TestMessage.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.string_field)) {
        this.string_field = data().deepCopy(fields()[0].schema(), other.string_field);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.number_field)) {
        this.number_field = data().deepCopy(fields()[1].schema(), other.number_field);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
    }

    /**
     * Creates a Builder by copying an existing TestMessage instance
     * @param other The existing instance to copy.
     */
    private Builder(fr.davit.avro.TestMessage other) {
      super(SCHEMA$);
      if (isValidValue(fields()[0], other.string_field)) {
        this.string_field = data().deepCopy(fields()[0].schema(), other.string_field);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.number_field)) {
        this.number_field = data().deepCopy(fields()[1].schema(), other.number_field);
        fieldSetFlags()[1] = true;
      }
    }

    /**
      * Gets the value of the 'string_field' field.
      * @return The value.
      */
    public java.lang.CharSequence getStringField() {
      return string_field;
    }


    /**
      * Sets the value of the 'string_field' field.
      * @param value The value of 'string_field'.
      * @return This builder.
      */
    public fr.davit.avro.TestMessage.Builder setStringField(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.string_field = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'string_field' field has been set.
      * @return True if the 'string_field' field has been set, false otherwise.
      */
    public boolean hasStringField() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'string_field' field.
      * @return This builder.
      */
    public fr.davit.avro.TestMessage.Builder clearStringField() {
      string_field = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'number_field' field.
      * @return The value.
      */
    public java.lang.Integer getNumberField() {
      return number_field;
    }


    /**
      * Sets the value of the 'number_field' field.
      * @param value The value of 'number_field'.
      * @return This builder.
      */
    public fr.davit.avro.TestMessage.Builder setNumberField(java.lang.Integer value) {
      validate(fields()[1], value);
      this.number_field = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'number_field' field has been set.
      * @return True if the 'number_field' field has been set, false otherwise.
      */
    public boolean hasNumberField() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'number_field' field.
      * @return This builder.
      */
    public fr.davit.avro.TestMessage.Builder clearNumberField() {
      number_field = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TestMessage build() {
      try {
        TestMessage record = new TestMessage();
        record.string_field = fieldSetFlags()[0] ? this.string_field : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.number_field = fieldSetFlags()[1] ? this.number_field : (java.lang.Integer) defaultValue(fields()[1]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<TestMessage>
    WRITER$ = (org.apache.avro.io.DatumWriter<TestMessage>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<TestMessage>
    READER$ = (org.apache.avro.io.DatumReader<TestMessage>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeString(this.string_field);

    if (this.number_field == null) {
      out.writeIndex(1);
      out.writeNull();
    } else {
      out.writeIndex(0);
      out.writeInt(this.number_field);
    }

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.string_field = in.readString(this.string_field instanceof Utf8 ? (Utf8)this.string_field : null);

      if (in.readIndex() != 0) {
        in.readNull();
        this.number_field = null;
      } else {
        this.number_field = in.readInt();
      }

    } else {
      for (int i = 0; i < 2; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.string_field = in.readString(this.string_field instanceof Utf8 ? (Utf8)this.string_field : null);
          break;

        case 1:
          if (in.readIndex() != 0) {
            in.readNull();
            this.number_field = null;
          } else {
            this.number_field = in.readInt();
          }
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










