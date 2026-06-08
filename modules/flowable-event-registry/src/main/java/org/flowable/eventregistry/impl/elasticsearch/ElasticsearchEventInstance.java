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
package org.flowable.eventregistry.impl.elasticsearch;

import org.flowable.eventregistry.api.runtime.EventInstance;

public class ElasticsearchEventInstance {

    protected String id;
    protected String eventKey;
    protected String eventCategory;
    protected String eventDefinitionKey;
    protected String tenantId;

    public ElasticsearchEventInstance() {
    }

    public ElasticsearchEventInstance(EventInstance eventInstance) {
        this.eventKey = eventInstance.getEventKey();
        this.eventCategory = eventInstance.getEventCategory();
        this.tenantId = eventInstance.getTenantId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public String getEventDefinitionKey() {
        return eventDefinitionKey;
    }

    public void setEventDefinitionKey(String eventDefinitionKey) {
        this.eventDefinitionKey = eventDefinitionKey;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}