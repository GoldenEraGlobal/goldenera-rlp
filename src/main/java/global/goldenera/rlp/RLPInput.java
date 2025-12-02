/*
 * Originally from Hyperledger Besu (https://github.com/hyperledger/besu)
 * Copyright ConsenSys AG.
 * 
 * Modified by The GoldenEraGlobal Developers
 * Copyright (c) 2025-2030 The GoldenEraGlobal Developers
 *
 * This file is dual-licensed under both:
 * 1. Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *    the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * 2. MIT License (See LICENSE file in the root of this repository)
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0 OR MIT
 */
package global.goldenera.rlp;

import java.math.BigInteger;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.apache.tuweni.bytes.Bytes48;
import org.apache.tuweni.units.bigints.UInt256;
import org.apache.tuweni.units.bigints.UInt64;
import org.apache.tuweni.units.ethereum.Wei;

/**
 * An input used to decode data in RLP encoding.
 *
 * <p>
 * An RLP "value" is fundamentally an {@code Item} defined the following way:
 *
 * <pre>
 *   Item ::= List | Bytes
 *   List ::= [ Item, ... , Item ]
 *   Bytes ::= a binary value (comprised of an arbitrary number of bytes).
 * </pre>
 *
 * <p>
 * In other words, RLP encodes binary data organized in arbitrary nested lists.
 *
 * <p>
 * A {@link RLPInput} thus provides methods to decode both lists and binary
 * values. A list in the
 * input is "entered" by calling {@link #enterList()} and left by calling
 * {@link #leaveList()}.
 * Binary values can be read directly with {@link #readBytes()}, but the
 * {@link RLPInput} interface
 * provides a wealth of convenience methods to read specific types of data that
 * are in specific
 * encoding.
 *
 * <p>
 * Amongst the methods to read binary data, some methods are provided to read
 * "scalar". A scalar
 * should simply be understood as a positive integer that is encoded with no
 * leading zeros. In other
 * word, a method like {@link #readLongScalar()} does not expect an encoded
 * value of exactly 8 bytes
 * (by opposition to {@link #readLong}), but rather one that is "up to" 8 bytes.
 *
 * @see BytesValueRLPInput
 */
public interface RLPInput {

  /**
   * Whether the input has been already fully decoded (has no more data to read).
   *
   * @return {@code false} if the input has more data to read, {@code true}
   *         otherwise.
   */
  boolean isDone();

  /**
   * Whether the next element to read from this input is a list.
   *
   * @return {@code true} if the input is not done and the next item to read is a
   *         list.
   */
  boolean nextIsList();

  /**
   * Whether the next element to read from this input is an RLP "null" (that is,
   * {@link
   * Bytes#EMPTY}).
   *
   * @return {@code true} if the input is not done and the next item to read is an
   *         empty value.
   */
  boolean nextIsNull();

  /**
   * Returns the payload size of the next item
   *
   * @return the payload size of the next item
   */
  int nextSize();

  /**
   * Returns the size of the current item
   *
   * @return the size of the current item
   */
  int currentSize();

  /**
   * Returns the offset of the next item, counting from the start of the RLP Input
   * as a whole.
   *
   * @return offset from buffer start
   */
  int nextOffset();

  /**
   * Whether the input is at the end of a currently entered list, that is if
   * {@link #leaveList()}
   * should be the next method called.
   *
   * @return Whether all elements of the current list have been read but said list
   *         haven't been
   *         "left" yet.
   */
  boolean isEndOfCurrentList();

  /**
   * Skips the next item to read in the input.
   *
   * <p>
   * Note that if the next item is a list, the whole list is skipped.
   */
  void skipNext();

  /**
   * If the next item to read is a list, enter that list, placing the input on the
   * first item of
   * that list.
   *
   * @return The number of item of the entered list.
   * @throws RLPException if the next item to read from this input is not a list,
   *                      or the input is
   *                      corrupted.
   */
  int enterList();

  /**
   * Exits the current list after all its items have been consumed.
   *
   * <p>
   * Note that this method technically doesn't consume any input but must be
   * called after having
   * read the last element of a list. This allows it to ensure the structure of
   * the input is indeed
   * the one expected.
   *
   * @throws RLPException if the current list is not finished (it has more items).
   */
  void leaveList();

  /**
   * Exits the current list, ignoring any remaining unconsumed elements.
   *
   * <p>
   * Note that this method technically doesn't consume any input but must be
   * called after having
   * read the last element of a list. This allows it to ensure the structure of
   * the input is indeed
   * the one expected.
   */
  void leaveListLenient();

  /**
   * Reads a non-negative scalar from the input and return it as a long value
   * which is interpreted
   * as an unsigned long.
   *
   * @return The next scalar item of this input as a long value.
   * @throws RLPException if the next item to read is a list, the input is at the
   *                      end of its current
   *                      list (and {@link #leaveList()} hasn't been called) or if
   *                      the next item is either too big to
   *                      fit a long or has leading zeros.
   */
  long readLongScalar();

