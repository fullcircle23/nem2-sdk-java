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

package io.nem.sdk.model.transaction;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.nem.core.crypto.Hashes;
import io.nem.sdk.infrastructure.RandomUtils;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;

class LockHashAlgorithmTypeTest {

    @Test
    void SHA3_256ShouldBeExactly64CharactersLength() {
        byte[] secretBytes = RandomUtils.generateRandomBytes(20);
        byte[] result = Hashes.sha3_256(secretBytes);
        String secret = Hex.encodeHexString(result);

        assertTrue(LockHashAlgorithmType.validator(LockHashAlgorithmType.SHA3_256, secret));
    }

    @Test
    void SHA3_256ShouldReturnFalseIfItIsNot64CharsLength() {
        byte[] secretBytes = RandomUtils.generateRandomBytes(20);
        byte[] result = Hashes.sha3_512(secretBytes);
        String secret = Hex.encodeHexString(result);

        assertFalse(LockHashAlgorithmType.validator(LockHashAlgorithmType.SHA3_256, secret));
    }

    @Test
    void SHA3_256ShouldReturnFalseIfItIsNotAValidHash() {
        String secret = "zyz6053bb910a6027f138ac5ebe92d43a9a18b7239b3c4d5ea69f1632e50aeef";
        assertFalse(LockHashAlgorithmType.validator(LockHashAlgorithmType.SHA3_256, secret));
    }

    @Test
    void KECCAK_256ShouldBeExactly64CharactersLength() {
        byte[] secretBytes = RandomUtils.generateRandomBytes(20);
        byte[] result = Hashes.keccak256(secretBytes);
        String secret = Hex.encodeHexString(result);

        assertTrue(LockHashAlgorithmType.validator(LockHashAlgorithmType.KECCAK_256, secret));
    }

    @Test
    void KECCAK_256ShouldReturnFalseIfItIsNot64CharsLength() {
        byte[] secretBytes = RandomUtils.generateRandomBytes(20);
        byte[] result = Hashes.ripemd160(secretBytes);
        String secret = Hex.encodeHexString(result);

        assertFalse(LockHashAlgorithmType.validator(LockHashAlgorithmType.KECCAK_256, secret));
    }

    @Test
    void KECCAK_256ShouldReturnFalseIfItIsNotAValidHash() {
        String secret = "zyz6053bb910a6027f138ac5ebe92d43a9a18b7239b3c4d5ea69f1632e50aeef";
        assertFalse(LockHashAlgorithmType.validator(LockHashAlgorithmType.KECCAK_256, secret));
    }

    @Test
    void HASH_160ShouldBeExactly40CharactersLength() {
        byte[] secretBytes = RandomUtils.generateRandomBytes(20);
        byte[] result = Hashes.hash160(secretBytes);
        String secret = Hex.encodeHexString(result);

        assertTrue(LockHashAlgorithmType.validator(LockHashAlgorithmType.HASH_160, secret));
    }

    @Test
    void HASH_160ShouldReturnFalseIfItIsNot40CharsLength() {
        byte[] secretBytes = RandomUtils.generateRandomBytes(20);
        byte[] result = Hashes.sha3_256(secretBytes);
        String secret = Hex.encodeHexString(result);

        assertFalse(LockHashAlgorithmType.validator(LockHashAlgorithmType.HASH_160, secret));
    }

    @Test
    void HASH_160ShouldReturnFalseIfItIsNotAValidHash() {
        String secret = "zyz6053bb910a6027f138ac5ebe92d43a9a18b7239b3c4d5ea69f1632e50aeef";
        assertFalse(LockHashAlgorithmType.validator(LockHashAlgorithmType.HASH_160, secret));
    }

    @Test
    void HASH_256ShouldBeExactly64CharactersLength() {
        byte[] secretBytes = RandomUtils.generateRandomBytes(20);
        byte[] result = Hashes.hash256(secretBytes);
        String secret = Hex.encodeHexString(result);

        assertTrue(LockHashAlgorithmType.validator(LockHashAlgorithmType.HASH_256, secret));
    }

    @Test
    void HASH_256ShouldReturnFalseIfItIsNot64CharsLength() {
        byte[] secretBytes = RandomUtils.generateRandomBytes(20);
        byte[] result = Hashes.ripemd160(secretBytes);
        String secret = Hex.encodeHexString(result);

        assertFalse(LockHashAlgorithmType.validator(LockHashAlgorithmType.HASH_256, secret));
    }

    @Test
    void HASH_256ShouldReturnFalseIfItIsNotAValidHash() {
        String secret = "zyz6053bb910a6027f138ac5ebe92d43a9a18b7239b3c4d5ea69f1632e50aeef";
        assertFalse(LockHashAlgorithmType.validator(LockHashAlgorithmType.HASH_256, secret));
    }
}
