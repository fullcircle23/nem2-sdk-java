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
import java.util.Objects;

/**
 * StorageInfoDTO
 */
@javax.annotation.Generated(
    value = "org.openapitools.codegen.languages.JavaClientCodegen",
    date = "2019-06-20T19:56:23.892+01:00[Europe/London]")
public class StorageInfoDTO {

    public static final String SERIALIZED_NAME_NUM_BLOCKS = "numBlocks";
    public static final String SERIALIZED_NAME_NUM_TRANSACTIONS = "numTransactions";
    public static final String SERIALIZED_NAME_NUM_ACCOUNTS = "numAccounts";
    @SerializedName(SERIALIZED_NAME_NUM_BLOCKS)
    private Integer numBlocks;
    @SerializedName(SERIALIZED_NAME_NUM_TRANSACTIONS)
    private Integer numTransactions;
    @SerializedName(SERIALIZED_NAME_NUM_ACCOUNTS)
    private Integer numAccounts;

    public StorageInfoDTO numBlocks(Integer numBlocks) {
        this.numBlocks = numBlocks;
        return this;
    }

    /**
     * The number of blocks stored.
     *
     * @return numBlocks
     */
    @ApiModelProperty(example = "245053", required = true, value = "The number of blocks stored.")
    public Integer getNumBlocks() {
        return numBlocks;
    }

    public void setNumBlocks(Integer numBlocks) {
        this.numBlocks = numBlocks;
    }

    public StorageInfoDTO numTransactions(Integer numTransactions) {
        this.numTransactions = numTransactions;
        return this;
    }

    /**
     * The number of transactions stored.
     *
     * @return numTransactions
     */
    @ApiModelProperty(
        example = "58590",
        required = true,
        value = "The number of transactions stored.")
    public Integer getNumTransactions() {
        return numTransactions;
    }

    public void setNumTransactions(Integer numTransactions) {
        this.numTransactions = numTransactions;
    }

    public StorageInfoDTO numAccounts(Integer numAccounts) {
        this.numAccounts = numAccounts;
        return this;
    }

    /**
     * The number of accounts created.
     *
     * @return numAccounts
     */
    @ApiModelProperty(example = "177", required = true, value = "The number of accounts created.")
    public Integer getNumAccounts() {
        return numAccounts;
    }

    public void setNumAccounts(Integer numAccounts) {
        this.numAccounts = numAccounts;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StorageInfoDTO storageInfoDTO = (StorageInfoDTO) o;
        return Objects.equals(this.numBlocks, storageInfoDTO.numBlocks)
            && Objects.equals(this.numTransactions, storageInfoDTO.numTransactions)
            && Objects.equals(this.numAccounts, storageInfoDTO.numAccounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numBlocks, numTransactions, numAccounts);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class StorageInfoDTO {\n");
        sb.append("    numBlocks: ").append(toIndentedString(numBlocks)).append("\n");
        sb.append("    numTransactions: ").append(toIndentedString(numTransactions)).append("\n");
        sb.append("    numAccounts: ").append(toIndentedString(numAccounts)).append("\n");
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
