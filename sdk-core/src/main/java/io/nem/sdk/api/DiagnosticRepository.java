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

package io.nem.sdk.api;

import io.nem.sdk.model.blockchain.BlockchainStorageInfo;
import io.nem.sdk.model.blockchain.ServerInfo;
import io.reactivex.Observable;

public interface DiagnosticRepository {

    /**
     * Gets blockchain storage info.
     *
     * @return Observable of {@link BlockchainStorageInfo}
     */
    Observable<BlockchainStorageInfo> getBlockchainStorage();

    /**
     * Gets blockchain server info.
     *
     * @return {@link Observable} of ServerInfo
     */
    Observable<ServerInfo> getServerInfo();
}
