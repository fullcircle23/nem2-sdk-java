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

package io.nem.sdk.infrastructure;

import static java.time.temporal.ChronoUnit.HOURS;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.nem.core.crypto.Hashes;
import io.nem.sdk.model.account.Account;
import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.mosaic.MosaicNonce;
import io.nem.sdk.model.mosaic.MosaicProperties;
import io.nem.sdk.model.mosaic.MosaicSupplyType;
import io.nem.sdk.model.mosaic.NetworkCurrencyMosaic;
import io.nem.sdk.model.namespace.NamespaceId;
import io.nem.sdk.model.transaction.AggregateTransaction;
import io.nem.sdk.model.transaction.CosignatureSignedTransaction;
import io.nem.sdk.model.transaction.CosignatureTransaction;
import io.nem.sdk.model.transaction.Deadline;
import io.nem.sdk.model.transaction.HashType;
import io.nem.sdk.model.transaction.LockFundsTransaction;
import io.nem.sdk.model.transaction.ModifyMultisigAccountTransaction;
import io.nem.sdk.model.transaction.MosaicDefinitionTransaction;
import io.nem.sdk.model.transaction.MosaicSupplyChangeTransaction;
import io.nem.sdk.model.transaction.MultisigCosignatoryModification;
import io.nem.sdk.model.transaction.MultisigCosignatoryModificationType;
import io.nem.sdk.model.transaction.PlainMessage;
import io.nem.sdk.model.transaction.RegisterNamespaceTransaction;
import io.nem.sdk.model.transaction.SecretLockTransaction;
import io.nem.sdk.model.transaction.SecretProofTransaction;
import io.nem.sdk.model.transaction.SignedTransaction;
import io.nem.sdk.model.transaction.Transaction;
import io.nem.sdk.model.transaction.TransactionAnnounceResponse;
import io.nem.sdk.model.transaction.TransferTransaction;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class E2ETest extends BaseTest {

    private TransactionHttp transactionHttp;
    private Account account;
    private Address recipient;
    private Account multisigAccount;
    private Account cosignatoryAccount;
    private Account cosignatoryAccount2;
    private NamespaceId namespaceId =
        new NamespaceId(
            new BigInteger(
                "-1999805136990834023")); // This namespace is created in functional testing
    private String namespaceName = "nem2-tests";
    private MosaicId mosaicId =
        new MosaicId(
            new BigInteger("4532189107927582222")); // This mosaic is created in functional testing
    private Listener listener;
    private String generationHash;

    @BeforeAll
    void setup() throws ExecutionException, InterruptedException, IOException {
        transactionHttp = new TransactionHttp(this.getApiUrl());
        account = this.getTestAccount();
        recipient = this.getRecipient();
        multisigAccount =
            new Account(
                "5edebfdbeb32e9146d05ffd232c8af2cf9f396caf9954289daa0362d097fff3b",
                NetworkType.MIJIN_TEST);
        cosignatoryAccount =
            new Account(
                "2a2b1f5d366a5dd5dc56c3c757cf4fe6c66e2787087692cf329d7a49a594658b",
                NetworkType.MIJIN_TEST);
        cosignatoryAccount2 =
            new Account(
                "b8afae6f4ad13a1b8aad047b488e0738a437c7389d4ff30c359ac068910c1d59",
                NetworkType.MIJIN_TEST);
        generationHash = this.getGenerationHash();
        listener = new Listener(this.getApiUrl());
        listener.open().get();
    }

    @Test
    void standaloneTransferTransaction() throws ExecutionException, InterruptedException {
        TransferTransaction transferTransaction =
            TransferTransaction.create(
                new Deadline(2, HOURS),
                BigInteger.ZERO,
                this.recipient,
                Collections
                    .singletonList(NetworkCurrencyMosaic.createAbsolute(BigInteger.valueOf(1))),
                new PlainMessage("E2ETest:standaloneTransferTransaction:message"),
                NetworkType.MIJIN_TEST);

        SignedTransaction signedTransaction = this.account
            .sign(transferTransaction, generationHash);
        String payload = signedTransaction.getPayload();
        assertEquals(420, payload.length());

        TransactionAnnounceResponse transactionAnnounceResponse =
            transactionHttp.announce(signedTransaction).toFuture().get();
        assertEquals(
            "packet 9 was pushed to the network via /transaction",
            transactionAnnounceResponse.getMessage());

        this.validateTransactionAnnounceCorrectly(
            this.account.getAddress(), signedTransaction.getHash());
    }

    @Test
    void aggregateTransferTransaction() throws ExecutionException, InterruptedException {
        TransferTransaction transferTransaction =
            TransferTransaction.create(
                new Deadline(2, HOURS),
                BigInteger.ZERO,
                this.recipient,
                Collections
                    .singletonList(NetworkCurrencyMosaic.createAbsolute(BigInteger.valueOf(1))),
                /*new PlainMessage(
                    "E2ETest:aggregateTransferTransaction:message"), */// short message for debugging
            new PlainMessage("E2ETest:aggregateTransferTransaction:messagelooooooooooooooooooooooooooooooooooooooo" +
            "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
            "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
            "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
            "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
            "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
            "oooooooong"), // Use long message to test if size of inner transaction is calculated correctly
                NetworkType.MIJIN_TEST);

        AggregateTransaction aggregateTransaction =
            AggregateTransaction.createComplete(
                new Deadline(2, HOURS),
                Collections.singletonList(
                    transferTransaction.toAggregate(this.account.getPublicAccount())),
                NetworkType.MIJIN_TEST);

        SignedTransaction signedTransaction = this.account
            .sign(aggregateTransaction, generationHash);

        TransactionAnnounceResponse transactionAnnounceResponse =
            transactionHttp.announce(signedTransaction).toFuture().get();
        System.out.println(transactionAnnounceResponse.getMessage());

        this.validateTransactionAnnounceCorrectly(
            this.account.getAddress(), signedTransaction.getHash());
    }

    @Test
    void standaloneRootRegisterNamespaceTransaction()
        throws ExecutionException, InterruptedException {
        String namespaceName =
            "test-root-namespace-" + new Double(Math.floor(Math.random() * 10000)).intValue();

        RegisterNamespaceTransaction registerNamespaceTransaction =
            RegisterNamespaceTransaction.createRootNamespace(
                new Deadline(2, HOURS),
                BigInteger.ZERO,
                namespaceName,
                BigInteger.valueOf(100),
                NetworkType.MIJIN_TEST);

        SignedTransaction signedTransaction =
            this.account.sign(registerNamespaceTransaction, generationHash);

        transactionHttp.announce(signedTransaction).toFuture().get();

        this.validateTransactionAnnounceCorrectly(
            this.account.getAddress(), signedTransaction.getHash());
    }

    @Test
    void aggregateRootRegisterNamespaceTransaction()
        throws ExecutionException, InterruptedException {
        String namespaceName =
            "test-root-namespace-" + new Double(Math.floor(Math.random() * 10000)).intValue();

        RegisterNamespaceTransaction registerNamespaceTransaction =
            RegisterNamespaceTransaction.createRootNamespace(
                new Deadline(2, HOURS),
                BigInteger.ZERO,
                namespaceName,
                BigInteger.valueOf(100),
                NetworkType.MIJIN_TEST);
        AggregateTransaction aggregateTransaction =
            AggregateTransaction.createComplete(
                new Deadline(2, HOURS),
                Collections.singletonList(
                    registerNamespaceTransaction.toAggregate(this.account.getPublicAccount())),
                NetworkType.MIJIN_TEST);

        SignedTransaction signedTransaction = this.account
            .sign(aggregateTransaction, generationHash);

        transactionHttp.announce(signedTransaction).toFuture().get();

        this.validateTransactionAnnounceCorrectly(
            this.account.getAddress(), signedTransaction.getHash());
    }

    @Test
    void standaloneSubNamespaceRegisterNamespaceTransaction()
        throws ExecutionException, InterruptedException {
        String namespaceName =
            "test-sub-namespace-" + new Double(Math.floor(Math.random() * 10000)).intValue();

        RegisterNamespaceTransaction registerNamespaceTransaction =
            RegisterNamespaceTransaction.createSubNamespace(
                new Deadline(2, HOURS),
                BigInteger.ZERO,
                namespaceName,
                this.namespaceId,
                NetworkType.MIJIN_TEST);

        SignedTransaction signedTransaction =
            this.account.sign(registerNamespaceTransaction, generationHash);

        transactionHttp.announce(signedTransaction).toFuture().get();

        this.validateTransactionAnnounceCorrectly(
            this.account.getAddress(), signedTransaction.getHash());
    }

    @Test
    void aggregateSubNamespaceRegisterNamespaceTransaction()
        throws ExecutionException, InterruptedException {
        String namespaceName =
            "test-sub-namespace-" + new Double(Math.floor(Math.random() * 10000)).intValue();

        RegisterNamespaceTransaction registerNamespaceTransaction =
            RegisterNamespaceTransaction.createSubNamespace(
                new Deadline(2, HOURS),
                BigInteger.ZERO,
                namespaceName,
                this.namespaceId,
                NetworkType.MIJIN_TEST);

        AggregateTransaction aggregateTransaction =
            AggregateTransaction.createComplete(
                new Deadline(2, HOURS),
                Collections.singletonList(
                    registerNamespaceTransaction.toAggregate(this.account.getPublicAccount())),
                NetworkType.MIJIN_TEST);

        SignedTransaction signedTransaction = this.account
            .sign(aggregateTransaction, generationHash);

        transactionHttp.announce(signedTransaction).toFuture().get();

        this.validateTransactionAnnounceCorrectly(
            this.account.getAddress(), signedTransaction.getHash());
    }

    @Test
    void standaloneMosaicDefinitionTransaction() throws ExecutionException, InterruptedException {
        String mosaicName =
            "test-mosaic-" + new Double(Math.floor(Math.random() * 10000)).intValue();

        MosaicDefinitionTransaction mosaicDefinitionTransaction =
            MosaicDefinitionTransaction.create(
                new Deadline(2, HOURS),
                BigInteger.ZERO,
                MosaicNonce.createFromBigInteger(new BigInteger("0")),
                new MosaicId(new BigInteger("0")),
                MosaicProperties.create(true, true, 4, BigInteger.valueOf(100)),
                NetworkType.MIJIN_TEST);

        SignedTransaction signedTransaction =
            this.account.sign(mosaicDefinitionTransaction, generationHash);

        transactionHttp.announce(signedTransaction).toFuture().get();

        this.validateTransactionAnnounceCorrectly(
            this.account.getAddress(), signedTransaction.getHash());
    }

    @Test
    void aggregateMosaicDefinitionTransaction() throws ExecutionException, InterruptedException {
        String mosaicName =
            "test-mosaic-" + new Double(Math.floor(Math.random() * 10000)).intValue();

        MosaicDefinitionTransaction mosaicDefinitionTransaction =
            MosaicDefinitionTransaction.create(
                new Deadline(2, HOURS),
                BigInteger.ZERO,
                MosaicNonce.createFromBigInteger(new BigInteger("0")),
                new MosaicId(new BigInteger("0")),
                MosaicProperties.create(true, false, 4, BigInteger.valueOf(100)),
                NetworkType.MIJIN_TEST);

        AggregateTransaction aggregateTransaction =
            AggregateTransaction.createComplete(
                new Deadline(2, HOURS),
                Collections.singletonList(
                    mosaicDefinitionTransaction.toAggregate(this.account.getPublicAccount())),
                NetworkType.MIJIN_TEST);

        SignedTransaction signedTransaction = this.account
            .sign(aggregateTransaction, generationHash);

        transactionHttp.announce(signedTransaction).toFuture().get();

        this.validateTransactionAnnounceCorrectly(
            this.account.getAddress(), signedTransaction.getHash());
    }

    @Test
    void standaloneMosaicSupplyChangeTransaction() throws ExecutionException, InterruptedException {
        MosaicSupplyChangeTransaction mosaicSupplyChangeTransaction =
            MosaicSupplyChangeTransaction.create(
                new Deadline(2, HOURS),
                this.mosaicId,
                MosaicSupplyType.INCREASE,
                BigInteger.valueOf(10),
                NetworkType.MIJIN_TEST);

        SignedTransaction signedTransaction =
            this.account.sign(mosaicSupplyChangeTransaction, generationHash);

        transactionHttp.announce(signedTransaction).toFuture().get();

        this.validateTransactionAnnounceCorrectly(
            this.account.getAddress(), signedTransaction.getHash());
    }

    @Test
    void aggregateMosaicSupplyChangeTransaction() throws ExecutionException, InterruptedException {
        MosaicSupplyChangeTransaction mosaicSupplyChangeTransaction =
            MosaicSupplyChangeTransaction.create(
                new Deadline(2, HOURS),
                this.mosaicId,
                MosaicSupplyType.INCREASE,
                BigInteger.valueOf(10),
                NetworkType.MIJIN_TEST);

        AggregateTransaction aggregateTransaction =
            AggregateTransaction.createComplete(
                new Deadline(2, HOURS),
                Collections.singletonList(
                    mosaicSupplyChangeTransaction.toAggregate(this.account.getPublicAccount())),
                NetworkType.MIJIN_TEST);

        SignedTransaction signedTransaction = this.account
            .sign(aggregateTransaction, generationHash);

        transactionHttp.announce(signedTransaction).toFuture().get();

        this.validateTransactionAnnounceCorrectly(
            this.account.getAddress(), signedTransaction.getHash());
    }

    @Test
    void shouldSignModifyMultisigAccountTransactionWithCosignatories()
        throws ExecutionException, InterruptedException {
        ModifyMultisigAccountTransaction modifyMultisigAccountTransaction =
            ModifyMultisigAccountTransaction.create(
                new Deadline(2, HOURS),
                (byte) 0,
                (byte) 0,
                Collections.singletonList(
                    new MultisigCosignatoryModification(
                        MultisigCosignatoryModificationType.ADD,
                        PublicAccount.createFromPublicKey(
                            "B0F93CBEE49EEB9953C6F3985B15A4F238E205584D8F924C621CBE4D7AC6EC24",
                            NetworkType.MIJIN_TEST))),
                NetworkType.MIJIN_TEST);
        AggregateTransaction aggregateTransaction =
            AggregateTransaction.createComplete(
                new Deadline(2, HOURS),
                Collections.singletonList(
                    modifyMultisigAccountTransaction.toAggregate(
                        this.multisigAccount.getPublicAccount())),
                NetworkType.MIJIN_TEST);

        SignedTransaction signedTransaction =
            this.cosignatoryAccount.signTransactionWithCosignatories(
                aggregateTransaction,
                Collections.singletonList(this.cosignatoryAccount2),
                generationHash);

        LockFundsTransaction lockFundsTransaction =
            LockFundsTransaction.create(
                new Deadline(2, HOURS),
                NetworkCurrencyMosaic.createRelative(BigInteger.valueOf(10)),
                BigInteger.valueOf(100),
                signedTransaction,
                NetworkType.MIJIN_TEST);

        SignedTransaction lockFundsSignedTransaction =
            this.cosignatoryAccount.sign(lockFundsTransaction, generationHash);

        transactionHttp.announce(lockFundsSignedTransaction).toFuture().get();

        listener.confirmed(this.cosignatoryAccount.getAddress()).take(1).toFuture().get();

        transactionHttp.announceAggregateBonded(signedTransaction).toFuture().get();

        this.validateAggregateBondedTransactionAnnounceCorrectly(
            this.cosignatoryAccount.getAddress(), signedTransaction.getHash());
    }

    @Test
    void CosignatureTransaction() throws ExecutionException, InterruptedException {
        TransferTransaction transferTransaction =
            TransferTransaction.create(
                new Deadline(2, HOURS),
                BigInteger.ZERO,
                new Address("SDRDGFTDLLCB67D4HPGIMIHPNSRYRJRT7DOBGWZY", NetworkType.MIJIN_TEST),
                Collections
                    .singletonList(NetworkCurrencyMosaic.createAbsolute(BigInteger.valueOf(1))),
                PlainMessage.create("test-message"),
                NetworkType.MIJIN_TEST);

        AggregateTransaction aggregateTransaction =
            AggregateTransaction.createComplete(
                new Deadline(2, HOURS),
                Collections.singletonList(
                    transferTransaction.toAggregate(this.multisigAccount.getPublicAccount())),
                NetworkType.MIJIN_TEST);
        SignedTransaction signedTransaction =
            this.cosignatoryAccount.sign(aggregateTransaction, generationHash);

        LockFundsTransaction lockFundsTransaction =
            LockFundsTransaction.create(
                new Deadline(2, HOURS),
                NetworkCurrencyMosaic.createRelative(BigInteger.valueOf(10)),
                BigInteger.valueOf(100),
                signedTransaction,
                NetworkType.MIJIN_TEST);

        SignedTransaction lockFundsSignedTransaction =
            this.cosignatoryAccount.sign(lockFundsTransaction, generationHash);

        transactionHttp.announce(lockFundsSignedTransaction).toFuture().get();

        listener.confirmed(this.cosignatoryAccount.getAddress()).take(1).toFuture().get();

        transactionHttp.announceAggregateBonded(signedTransaction).toFuture().get();

        this.validateAggregateBondedTransactionAnnounceCorrectly(
            this.cosignatoryAccount.getAddress(), signedTransaction.getHash());

        CosignatureTransaction cosignatureTransaction =
            CosignatureTransaction.create(aggregateTransaction);

        CosignatureSignedTransaction cosignatureSignedTransaction =
            this.cosignatoryAccount2.signCosignatureTransaction(cosignatureTransaction);

        transactionHttp
            .announceAggregateBondedCosignature(cosignatureSignedTransaction)
            .toFuture()
            .get();

        this.validateAggregateBondedCosignatureTransactionAnnounceCorrectly(
            this.cosignatoryAccount.getAddress(), cosignatureSignedTransaction.getParentHash());
    }

    @Test
    void standaloneLockFundsTransaction() throws ExecutionException, InterruptedException {
        AggregateTransaction aggregateTransaction =
            AggregateTransaction.createBonded(
                new Deadline(2, HOURS), Collections.emptyList(), NetworkType.MIJIN_TEST);
        SignedTransaction signedTransaction = this.account
            .sign(aggregateTransaction, generationHash);
        LockFundsTransaction lockFundstx =
            LockFundsTransaction.create(
                new Deadline(2, HOURS),
                NetworkCurrencyMosaic.createRelative(BigInteger.valueOf(10)),
                BigInteger.valueOf(100),
                signedTransaction,
                NetworkType.MIJIN_TEST);

        SignedTransaction lockFundsTransactionSigned = this.account
            .sign(lockFundstx, generationHash);
        transactionHttp.announce(lockFundsTransactionSigned).toFuture().get();

        this.validateTransactionAnnounceCorrectly(
            this.account.getAddress(), lockFundsTransactionSigned.getHash());
    }

    @Test
    void aggregateLockFundsTransaction() throws ExecutionException, InterruptedException {
        AggregateTransaction aggregateTransaction =
            AggregateTransaction.createBonded(
                new Deadline(2, HOURS), Collections.emptyList(), NetworkType.MIJIN_TEST);
        SignedTransaction signedTransaction = this.account
            .sign(aggregateTransaction, generationHash);
        LockFundsTransaction lockFundstx =
            LockFundsTransaction.create(
                new Deadline(2, HOURS),
                NetworkCurrencyMosaic.createRelative(BigInteger.valueOf(10)),
                BigInteger.valueOf(100),
                signedTransaction,
                NetworkType.MIJIN_TEST);

        AggregateTransaction lockFundsAggregatetx =
            AggregateTransaction.createComplete(
                new Deadline(2, HOURS),
                Collections.singletonList(lockFundstx.toAggregate(this.account.getPublicAccount())),
                NetworkType.MIJIN_TEST);

        SignedTransaction lockFundsTransactionSigned =
            this.account.sign(lockFundsAggregatetx, generationHash);

        transactionHttp.announce(lockFundsTransactionSigned).toFuture().get();

        this.validateTransactionAnnounceCorrectly(
            this.account.getAddress(), lockFundsTransactionSigned.getHash());
    }

    @Test
    void standaloneSecretLockTransaction() throws ExecutionException, InterruptedException {
        byte[] secretBytes = new byte[20];
        new Random().nextBytes(secretBytes);
        byte[] result = Hashes.sha3_256(secretBytes);
        String secret = Hex.encodeHexString(result);
        SecretLockTransaction secretLocktx =
            SecretLockTransaction.create(
                new Deadline(2, HOURS),
                NetworkCurrencyMosaic.createRelative(BigInteger.valueOf(10)),
                BigInteger.valueOf(100),
                HashType.SHA3_256,
                secret,
                Address.createFromRawAddress("SDUP5PLHDXKBX3UU5Q52LAY4WYEKGEWC6IB3VBFM"),
                NetworkType.MIJIN_TEST);

        SignedTransaction secretLockTransactionSigned = this.account
            .sign(secretLocktx, generationHash);

        transactionHttp.announce(secretLockTransactionSigned).toFuture().get();

        this.validateTransactionAnnounceCorrectly(
            this.account.getAddress(), secretLockTransactionSigned.getHash());
    }

    @Test
    void aggregateSecretLockTransaction() throws ExecutionException, InterruptedException {
        byte[] secretBytes = new byte[20];
        new Random().nextBytes(secretBytes);
        byte[] result = Hashes.sha3_256(secretBytes);
        String secret = Hex.encodeHexString(result);
        SecretLockTransaction secretLocktx =
            SecretLockTransaction.create(
                new Deadline(2, HOURS),
                NetworkCurrencyMosaic.createRelative(BigInteger.valueOf(10)),
                BigInteger.valueOf(100),
                HashType.SHA3_256,
                secret,
                Address.createFromRawAddress("SDUP5PLHDXKBX3UU5Q52LAY4WYEKGEWC6IB3VBFM"),
                NetworkType.MIJIN_TEST);

        AggregateTransaction secretLockAggregatetx =
            AggregateTransaction.createComplete(
                new Deadline(2, HOURS),
                Collections
                    .singletonList(secretLocktx.toAggregate(this.account.getPublicAccount())),
                NetworkType.MIJIN_TEST);

        SignedTransaction secretLockTransactionSigned =
            this.account.sign(secretLockAggregatetx, generationHash);

        transactionHttp.announce(secretLockTransactionSigned).toFuture().get();

        this.validateTransactionAnnounceCorrectly(
            this.account.getAddress(), secretLockTransactionSigned.getHash());
    }

    @Test
    void standaloneSecretProofTransaction() throws ExecutionException, InterruptedException {
        byte[] secretBytes = new byte[20];
        new Random().nextBytes(secretBytes);
        byte[] result = Hashes.sha3_256(secretBytes);
        String secret = Hex.encodeHexString(result);
        String proof = Hex.encodeHexString(secretBytes);
        SecretLockTransaction secretLocktx =
            SecretLockTransaction.create(
                new Deadline(2, HOURS),
                NetworkCurrencyMosaic.createRelative(BigInteger.valueOf(10)),
                BigInteger.valueOf(100),
                HashType.SHA3_256,
                secret,
                Address.createFromRawAddress("SDUP5PLHDXKBX3UU5Q52LAY4WYEKGEWC6IB3VBFM"),
                NetworkType.MIJIN_TEST);

        SignedTransaction lockFundsTransactionSigned = this.account
            .sign(secretLocktx, generationHash);

        transactionHttp.announce(lockFundsTransactionSigned).toFuture().get();

        this.validateTransactionAnnounceCorrectly(
            this.account.getAddress(), lockFundsTransactionSigned.getHash());

        SecretProofTransaction secretProoftx =
            SecretProofTransaction.create(
                new Deadline(2, HOURS),
                BigInteger.ZERO,
                HashType.SHA3_256,
                Address.createFromRawAddress("SDUP5PLHDXKBX3UU5Q52LAY4WYEKGEWC6IB3VBFM"),
                secret,
                proof,
                NetworkType.MIJIN_TEST);

        SignedTransaction secretProofTransactionSigned =
            this.account.sign(secretProoftx, generationHash);

        transactionHttp.announce(secretProofTransactionSigned).toFuture().get();

        this.validateTransactionAnnounceCorrectly(
            this.account.getAddress(), secretProofTransactionSigned.getHash());
    }

    @Test
    void aggregateSecretProofTransaction() throws ExecutionException, InterruptedException {
        byte[] secretBytes = new byte[20];
        new Random().nextBytes(secretBytes);
        byte[] result = Hashes.sha3_256(secretBytes);
        String secret = Hex.encodeHexString(result);
        String proof = Hex.encodeHexString(secretBytes);
        SecretLockTransaction secretLocktx =
            SecretLockTransaction.create(
                new Deadline(2, HOURS),
                NetworkCurrencyMosaic.createRelative(BigInteger.valueOf(10)),
                BigInteger.valueOf(100),
                HashType.SHA3_256,
                secret,
                Address.createFromRawAddress("SDUP5PLHDXKBX3UU5Q52LAY4WYEKGEWC6IB3VBFM"),
                NetworkType.MIJIN_TEST);

        SignedTransaction lockFundsTransactionSigned = this.account
            .sign(secretLocktx, generationHash);

        transactionHttp.announce(lockFundsTransactionSigned).toFuture().get();

        this.validateTransactionAnnounceCorrectly(
            this.account.getAddress(), lockFundsTransactionSigned.getHash());

        SecretProofTransaction secretProoftx =
            SecretProofTransaction.create(
                new Deadline(2, HOURS),
                BigInteger.ZERO,
                HashType.SHA3_256,
                Address.createFromRawAddress("SDUP5PLHDXKBX3UU5Q52LAY4WYEKGEWC6IB3VBFM"),
                secret,
                proof,
                NetworkType.MIJIN_TEST);

        AggregateTransaction secretProofAggregatetx =
            AggregateTransaction.createComplete(
                new Deadline(2, HOURS),
                Collections
                    .singletonList(secretProoftx.toAggregate(this.account.getPublicAccount())),
                NetworkType.MIJIN_TEST);

        SignedTransaction secretProofTransactionSigned =
            this.account.sign(secretProofAggregatetx, generationHash);

        transactionHttp.announce(secretProofTransactionSigned).toFuture().get();

        this.validateTransactionAnnounceCorrectly(
            this.account.getAddress(), secretProofTransactionSigned.getHash());
    }

    void validateTransactionAnnounceCorrectly(Address address, String transactionHash)
        throws ExecutionException, InterruptedException {
        Transaction transaction = listener.confirmed(address).take(1).toFuture().get();

        assertEquals(transactionHash, transaction.getTransactionInfo().get().getHash().get());
    }

    void validateAggregateBondedTransactionAnnounceCorrectly(Address address,
        String transactionHash)
        throws ExecutionException, InterruptedException {
        AggregateTransaction aggregateTransaction =
            listener.aggregateBondedAdded(address).take(1).toFuture().get();
        assertEquals(transactionHash,
            aggregateTransaction.getTransactionInfo().get().getHash().get());
    }

    void validateAggregateBondedCosignatureTransactionAnnounceCorrectly(
        Address address, String transactionHash) throws ExecutionException, InterruptedException {
        String hash = listener.cosignatureAdded(address).take(1).toFuture().get().getParentHash();
        assertEquals(transactionHash, hash);
    }
}
