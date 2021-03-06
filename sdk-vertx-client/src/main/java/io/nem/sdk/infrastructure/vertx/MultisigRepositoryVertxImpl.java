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

package io.nem.sdk.infrastructure.vertx;

import io.nem.sdk.api.MultisigRepository;
import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.account.MultisigAccountGraphInfo;
import io.nem.sdk.model.account.MultisigAccountInfo;
import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.openapi.vertx.api.MultisigRoutesApi;
import io.nem.sdk.openapi.vertx.api.MultisigRoutesApiImpl;
import io.nem.sdk.openapi.vertx.invoker.ApiClient;
import io.nem.sdk.openapi.vertx.model.MultisigAccountGraphInfoDTO;
import io.nem.sdk.openapi.vertx.model.MultisigAccountInfoDTO;
import io.nem.sdk.openapi.vertx.model.MultisigDTO;
import io.reactivex.Observable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MultisigRepositoryVertxImpl extends AbstractRepositoryVertxImpl implements
    MultisigRepository {

    private final MultisigRoutesApi client;

    private final Observable<NetworkType> networkTypeObservable;

    public MultisigRepositoryVertxImpl(ApiClient apiClient,
        Observable<NetworkType> networkTypeObservable) {
        super(apiClient);
        this.client = new MultisigRoutesApiImpl(apiClient);
        this.networkTypeObservable = networkTypeObservable;
    }

    @Override
    public Observable<MultisigAccountInfo> getMultisigAccountInfo(Address address) {
        return exceptionHandling(networkTypeObservable.flatMap(networkType -> call(
            (Handler<AsyncResult<MultisigAccountInfoDTO>> handler) -> getClient()
                .getAccountMultisig(address.plain(), handler))
            .map(MultisigAccountInfoDTO::getMultisig)
            .map(dto -> toMultisigAccountInfo(dto, networkType))));

    }


    @Override
    public Observable<MultisigAccountGraphInfo> getMultisigAccountGraphInfo(Address address) {

        return exceptionHandling(networkTypeObservable.flatMap(networkType -> call(
            (Handler<AsyncResult<List<MultisigAccountGraphInfoDTO>>> handler) -> getClient()
                .getAccountMultisigGraph(address.plain(), handler))
            .map(
                multisigAccountGraphInfoDTOList -> {
                    Map<Integer, List<MultisigAccountInfo>> multisigAccountInfoMap =
                        new HashMap<>();
                    multisigAccountGraphInfoDTOList.forEach(
                        item ->
                            multisigAccountInfoMap.put(
                                item.getLevel(),
                                toMultisigAccountInfo(item, networkType)));
                    return new MultisigAccountGraphInfo(multisigAccountInfoMap);
                })));
    }


    private List<MultisigAccountInfo> toMultisigAccountInfo(MultisigAccountGraphInfoDTO item,
        NetworkType networkType) {
        return item.getMultisigEntries().stream()
            .map(MultisigAccountInfoDTO::getMultisig)
            .map(dto -> toMultisigAccountInfo(dto, networkType))
            .collect(Collectors.toList());
    }


    private MultisigAccountInfo toMultisigAccountInfo(MultisigDTO dto, NetworkType networkType) {
        return new MultisigAccountInfo(
            new PublicAccount(dto.getAccountPublicKey(), networkType),
            dto.getMinApproval(),
            dto.getMinRemoval(),
            dto.getCosignatoryPublicKeys().stream()
                .map(
                    cosigner ->
                        new PublicAccount(
                            cosigner, networkType))
                .collect(Collectors.toList()),
            dto.getMultisigPublicKeys().stream()
                .map(
                    multisigAccount ->
                        new PublicAccount(
                            multisigAccount,
                            networkType))
                .collect(Collectors.toList()));
    }


    public MultisigRoutesApi getClient() {
        return client;
    }
}
