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
package org.flowable.eventregistry.impl.persistence.entity;

import java.io.Serializable;
import java.util.Collection;

import org.flowable.eventregistry.api.runtime.EventPayloadInstance;
import org.flowable.eventregistry.impl.EventRegistryEngineConfiguration;

public class EventInstanceEntityImpl extends AbstractEventRegistryNoRevisionEntity implements EventInstanceEntity, Serializable {

    private static final long serialVersionUID = 1L;

    protected String eventKey;
    protected String eventCategory;
    protected String eventDefinitionKey;
    protected String tenantId = EventRegistryEngineConfiguration.NO_TENANT_ID;

    @Override
    public String getEventKey() {
        return eventKey;
    }

    @Override
    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    @Override
    public String getEventCategory() {
        return eventCategory;
    }

    @Override
    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    @Override
    public String getEventDefinitionKey() {
        return eventDefinitionKey;
    }

    public void setEventDefinitionKey(String eventDefinitionKey) {
        this.eventDefinitionKey = eventDefinitionKey;
    }

    @Override
    public Collection<EventPayloadInstance> getPayloadInstances() {
        return null;
    }

    @Override
    public Collection<EventPayloadInstance> getHeaderInstances() {
        return null;
    }

    @Override
    public Collection<EventPayloadInstance> getCorrelationParameterInstances() {
        return null;
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }

    @Override
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public Object getPersistentState() {
        return eventCategory;
    }
}