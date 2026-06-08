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

package org.flowable.cmmn.rest.service.api.runtime;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.flowable.cmmn.api.repository.CaseDefinition;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.flowable.cmmn.engine.test.CmmnDeployment;
import org.flowable.cmmn.rest.service.BaseSpringRestTestCase;
import org.flowable.cmmn.rest.service.api.CmmnRestUrls;
import org.flowable.eventregistry.api.EventDeployment;
import org.flowable.eventregistry.api.EventRepositoryService;
import org.flowable.eventsubscription.api.EventSubscription;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import tools.jackson.databind.JsonNode;

import net.javacrumbs.jsonunit.core.Option;

public class EventSubscriptionResourceTest extends BaseSpringRestTestCase {

    @Autowired
    protected EventRepositoryService eventRepositoryService;

    @Test
    @CmmnDeployment(resources = { "org/flowable/cmmn/rest/service/api/runtime/signalEventListener.cmmn" })
    public void testQueryEventSubscriptions() throws Exception {
        Calendar hourAgo = Calendar.getInstance();
        hourAgo.add(Calendar.HOUR, -1);

        Calendar inAnHour = Calendar.getInstance();
        inAnHour.add(Calendar.HOUR, 1);
        
        CaseInstance caseInstance = runtimeService.createCaseInstanceBuilder().caseDefinitionKey("testSimpleEnableTask").start();

        CaseDefinition caseDefinition = repositoryService.createCaseDefinitionQuery().singleResult();

        EventSubscription eventSubscription = runtimeService.createEventSubscriptionQuery().singleResult();

        String url = CmmnRestUrls.createRelativeResourceUrl(CmmnRestUrls.URL_EVENT_SUBSCRIPTION_COLLECTION);
        assertResultsPresentInDataResponse(url, eventSubscription.getId());

        url = CmmnRestUrls.createRelativeResourceUrl(CmmnRestUrls.URL_EVENT_SUBSCRIPTION_COLLECTION) + "?eventType=signal";
        assertResultsPresentInDataResponse(url, eventSubscription.getId());

        url = CmmnRestUrls.createRelativeResourceUrl(CmmnRestUrls.URL_EVENT_SUBSCRIPTION_COLLECTION) + "?eventName=testSignal";
        assertResultsPresentInDataResponse(url, eventSubscription.getId());

        url = CmmnRestUrls.createRelativeResourceUrl(CmmnRestUrls.URL_EVENT_SUBSCRIPTION_COLLECTION) + "?withoutProcessInstanceId=true";
        assertResultsPresentInDataResponse(url, eventSubscription.getId());
        
        url = CmmnRestUrls.createRelativeResourceUrl(CmmnRestUrls.URL_EVENT_SUBSCRIPTION_COLLECTION) + "?withoutProcessDefinitionId=true";
        assertResultsPresentInDataResponse(url, eventSubscription.getId());
        
        url = CmmnRestUrls.createRelativeResourceUrl(CmmnRestUrls.URL_EVENT_SUBSCRIPTION_COLLECTION) + "?withoutScopeId=true";
        assertResultsPresentInDataResponse(url);
        
        url = CmmnRestUrls.createRelativeResourceUrl(CmmnRestUrls.URL_EVENT_SUBSCRIPTION_COLLECTION) + "?withoutScopeDefinitionId=true";
        assertResultsPresentInDataResponse(url);
        
        url = CmmnRestUrls.createRelativeResourceUrl(CmmnRestUrls.URL_EVENT_SUBSCRIPTION_COLLECTION) + "?caseInstanceId=" + caseInstance.getId();
        assertResultsPresentInDataResponse(url, eventSubscription.getId());

        url = CmmnRestUrls.createRelativeResourceUrl(CmmnRestUrls.URL_EVENT_SUBSCRIPTION_COLLECTION) + "?caseInstanceId=nonexisting";
        assertEmptyResultsPresentInDataResponse(url);

        url = CmmnRestUrls.createRelativeResourceUrl(CmmnRestUrls.URL_EVENT_SUBSCRIPTION_COLLECTION) + "?caseDefinitionId=" + caseDefinition.getId();
        assertResultsPresentInDataResponse(url, eventSubscription.getId());

        url = CmmnRestUrls.createRelativeResourceUrl(CmmnRestUrls.URL_EVENT_SUBSCRIPTION_COLLECTION) + "?caseDefinitionId=nonexisting";
        assertEmptyResultsPresentInDataResponse(url);

        url = CmmnRestUrls.createRelativeResourceUrl(CmmnRestUrls.URL_EVENT_SUBSCRIPTION_COLLECTION) + "?createdBefore=" + getISODateString(inAnHour.getTime());
        assertResultsPresentInDataResponse(url, eventSubscription.getId());

        url = CmmnRestUrls.createRelativeResourceUrl(CmmnRestUrls.URL_EVENT_SUBSCRIPTION_COLLECTION) + "?createdAfter=" + getISODateString(hourAgo.getTime());
        assertResultsPresentInDataResponse(url, eventSubscription.getId());
    }