  /**
   * Reads a scalar from the input and return is as an int value.
   *
   * @return The next scalar item of this input as an int value.
   * @throws RLPException if the next item to read is a list, the input is at the
   *                      end of its current
   *                      list (and {@link #leaveList()} hasn't been called) or if
   *                      the next item is either too big to
   *                      fit a long or has leading zeros.
   */
  int readIntScalar();

  /**
   * Reads a scalar from the input and return is as a {@link BigInteger}.
   *
   * @return The next scalar item of this input as a {@link BigInteger}.
   * @throws RLPException if the next item to read is a list, the input is at the
   *                      end of its current
   *                      list (and {@link #leaveList()} hasn't been called) or if
   *                      the next item has leading zeros.
   */
  BigInteger readBigIntegerScalar();

  /**
   * Reads a scalar from the input and return is as a {@link UInt64}.
   *
   * @return The next scalar item of this input as a {@link UInt64}.
   * @throws RLPException if the next item to read is a list, the input is at the
   *                      end of its current
   *                      list (and {@link #leaveList()} hasn't been called) or if
   *                      the next item is either too big to
   *                      fit a {@link UInt64} or has leading zeros.
   */
  UInt64 readUInt64Scalar();

  /**
   * Reads a scalar from the input and return is as a {@link UInt256}.
   *
   * @return The next scalar item of this input as a {@link UInt256}.
   * @throws RLPException if the next item to read is a list, the input is at the
   *                      end of its current
   *                      list (and {@link #leaveList()} hasn't been called) or if
   *                      the next item is either too big to
   *                      fit a {@link UInt256} or has leading zeros.
   */
  UInt256 readUInt256Scalar();

  /**
   * Reads the next item of this input (which must be exactly 1 byte) as a byte.
   *
   * @return The byte corresponding to the next item of this input.
   * @throws RLPException if the next item to read is a list, the input is at the
   *                      end of its current
   *                      list (and {@link #leaveList()} hasn't been called) or if
   *                      the next item is not a single byte
   *                      long.
   */
  byte readByte();

  /**
   * Reads the next item of this input (which must be exactly 2-bytes) as a
   * (signed) short.
   *
   * @return The short corresponding to the next item of this input.
   * @throws RLPException if the next item to read is a list, the input is at the
   *                      end of its current
   *                      list (and {@link #leaveList()} hasn't been called) or if
   *                      the next item is not 2-bytes.
   */
  short readShort();

  /**
   * Reads the next item of this input (which must be exactly 4-bytes) as a
   * (signed) int.
   *
   * @return The int corresponding to the next item of this input.
   * @throws RLPException if the next item to read is a list, the input is at the
   *                      end of its current
   *                      list (and {@link #leaveList()} hasn't been called) or if
   *                      the next item is not 4-bytes.
   */
  int readInt();

  /**
   * Reads the next item of this input (which must be exactly 8-bytes) as a
   * (signed) long.
   *
   * @return The long corresponding to the next item of this input.
   * @throws RLPException if the next item to read is a list, the input is at the
   *                      end of its current
   *                      list (and {@link #leaveList()} hasn't been called) or if
   *                      the next item is not 8-bytes.
   */
  long readLong();

  /**
   * Reads the next item of this input (which must be exactly 1 byte) as an
   * unsigned byte.
   *
   * @return The value of the next item interpreted as an unsigned byte.
   * @throws RLPException if the next item to read is a list, the input is at the
   *                      end of its current
   *                      list (and {@link #leaveList()} hasn't been called) or if
   *                      the next item is not a single byte
   *                      long.
   */
  default int readUnsignedByte() {
    if (isZeroLengthString()) {
      // Decode an empty string (0x80) as an unsigned byte with value 0
      readBytes();
      return 0;
    }
    return readByte() & 0xFF;
  }

  /**
   * Reads the next item of this input (which must be exactly 2-bytes) as an
   * unsigned short.
   *
   * @return The value of the next item interpreted as an unsigned short.
   * @throws RLPException if the next item to read is a list, the input is at the
   *                      end of its current
   *                      list (and {@link #leaveList()} hasn't been called) or if
   *                      the next item is not 2-bytes.
   */
  default int readUnsignedShort() {
    return readShort() & 0xFFFF;
  }

  /**
   * Reads the next item of this input (which must be exactly 4-bytes) as an
   * unsigned int.
   *
   * @return The value of the next item interpreted as an unsigned int.
   * @throws RLPException if the next item to read is a list, the input is at the
   *                      end of its current
   *                      list (and {@link #leaveList()} hasn't been called) or if
   *                      the next item is not 4-bytes.
   */
  default long readUnsignedInt() {
    return (readInt()) & 0xFFFFFFFFL;
  }

