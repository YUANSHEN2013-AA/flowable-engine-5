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
package org.flowable.eventregistry.impl.persistence.entity.data.impl;

import java.util.List;

import org.flowable.eventregistry.api.runtime.EventInstance;
import org.flowable.eventregistry.impl.EventInstanceQueryImpl;
import org.flowable.eventregistry.impl.EventRegistryEngineConfiguration;
import org.flowable.eventregistry.impl.persistence.entity.EventInstanceEntity;
import org.flowable.eventregistry.impl.persistence.entity.EventInstanceEntityImpl;
import org.flowable.eventregistry.impl.persistence.entity.data.AbstractEventDataManager;
import org.flowable.eventregistry.impl.persistence.entity.data.EventInstanceDataManager;

@SuppressWarnings("unchecked")
public class MybatisEventInstanceDataManager extends AbstractEventDataManager<EventInstanceEntity> implements EventInstanceDataManager {

    public MybatisEventInstanceDataManager(EventRegistryEngineConfiguration eventRegistryConfiguration) {
        super(eventRegistryConfiguration);
    }

    @Override
    public Class<? extends EventInstanceEntity> getManagedEntityClass() {
        return EventInstanceEntityImpl.class;
    }

    @Override
    public EventInstanceEntity create() {
        return new EventInstanceEntityImpl();
    }

    @Override
    public List<EventInstance> findEventInstancesByQueryCriteria(EventInstanceQueryImpl eventInstanceQuery) {
        return (List) getDbSqlSession().selectList("selectEventInstancesByQueryCriteria", eventInstanceQuery);
    }

    @Override
    public long findEventInstanceCountByQueryCriteria(EventInstanceQueryImpl eventInstanceQuery) {
        return (Long) getDbSqlSession().selectOne("selectEventInstanceCountByQueryCriteria", eventInstanceQuery);
    }

}