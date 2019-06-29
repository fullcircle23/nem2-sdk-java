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

package io.nem.sdk.model.transaction;

import io.nem.core.crypto.Hashes;
import io.nem.core.crypto.Signature;
import io.nem.core.crypto.Signer;
import io.nem.core.utils.HexEncoder;
import io.nem.sdk.model.account.Account;
import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.blockchain.NetworkType;
import org.apache.commons.lang3.Validate;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Optional;

/**
 * An abstract transaction class that serves as the base class of all NEM transactions.
 *
 * @since 1.0
 */
public abstract class Transaction {
	private final TransactionType type;
	private final NetworkType networkType;
	private final Integer version;
	private final Deadline deadline;
	private final BigInteger fee;
	private final Optional<String> signature;
	private final Optional<TransactionInfo> transactionInfo;
	private Optional<PublicAccount> signer;

	/**
	 * Constructor
	 *
	 * @param type            Transaction type.
	 * @param networkType     Network type.
	 * @param version         Transaction version.
	 * @param deadline        Transaction deadline.
	 * @param fee             Transaction fee.
	 * @param signature       Transaction signature.
	 * @param signer          Transaction signer.
	 * @param transactionInfo Transaction meta data info.
	 */
	public Transaction(TransactionType type, NetworkType networkType, Integer version, Deadline deadline, BigInteger fee,
					   Optional<String> signature, Optional<PublicAccount> signer, Optional<TransactionInfo> transactionInfo) {
		Validate.notNull(type, "Type must not be null");
		Validate.notNull(networkType, "NetworkType must not be null");
		Validate.notNull(version, "Version must not be null");
		Validate.notNull(deadline, "Deadline must not be null");
		Validate.notNull(fee, "Fee must not be null");
		this.type = type;
		this.networkType = networkType;
		this.version = version;
		this.deadline = deadline;
		this.fee = fee;
		this.signature = signature;
		this.signer = signer;
		this.transactionInfo = transactionInfo;
	}

	/**
	 * Generates hash for a serialized transaction payload.
	 *
	 * @param transactionPayload Transaction payload
	 * @return generated transaction hash.
	 */
	public static String createTransactionHash(String transactionPayload, final byte[] generationhashBytes) {
		byte[] bytes = Hex.decode(transactionPayload);
		byte[] signingBytes = new byte[bytes.length + generationhashBytes.length - 36];
		System.arraycopy(bytes, 4, signingBytes, 0, 32);
		System.arraycopy(bytes, 68, signingBytes, 32, 32);
		System.arraycopy(generationhashBytes, 0, signingBytes, 64, generationhashBytes.length);
		System.arraycopy(bytes, 100, signingBytes, generationhashBytes.length + 64, bytes.length - 100);

		byte[] result = Hashes.sha3_256(signingBytes);
		return Hex.toHexString(result).toUpperCase();
	}

	/**
	 * Returns the transaction type.
	 *
	 * @return transaction type
	 */
	public TransactionType getType() {
		return type;
	}

	/**
	 * Returns the network type.
	 *
	 * @return the network type
	 */
	public NetworkType getNetworkType() {
		return networkType;
	}

	/**
	 * Returns the transaction version.
	 *
	 * @return transaction version
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * Returns the deadline to include the transaction.
	 *
	 * @return deadline to include transaction into a block.
	 */
	public Deadline getDeadline() {
		return deadline;
	}

	/**
	 * Returns the fee for the transaction. The higher the fee, the higher the priority of the transaction.
	 * Transactions with high priority get included in a block before transactions with lower priority.
	 *
	 * @return fee amount
	 */
	public BigInteger getFee() {
		return fee;
	}

	/**
	 * Returns the transaction signature (missing if part of an aggregate transaction).
	 *
	 * @return transaction signature
	 */
	public Optional<String> getSignature() {
		return signature;
	}

	/**
	 * Returns the transaction creator public account.
	 *
	 * @return signer public account
	 */
	public Optional<PublicAccount> getSigner() {
		return signer;
	}

	/**
	 * Returns meta data object contains additional information about the transaction.
	 *
	 * @return transaction meta data info.
	 */
	public Optional<TransactionInfo> getTransactionInfo() {
		return transactionInfo;
	}

	/**
	 * @return
	 */
	abstract byte[] generateBytes();