    @Test
    public void testQueryEventSubscriptionsByEventCategory() throws Exception {
        org.flowable.cmmn.api.repository.CmmnDeployment caseDeployment = repositoryService.createDeployment()
                .addString("eventRegistryDynamicStartTestCase.cmmn", String.join("\n",
                        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
                        "<definitions xmlns=\"http://www.omg.org/spec/CMMN/20151109/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:flowable=\"http://flowable.org/cmmn\" targetNamespace=\"http://flowable.org/cmmn\">",
                        "  <case id=\"eventRegistryDynamicStartTestCase\" name=\"Event Registry Dynamic Start Test Case\">",
                        "    <extensionElements>",
                        "      <flowable:eventType><![CDATA[simpleTest]]></flowable:eventType>",
                        "      <flowable:startEventCorrelationConfiguration><![CDATA[manualSubscription]]></flowable:startEventCorrelationConfiguration>",
                        "      <flowable:eventOutParameter source=\"customer\" target=\"customer\"></flowable:eventOutParameter>",
                        "      <flowable:eventOutParameter source=\"name\" target=\"name\"></flowable:eventOutParameter>",
                        "      <flowable:eventOutParameter source=\"action\" target=\"action\"></flowable:eventOutParameter>",
                        "    </extensionElements>",
                        "    <casePlanModel id=\"casePlanModel1\" name=\"Case plan model\">",
                        "      <planItem id=\"planItemTask1\" definitionRef=\"task1\"></planItem>",
                        "      <humanTask id=\"task1\" name=\"Test Task\" />",
                        "    </casePlanModel>",
                        "  </case>",
                        "</definitions>"))
                .deploy();

        EventDeployment eventDeployment = eventRepositoryService.createDeployment()
                .name("SimpleEvent")
                .addString("simple.event", String.join("\n",
                        "{",
                        "  \"key\": \"simpleTest\",",
                        "  \"name\": \"My event\",",
                        "  \"correlationParameters\": [",
                        "    { \"name\": \"customer\", \"type\": \"string\" },",
                        "    { \"name\": \"action\", \"type\": \"string\" }",
                        "  ],",
                        "  \"payload\": [",
                        "    { \"name\": \"customer\", \"type\": \"string\" },",
                        "    { \"name\": \"name\", \"type\": \"string\" },",
                        "    { \"name\": \"action\", \"type\": \"string\" }",
                        "  ]",
                        "}"))
                .deploy();

        try {
            eventRepositoryService.createEventDefinitionQuery()
                    .eventDefinitionKey("simpleTest")
                    .list()
                    .forEach(eventDefinition -> eventRepositoryService.setEventDefinitionCategory(eventDefinition.getId(), "customer-events"));

            EventSubscription eventSubscription = runtimeService.createCaseInstanceStartEventSubscriptionBuilder()
                    .caseDefinitionKey("eventRegistryDynamicStartTestCase")
                    .addCorrelationParameterValue("customer", "kermit")
                    .addCorrelationParameterValue("action", "start")
                    .subscribe();

            String url = CmmnRestUrls.createRelativeResourceUrl(CmmnRestUrls.URL_EVENT_SUBSCRIPTION_COLLECTION) + "?eventCategory=customer-events";
            assertResultsPresentInDataResponse(url, eventSubscription.getId());

            url = CmmnRestUrls.createRelativeResourceUrl(CmmnRestUrls.URL_EVENT_SUBSCRIPTION_COLLECTION) + "?eventCategory=other-events";
            assertEmptyResultsPresentInDataResponse(url);
        } finally {
            repositoryService.deleteDeployment(caseDeployment.getId());
            eventRepositoryService.deleteDeployment(eventDeployment.getId());
        }
    }

    @Test
    @CmmnDeployment(resources = { "org/flowable/cmmn/rest/service/api/runtime/signalEventListener.cmmn" })
    public void testGetEventSubscription() throws Exception {
        runtimeService.createCaseInstanceBuilder()
                .caseDefinitionKey("testSimpleEnableTask")
                .overrideCaseDefinitionTenantId("acme")
                .start();
        EventSubscription eventSubscription = runtimeService.createEventSubscriptionQuery().singleResult();

        String url = CmmnRestUrls.createRelativeResourceUrl(CmmnRestUrls.URL_EVENT_SUBSCRIPTION, eventSubscription.getId());
        CloseableHttpResponse response = executeRequest(new HttpGet(SERVER_URL_PREFIX + url), HttpStatus.SC_OK);
        JsonNode responseNode = objectMapper.readTree(response.getEntity().getContent());
        closeResponse(response);
        assertThat(responseNode).isNotNull();
        assertThatJson(responseNode)
                .when(Option.IGNORING_EXTRA_FIELDS)
                .isEqualTo("{"
                        + "id: '" + eventSubscription.getId() + "',"
                        + "eventType: '" + eventSubscription.getEventType() + "',"
                        + "eventName: '" + eventSubscription.getEventName() + "',"
                        + "caseInstanceId: '" + eventSubscription.getScopeId() + "',"
                        + "caseDefinitionId: '" + eventSubscription.getScopeDefinitionId() + "',"
                        + "created: '" + getISODateString(eventSubscription.getCreated()) + "',"
                        + "tenantId: 'acme'"
                        + "}");
    }
}
