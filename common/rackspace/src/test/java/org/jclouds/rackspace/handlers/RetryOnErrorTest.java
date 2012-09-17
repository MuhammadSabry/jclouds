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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.testng.Assert.assertTrue;

import java.util.Properties;

import org.jclouds.date.joda.JodaDateService;
import org.jclouds.domain.Credentials;
import org.jclouds.http.HttpCommand;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpResponse;
import org.jclouds.io.Payloads;
import org.jclouds.json.Json;
import org.jclouds.json.config.GsonModule;
import org.jclouds.openstack.keystone.v2_0.domain.Access;
import org.jclouds.providers.ProviderMetadata;
import org.jclouds.rest.Utils;
import org.testng.annotations.Test;

import com.google.common.cache.LoadingCache;
import com.google.inject.Guice;

/**
 * Tests behavior of {@code RetryOnError} handler
 * 
 * @author Everett Toews
 */
@Test(groups = "unit", testName = "RetryOnErrorTest")
public class RetryOnErrorTest {
   String overLimitStr = "{\"overLimit\": {\"code\": 413, \"message\": \"OverLimit Retry...\", \"details\": \"Error Details...\", \"retryAfter\": \"2012-08-23T19:45:24UTC\"}}";

   @Test
   public void test() {
      HttpCommand command = createMock(HttpCommand.class);
      HttpRequest request = createMock(HttpRequest.class);
      HttpResponse response = createMock(HttpResponse.class);
      
      @SuppressWarnings("unchecked")
      LoadingCache<Credentials, Access> cache = createMock(LoadingCache.class);
      Utils utils = createMock(Utils.class);
      ProviderMetadata providerMetadata = createMock(ProviderMetadata.class);

      expect(command.getCurrentRequest()).andReturn(request);
      
      JodaDateService jodaDateService = new JodaDateService();  // rackspace-common shouldn't depend on jclouds-joda!
      Json json = Guice.createInjector(new GsonModule()).getInstance(Json.class);
      Properties properties = new Properties();
      properties.put("foo", 3);      

      cache.invalidateAll();
      expectLastCall();

      expect(utils.getDateService()).andReturn(jodaDateService).anyTimes();
      expect(utils.getJson()).andReturn(json).anyTimes();
      expect(providerMetadata.getDefaultProperties()).andReturn(properties);
      expect(response.getPayload()).andReturn(Payloads.newStringPayload(overLimitStr)).anyTimes();
      expect(response.getStatusCode()).andReturn(413).atLeastOnce();

      replay(utils);
      replay(providerMetadata);
      replay(command);
      replay(response);
      replay(cache);

      RetryOnError retry = new RetryOnError(cache, utils, providerMetadata);

      assertTrue(retry.shouldRetryRequest(command, response));

      verify(utils);
      verify(providerMetadata);
      verify(command);
      verify(response);
      verify(cache);
   }
}
