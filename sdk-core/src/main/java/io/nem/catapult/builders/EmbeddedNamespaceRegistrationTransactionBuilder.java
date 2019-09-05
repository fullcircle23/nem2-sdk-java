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

/** Binary layout for an embedded namespace registration transaction. */
public final class EmbeddedNamespaceRegistrationTransactionBuilder extends EmbeddedTransactionBuilder {
    /** Namespace registration transaction body. */
    private final NamespaceRegistrationTransactionBodyBuilder namespaceRegistrationTransactionBody;

    /**
     * Constructor - Creates an object from stream.
     *
     * @param stream Byte stream to use to serialize the object.
     */
    protected EmbeddedNamespaceRegistrationTransactionBuilder(final DataInput stream) {
        super(stream);
        this.namespaceRegistrationTransactionBody = NamespaceRegistrationTransactionBodyBuilder.loadFromBinary(stream);
    }

    /**
     * Constructor.
     *
     * @param signerPublicKey Entity signer's public key.
     * @param version Entity version.
     * @param type Entity type.
     * @param duration Namespace duration.
     * @param id Namespace identifier.
     * @param name Namespace name.
     */
    protected EmbeddedNamespaceRegistrationTransactionBuilder(final KeyDto signerPublicKey, final short version, final EntityTypeDto type, final BlockDurationDto duration, final NamespaceIdDto id, final ByteBuffer name) {
        super(signerPublicKey, version, type);
        this.namespaceRegistrationTransactionBody = NamespaceRegistrationTransactionBodyBuilder.create(duration, id, name);
    }

    /**
     * Constructor.
     *
     * @param signerPublicKey Entity signer's public key.
     * @param version Entity version.
     * @param type Entity type.
     * @param parentId Parent namespace identifier.
     * @param id Namespace identifier.
     * @param name Namespace name.
     */
    protected EmbeddedNamespaceRegistrationTransactionBuilder(final KeyDto signerPublicKey, final short version, final EntityTypeDto type, final NamespaceIdDto parentId, final NamespaceIdDto id, final ByteBuffer name) {
        super(signerPublicKey, version, type);
        this.namespaceRegistrationTransactionBody = NamespaceRegistrationTransactionBodyBuilder.create(parentId, id, name);
    }

    /**
     * Creates an instance of EmbeddedNamespaceRegistrationTransactionBuilder.
     *
     * @param signerPublicKey Entity signer's public key.
     * @param version Entity version.
     * @param type Entity type.
     * @param duration Namespace duration.
     * @param id Namespace identifier.
     * @param name Namespace name.
     * @return Instance of EmbeddedNamespaceRegistrationTransactionBuilder.
     */
    public static EmbeddedNamespaceRegistrationTransactionBuilder create(final KeyDto signerPublicKey, final short version, final EntityTypeDto type, final BlockDurationDto duration, final NamespaceIdDto id, final ByteBuffer name) {
        return new EmbeddedNamespaceRegistrationTransactionBuilder(signerPublicKey, version, type, duration, id, name);
    }

    /**
     * Creates an instance of EmbeddedNamespaceRegistrationTransactionBuilder.
     *
     * @param signerPublicKey Entity signer's public key.
     * @param version Entity version.
     * @param type Entity type.
     * @param parentId Parent namespace identifier.
     * @param id Namespace identifier.
     * @param name Namespace name.
     * @return Instance of EmbeddedNamespaceRegistrationTransactionBuilder.
     */
    public static EmbeddedNamespaceRegistrationTransactionBuilder create(final KeyDto signerPublicKey, final short version, final EntityTypeDto type, final NamespaceIdDto parentId, final NamespaceIdDto id, final ByteBuffer name) {
        return new EmbeddedNamespaceRegistrationTransactionBuilder(signerPublicKey, version, type, parentId, id, name);
    }

    /**
     * Gets namespace registration type.
     *
     * @return Namespace registration type.
     */
    public NamespaceRegistrationTypeDto getRegistrationType() {
        return this.namespaceRegistrationTransactionBody.getRegistrationType();
    }

    /**
     * Gets namespace duration.
     *
     * @return Namespace duration.
     */
    public BlockDurationDto getDuration() {
        return this.namespaceRegistrationTransactionBody.getDuration();
    }

    /**
     * Gets parent namespace identifier.
     *
     * @return Parent namespace identifier.
     */
    public NamespaceIdDto getParentId() {
        return this.namespaceRegistrationTransactionBody.getParentId();
    }

    /**
     * Gets namespace identifier.
     *
     * @return Namespace identifier.
     */
    public NamespaceIdDto getId() {
        return this.namespaceRegistrationTransactionBody.getId();
    }

    /**
     * Gets namespace name.
     *
     * @return Namespace name.
     */
    public ByteBuffer getName() {
        return this.namespaceRegistrationTransactionBody.getName();
    }

    /**
     * Gets the size of the object.
     *
     * @return Size in bytes.
     */
    @Override
    public int getSize() {
        int size = super.getSize();
        size += this.namespaceRegistrationTransactionBody.getSize();
        return size;
    }

    /**
     * Creates an instance of EmbeddedNamespaceRegistrationTransactionBuilder from a stream.
     *
     * @param stream Byte stream to use to serialize the object.
     * @return Instance of EmbeddedNamespaceRegistrationTransactionBuilder.
     */
    public static EmbeddedNamespaceRegistrationTransactionBuilder loadFromBinary(final DataInput stream) {
        return new EmbeddedNamespaceRegistrationTransactionBuilder(stream);
    }

    /**
     * Serializes an object to bytes.
     *
     * @return Serialized bytes.
     */
    public byte[] serialize() {
        return GeneratorUtils.serialize(dataOutputStream -> {
            final byte[] superBytes = super.serialize();
            dataOutputStream.write(superBytes, 0, superBytes.length);
            final byte[] namespaceRegistrationTransactionBodyBytes = this.namespaceRegistrationTransactionBody.serialize();
            dataOutputStream.write(namespaceRegistrationTransactionBodyBytes, 0, namespaceRegistrationTransactionBodyBytes.length);
        });
    }
}
