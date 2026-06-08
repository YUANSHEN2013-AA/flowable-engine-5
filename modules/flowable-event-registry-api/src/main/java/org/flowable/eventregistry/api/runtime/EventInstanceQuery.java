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
package org.flowable.eventregistry.api.runtime;

import org.flowable.common.engine.api.query.Query;

public interface EventInstanceQuery extends Query<EventInstanceQuery, EventInstance> {

    EventInstanceQuery eventInstanceId(String eventInstanceId);

    EventInstanceQuery eventKey(String eventKey);

    EventInstanceQuery eventKeyLike(String eventKeyLike);

    EventInstanceQuery eventKeyLikeIgnoreCase(String eventKeyLikeIgnoreCase);

    EventInstanceQuery eventDefinitionKey(String eventDefinitionKey);

    EventInstanceQuery eventCategory(String eventCategory);

    EventInstanceQuery eventCategoryLike(String eventCategoryLike);

    EventInstanceQuery eventCategoryNotEquals(String eventCategoryNotEquals);

    EventInstanceQuery tenantId(String tenantId);

    EventInstanceQuery tenantIdLike(String tenantIdLike);

    EventInstanceQuery withoutTenantId();

    EventInstanceQuery orderByEventCategory();

    EventInstanceQuery orderByEventKey();

    EventInstanceQuery orderByTenantId();

}