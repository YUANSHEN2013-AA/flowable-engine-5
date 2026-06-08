/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flowable.eventregistry.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.flowable.common.engine.impl.interceptor.CommandExecutor;
import org.flowable.eventregistry.api.EventManagementService;
import org.flowable.eventregistry.api.runtime.EventInstance;
import org.flowable.eventregistry.impl.EventRegistryEngine;
import org.flowable.eventregistry.impl.EventRegistryEngineConfiguration;
import org.flowable.eventregistry.impl.persistence.entity.EventInstanceEntityImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class EventInstanceQueryIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:14-alpine"))
            .withDatabaseName("flowable")
            .withUsername("flowable")
            .withPassword("flowable");

    private EventRegistryEngine eventRegistryEngine;
    private EventManagementService managementService;
    private CommandExecutor commandExecutor;

    @BeforeEach
    void setUp() {
        DataSource dataSource = createDataSource();

        EventRegistryEngineConfiguration config = new EventRegistryEngineConfiguration();
        config.setDataSource(dataSource);
        config.setDatabaseSchemaUpdate(EventRegistryEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        eventRegistryEngine = config.buildEventRegistryEngine();
        managementService = eventRegistryEngine.getEventManagementService();
        commandExecutor = config.getCommandExecutor();
    }

    @AfterEach
    void tearDown() {
        if (eventRegistryEngine != null) {
            eventRegistryEngine.close();
        }
    }

    @Test
    void testQueryByEventCategory() {
        saveEventInstance("orderKey", "order", "orderDef", null);
        saveEventInstance("paymentKey", "payment", "paymentDef", null);
        saveEventInstance("anotherOrderKey", "order", "anotherOrderDef", null);

        List<EventInstance> results = managementService.createEventInstanceQuery()
                .eventCategory("order")
                .list();

        assertThat(results).hasSize(2);
        assertThat(results).allMatch(e -> "order".equals(e.getEventCategory()));
    }

    @Test
    void testQueryByEventCategoryLike() {
        saveEventInstance("orderCreated", "order-events", "orderDef", null);
        saveEventInstance("paymentSuccess", "payment-events", "paymentDef", null);
        saveEventInstance("orderCancelled", "order-events", "anotherOrderDef", null);

        List<EventInstance> results = managementService.createEventInstanceQuery()
                .eventCategoryLike("%order%")
                .list();

        assertThat(results).hasSize(2);
        assertThat(results).allMatch(e -> e.getEventCategory().contains("order"));
    }

    @Test
    void testQueryByEventCategoryNotEquals() {
        saveEventInstance("key1", "categoryA", "def1", null);
        saveEventInstance("key2", "categoryB", "def2", null);
        saveEventInstance("key3", "categoryA", "def3", null);
        saveEventInstance("key4", null, "def4", null);

        List<EventInstance> results = managementService.createEventInstanceQuery()
                .eventCategoryNotEquals("categoryA")
                .list();

        assertThat(results).hasSize(2);
        assertThat(results).extracting(EventInstance::getEventCategory).doesNotContain("categoryA");
    }

    @Test
    void testQueryByEventCategoryAndEventKey() {
        saveEventInstance("key1", "categoryA", "def1", null);
        saveEventInstance("key2", "categoryA", "def2", null);
        saveEventInstance("key3", "categoryB", "def3", null);

        List<EventInstance> results = managementService.createEventInstanceQuery()
                .eventCategory("categoryA")
                .eventKey("key1")
                .list();

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getEventKey()).isEqualTo("key1");
        assertThat(results.get(0).getEventCategory()).isEqualTo("categoryA");
    }

    @Test
    void testQueryWithNoCategoryFilter() {
        saveEventInstance("key1", "categoryA", "def1", null);
        saveEventInstance("key2", "categoryB", "def2", null);

        List<EventInstance> results = managementService.createEventInstanceQuery().list();

        assertThat(results).hasSize(2);
    }

    @Test
    void testQueryOrderByEventCategory() {
        saveEventInstance("keyB", "categoryB", "defB", null);
        saveEventInstance("keyA", "categoryA", "defA", null);
        saveEventInstance("keyC", "categoryA", "defC", null);

        List<EventInstance> results = managementService.createEventInstanceQuery()
                .orderByEventCategory()
                .asc()
                .list();

        assertThat(results).hasSize(3);
        assertThat(results.get(0).getEventCategory()).isEqualTo("categoryA");
        assertThat(results.get(1).getEventCategory()).isEqualTo("categoryA");
        assertThat(results.get(2).getEventCategory()).isEqualTo("categoryB");
    }

    @Test
    void testQueryCountByEventCategory() {
        saveEventInstance("key1", "categoryA", "def1", null);
        saveEventInstance("key2", "categoryB", "def2", null);
        saveEventInstance("key3", "categoryA", "def3", null);

        long count = managementService.createEventInstanceQuery()
                .eventCategory("categoryA")
                .count();

        assertThat(count).isEqualTo(2);
    }

    @Test
    void testQueryByTenantId() {
        saveEventInstance("key1", "categoryA", "def1", "tenant1");
        saveEventInstance("key2", "categoryA", "def2", "tenant2");
        saveEventInstance("key3", "categoryB", "def3", "tenant1");

        List<EventInstance> results = managementService.createEventInstanceQuery()
                .tenantId("tenant1")
                .list();

        assertThat(results).hasSize(2);
        assertThat(results).allMatch(e -> "tenant1".equals(e.getTenantId()));
    }

    private void saveEventInstance(String eventKey, String eventCategory, String eventDefinitionKey, String tenantId) {
        commandExecutor.execute(commandContext -> {
            EventInstanceEntityImpl entity = new EventInstanceEntityImpl();
            entity.setId(UUID.randomUUID().toString());
            entity.setEventKey(eventKey);
            entity.setEventCategory(eventCategory);
            entity.setEventDefinitionKey(eventDefinitionKey);
            entity.setTenantId(tenantId);
            org.flowable.eventregistry.impl.util.CommandContextUtil.getEventInstanceEntityManager(commandContext).insert(entity);
            return null;
        });
    }

    private DataSource createDataSource() {
        org.postgresql.ds.PGSimpleDataSource ds = new org.postgresql.ds.PGSimpleDataSource();
        ds.setUrl(postgres.getJdbcUrl());
        ds.setUser(postgres.getUsername());
        ds.setPassword(postgres.getPassword());
        return ds;
    }
}