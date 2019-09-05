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
import java.util.ArrayList;
import java.nio.ByteBuffer;

/** Binary layout for a transfer transaction. */
final class TransferTransactionBodyBuilder {
    /** Recipient address. */
    private final UnresolvedAddressDto recipientAddress;
    /** Attached message. */
    private final ByteBuffer message;
    /** Attached mosaics. */
    private final ArrayList<UnresolvedMosaicBuilder> mosaics;

    /**
     * Constructor - Creates an object from stream.
     *
     * @param stream Byte stream to use to serialize the object.
     */
    protected TransferTransactionBodyBuilder(final DataInput stream) {
        try {
            this.recipientAddress = UnresolvedAddressDto.loadFromBinary(stream);
            final short messageSize = Short.reverseBytes(stream.readShort());
            final byte mosaicsCount = stream.readByte();
            this.message = ByteBuffer.allocate(messageSize);
            stream.readFully(this.message.array());
            this.mosaics = new java.util.ArrayList<>(mosaicsCount);
            for (int i = 0; i < mosaicsCount; i++) {
                mosaics.add(UnresolvedMosaicBuilder.loadFromBinary(stream));
            }
        } catch(Exception e) {
            throw GeneratorUtils.getExceptionToPropagate(e);
        }
    }

    /**
     * Constructor.
     *
     * @param recipientAddress Recipient address.
     * @param message Attached message.
     * @param mosaics Attached mosaics.
     */
    protected TransferTransactionBodyBuilder(final UnresolvedAddressDto recipientAddress, final ByteBuffer message, final ArrayList<UnresolvedMosaicBuilder> mosaics) {
        GeneratorUtils.notNull(recipientAddress, "recipientAddress is null");
        GeneratorUtils.notNull(message, "message is null");
        GeneratorUtils.notNull(mosaics, "mosaics is null");
        this.recipientAddress = recipientAddress;
        this.message = message;
        this.mosaics = mosaics;
    }

    /**
     * Creates an instance of TransferTransactionBodyBuilder.
     *
     * @param recipientAddress Recipient address.
     * @param message Attached message.
     * @param mosaics Attached mosaics.
     * @return Instance of TransferTransactionBodyBuilder.
     */
    public static TransferTransactionBodyBuilder create(final UnresolvedAddressDto recipientAddress, final ByteBuffer message, final ArrayList<UnresolvedMosaicBuilder> mosaics) {
        return new TransferTransactionBodyBuilder(recipientAddress, message, mosaics);
    }

    /**
     * Gets recipient address.
     *
     * @return Recipient address.
     */
    public UnresolvedAddressDto getRecipientAddress() {
        return this.recipientAddress;
    }

    /**
     * Gets attached message.
     *
     * @return Attached message.
     */
    public ByteBuffer getMessage() {
        return this.message;
    }

    /**
     * Gets attached mosaics.
     *
     * @return Attached mosaics.
     */
    public ArrayList<UnresolvedMosaicBuilder> getMosaics() {
        return this.mosaics;
    }

    /**
     * Gets the size of the object.
     *
     * @return Size in bytes.
     */
    public int getSize() {
        int size = 0;
        size += this.recipientAddress.getSize();
        size += 2; // messageSize
        size += 1; // mosaicsCount
        size += this.message.array().length;
        size += this.mosaics.stream().mapToInt(o -> o.getSize()).sum();
        return size;
    }

    /**
     * Creates an instance of TransferTransactionBodyBuilder from a stream.
     *
     * @param stream Byte stream to use to serialize the object.
     * @return Instance of TransferTransactionBodyBuilder.
     */
    public static TransferTransactionBodyBuilder loadFromBinary(final DataInput stream) {
        return new TransferTransactionBodyBuilder(stream);
    }

    /**
     * Serializes an object to bytes.
     *
     * @return Serialized bytes.
     */
    public byte[] serialize() {
        return GeneratorUtils.serialize(dataOutputStream -> {
            final byte[] recipientAddressBytes = this.recipientAddress.serialize();
            dataOutputStream.write(recipientAddressBytes, 0, recipientAddressBytes.length);
            dataOutputStream.writeShort(Short.reverseBytes((short) this.message.array().length));
            dataOutputStream.writeByte((byte) this.mosaics.size());
            dataOutputStream.write(this.message.array(), 0, this.message.array().length);
            for (int i = 0; i < this.mosaics.size(); i++) {
                final byte[] mosaicsBytes = this.mosaics.get(i).serialize();
                dataOutputStream.write(mosaicsBytes, 0, mosaicsBytes.length);
            }
        });
    }
}
