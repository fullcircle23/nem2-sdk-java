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

import io.nem.sdk.api.TransactionRepository;
import io.nem.sdk.model.transaction.CosignatureSignedTransaction;
import io.nem.sdk.model.transaction.Deadline;
import io.nem.sdk.model.transaction.SignedTransaction;
import io.nem.sdk.model.transaction.Transaction;
import io.nem.sdk.model.transaction.TransactionAnnounceResponse;
import io.nem.sdk.model.transaction.TransactionStatus;
import io.nem.sdk.openapi.okhttp_gson.api.TransactionRoutesApi;
import io.nem.sdk.openapi.okhttp_gson.invoker.ApiCallback;
import io.nem.sdk.openapi.okhttp_gson.invoker.ApiClient;
import io.nem.sdk.openapi.okhttp_gson.model.AnnounceTransactionInfoDTO;
import io.nem.sdk.openapi.okhttp_gson.model.Cosignature;
import io.nem.sdk.openapi.okhttp_gson.model.TransactionHashes;
import io.nem.sdk.openapi.okhttp_gson.model.TransactionIds;
import io.nem.sdk.openapi.okhttp_gson.model.TransactionInfoDTO;
import io.nem.sdk.openapi.okhttp_gson.model.TransactionPayload;
import io.nem.sdk.openapi.okhttp_gson.model.TransactionStatusDTO;
import io.reactivex.Observable;
import java.util.List;

/**
 * Transaction http repository.
 *
 * @since 1.0
 */
public class TransactionRepositoryOkHttpImpl extends AbstractRepositoryOkHttpImpl implements
    TransactionRepository {

    private final TransactionRoutesApi client;

    public TransactionRepositoryOkHttpImpl(ApiClient apiClient) {
        super(apiClient);
        client = new TransactionRoutesApi(apiClient);
    }


    public TransactionRoutesApi getClient() {
        return client;
    }

    @Override
    public Observable<Transaction> getTransaction(String transactionHash) {
        ApiCall<ApiCallback<TransactionInfoDTO>> callback = handler -> getClient()
            .getTransactionAsync(transactionHash, handler);
        return exceptionHandling(call(callback).map(this::toTransaction));
    }

    private Transaction toTransaction(TransactionInfoDTO input) {
        return new TransactionMappingOkHttp(getJsonHelper()).apply(input);
    }

    @Override
    public Observable<List<Transaction>> getTransactions(List<String> transactionHashes) {
        ApiCall<ApiCallback<List<TransactionInfoDTO>>> callback = (handler) ->
            client.getTransactionsAsync(new TransactionIds().transactionIds(transactionHashes),
                handler);
        return exceptionHandling(
            call(callback).flatMapIterable(item -> item).map(this::toTransaction).toList()
                .toObservable());


    }

    @Override
    public Observable<TransactionStatus> getTransactionStatus(String transactionHash) {
        ApiCall<ApiCallback<TransactionStatusDTO>> callback = handler -> getClient()
            .getTransactionStatusAsync(transactionHash, handler);
        return exceptionHandling(call(callback).map(this::toTransactionStatus));
    }

    private TransactionStatus toTransactionStatus(TransactionStatusDTO transactionStatusDTO) {
        return new TransactionStatus(
            transactionStatusDTO.getGroup(),
            transactionStatusDTO.getStatus(),
            transactionStatusDTO.getHash(),
            new Deadline(extractIntArray(transactionStatusDTO.getDeadline())),
            extractIntArray(transactionStatusDTO.getHeight()));
    }

    @Override
    public Observable<List<TransactionStatus>> getTransactionStatuses(
        List<String> transactionHashes) {
        ApiCall<ApiCallback<List<TransactionStatusDTO>>> callback = (handler) ->
            client.getTransactionsStatusesAsync(new TransactionHashes().hashes(transactionHashes),
                handler);
        return exceptionHandling(
            call(callback).flatMapIterable(item -> item).map(this::toTransactionStatus).toList()
                .toObservable());

    }

    @Override
    public Observable<TransactionAnnounceResponse> announce(SignedTransaction signedTransaction) {

        ApiCall<ApiCallback<AnnounceTransactionInfoDTO>> callback = handler -> getClient()
            .announceTransactionAsync(
                new TransactionPayload().payload(signedTransaction.getPayload()),
                handler);
        return exceptionHandling(
            call(callback).map(dto -> new TransactionAnnounceResponse(dto.getMessage())));
    }

    @Override
    public Observable<TransactionAnnounceResponse> announceAggregateBonded(
        SignedTransaction signedTransaction) {
        ApiCall<ApiCallback<AnnounceTransactionInfoDTO>> callback = handler -> getClient()
            .announcePartialTransactionAsync(
                new TransactionPayload().payload(signedTransaction.getPayload()),
                handler);
        return exceptionHandling(
            call(callback).map(dto -> new TransactionAnnounceResponse(dto.getMessage())));
    }

    @Override
    public Observable<TransactionAnnounceResponse> announceAggregateBondedCosignature(
        CosignatureSignedTransaction cosignatureSignedTransaction) {

        ApiCall<ApiCallback<AnnounceTransactionInfoDTO>> callback = handler -> getClient()
            .announceCosignatureTransactionAsync(
                new Cosignature().parentHash(cosignatureSignedTransaction.getParentHash())
                    .signature(cosignatureSignedTransaction.getSignature())
                    .signature(cosignatureSignedTransaction.getSigner()),
                handler);
        return exceptionHandling(
            call(callback).map(dto -> new TransactionAnnounceResponse(dto.getMessage())));


    }
}
