/*
 *  Copyright 2019 NEM
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.nem.sdk.infrastructure.okhttp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.nem.sdk.infrastructure.okhttp.mappers.GeneralTransactionMapper;
import io.nem.sdk.model.account.Account;
import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.message.PlainMessage;
import io.nem.sdk.model.mosaic.Mosaic;
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.mosaic.NetworkCurrencyMosaic;
import io.nem.sdk.model.transaction.AggregateTransaction;
import io.nem.sdk.model.transaction.AggregateTransactionFactory;
import io.nem.sdk.model.transaction.JsonHelper;
import io.nem.sdk.model.transaction.SignedTransaction;
import io.nem.sdk.model.transaction.TransferTransaction;
import io.nem.sdk.model.transaction.TransferTransactionFactory;
import io.nem.sdk.openapi.okhttp_gson.model.TransactionInfoDTO;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Collections;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OkHttpAggregateTransactionTest {

    private final String generationHash =
        "57F7DA205008026C776CB6AED843393F04CD458E0AA2D9F1D5F31A402072B2D6";

    private final JsonHelper jsonHelper = new JsonHelperGson();

    @Test
    void createAAggregateTransactionViaStaticConstructor() {

        TransferTransaction transferTx =
            TransferTransactionFactory.create(
                NetworkType.MIJIN_TEST,
                new Address("SDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26",
                    NetworkType.MIJIN_TEST),
                Collections.emptyList(),
                PlainMessage.Empty).build();

        AggregateTransaction aggregateTx =
            AggregateTransactionFactory.createComplete(NetworkType.MIJIN_TEST,
                Collections.singletonList(
                    transferTx.toAggregate(
                        new PublicAccount(
                            "9A49366406ACA952B88BADF5F1E9BE6CE4968141035A60BE503273EA65456B24",
                            NetworkType.MIJIN_TEST)))).build();

        assertEquals(NetworkType.MIJIN_TEST, aggregateTx.getNetworkType());
        assertEquals(1, (int) aggregateTx.getVersion());
        assertTrue(LocalDateTime.now().isBefore(aggregateTx.getDeadline().getLocalDateTime()));
        assertEquals(BigInteger.valueOf(0), aggregateTx.getMaxFee());
        assertEquals(1, aggregateTx.getInnerTransactions().size());
    }

    @Test
    @DisplayName("Serialization")
    void serialization() {
        // Generated at nem2-library-js/test/transactions/RegisterNamespaceTransaction.spec.js
        String expected =
            "1001000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000190414100000000000000000100000000000000de78f6d81ae02ad16559f6e4d3d4acc5ed343ee0ae65b1c9ad4fc0091a3903b568000000000000006100000000000000846b4439154579a5903b1459c9cf69cb8153f6d0110a7a0ed61de29ae4810bf200000000019054419050b9837efab4bbe8a4b9bb32d812f9885c00d8fc1650e1420101000000000044b262c46ceabb8580969800000000000000000000000000";

        TransferTransaction transferTx =
            TransferTransactionFactory.create(
                NetworkType.MIJIN_TEST,
                new Address("SBILTA367K2LX2FEXG5TFWAS7GEFYAGY7QLFBYKC", NetworkType.MIJIN_TEST),
                Collections.singletonList(
                    new Mosaic(new MosaicId(NetworkCurrencyMosaic.NAMESPACEID.getId()),
                        BigInteger.valueOf(10000000))),
                PlainMessage.Empty).deadline(new OkHttpFakeDeadline()).build();

        AggregateTransaction aggregateTx =
            AggregateTransactionFactory.createComplete(
                NetworkType.MIJIN_TEST,
                Collections.singletonList(
                    transferTx.toAggregate(
                        new PublicAccount(
                            "846B4439154579A5903B1459C9CF69CB8153F6D0110A7A0ED61DE29AE4810BF2",
                            NetworkType.MIJIN_TEST)))).deadline(new OkHttpFakeDeadline()).build();

        byte[] actual = aggregateTx.serialize();
        assertEquals(expected, Hex.toHexString(actual));
    }

    @Test
    void shouldCreateAggregateTransactionAndSignWithMultipleCosignatories() {

        TransferTransaction transferTx =
            TransferTransactionFactory.create(
                NetworkType.MIJIN_TEST,
                new Address("SBILTA367K2LX2FEXG5TFWAS7GEFYAGY7QLFBYKC", NetworkType.MIJIN_TEST),
                Collections.emptyList(),
                new PlainMessage("test-message")
            ).build();

        AggregateTransaction aggregateTx =
            AggregateTransactionFactory.createComplete(NetworkType.MIJIN_TEST,
                Collections.singletonList(
                    transferTx.toAggregate(
                        new PublicAccount(
                            "B694186EE4AB0558CA4AFCFDD43B42114AE71094F5A1FC4A913FE9971CACD21D",
                            NetworkType.MIJIN_TEST)))
            ).deadline(new OkHttpFakeDeadline()).build();

        Account cosignatoryAccount =
            new Account(
                "2a2b1f5d366a5dd5dc56c3c757cf4fe6c66e2787087692cf329d7a49a594658b",
                NetworkType.MIJIN_TEST);
        Account cosignatoryAccount2 =
            new Account(
                "b8afae6f4ad13a1b8aad047b488e0738a437c7389d4ff30c359ac068910c1d59",
                NetworkType.MIJIN_TEST); // TODO bug with private key

        SignedTransaction signedTransaction =
            cosignatoryAccount.signTransactionWithCosignatories(
                aggregateTx, Collections.singletonList(cosignatoryAccount2), generationHash);

        assertEquals("6801000000000000", signedTransaction.getPayload().substring(0, 16));
        assertEquals("00000000d6a52a97", signedTransaction.getPayload().substring(248, 264));

    }

    @Test
    void shouldFindAccountInAsASignerOfTheTransaction() {
        TransactionInfoDTO aggregateTransferTransactionDTO = TestHelperOkHttp
            .loadAggregateTransactionInfoDTO(
                "shouldFindAccountInAsASignerOfTheTransaction.json");

        AggregateTransaction aggregateTransferTransaction =
            (AggregateTransaction) new GeneralTransactionMapper(jsonHelper)
                .map(aggregateTransferTransactionDTO);

        assertTrue(
            aggregateTransferTransaction.signedByAccount(
                PublicAccount.createFromPublicKey(
                    "A5F82EC8EBB341427B6785C8111906CD0DF18838FB11B51CE0E18B5E79DFF630",
                    NetworkType.MIJIN_TEST)));
        assertTrue(
            aggregateTransferTransaction.signedByAccount(
                PublicAccount.createFromPublicKey(
                    "7681ED5023141D9CDCF184E5A7B60B7D466739918ED5DA30F7E71EA7B86EFF2D",
                    NetworkType.MIJIN_TEST)));
        assertFalse(
            aggregateTransferTransaction.signedByAccount(
                PublicAccount.createFromPublicKey(
                    "B4F12E7C9F6946091E2CB8B6D3A12B50D17CCBBF646386EA27CE2946A7423DCF",
                    NetworkType.MIJIN_TEST)));
    }


}
