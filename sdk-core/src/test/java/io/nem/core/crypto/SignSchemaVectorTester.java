/*
 * Copyright 2019. NEM
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.nem.core.crypto;

import io.nem.core.utils.AbstractVectorTester;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * ex Test of SignSchema.
 */
public class SignSchemaVectorTester extends AbstractVectorTester {

    private static Stream<Arguments> testKeccak256() throws Exception {
        return createArguments("0.test-keccak-256.json", SignSchemaVectorTester::extractArguments, 0,
            10
        );
    }

    private static Stream<Arguments> testSha256() throws Exception {
        return createArguments("0.test-sha3-256.json", SignSchemaVectorTester::extractArguments, 0, 10
        );
    }

    private static List<Arguments> extractArguments(Map<String, String> entry) {
        return Collections
            .singletonList(Arguments.of(entry.get("data"), entry.get("hash"), entry.get("length")));
    }


    @ParameterizedTest
    @MethodSource("testSha256")
    void testSha256(String data, String hash, int length) {
        byte[] decode = Hex.decode(data);
        Assertions.assertEquals(length, decode.length);
        Assertions.assertEquals(hash.toUpperCase(),
            Hex.toHexString(SignSchema.toHash32Bytes(SignSchema.SHA3, decode)).toUpperCase());
    }

    @ParameterizedTest
    @MethodSource("testKeccak256")
    void testKeccak256(String data, String hash, int length) {
        byte[] decode = Hex.decode(data);
        Assertions.assertEquals(length, decode.length);
        Assertions.assertEquals(hash.toUpperCase(),
            Hex.toHexString(SignSchema.toHash32Bytes(SignSchema.KECCAK, decode)).toUpperCase());
    }


}
