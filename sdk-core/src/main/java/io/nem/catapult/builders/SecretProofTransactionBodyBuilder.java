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
import java.nio.ByteBuffer;

/** Binary layout for a secret proof transaction. */
final class SecretProofTransactionBodyBuilder {
    /** Hash algorithm. */
    private final LockHashAlgorithmDto hashAlgorithm;
    /** Secret. */
    private final Hash256Dto secret;
    /** Locked mosaic recipient address. */
    private final UnresolvedAddressDto recipientAddress;
    /** Proof data. */
    private final ByteBuffer proof;

    /**
     * Constructor - Creates an object from stream.
     *
     * @param stream Byte stream to use to serialize the object.
     */
    protected SecretProofTransactionBodyBuilder(final DataInput stream) {
        try {
            this.hashAlgorithm = LockHashAlgorithmDto.loadFromBinary(stream);
            this.secret = Hash256Dto.loadFromBinary(stream);
            this.recipientAddress = UnresolvedAddressDto.loadFromBinary(stream);
            final short proofSize = Short.reverseBytes(stream.readShort());
            this.proof = ByteBuffer.allocate(proofSize);
            stream.readFully(this.proof.array());
        } catch(Exception e) {
            throw GeneratorUtils.getExceptionToPropagate(e);
        }
    }

    /**
     * Constructor.
     *
     * @param hashAlgorithm Hash algorithm.
     * @param secret Secret.
     * @param recipientAddress Locked mosaic recipient address.
     * @param proof Proof data.
     */
    protected SecretProofTransactionBodyBuilder(final LockHashAlgorithmDto hashAlgorithm, final Hash256Dto secret, final UnresolvedAddressDto recipientAddress, final ByteBuffer proof) {
        GeneratorUtils.notNull(hashAlgorithm, "hashAlgorithm is null");
        GeneratorUtils.notNull(secret, "secret is null");
        GeneratorUtils.notNull(recipientAddress, "recipientAddress is null");
        GeneratorUtils.notNull(proof, "proof is null");
        this.hashAlgorithm = hashAlgorithm;
        this.secret = secret;
        this.recipientAddress = recipientAddress;
        this.proof = proof;
    }

    /**
     * Creates an instance of SecretProofTransactionBodyBuilder.
     *
     * @param hashAlgorithm Hash algorithm.
     * @param secret Secret.
     * @param recipientAddress Locked mosaic recipient address.
     * @param proof Proof data.
     * @return Instance of SecretProofTransactionBodyBuilder.
     */
    public static SecretProofTransactionBodyBuilder create(final LockHashAlgorithmDto hashAlgorithm, final Hash256Dto secret, final UnresolvedAddressDto recipientAddress, final ByteBuffer proof) {
        return new SecretProofTransactionBodyBuilder(hashAlgorithm, secret, recipientAddress, proof);
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
     * Gets proof data.
     *
     * @return Proof data.
     */
    public ByteBuffer getProof() {
        return this.proof;
    }

    /**
     * Gets the size of the object.
     *
     * @return Size in bytes.
     */
    public int getSize() {
        int size = 0;
        size += this.hashAlgorithm.getSize();
        size += this.secret.getSize();
        size += this.recipientAddress.getSize();
        size += 2; // proofSize
        size += this.proof.array().length;
        return size;
    }

    /**
     * Creates an instance of SecretProofTransactionBodyBuilder from a stream.
     *
     * @param stream Byte stream to use to serialize the object.
     * @return Instance of SecretProofTransactionBodyBuilder.
     */
    public static SecretProofTransactionBodyBuilder loadFromBinary(final DataInput stream) {
        return new SecretProofTransactionBodyBuilder(stream);
    }

    /**
     * Serializes an object to bytes.
     *
     * @return Serialized bytes.
     */
    public byte[] serialize() {
        return GeneratorUtils.serialize(dataOutputStream -> {
            final byte[] hashAlgorithmBytes = this.hashAlgorithm.serialize();
            dataOutputStream.write(hashAlgorithmBytes, 0, hashAlgorithmBytes.length);
            final byte[] secretBytes = this.secret.serialize();
            dataOutputStream.write(secretBytes, 0, secretBytes.length);
            final byte[] recipientAddressBytes = this.recipientAddress.serialize();
            dataOutputStream.write(recipientAddressBytes, 0, recipientAddressBytes.length);
            dataOutputStream.writeShort(Short.reverseBytes((short) this.proof.array().length));
            dataOutputStream.write(this.proof.array(), 0, this.proof.array().length);
        });
    }
}