  /**
   * Reads a scalar from the input and return is as an unsigned int contained in a
   * long
   *
   * @return The next scalar item of this input as an unsigned int value as long
   * @throws RLPException if the next item to read is a list, the input is at the
   *                      end of its current
   *                      list (and {@link #leaveList()} hasn't been called) or if
   *                      the next item is either too big to
   *                      fit an unsigned int or has leading zeros.
   */
  long readUnsignedIntScalar();

  /**
   * Reads a scalar from the input and return is as an unsigned byte contained in
   * an int
   *
   * @return The next scalar item of this input as an unsigned byte value as int
   * @throws RLPException if the next item to read is a list, the input is at the
   *                      end of its current
   *                      list (and {@link #leaveList()} hasn't been called) or if
   *                      the next item is either too big to
   *                      fit an unsigned byte or has leading zeros.
   */
  int readUnsignedByteScalar();

  /**
   * Reads an inet address from this input.
   *
   * @return The inet address corresponding to the next item of this input.
   * @throws RLPException if the next item to read is a list, the input is at the
   *                      end of its current
   *                      list (and {@link #leaveList()} hasn't been called) or if
   *                      the next item is neither 4 nor 16
   *                      bytes.
   */
  InetAddress readInetAddress();

  /**
   * Reads the next item of this input (assuming it is not a list).
   *
   * @return The next item read of this input.
   * @throws RLPException if the next item to read is a list or the input is at
   *                      the end of its
   *                      current list (and {@link #leaveList()} hasn't been
   *                      called).
   */
  Bytes readBytes();

  /**
   * Reads the next item of this input (assuming it is not a list) that must be
   * exact 32 bytes.
   *
   * @return The next item read of this input.
   * @throws RLPException if the next item to read is a list, the input is at the
   *                      end of its current
   *                      list (and {@link #leaveList()} hasn't been called) or
   *                      the next element is not exactly 32
   *                      bytes.
   */
  Bytes32 readBytes32();

  /**
   * Reads the next item of this input (assuming it is not a list) that must be
   * exact 48 bytes.
   *
   * @return The next item read of this input.
   * @throws RLPException if the next item to read is a list, the input is at the
   *                      end of its current
   *                      list (and {@link #leaveList()} hasn't been called) or
   *                      the next element is not exactly 48
   *                      bytes.
   */
  Bytes48 readBytes48();

  /**
   * Reads the next item of this input (assuming it is not a list) and transforms
   * it with the
   * provided mapping function.
   *
   * <p>
   * Note that the only benefit of this method over calling the mapper function on
   * the result of
   * {@link #readBytes()} is that any error thrown by the mapper will be wrapped
   * by a {@link
   * RLPException}, which can make error handling more convenient (having a
   * particular decoded value
   * not match what is expected is not fundamentally different from trying to read
   * an unsigned short
   * from an item with strictly more or less than 2 bytes).
   *
   * @param mapper The mapper to apply to the read value.
   * @param <T>    The type of the result.
   * @return The next item read from this input, mapped through {@code mapper}.
   * @throws RLPException if the next item to read is a list, the input is at the
   *                      end of its current
   *                      list (and {@link #leaveList()} hasn't been called) or
   *                      {@code mapper} throws an exception
   *                      when applied.
   */
  <T> T readBytes(Function<Bytes, T> mapper);

  /**
   * Reads an optional uint64 scalar from the input.
   *
   * @return The uint64 scalar to read.
   */
  default UInt64 readOptionalUInt64Scalar() {
    int size = enterList();
    if (size == 0) {
      leaveList();
      return null;
    }
    UInt64 value = readUInt64Scalar();
    leaveList();
    return value;
  }

  /**
   * Reads an optional uint256 scalar from the input.
   *
   * @return The uint256 scalar to read.
   */
  default UInt256 readOptionalUInt256Scalar() {
    int size = enterList();
    if (size == 0) {
      leaveList();
      return null;
    }
    UInt256 value = readUInt256Scalar();
    leaveList();
    return value;
  }

  /**
   * Reads a wei from the input as a big integer scalar.
   *
   * @return The wei to read.
   */
  default Wei readWeiScalar() {
    return Wei.valueOf(readBigIntegerScalar());
  }

  /**
   * Reads an optional wei from the input as a big integer scalar.
   *
   * @return The wei to read.
   */
  default Wei readOptionalWeiScalar() {
    int size = enterList();
    if (size == 0) {
      leaveList();
      return null;
    }
    Wei value = readWeiScalar();
    leaveList();
    return value;
  }

  /**
   * Reads a string from the input as UTF-8 encoded bytes.
   *
   * @return The string to read.
   */
  default String readString() {
    Bytes value = readBytes();
    return new String(value.toArray(), StandardCharsets.UTF_8);
  }

