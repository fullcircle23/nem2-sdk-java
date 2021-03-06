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

package io.nem.sdk.api;

import io.nem.sdk.model.blockchain.NetworkInfo;
import io.nem.sdk.model.blockchain.NetworkType;
import io.reactivex.Observable;

/**
 * Network interface repository.
 *
 * @since 1.0
 */
public interface NetworkRepository {

    /**
     * Get current network type.
     *
     * @return network type enum.
     */
    Observable<NetworkType> getNetworkType();

    /**
     * @return the network information with like the network's name and description.
     */
    Observable<NetworkInfo> getNetworkInfo();
}
