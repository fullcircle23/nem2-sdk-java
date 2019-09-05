/**
*** Copyright (c) 2016-present,
*** Jaguar0625, gimre, BloodyRookie, Tech Bureau, Corp. All rights reserved.
***
*** This file is part of Catapult.
***
*** Catapult is free software: you can redistribute it and/or modify
*** it under the terms of the GNU Lesser General Public License as published by
*** the Free Software Foundation, either version 3 of the License, or
*** (at your option) any later version.
***
*** Catapult is distributed in the hope that it will be useful,
*** but WITHOUT ANY WARRANTY; without even the implied warranty of
*** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
*** GNU Lesser General Public License for more details.
***
*** You should have received a copy of the GNU Lesser General Public License
*** along with Catapult. If not, see <http://www.gnu.org/licenses/>.
**/

package io.nem.catapult.builders;

import java.io.DataInput;

/** Binary layout for a secret lock transaction. */
final class SecretLockTransactionBodyBuilder {
    /** Locked mosaic. */
    private final UnresolvedMosaicBuilder mosaic;
    /** Number of blocks for which a lock should be valid. */
    private final BlockDurationDto duration;
    /** Hash algorithm. */
    private final LockHashAlgorithmDto hashAlgorithm;
    /** Secret. */
    private final Hash256Dto secret;
    /** Locked mosaic recipient address. */
    private final UnresolvedAddressDto recipientAddress;

    /**
     * Constructor - Creates an object from stream.
     *
     * @param stream Byte stream to use to serialize the object.
     */
    protected SecretLockTransactionBodyBuilder(final DataInput stream) {
        this.mosaic = UnresolvedMosaicBuilder.loadFromBinary(stream);
        this.duration = BlockDurationDto.loadFromBinary(stream);
        this.hashAlgorithm = LockHashAlgorithmDto.loadFromBinary(stream);
        this.secret = Hash256Dto.loadFromBinary(stream);
        this.recipientAddress = UnresolvedAddressDto.loadFromBinary(stream);
    }

    /**
     * Constructor.
     *
     * @param mosaic Locked mosaic.
     * @param duration Number of blocks for which a lock should be valid.
     * @param hashAlgorithm Hash algorithm.
     * @param secret Secret.
     * @param recipientAddress Locked mosaic recipient address.
     */
    protected SecretLockTransactionBodyBuilder(final UnresolvedMosaicBuilder mosaic, final BlockDurationDto duration, final LockHashAlgorithmDto hashAlgorithm, final Hash256Dto secret, final UnresolvedAddressDto recipientAddress) {
        GeneratorUtils.notNull(mosaic, "mosaic is null");
        GeneratorUtils.notNull(duration, "duration is null");
        GeneratorUtils.notNull(hashAlgorithm, "hashAlgorithm is null");
        GeneratorUtils.notNull(secret, "secret is null");
        GeneratorUtils.notNull(recipientAddress, "recipientAddress is null");
        this.mosaic = mosaic;
        this.duration = duration;
        this.hashAlgorithm = hashAlgorithm;
        this.secret = secret;
        this.recipientAddress = recipientAddress;
    }

    /**
     * Creates an instance of SecretLockTransactionBodyBuilder.
     *
     * @param mosaic Locked mosaic.
     * @param duration Number of blocks for which a lock should be valid.
     * @param hashAlgorithm Hash algorithm.
     * @param secret Secret.
     * @param recipientAddress Locked mosaic recipient address.
     * @return Instance of SecretLockTransactionBodyBuilder.
     */
    public static SecretLockTransactionBodyBuilder create(final UnresolvedMosaicBuilder mosaic, final BlockDurationDto duration, final LockHashAlgorithmDto hashAlgorithm, final Hash256Dto secret, final UnresolvedAddressDto recipientAddress) {
        return new SecretLockTransactionBodyBuilder(mosaic, duration, hashAlgorithm, secret, recipientAddress);
    }

    /**
     * Gets locked mosaic.
     *
     * @return Locked mosaic.
     */
    public UnresolvedMosaicBuilder getMosaic() {
        return this.mosaic;
    }

    /**
     * Gets number of blocks for which a lock should be valid.
     *
     * @return Number of blocks for which a lock should be valid.
     */
    public BlockDurationDto getDuration() {
        return this.duration;
    }

    /**
     * Gets hash algorithm.
     *
     * @return Hash algorithm.
     */
    public LockHashAlgorithmDto getHashAlgorithm() {
        return this.hashAlgorithm;
    }

    /**
     * Gets secret.
     *
     * @return Secret.
     */
    public Hash256Dto getSecret() {
        return this.secret;
    }

    /**
     * Gets locked mosaic recipient address.
     *
     * @return Locked mosaic recipient address.
     */
    public UnresolvedAddressDto getRecipientAddress() {
        return this.recipientAddress;
    }

    /**
     * Gets the size of the object.
     *
     * @return Size in bytes.
     */
    public int getSize() {
        int size = 0;
        size += this.mosaic.getSize();
        size += this.duration.getSize();
        size += this.hashAlgorithm.getSize();
        size += this.secret.getSize();
        size += this.recipientAddress.getSize();
        return size;
    }

    /**
     * Creates an instance of SecretLockTransactionBodyBuilder from a stream.
     *
     * @param stream Byte stream to use to serialize the object.
     * @return Instance of SecretLockTransactionBodyBuilder.
     */
    public static SecretLockTransactionBodyBuilder loadFromBinary(final DataInput stream) {
        return new SecretLockTransactionBodyBuilder(stream);
    }

    /**
     * Serializes an object to bytes.
     *
     * @return Serialized bytes.
     */
    public byte[] serialize() {
        return GeneratorUtils.serialize(dataOutputStream -> {
            final byte[] mosaicBytes = this.mosaic.serialize();
            dataOutputStream.write(mosaicBytes, 0, mosaicBytes.length);
            final byte[] durationBytes = this.duration.serialize();
            dataOutputStream.write(durationBytes, 0, durationBytes.length);
            final byte[] hashAlgorithmBytes = this.hashAlgorithm.serialize();
            dataOutputStream.write(hashAlgorithmBytes, 0, hashAlgorithmBytes.length);
            final byte[] secretBytes = this.secret.serialize();
            dataOutputStream.write(secretBytes, 0, secretBytes.length);
            final byte[] recipientAddressBytes = this.recipientAddress.serialize();
            dataOutputStream.write(recipientAddressBytes, 0, recipientAddressBytes.length);
        });
    }
}
