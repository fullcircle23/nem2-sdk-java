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

package io.nem.sdk.infrastructure.vertx;

import static io.nem.core.utils.MapperUtils.toMosaicId;

import io.nem.sdk.api.MosaicRepository;
import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.account.PublicAccount;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.MosaicFlags;
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.mosaic.MosaicInfo;
import io.nem.sdk.openapi.vertx.api.MosaicRoutesApi;
import io.nem.sdk.openapi.vertx.api.MosaicRoutesApiImpl;
import io.nem.sdk.openapi.vertx.invoker.ApiClient;
import io.nem.sdk.openapi.vertx.model.AccountIds;
import io.nem.sdk.openapi.vertx.model.MosaicDTO;
import io.nem.sdk.openapi.vertx.model.MosaicIds;
import io.nem.sdk.openapi.vertx.model.MosaicInfoDTO;
import io.nem.sdk.openapi.vertx.model.MosaicsInfoDTO;
import io.reactivex.Observable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Mosaic http repository.
 *
 * @since 1.0
 */
public class MosaicRepositoryVertxImpl extends AbstractRepositoryVertxImpl implements
    MosaicRepository {

    private final MosaicRoutesApi client;

    private final Observable<NetworkType> networkTypeObservable;

    public MosaicRepositoryVertxImpl(ApiClient apiClient,
        Observable<NetworkType> networkTypeObservable) {
        super(apiClient);
        this.client = new MosaicRoutesApiImpl(apiClient);
        this.networkTypeObservable = networkTypeObservable;
    }

    public MosaicRoutesApi getClient() {
        return client;
    }

    @Override
    public Observable<MosaicInfo> getMosaic(MosaicId mosaicId) {

        Consumer<Handler<AsyncResult<MosaicInfoDTO>>> callback = handler -> getClient()
            .getMosaic(mosaicId.getIdAsHex(), handler);
        return exceptionHandling(networkTypeObservable.flatMap(networkType -> call(callback).map(
            mosaicInfoDTO -> createMosaicInfo(mosaicInfoDTO, networkType))));
    }

    @Override
    public Observable<List<MosaicInfo>> getMosaics(List<MosaicId> ids) {

        MosaicIds mosaicIds = new MosaicIds();
        mosaicIds.mosaicIds(ids.stream()
            .map(MosaicId::getIdAsHex)
            .collect(Collectors.toList()));
        Consumer<Handler<AsyncResult<List<MosaicInfoDTO>>>> callback = handler -> getClient()
            .getMosaics(mosaicIds, handler);
        return exceptionHandling(networkTypeObservable.flatMap(networkType ->
            call(callback).flatMapIterable(item -> item).map(
                mosaicInfoDTO -> createMosaicInfo(mosaicInfoDTO, networkType))
                .toList()
                .toObservable()));
    }

    @Override
    public Observable<List<MosaicInfo>> getMosaicsFromAccount(Address address) {
        Consumer<Handler<AsyncResult<MosaicsInfoDTO>>> callback = handler -> getClient()
            .getMosaicsFromAccount(address.plain(), handler);

        return exceptionHandling(networkTypeObservable.flatMap(networkType ->
            call(callback).map(MosaicsInfoDTO::getMosaics).flatMapIterable(item -> item).map(
                mosaicInfoDTO -> createMosaicInfo(mosaicInfoDTO, networkType))
                .toList().toObservable()));
    }

    @Override
    public Observable<List<MosaicInfo>> getMosaicsFromAccounts(List<Address> addresses) {
        AccountIds accountIds = new AccountIds()
            .addresses(addresses.stream().map(Address::plain).collect(Collectors.toList()));
        Consumer<Handler<AsyncResult<MosaicsInfoDTO>>> callback = handler -> getClient()
            .getMosaicsFromAccounts(accountIds, handler);
        return exceptionHandling(networkTypeObservable.flatMap(networkType ->
            call(callback).map(MosaicsInfoDTO::getMosaics).flatMapIterable(item -> item).map(
                mosaicInfoDTO -> createMosaicInfo(mosaicInfoDTO, networkType))
                .toList().toObservable()));
    }


    private MosaicInfo createMosaicInfo(MosaicInfoDTO mosaicInfoDTO, NetworkType networkType) {
        return createMosaicInfo(mosaicInfoDTO.getMosaic(), networkType);
    }

    private MosaicInfo createMosaicInfo(MosaicDTO mosaic, NetworkType networkType) {
        return MosaicInfo.create(
            toMosaicId(mosaic.getId()),
            mosaic.getSupply(),
            mosaic.getStartHeight(),
            new PublicAccount(mosaic.getOwnerPublicKey(), networkType),
            mosaic.getRevision(),
            extractMosaicFlags(mosaic),
            mosaic.getDivisibility(),
            mosaic.getDuration());
    }

    private static MosaicFlags extractMosaicFlags(MosaicDTO mosaicDTO) {
        return MosaicFlags.create(mosaicDTO.getFlags());
    }
}
