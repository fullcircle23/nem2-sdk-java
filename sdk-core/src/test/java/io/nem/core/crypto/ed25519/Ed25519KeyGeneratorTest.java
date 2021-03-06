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

import io.nem.core.crypto.CryptoEngine;
import io.nem.core.crypto.CryptoEngines;
import io.nem.core.crypto.KeyGenerator;
import io.nem.core.crypto.KeyGeneratorTest;
import io.nem.core.crypto.KeyPair;
import io.nem.core.crypto.PrivateKey;
import io.nem.core.crypto.PublicKey;
import io.nem.core.crypto.SignSchema;
import io.nem.core.crypto.ed25519.arithmetic.Ed25519EncodedGroupElement;
import io.nem.core.crypto.ed25519.arithmetic.Ed25519GroupElement;
import io.nem.core.crypto.ed25519.arithmetic.MathUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class Ed25519KeyGeneratorTest extends KeyGeneratorTest {


    @ParameterizedTest
    @EnumSource(SignSchema.class)
    public void derivedPublicKeyIsValidPointOnCurve(SignSchema signSchema) {
        // Arrange:
        final KeyGenerator generator = this.getKeyGenerator(signSchema);
        for (int i = 0; i < 100; i++) {
            final KeyPair kp = generator.generateKeyPair();

            // Act:
            final PublicKey publicKey = generator.derivePublicKey(kp.getPrivateKey());

            // Assert (throws if not on the curve):
            Ed25519GroupElement decode = new Ed25519EncodedGroupElement(publicKey.getBytes())
                .decode();

            Assertions.assertNotNull(decode);
        }
    }

    @ParameterizedTest
    @EnumSource(SignSchema.class)
    public void derivePublicKeyReturnsExpectedPublicKey(SignSchema signSchema) {
        // Arrange:
        final KeyGenerator generator = this.getKeyGenerator(signSchema);
        for (int i = 0; i < 100; i++) {
            final KeyPair kp = generator.generateKeyPair();

            // Act:
            final PublicKey publicKey1 = generator.derivePublicKey(kp.getPrivateKey());
            final PublicKey publicKey2 = MathUtils.derivePublicKey(kp.getPrivateKey(), signSchema);

            // Assert:
            MatcherAssert.assertThat(publicKey1, IsEqual.equalTo(publicKey2));
        }
    }

    @Test
    public void derivePublicKey() {
        SignSchema signSchema = SignSchema.SHA3;
        final KeyGenerator generator = this.getKeyGenerator(signSchema);
        final KeyPair keyPair =
            KeyPair.fromPrivate(
                PrivateKey.fromHexString(
                    "787225aaff3d2c71f4ffa32d4f19ec4922f3cd869747f267378f81f8e3fcb12d"),
                signSchema);

        final PublicKey publicKey = generator.derivePublicKey(keyPair.getPrivateKey());

        final PublicKey expected =
            PublicKey
                .fromHexString("1026d70e1954775749c6811084d6450a3184d977383f0e4282cd47118af37755");
        MatcherAssert.assertThat(publicKey, IsEqual.equalTo(expected));
    }

    @Override
    protected CryptoEngine getCryptoEngine() {
        return CryptoEngines.ed25519Engine();
    }
}