	/**
	 * Geneterate the
	 *
	 * @return
	 */
	abstract byte[] generateEmbeddedBytes();

	/**
	 * Serialize and sign transaction creating a new SignedTransaction.
	 *
	 * @param account        The account to sign the transaction.
	 * @param generationHash The generation hash for the network.
	 * @return {@link SignedTransaction}
	 */
	public SignedTransaction signWith(final Account account, final String generationHash) {

		final Signer signer = new Signer(account.getKeyPair());
		final byte[] bytes = this.generateBytes();
		final byte[] generationHashBytes = HexEncoder.getBytes(generationHash);
		final byte[] signingBytes = new byte[bytes.length + generationHashBytes.length - 100];
		System.arraycopy(generationHashBytes, 0, signingBytes, 0, generationHashBytes.length);
		System.arraycopy(bytes, 100, signingBytes, generationHashBytes.length, bytes.length - 100);
		final Signature signature = signer.sign(signingBytes);

		final byte[] payload = new byte[bytes.length];
		System.arraycopy(bytes, 0, payload, 0, 4); // Size
		System.arraycopy(signature.getBytes(), 0, payload, 4, signature.getBytes().length); // Signature
		System.arraycopy(account.getKeyPair().getPublicKey().getBytes(), 0, payload, 64 + 4,
				account.getKeyPair().getPublicKey().getBytes().length); // Signer
		System.arraycopy(bytes, 100, payload, 100, bytes.length - 100);

		final String hash = Transaction.createTransactionHash(Hex.toHexString(payload), generationHashBytes);
		return new SignedTransaction(Hex.toHexString(payload).toUpperCase(), hash, type);
	}

	/**
	 * Takes a transaction and formats bytes to be included in an aggregate transaction.
	 *
	 * @return transaction with signer serialized to be part of an aggregate transaction
	 */
	byte[] toAggregateTransactionBytes() {
		return this.generateEmbeddedBytes();
	}

	/**
	 * Convert an aggregate transaction to an inner transaction including transaction signer.
	 *
	 * @param signer Transaction signer.
	 * @return instance of Transaction with signer
	 */
	public Transaction toAggregate(final PublicAccount signer) {
		this.signer = Optional.of(signer);
		return this;
	}

	/**
	 * Returns if a transaction is pending to be included in a block.
	 *
	 * @return if a transaction is pending to be included in a block
	 */
	public boolean isUnconfirmed() {
		return this.transactionInfo.isPresent() && this.transactionInfo.get().getHeight().equals(BigInteger.valueOf(0)) &&
				this.transactionInfo.get().getHash().equals(this.transactionInfo.get().getMerkleComponentHash());
	}

	/**
	 * Return if a transaction is included in a block.
	 *
	 * @return if a transaction is included in a block
	 */
	public boolean isConfirmed() {
		return this.transactionInfo.isPresent() && this.transactionInfo.get().getHeight().intValue() > 0;
	}

	/**
	 * Returns if a transaction has missing signatures.
	 *
	 * @return if a transaction has missing signatures
	 */
	public boolean hasMissingSignatures() {
		return this.transactionInfo.isPresent() && this.transactionInfo.get().getHeight().equals(BigInteger.valueOf(0)) &&
				!this.transactionInfo.get().getHash().equals(this.transactionInfo.get().getMerkleComponentHash());
	}

	/**
	 * Returns if a transaction is not known by the network.
	 *
	 * @return if a transaction is not known by the network
	 */
	public boolean isUnannounced() {
		return !this.transactionInfo.isPresent();
	}

	/**
	 * Gets the version of the transaction to send to the server.
	 *
	 * @return Version of the transaction
	 */
	protected short getNetworkVersion() {
		return (short) Long.parseLong(
				Integer.toHexString(getNetworkType().getValue()) + "0" + Integer.toHexString(getVersion()), 16);
	}

	/**
	 * Returns the transaction signature (missing if part of an aggregate transaction).
	 *
	 * @return transaction signature
	 */
	public Optional<String> getSignatureBytes() {
		return signature;
	}

	/**
	 * Returns the transaction creator public account.
	 *
	 * @return signer public account
	 */
	protected Optional<ByteBuffer> getSignerBytes() {
		if (signer.isPresent()) {
			final byte[] bytes = signer.get().getPublicKey().getBytes();
			return Optional.of(ByteBuffer.wrap(bytes));
		}
		return Optional.empty();
	}

}
