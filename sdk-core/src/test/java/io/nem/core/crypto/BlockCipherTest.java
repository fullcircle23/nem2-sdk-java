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

import io.nem.sdk.infrastructure.RandomUtils;
import org.bouncycastle.util.encoders.Hex;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public abstract class BlockCipherTest {

    @ParameterizedTest
    @EnumSource(SignSchema.class)
    public void encryptedDataCanBeDecrypted(SignSchema signSchema) {
        // Arrange:
        final CryptoEngine engine = this.getCryptoEngine();
        final KeyPair kp = KeyPair.random(engine, signSchema);
        final BlockCipher blockCipher = this.getBlockCipher(kp, kp, signSchema);
        final byte[] input = RandomUtils.generateRandomBytes();

        // Act:
        final byte[] encryptedBytes = blockCipher.encrypt(input);
        final byte[] decryptedBytes = blockCipher.decrypt(encryptedBytes);

        // Assert:
        MatcherAssert.assertThat(encryptedBytes, IsNot.not(IsEqual.equalTo(decryptedBytes)));
        MatcherAssert.assertThat(decryptedBytes, IsEqual.equalTo(input));
    }

    @ParameterizedTest
    @EnumSource(SignSchema.class)
    public void dataCanBeEncryptedWithSenderPrivateKeyAndRecipientPublicKey(SignSchema signSchema) {
        // Arrange:
        final CryptoEngine engine = this.getCryptoEngine();
        final KeyPair skp = KeyPair.random(engine, signSchema);
        final KeyPair rkp = KeyPair.random(engine, signSchema);
        final BlockCipher blockCipher =
            this.getBlockCipher(skp, KeyPair.onlyPublic(rkp.getPublicKey(), engine), signSchema);
        final byte[] input = RandomUtils.generateRandomBytes();

        // Act:
        final byte[] encryptedBytes = blockCipher.encrypt(input);

        // Assert:
        MatcherAssert.assertThat(encryptedBytes, IsNot.not(IsEqual.equalTo(input)));
    }

    @ParameterizedTest
    @EnumSource(SignSchema.class)
    public void dataCanBeDecryptedWithSenderPublicKeyAndRecipientPrivateKey(SignSchema signSchema) {
        // Arrange:
        final CryptoEngine engine = this.getCryptoEngine();
        final KeyPair skp = KeyPair.random(engine, signSchema);
        final KeyPair rkp = KeyPair.random(engine, signSchema);
        final BlockCipher blockCipher1 =
            this.getBlockCipher(skp, KeyPair.onlyPublic(rkp.getPublicKey(), engine), signSchema);
        final BlockCipher blockCipher2 =
            this.getBlockCipher(KeyPair.onlyPublic(skp.getPublicKey(), engine), rkp, signSchema);
        final byte[] input = RandomUtils.generateRandomBytes();

        // Act:
        final byte[] encryptedBytes = blockCipher1.encrypt(input);
        final byte[] decryptedBytes = blockCipher2.decrypt(encryptedBytes);

        // Assert:
        Assertions.assertEquals(Hex.toHexString(decryptedBytes), Hex.toHexString(input));
    }

    @ParameterizedTest
    @EnumSource(SignSchema.class)
    public void dataCanBeDecryptedWithSenderPrivateKeyAndRecipientPublicKey(SignSchema signSchema) {
        // Arrange:
        final CryptoEngine engine = this.getCryptoEngine();
        final KeyPair skp = KeyPair.random(engine, signSchema);
        final KeyPair rkp = KeyPair.random(engine, signSchema);
        final BlockCipher blockCipher1 =
            this.getBlockCipher(skp, KeyPair.onlyPublic(rkp.getPublicKey(), engine), signSchema);
        final BlockCipher blockCipher2 =
            this.getBlockCipher(KeyPair.onlyPublic(rkp.getPublicKey(), engine), skp, signSchema);
        final byte[] input = RandomUtils.generateRandomBytes();

        // Act:
        final byte[] encryptedBytes = blockCipher1.encrypt(input);
        final byte[] decryptedBytes = blockCipher2.decrypt(encryptedBytes);

        // Assert:
        MatcherAssert.assertThat(decryptedBytes, IsEqual.equalTo(input));
    }

    @ParameterizedTest
    @EnumSource(SignSchema.class)
    public void dataEncryptedWithPrivateKeyCanOnlyBeDecryptedByMatchingPublicKey(
        SignSchema signSchema) {
        // Arrange:
        final CryptoEngine engine = this.getCryptoEngine();
        final BlockCipher blockCipher1 =
            this.getBlockCipher(KeyPair.random(engine, signSchema), KeyPair.random(engine,
                signSchema), signSchema);
        final BlockCipher blockCipher2 =
            this.getBlockCipher(KeyPair.random(engine, signSchema), KeyPair.random(engine,
                signSchema), signSchema);
        final byte[] input = RandomUtils.generateRandomBytes();

        // Act:
        final byte[] encryptedBytes1 = blockCipher1.encrypt(input);
        final byte[] encryptedBytes2 = blockCipher2.encrypt(input);

        // Assert:
        MatcherAssert.assertThat(blockCipher1.decrypt(encryptedBytes1), IsEqual.equalTo(input));
        MatcherAssert.assertThat(blockCipher1.decrypt(encryptedBytes2), IsNot.not(IsEqual.equalTo(input)));
        MatcherAssert.assertThat(blockCipher2.decrypt(encryptedBytes1), IsNot.not(IsEqual.equalTo(input)));
        MatcherAssert.assertThat(blockCipher2.decrypt(encryptedBytes2), IsEqual.equalTo(input));
    }

    protected BlockCipher getBlockCipher(
        final KeyPair senderKeyPair, final KeyPair recipientKeyPair, SignSchema signSchema) {
        return this.getCryptoEngine()
            .createBlockCipher(senderKeyPair, recipientKeyPair, signSchema);
    }

    protected abstract CryptoEngine getCryptoEngine();
}
