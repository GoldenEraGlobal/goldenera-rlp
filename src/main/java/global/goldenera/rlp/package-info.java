/*
 * Originally from Hyperledger Besu (https://github.com/hyperledger/besu)
 * Copyright ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
/**
 * Recursive Length Prefix (RLP) encoding and decoding.
 *
 * <p>
 * This package provides encoding and decoding of data with the RLP encoding
 * scheme.
 * Encoding is done through writing data to a
 * {@link global.goldenera.rlp.RLPOutput}
 * (for instance {@link global.goldenera.rlp.BytesValueRLPOutput}, which then
 * exposes
 * the encoded output as a {@link org.apache.tuweni.bytes.Bytes} through
 * {@link global.goldenera.rlp.BytesValueRLPOutput#encoded()}). Decoding is done
 * by
 * wrapping encoded data in a {@link global.goldenera.rlp.RLPInput} (using, for
 * instance,
 * {@link global.goldenera.rlp.RLP#input}) and reading from it.
 *
 * <p>
 * This implementation is derived from Hyperledger Besu.
 */
package global.goldenera.rlp;