  /**
   * Reads the next item (List or String) as-is, returning its full RLP encoding
   * (Prefix + Data).
   * This is the counterpart to writeRaw().
   *
   * Use this when you want to extract an RLP blob to pass it to another decoder
   * (e.g. extracting BlockHeader bytes to pass to BlockHeaderDecoder).
   *
   * @return The raw RLP bytes of the current item.
   */
  default Bytes readRaw() {
    return readAsRlp().raw();
  }

  /**
   * Reads an optional string from the input as UTF-8 encoded bytes.
   *
   * @return The string to read.
   */
  default String readOptionalString() {
    int size = enterList();
    if (size == 0) {
      leaveList();
      return null;
    }
    String value = readString();
    leaveList();
    return value;
  }

  /**
   * Reads an optional long scalar from the input.
   *
   * @return The long scalar to read.
   */
  default Long readOptionalLongScalar() {
    int size = enterList();
    if (size == 0) {
      leaveList();
      return null;
    }
    Long value = readLongScalar();
    leaveList();
    return value;
  }

  /**
   * Reads an optional int scalar from the input.
   *
   * @return The int scalar to read.
   */
  default Integer readOptionalIntScalar() {
    int size = enterList();
    if (size == 0) {
      leaveList();
      return null;
    }
    Integer value = readIntScalar();
    leaveList();
    return value;
  }

  /**
   * Reads an optional short scalar from the input.
   *
   * @return The short scalar to read.
   */
  default Short readOptionalShort() {
    int size = enterList();
    if (size == 0) {
      leaveList();
      return null;
    }
    Short value = readShort();
    leaveList();
    return value;
  }

  /**
   * Reads an optional big integer scalar from the input.
   *
   * @return The big integer scalar to read.
   */
  default BigInteger readOptionalBigIntegerScalar() {
    int size = enterList();
    if (size == 0) {
      leaveList();
      return null;
    }
    BigInteger value = readBigIntegerScalar();
    leaveList();
    return value;
  }

  /**
   * Reads an optional bytes from the input.
   *
   * @return The bytes to read.
   */
  default Bytes readOptionalBytes() {
    int size = enterList();
    if (size == 0) {
      leaveList();
      return null;
    }
    Bytes value = readBytes();
    leaveList();
    return value;
  }

  /**
   * Reads an optional bytes32 from the input.
   *
   * @return The bytes32 to read.
   */
  default Bytes32 readOptionalBytes32() {
    int size = enterList();
    if (size == 0) {
      leaveList();
      return null;
    }
    Bytes32 value = readBytes32();
    leaveList();
    return value;
  }

  /**
   * Reads an optional byte from the input.
   *
   * @return The byte to read.
   */
  default Byte readOptionalByte() {
    int size = enterList();
    if (size == 0) {
      leaveList();
      return null;
    }
    Byte value = readByte();
    leaveList();
    return value;
  }

  default Bytes readOptionalRaw() {
    int size = enterList();
    if (size == 0) {
      leaveList();
      return null;
    }
    Bytes result = readRaw();
    leaveList();
    return result;
  }

  /**
   * Returns the current element as a standalone RLP element.
   *
   * <p>
   * This method is useful to extract self-contained RLP elements from a list, so
   * they can be
   * processed individually later.
   *
   * @return The current element as a standalone RLP input element.
   */
  RLPInput readAsRlp();

  /**
   * Returns a raw {@link Bytes} representation of this RLP.
   *
   * @return The raw RLP.
   */
  Bytes raw();

  boolean isZeroLengthString();

  /** Resets this RLP input to the start. */
  void reset();

  /**
   * Returns a raw {@link Bytes} representation of this RLP list.
   *
   * @return The raw RLP list.
   */
  Bytes currentListAsBytes();

  /**
   * Reads a full list from the input given a method that knows how to read its
   * elements.
   *
   * @param valueReader A method that can decode a single list element.
   * @param <T>         The type of the elements of the decoded list.
   * @return The next list of this input, where elements are decoded using
   *         {@code valueReader}.
   * @throws RLPException is the next item to read is not a list, of if any error
   *                      happens when
   *                      applying {@code valueReader} to read elements of the
   *                      list.
   */
  default <T> List<T> readList(final Function<RLPInput, T> valueReader) {
    final int size = enterList();
    final List<T> res = size == 0 ? List.of() : new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      try {
        res.add(valueReader.apply(this));
      } catch (final RLPException e) {
        throw e;
      } catch (final Exception e) {
        throw new RLPException(
            String.format("Error applying element decoding function on element %d of the list", i),
            e);
      }
    }
    leaveList();
    return res;
  }

  default Bytes currentListAsBytesNoCopy(final boolean moveToNextItem) {
    throw new UnsupportedOperationException("Not supported by this input");
  }
}
