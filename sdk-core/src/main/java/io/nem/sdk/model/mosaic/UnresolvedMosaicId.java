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

package io.nem.sdk.model.mosaic;

import java.math.BigInteger;

/**
 * This interface is used when a NamespaceId can be provided as an alias of a Mosaic Id.
 */
public interface UnresolvedMosaicId {

    /**
     * Gets the MosaicId/NamespaceId as a long number. It may be negative is it's overflowed.
     *
     * @return Long id.
     */
    long getIdAsLong();

    /**
     * Gets the MosaicId/NamespaceId as an hex string.
     *
     * @return the  hex string.
     */
    String getIdAsHex();

    /**
     * Gets the MosaicId/NamespaceId as a {@link BigInteger}.
     *
     * @return Long id.
     */
    BigInteger getId();

    /**
     * @return if the mosaic is an alias (namespace).
     */
    boolean isAlias();
}
