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

package io.nem.sdk.model.receipt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.nem.core.utils.MapperUtils;
import io.nem.sdk.model.account.Account;
import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.account.UnresolvedAddress;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.MosaicId;
import java.math.BigInteger;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class BalanceTransferReceiptTest {

    static Account account;
    static MosaicId mosaicId;
    static UnresolvedAddress unresolvedAddress;
    static Address address;
    static NetworkType networkType = NetworkType.MIJIN_TEST;

    @BeforeAll
    public static void setup() {
        account =
            new Account("787225aaff3d2c71f4ffa32d4f19ec4922f3cd869747f267378f81f8e3fcb12d",
                networkType);
        mosaicId = new MosaicId("85BBEA6CC462B244");
        address = new Address("SDGLFW-DSHILT-IUHGIB-H5UGX2-VYF5VN-JEKCCD-BR26",
            NetworkType.MIJIN_TEST);
        unresolvedAddress = MapperUtils.toUnresolvedAddress(address.encoded(networkType));

    }

    @Test
    void shouldCreateMosaicRentalFeeReceipt() {

        BalanceTransferReceipt balanceTransferReceipt =
            new BalanceTransferReceipt(
                account.getPublicAccount(),
                unresolvedAddress,
                mosaicId,
                BigInteger.valueOf(10),
                ReceiptType.MOSAIC_RENTAL_FEE,
                ReceiptVersion.BALANCE_TRANSFER);
        assertEquals(ReceiptType.MOSAIC_RENTAL_FEE, balanceTransferReceipt.getType());
        assertFalse(balanceTransferReceipt.getSize().isPresent());
        assertEquals(ReceiptVersion.BALANCE_TRANSFER, balanceTransferReceipt.getVersion());
        assertEquals(
            balanceTransferReceipt.getSender().getPublicKey().toHex().toUpperCase(),
            account.getPublicKey().toUpperCase());
        assertEquals(MapperUtils.toAddressFromEncoded(balanceTransferReceipt.getRecipient().encoded(
            networkType)), address);
        assertEquals("85BBEA6CC462B244",
            balanceTransferReceipt.getMosaicId().getIdAsHex().toUpperCase());
        assertEquals(BigInteger.TEN, balanceTransferReceipt.getAmount());

        String hex = Hex.toHexString(balanceTransferReceipt.serialize());
        Assertions.assertEquals(
            "01004d1244b262c46ceabb850a000000000000001026d70e1954775749c6811084d6450a3184d977383f0e4282cd47118af3775590ccb2d8723a173450e6404fda1afaae0bdab524508430c75e",
            hex);

    }

    @Test
    void shouldSerializeTheSameAsTypescript() {

        BalanceTransferReceipt balanceTransferReceipt =
            new BalanceTransferReceipt(
                Account.createFromPrivateKey(
                    "D242FB34C2C4DD36E995B9C865F93940065E326661BA5A4A247331D211FE3A3D", networkType)
                    .getPublicAccount(),
                Address.createFromEncoded("9103B60AAF2762688300000000000000000000000000000000"),
                new MosaicId("941299B2B7E1291C"),
                BigInteger.valueOf(1000),
                ReceiptType.MOSAIC_RENTAL_FEE,
                ReceiptVersion.BALANCE_TRANSFER);

        String hex = Hex.toHexString(balanceTransferReceipt.serialize());
        Assertions.assertEquals(
            "01004D121C29E1B7B2991294E8030000000000002FC3872A792933617D70E02AFF8FBDE152821A0DF0CA5FB04CB56FC3D21C88639103B60AAF2762688300000000000000000000000000000000",
            hex.toUpperCase());
    }

    @Test
    void shouldCreateNamespaceRentalFeeReceipt() {

        BalanceTransferReceipt balanceTransferReceipt =
            new BalanceTransferReceipt(
                account.getPublicAccount(),
                unresolvedAddress,
                mosaicId,
                BigInteger.valueOf(10),
                ReceiptType.NAMESPACE_RENTAL_FEE,
                ReceiptVersion.BALANCE_TRANSFER);
        assertEquals(ReceiptType.NAMESPACE_RENTAL_FEE, balanceTransferReceipt.getType());
        assertFalse(balanceTransferReceipt.getSize().isPresent());
        assertEquals(ReceiptVersion.BALANCE_TRANSFER, balanceTransferReceipt.getVersion());
        assertEquals(
            balanceTransferReceipt.getSender().getPublicKey().toHex().toUpperCase(),
            account.getPublicKey().toUpperCase());
        assertEquals(MapperUtils.toAddressFromEncoded(balanceTransferReceipt.getRecipient().encoded(
            networkType)), address);
        assertEquals("85BBEA6CC462B244",
            balanceTransferReceipt.getMosaicId().getIdAsHex().toUpperCase());
        assertEquals(BigInteger.TEN, balanceTransferReceipt.getAmount());

        String hex = Hex.toHexString(balanceTransferReceipt.serialize());
        Assertions.assertEquals(
            "01004e1344b262c46ceabb850a000000000000001026d70e1954775749c6811084d6450a3184d977383f0e4282cd47118af3775590ccb2d8723a173450e6404fda1afaae0bdab524508430c75e",
            hex);

    }

    @Test
    void shouldCreateNamespaceRentalFeeReceiptWithAlias() {

        BalanceTransferReceipt balanceTransferReceipt =
            new BalanceTransferReceipt(
                account.getPublicAccount(),
                unresolvedAddress,
                mosaicId,
                BigInteger.valueOf(10),
                ReceiptType.NAMESPACE_RENTAL_FEE,
                ReceiptVersion.BALANCE_TRANSFER);
        assertEquals(ReceiptType.NAMESPACE_RENTAL_FEE, balanceTransferReceipt.getType());
        assertFalse(balanceTransferReceipt.getSize().isPresent());
        assertEquals(ReceiptVersion.BALANCE_TRANSFER, balanceTransferReceipt.getVersion());
        assertEquals(
            balanceTransferReceipt.getSender().getPublicKey().toHex().toUpperCase(),
            account.getPublicKey().toUpperCase());
        assertEquals(MapperUtils.toAddressFromEncoded(balanceTransferReceipt.getRecipient().encoded(
            networkType)), address);
        assertEquals("85BBEA6CC462B244",
            balanceTransferReceipt.getMosaicId().getIdAsHex().toUpperCase());
        assertEquals(BigInteger.TEN, balanceTransferReceipt.getAmount());

        String hex = Hex.toHexString(balanceTransferReceipt.serialize());
        Assertions.assertEquals(
            "01004e1344b262c46ceabb850a000000000000001026d70e1954775749c6811084d6450a3184d977383f0e4282cd47118af3775590ccb2d8723a173450e6404fda1afaae0bdab524508430c75e",
            hex);

    }

    @Test
    void shouldThrowIllegalArgumentExceptionWithWrongReceiptType() {
        assertThrows(
            IllegalArgumentException.class,
            () -> {
                new BalanceChangeReceipt(
                    account.getPublicAccount(),
                    mosaicId,
                    BigInteger.valueOf(10),
                    ReceiptType.NAMESPACE_EXPIRED,
                    ReceiptVersion.BALANCE_TRANSFER);
            });
    }
}
