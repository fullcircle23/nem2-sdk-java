/*
 * Copyright 2019 NEM
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

package io.nem.sdk.model.message;

import io.nem.core.crypto.KeyPair;
import io.nem.sdk.model.blockchain.NetworkType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test class for the EncryptMessage.
 */
public class PersistentHarvestingDelegationMessageTest {

    @Test
    public void testCreateEncryptedMessage() {
        NetworkType networkType = NetworkType.MIJIN_TEST;
        KeyPair proxy = KeyPair.random(networkType.resolveSignSchema());
        KeyPair sender = KeyPair.random(networkType.resolveSignSchema());
        KeyPair harvester = KeyPair.random(networkType.resolveSignSchema());

        PersistentHarvestingDelegationMessage encryptedMessage = PersistentHarvestingDelegationMessage
            .create(proxy.getPrivateKey(), sender.getPrivateKey(), harvester.getPublicKey(),
                networkType);

        Assertions.assertEquals(MessageType.PERSISTENT_HARVESTING_DELEGATION_MESSAGE,
            encryptedMessage.getType());

        String plainMessage = encryptedMessage
            .decryptPayload(sender.getPublicKey(), harvester.getPrivateKey(), networkType);

        Assertions.assertEquals(proxy.getPrivateKey().toHex().toUpperCase(), plainMessage);
    }


}
