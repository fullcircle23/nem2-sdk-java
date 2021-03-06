package io.nem.sdk.model.namespace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.mosaic.MosaicId;
import org.junit.jupiter.api.Test;

public class AliasTest {

    Address address = Address.createFromRawAddress("SCTVW23D2MN5VE4AQ4TZIDZENGNOZXPRPRLIKCF2");
    Address address2 = Address.createFromRawAddress("SARNASAS2BIAB6LMFA3FPMGBPGIJGK6IJETM3ZSP");
    MosaicId mosaicId = new MosaicId("0dc67fbe1cad29e3");
    MosaicId mosaicId2 = new MosaicId("0dc67fbd1cad29e2");

    @Test
    void shouldCreateAEmptyAlias() {
        Alias alias = new EmptyAlias();
        assertEquals(AliasType.NONE, alias.getType());
        assertTrue(alias.isEmpty());
    }

    @Test
    void shouldCreateAAddressAlias() {
        AddressAlias addressAlias = new AddressAlias(address);
        assertEquals(AliasType.ADDRESS, addressAlias.getType());
        assertEquals(address, addressAlias.getAliasValue());
        assertFalse(addressAlias.isEmpty());
    }

    @Test
    void shouldCreateAMosaicAlias() {
        MosaicAlias mosaicAlias = new MosaicAlias(mosaicId);
        assertEquals(AliasType.MOSAIC, mosaicAlias.getType());
        assertEquals(mosaicId, mosaicAlias.getAliasValue());
        assertFalse(mosaicAlias.isEmpty());
    }

    @Test
    void shouldCompareAddressInAdressAlias() {
        AddressAlias addressAlias = new AddressAlias(address);
        AddressAlias addressAlias1 = new AddressAlias(address);
        AddressAlias addressAlias2 = new AddressAlias(address2);
        assertEquals(addressAlias.getAliasValue(), addressAlias1.getAliasValue());
        assertNotEquals(addressAlias.getAliasValue(), addressAlias2.getAliasValue());
    }

    @Test
    void shouldCompareMosaicIdInMosaicAlias() {
        MosaicAlias mosaicAlias = new MosaicAlias(mosaicId);
        MosaicAlias mosaicAlias1 = new MosaicAlias(mosaicId);
        MosaicAlias mosaicAlias2 = new MosaicAlias(mosaicId2);
        assertEquals(mosaicAlias.getAliasValue(), mosaicAlias1.getAliasValue());
        assertNotEquals(mosaicAlias.getAliasValue(), mosaicAlias2.getAliasValue());
    }
}
