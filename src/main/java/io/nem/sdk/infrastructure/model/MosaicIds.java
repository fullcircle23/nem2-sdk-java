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
/*
 * Catapult REST API Reference
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 0.7.15
 *
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package io.nem.sdk.infrastructure.model;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * MosaicIds
 */
@javax.annotation.Generated(
    value = "org.openapitools.codegen.languages.JavaClientCodegen",
    date = "2019-06-20T19:56:23.892+01:00[Europe/London]")
public class MosaicIds {

    public static final String SERIALIZED_NAME_MOSAIC_IDS = "mosaicIds";

    @SerializedName(SERIALIZED_NAME_MOSAIC_IDS)
    private List<String> mosaicIds = new ArrayList<String>();

    public MosaicIds mosaicIds(List<String> mosaicIds) {
        this.mosaicIds = mosaicIds;
        return this;
    }

    public MosaicIds addMosaicIdsItem(String mosaicIdsItem) {
        if (this.mosaicIds == null) {
            this.mosaicIds = new ArrayList<String>();
        }
        this.mosaicIds.add(mosaicIdsItem);
        return this;
    }

    /**
     * The array of mosaic identifiers.
     *
     * @return mosaicIds
     */
    @ApiModelProperty(example = "[d525ad41d95fcf29]", value = "The array of mosaic identifiers.")
    public List<String> getMosaicIds() {
        return mosaicIds;
    }

    public void setMosaicIds(List<String> mosaicIds) {
        this.mosaicIds = mosaicIds;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MosaicIds mosaicIds = (MosaicIds) o;
        return Objects.equals(this.mosaicIds, mosaicIds.mosaicIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mosaicIds);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MosaicIds {\n");
        sb.append("    mosaicIds: ").append(toIndentedString(mosaicIds)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces (except the first
     * line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
