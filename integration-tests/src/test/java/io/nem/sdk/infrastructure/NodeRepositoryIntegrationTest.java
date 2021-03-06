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

package io.nem.sdk.infrastructure;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.nem.sdk.api.NodeRepository;
import io.nem.sdk.model.node.NodeInfo;
import io.nem.sdk.model.node.NodeTime;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NodeRepositoryIntegrationTest extends BaseIntegrationTest {

    @ParameterizedTest
    @EnumSource(RepositoryType.class)
    void getNodeInfo(RepositoryType type) {
        NodeInfo nodeInfo = get(getNodeRepository(type).getNodeInfo());

        assertTrue(!nodeInfo.getPublicKey().equals(""));
        assertNotNull(nodeInfo.getHost());
        assertTrue(nodeInfo.getPort() > 0);
        assertTrue(nodeInfo.getNetworkIdentifier().getValue() > 0);
    }

    private NodeRepository getNodeRepository(RepositoryType type) {
        return getRepositoryFactory(type).createNodeRepository();
    }

    @ParameterizedTest
    @EnumSource(RepositoryType.class)
    void getNodeTime(RepositoryType type) {
        NodeTime nodeTime = get(getNodeRepository(type).getNodeTime());
        assertTrue(nodeTime.getReceiveTimestamp().longValue() > 0);
        assertTrue(nodeTime.getSendTimestamp().longValue() > 0);
    }
}
