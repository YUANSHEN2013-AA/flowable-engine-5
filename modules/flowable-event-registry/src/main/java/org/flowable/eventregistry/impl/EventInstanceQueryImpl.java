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
package org.flowable.eventregistry.impl;

import java.util.List;

import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.common.engine.impl.interceptor.CommandExecutor;
import org.flowable.common.engine.impl.query.AbstractQuery;
import org.flowable.eventregistry.api.runtime.EventInstance;
import org.flowable.eventregistry.api.runtime.EventInstanceQuery;
import org.flowable.eventregistry.impl.util.CommandContextUtil;

public class EventInstanceQueryImpl extends AbstractQuery<EventInstanceQuery, EventInstance> implements EventInstanceQuery {

    private static final long serialVersionUID = 1L;
    protected String id;
    protected String eventKey;
    protected String eventKeyLike;
    protected String eventKeyLikeIgnoreCase;
    protected String eventDefinitionKey;
    protected String eventCategory;
    protected String eventCategoryLike;
    protected String eventCategoryNotEquals;
    protected String tenantId;
    protected String tenantIdLike;
    protected boolean withoutTenantId;

    public EventInstanceQueryImpl() {
    }

    public EventInstanceQueryImpl(CommandContext commandContext) {
        super(commandContext);
    }

    public EventInstanceQueryImpl(CommandExecutor commandExecutor) {
        super(commandExecutor);
    }

    @Override
    public EventInstanceQueryImpl eventInstanceId(String eventInstanceId) {
        if (eventInstanceId == null) {
            throw new FlowableIllegalArgumentException("eventInstanceId is null");
        }
        this.id = eventInstanceId;
        return this;
    }

    @Override
    public EventInstanceQueryImpl eventKey(String eventKey) {
        if (eventKey == null) {
            throw new FlowableIllegalArgumentException("eventKey is null");
        }
        this.eventKey = eventKey;
        return this;
    }

    @Override
    public EventInstanceQueryImpl eventKeyLike(String eventKeyLike) {
        if (eventKeyLike == null) {
            throw new FlowableIllegalArgumentException("eventKeyLike is null");
        }
        this.eventKeyLike = eventKeyLike;
        return this;
    }

    @Override
    public EventInstanceQueryImpl eventKeyLikeIgnoreCase(String eventKeyLikeIgnoreCase) {
        if (eventKeyLikeIgnoreCase == null) {
            throw new FlowableIllegalArgumentException("eventKeyLikeIgnoreCase is null");
        }
        this.eventKeyLikeIgnoreCase = eventKeyLikeIgnoreCase;
        return this;
    }

    @Override
    public EventInstanceQueryImpl eventDefinitionKey(String eventDefinitionKey) {
        if (eventDefinitionKey == null) {
            throw new FlowableIllegalArgumentException("eventDefinitionKey is null");
        }
        this.eventDefinitionKey = eventDefinitionKey;
        return this;
    }

    @Override
    public EventInstanceQueryImpl eventCategory(String eventCategory) {
        if (eventCategory == null) {
            throw new FlowableIllegalArgumentException("eventCategory is null");
        }
        this.eventCategory = eventCategory;
        return this;
    }

    @Override
    public EventInstanceQueryImpl eventCategoryLike(String eventCategoryLike) {
        if (eventCategoryLike == null) {
            throw new FlowableIllegalArgumentException("eventCategoryLike is null");
        }
        this.eventCategoryLike = eventCategoryLike;
        return this;
    }

    @Override
    public EventInstanceQueryImpl eventCategoryNotEquals(String eventCategoryNotEquals) {
        if (eventCategoryNotEquals == null) {
            throw new FlowableIllegalArgumentException("eventCategoryNotEquals is null");
        }
        this.eventCategoryNotEquals = eventCategoryNotEquals;
        return this;
    }

    @Override
    public EventInstanceQueryImpl tenantId(String tenantId) {
        if (tenantId == null) {
            throw new FlowableIllegalArgumentException("tenantId is null");
        }
        this.tenantId = tenantId;
        return this;
    }

    @Override
    public EventInstanceQueryImpl tenantIdLike(String tenantIdLike) {
        if (tenantIdLike == null) {
            throw new FlowableIllegalArgumentException("tenantIdLike is null");
        }
        this.tenantIdLike = tenantIdLike;
        return this;
    }

    @Override
    public EventInstanceQueryImpl withoutTenantId() {
        this.withoutTenantId = true;
        return this;
    }

    @Override
    public EventInstanceQueryImpl orderByEventCategory() {
        return (EventInstanceQueryImpl) orderBy(EventInstanceQueryProperty.CATEGORY);
    }

    @Override
    public EventInstanceQueryImpl orderByEventKey() {
        return (EventInstanceQueryImpl) orderBy(EventInstanceQueryProperty.EVENT_KEY);
    }

    @Override
    public EventInstanceQueryImpl orderByTenantId() {
        return (EventInstanceQueryImpl) orderBy(EventInstanceQueryProperty.TENANT_ID);
    }

    @Override
    public long executeCount(CommandContext commandContext) {
        return CommandContextUtil.getEventInstanceEntityManager(commandContext).findEventInstanceCountByQueryCriteria(this);
    }

    @Override
    public List<EventInstance> executeList(CommandContext commandContext) {
        return CommandContextUtil.getEventInstanceEntityManager(commandContext).findEventInstancesByQueryCriteria(this);
    }

    public String getId() {
        return id;
    }

    public String getEventKey() {
        return eventKey;
    }

    public String getEventKeyLike() {
        return eventKeyLike;
    }

    public String getEventKeyLikeIgnoreCase() {
        return eventKeyLikeIgnoreCase;
    }

    public String getEventDefinitionKey() {
        return eventDefinitionKey;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public String getEventCategoryLike() {
        return eventCategoryLike;
    }

    public String getEventCategoryNotEquals() {
        return eventCategoryNotEquals;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getTenantIdLike() {
        return tenantIdLike;
    }

    public boolean isWithoutTenantId() {
        return withoutTenantId;
    }
}