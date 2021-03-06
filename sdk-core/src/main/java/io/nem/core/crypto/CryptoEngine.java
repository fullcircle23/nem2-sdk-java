/*
 * Copyright 2018 NEM
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

package io.nem.core.crypto;

/**
 * Represents a cryptographic engine that is a factory of crypto-providers.
 */
public interface CryptoEngine {

    /**
     * Return The underlying curve.
     *
     * @return The curve.
     */
    Curve getCurve();

    /**
     * Creates a DSA signer.
     *
     * @param keyPair The key pair.
     * @param signSchema the signSchema used to generate the signer.
     * @return The DSA signer.
     */
    DsaSigner createDsaSigner(final KeyPair keyPair, SignSchema signSchema);

    /**
     * Creates a key generator.
     *
     * @param signSchema the schema that defines how to create private keys and hashes.
     * @return The key generator.
     */
    KeyGenerator createKeyGenerator(SignSchema signSchema);

    /**
     * Creates a block cipher.
     *
     * @param senderKeyPair The sender KeyPair. The sender's private key is required for
     * encryption.
     * @param recipientKeyPair The recipient KeyPair. The recipient's private key is required for
     * decryption.
     * @param signSchema the schema that defines how to create private keys and hashes.
     * @return The IES cipher.
     */
    BlockCipher createBlockCipher(final KeyPair senderKeyPair, final KeyPair recipientKeyPair,
        SignSchema signSchema);

    /**
     * Creates a key analyzer.
     *
     * @return The key analyzer.
     */
    KeyAnalyzer createKeyAnalyzer();
}
