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

import java.util.List;

import org.flowable.common.engine.impl.persistence.entity.AbstractEngineEntityManager;
import org.flowable.eventregistry.api.runtime.EventInstance;
import org.flowable.eventregistry.impl.EventInstanceQueryImpl;
import org.flowable.eventregistry.impl.EventRegistryEngineConfiguration;
import org.flowable.eventregistry.impl.persistence.entity.data.EventInstanceDataManager;

public class EventInstanceEntityManagerImpl
        extends AbstractEngineEntityManager<EventRegistryEngineConfiguration, EventInstanceEntity, EventInstanceDataManager>
        implements EventInstanceEntityManager {

    public EventInstanceEntityManagerImpl(EventRegistryEngineConfiguration eventRegistryEngineConfiguration, EventInstanceDataManager eventInstanceDataManager) {
        super(eventRegistryEngineConfiguration, eventInstanceDataManager);
    }

    @Override
    public List<EventInstance> findEventInstancesByQueryCriteria(EventInstanceQueryImpl eventInstanceQuery) {
        return dataManager.findEventInstancesByQueryCriteria(eventInstanceQuery);
    }

    @Override
    public long findEventInstanceCountByQueryCriteria(EventInstanceQueryImpl eventInstanceQuery) {
        return dataManager.findEventInstanceCountByQueryCriteria(eventInstanceQuery);
    }

}