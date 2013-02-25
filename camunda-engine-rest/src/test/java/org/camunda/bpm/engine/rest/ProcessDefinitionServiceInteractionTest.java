package org.camunda.bpm.engine.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response.Status;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.util.ReflectUtil;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.camunda.bpm.engine.rest.helper.EqualsMap;
import org.camunda.bpm.engine.rest.helper.MockDefinitionBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;

public class ProcessDefinitionServiceInteractionTest extends
    AbstractRestServiceTest {

  private static final String APPLICATION_BPMN20_XML_TYPE = 
      ContentType.create(ProcessDefinitionService.APPLICATION_BPMN20_XML, "UTF-8").toString();
  
  private static final String EXAMPLE_PROCESS_DEFINITION_ID = "aProcessDefinitionId";
  private static final String EXAMPLE_CATEGORY = "aCategory";
  private static final String EXAMPLE_DEFINITION_NAME = "aName";
  private static final String EXAMPLE_DEFINITION_KEY = "aKey";
  private static final String EXAMPLE_DEFINITION_DESCRIPTION = "aDescription";
  private static final int EXAMPLE_VERSION = 42;
  private static final String EXAMPLE_RESOURCE_NAME = "aResourceName";
  private static final String EXAMPLE_DEPLOYMENT_ID = "aDeploymentId";
  private static final String EXAMPLE_DIAGRAM_RESOURCE_NAME = "aResourceName";
  private static final boolean EXAMPLE_IS_SUSPENDED = true;
  
  
  private static final String EXAMPLE_INSTANCE_ID = "anId";
  
  private static final String SINGLE_PROCESS_DEFINITION_URL = TEST_RESOURCE_ROOT_PATH + "/process-definition/{id}";
  private static final String START_PROCESS_INSTANCE_URL = SINGLE_PROCESS_DEFINITION_URL + "/start";
  
  private RuntimeService runtimeServiceMock;
  private RepositoryService repositoryServiceMock;
  
  public void setupMocks() throws IOException {
    setupTestScenario();
    ProcessInstance mockInstance = createMockInstance();
    ProcessDefinition mockDefinition = createMockDefinition();
    
    // we replace this mock with every test in order to have a clean one (in terms of invocations) for verification
    runtimeServiceMock = mock(RuntimeService.class);
    when(processEngine.getRuntimeService()).thenReturn(runtimeServiceMock);
    when(runtimeServiceMock.startProcessInstanceById(eq(EXAMPLE_PROCESS_DEFINITION_ID), Matchers.<Map<String, Object>>any())).thenReturn(mockInstance);
    
    repositoryServiceMock = mock(RepositoryService.class);
    when(processEngine.getRepositoryService()).thenReturn(repositoryServiceMock);
    when(repositoryServiceMock.getProcessDefinition(eq(EXAMPLE_PROCESS_DEFINITION_ID))).thenReturn(mockDefinition);
    when(repositoryServiceMock.getProcessModel(eq(EXAMPLE_PROCESS_DEFINITION_ID))).thenReturn(createMockProcessDefinionBpmn20Xml());
  }
  
  private ProcessInstance createMockInstance() {
    ProcessInstance mock = mock(ProcessInstance.class);
    
    when(mock.getId()).thenReturn(EXAMPLE_INSTANCE_ID);
    
    return mock;
  }
  
  private ProcessDefinition createMockDefinition() {
    MockDefinitionBuilder builder = new MockDefinitionBuilder();
    ProcessDefinition definition = 
        builder.id(EXAMPLE_PROCESS_DEFINITION_ID).category(EXAMPLE_CATEGORY).name(EXAMPLE_DEFINITION_NAME)
          .key(EXAMPLE_DEFINITION_KEY).description(EXAMPLE_DEFINITION_DESCRIPTION)
          .version(EXAMPLE_VERSION).resource(EXAMPLE_RESOURCE_NAME)
          .deploymentId(EXAMPLE_DEPLOYMENT_ID).diagram(EXAMPLE_DIAGRAM_RESOURCE_NAME)
          .suspended(EXAMPLE_IS_SUSPENDED).build();
    return definition;
  }
  
  private InputStream createMockProcessDefinionBpmn20Xml() {
    // do not close the input stream, will be done in implementation
    InputStream bpmn20XmlIn = null;
    bpmn20XmlIn = ReflectUtil.getResourceAsStream("processes/fox-invoice_en_long_id.bpmn");
    Assert.assertNotNull(bpmn20XmlIn);
    return bpmn20XmlIn;
  }
  
  @Test
  public void testSimpleProcessInstantiation() throws IOException {
    setupMocks();
    
    given().pathParam("id", EXAMPLE_PROCESS_DEFINITION_ID)
      .contentType(POST_JSON_CONTENT_TYPE).body(EMPTY_JSON_OBJECT)
      .then().expect()
        .statusCode(Status.OK.getStatusCode())
        .body("id", equalTo(EXAMPLE_INSTANCE_ID))
      .when().post(START_PROCESS_INSTANCE_URL);
  }
  
  @Test
  public void testProcessInstantiationWithParameters() throws IOException {
    setupMocks();
    
    Map<String, Object> parameters = getInstanceVariablesParameters();
    
    Map<String, Object> json = new HashMap<String, Object>();
    json.put("variables", parameters);
    
    given().pathParam("id", EXAMPLE_PROCESS_DEFINITION_ID)
      .contentType(POST_JSON_CONTENT_TYPE).body(json)
      .then().expect()
        .statusCode(Status.OK.getStatusCode())
        .body("id", equalTo(EXAMPLE_INSTANCE_ID))
      .when().post(START_PROCESS_INSTANCE_URL);
    
    verify(runtimeServiceMock).startProcessInstanceById(eq(EXAMPLE_PROCESS_DEFINITION_ID), argThat(new EqualsMap(parameters)));
    
  }
  
  private Map<String, Object> getInstanceVariablesParameters() {
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("aBoolean", Boolean.TRUE);
    variables.put("aString", "aStringVariableValue");
    variables.put("anInteger", 42);
    
    return variables;
  }
  
  /**
   * {@link RuntimeService#startProcessInstanceById(String, Map)} throws an {@link ActivitiException}, if a definition with the given id does not exist.
   */
  @Test
  public void testUnsuccessfulInstantiation() throws IOException {
    setupMocks();
    
    when(runtimeServiceMock.startProcessInstanceById(eq(EXAMPLE_PROCESS_DEFINITION_ID), Matchers.<Map<String, Object>>any()))
      .thenThrow(new ActivitiException("expected exception"));
    
    given().pathParam("id", EXAMPLE_PROCESS_DEFINITION_ID)
      .contentType(POST_JSON_CONTENT_TYPE).body(EMPTY_JSON_OBJECT)
      .then().expect()
        .statusCode(Status.INTERNAL_SERVER_ERROR.getStatusCode())
      .when().post(START_PROCESS_INSTANCE_URL);
  }
  
  @Test
  public void testInstanceResourceLinkResult() throws IOException {
    setupMocks();
    
    String fullInstanceUrl = "http://localhost:" + PORT + TEST_RESOURCE_ROOT_PATH + "/process-instance/" + EXAMPLE_INSTANCE_ID;
    
    given().pathParam("id", EXAMPLE_PROCESS_DEFINITION_ID)
      .contentType(POST_JSON_CONTENT_TYPE).body(EMPTY_JSON_OBJECT)
      .then().expect()
        .statusCode(Status.OK.getStatusCode())
        .body("links[0].href", equalTo(fullInstanceUrl))
      .when().post(START_PROCESS_INSTANCE_URL);
  }
  
  @Test
  public void testDefinitionRetrieval() throws IOException {
    setupMocks();
    
    given().pathParam("id", EXAMPLE_PROCESS_DEFINITION_ID)
    .then().expect()
      .statusCode(Status.OK.getStatusCode())
      .body("id", equalTo(EXAMPLE_PROCESS_DEFINITION_ID))
      .body("key", equalTo(EXAMPLE_DEFINITION_KEY))
      .body("category", equalTo(EXAMPLE_CATEGORY))
      .body("name", equalTo(EXAMPLE_DEFINITION_NAME))
      .body("description", equalTo(EXAMPLE_DEFINITION_DESCRIPTION))
      .body("deploymentId", equalTo(EXAMPLE_DEPLOYMENT_ID))
      .body("version", equalTo(EXAMPLE_VERSION))
      .body("resource", equalTo(EXAMPLE_RESOURCE_NAME))
      .body("diagram", equalTo(EXAMPLE_DIAGRAM_RESOURCE_NAME))
      .body("suspended", equalTo(EXAMPLE_IS_SUSPENDED))
    .when().get(SINGLE_PROCESS_DEFINITION_URL);
    
    verify(repositoryServiceMock).getProcessDefinition(EXAMPLE_PROCESS_DEFINITION_ID);
  }
  
  @Test
  public void testNonExistingProcessDefinitionRetrieval() throws IOException {
    setupMocks();
    
    String nonExistingId = "aNonExistingDefinitionId";
    when(repositoryServiceMock.getProcessDefinition(eq(nonExistingId))).thenThrow(new ActivitiException("no matching definition"));
    
    given().pathParam("id", "aNonExistingDefinitionId")
    .then().expect()
      .statusCode(Status.BAD_REQUEST.getStatusCode())
    .when().get(SINGLE_PROCESS_DEFINITION_URL);
  }
  
  @Test
  public void testProcessDefinitionBpmn20XmlRetrieval() throws IOException {
    setupMocks();
    
    String processDefinitionXmlUrl = "http://localhost:" + PORT + TEST_RESOURCE_ROOT_PATH + "/process-definition/" + EXAMPLE_PROCESS_DEFINITION_ID;
    
    HttpClient client = new DefaultHttpClient();
    HttpGet get = new HttpGet(processDefinitionXmlUrl);
    get.addHeader("Content-Type", APPLICATION_BPMN20_XML_TYPE);
    
    ResponseHandler<String> responseHandler = new BasicResponseHandler();
    String responseBody = client.execute(get, responseHandler);
    assertTrue(responseBody.contains("id"));
    assertTrue(responseBody.contains("bpmn20Xml"));
    assertTrue(responseBody.contains("<?xml"));
    
    // RESTassured does not allow changing content-type for GET request
    
//    given().log().all().pathParam("id", EXAMPLE_PROCESS_DEFINITION_ID)
//      .contentType(APPLICATION_BPMN20_XML_TYPE)
//    .then()
////      .expect()
////      .statusCode(Status.OK.getStatusCode())
////      .body("id", equalTo(EXAMPLE_PROCESS_DEFINITION_ID))
////      .body("bpmn20Xml", startsWith("<?xml"))
//    .when().log().all().get(SINGLE_PROCESS_DEFINITION_URL);
  }
  
  @Test
  public void testNonExistingProcessDefinitionBpmn20XmlRetrieval() throws IOException {
    setupMocks();
    
    String nonExistingId = "aNonExistingDefinitionId";
    when(repositoryServiceMock.getProcessModel(eq(nonExistingId))).thenThrow(new ActivitiException("no matching process definition found."));
    
    given().log().all().pathParam("id", nonExistingId)
      .header("Content-Type", APPLICATION_BPMN20_XML_TYPE)
    .then().expect()
      .statusCode(Status.BAD_REQUEST.getStatusCode())
    .when().log().all().get(SINGLE_PROCESS_DEFINITION_URL);
  }
  
}
