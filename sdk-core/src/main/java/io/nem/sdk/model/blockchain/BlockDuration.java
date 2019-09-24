/*
 * Copyright 2019. NEM
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.nem.sdk.model.blockchain;

import java.math.BigInteger;

public class BlockDuration {
    /**
     * The duration in blocks a mosaic will be available. After the duration finishes mosaic is
     * inactive and can be renewed. Duration is required when defining the mosaic
     */
    private final long duration;


    public BlockDuration(long duration) {
        this.duration = duration;
    }

    public BlockDuration(BigInteger duration) {
        this.duration = duration.longValue();
    }

    /**
     * Returns the number of blocks from height it will be active
     *
     * @return the number of blocks from height it will be active
     */
    public long getDuration() {
        return duration;
    }
}