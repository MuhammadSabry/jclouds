/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.rackspace.handlers;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.Properties;

import org.jclouds.apis.ApiMetadata;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpResponse;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.internal.BaseNovaApiExpectTest;
import org.jclouds.providers.ProviderMetadata;
import org.jclouds.rest.InsufficientResourcesException;
import org.testng.annotations.Test;

import com.google.common.base.Function;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * @author Everett Toews
 */
@Test(groups = "unit", testName = "RetryOnErrorExpectTest")
public class RetryOnErrorExpectTest extends BaseNovaApiExpectTest {

   @Override
   protected ApiMetadata createApiMetadata() {
      // TODO: how to put the RackspaceRestClientModule into the NovaApiMetadata?
      System.err.println("*************");
      return super.createApiMetadata();
   }
   
   @Override
   protected ProviderMetadata createProviderMetadata() {
      // TODO: how to put the RackspaceRestClientModule into the NovaProviderMetadata?
      System.err.println("#############");
      return super.createProviderMetadata();
   }
   
   @Override
	protected Injector createInjector(Function<HttpRequest, HttpResponse> fn,
			Module module, Properties props) {
		// TODO Auto-generated method stub
		Injector injector = super.createInjector(fn, module, props);
		return injector;
	}
   
   @Test(expectedExceptions = InsufficientResourcesException.class)
   public void test() {
      HttpRequest request = HttpRequest.builder()
         .method("POST")
         .endpoint("https://az-1.region-a.geo-1.compute.hpcloudsvc.com/v1.1/3456/servers/56789/action")
         .addHeader("X-Auth-Token", authToken)
         .addHeader("Accept", "*/*")
         .payload(payloadFromStringWithContentType("{\"changePassword\":{\"adminPass\":\"foobar\"}}", APPLICATION_JSON))
         .build();
      
      HttpResponse response = HttpResponse.builder()
         .payload(payloadFromResourceWithContentType("/over_limit.json", APPLICATION_JSON))
         .statusCode(413)
         .build();

      NovaApi computeService = requestsSendResponses(keystoneAuthWithUsernameAndPasswordAndTenantName,
              responseWithKeystoneAccess, request, response);

      computeService.getServerApiForZone("az-1.region-a.geo-1").changeAdminPass("56789", "foobar");
   }
}