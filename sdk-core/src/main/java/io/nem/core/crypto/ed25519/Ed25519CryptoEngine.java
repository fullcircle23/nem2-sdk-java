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

package io.nem.core.crypto.ed25519;

import io.nem.core.crypto.BlockCipher;
import io.nem.core.crypto.CryptoEngine;
import io.nem.core.crypto.Curve;
import io.nem.core.crypto.DsaSigner;
import io.nem.core.crypto.KeyAnalyzer;
import io.nem.core.crypto.KeyGenerator;
import io.nem.core.crypto.KeyPair;
import io.nem.core.crypto.SignSchema;

/**
 * Class that wraps the Ed25519 specific implementation.
 */
public class Ed25519CryptoEngine implements CryptoEngine {

    @Override
    public Curve getCurve() {
        return Ed25519Curve.ed25519();
    }

    @Override
    public DsaSigner createDsaSigner(final KeyPair keyPair, SignSchema signSchema) {
        return new Ed25519DsaSigner(keyPair, signSchema);
    }

    @Override
    public KeyGenerator createKeyGenerator(SignSchema signSchema) {
        return new Ed25519KeyGenerator(signSchema);
    }

    @Override
    public BlockCipher createBlockCipher(final KeyPair senderKeyPair,
        final KeyPair recipientKeyPair, final SignSchema signSchema) {
        return new Ed25519BlockCipher(senderKeyPair, recipientKeyPair, signSchema);
    }

    @Override
    public KeyAnalyzer createKeyAnalyzer() {
        return new Ed25519KeyAnalyzer();
    }
}
