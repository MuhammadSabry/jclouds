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
package org.jclouds.openstack.keystone.v1_1.parse;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import org.jclouds.date.internal.SimpleDateFormatDateService;
import org.jclouds.json.BaseItemParserTest;
import org.jclouds.openstack.keystone.v1_1.domain.Auth;
import org.jclouds.openstack.keystone.v1_1.domain.Endpoint;
import org.jclouds.openstack.keystone.v1_1.domain.Token;
import org.jclouds.rest.annotations.SelectJson;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;

/**
 * @author Adrian Cole
 */
@Test(groups = "unit", testName = "ParseAuthWithMultipleRegionsTest")
public class ParseAuthWithMultipleRegionsTest extends BaseItemParserTest<Auth> {

   @Override
   public String resource() {
      return "/auth1_1-multistorage.json";
   }

   @Override
   @SelectJson("auth")
   @Consumes(MediaType.APPLICATION_JSON)
   public Auth expected() {
	  Builder<String, Endpoint> serviceCatalogBuilder = ImmutableMultimap.<String, Endpoint>builder();
      serviceCatalogBuilder.put("cloudServersOpenStack",
        Endpoint
              .builder()
              .region("DFW")
              .publicURL(
                    URI.create("https://dfw.servers.api.rackspacecloud.com/v2/123123"))
              .v1Default(true).build());
      serviceCatalogBuilder.put("cloudServersOpenStack",
        Endpoint
              .builder()
              .region("ORD")
              .publicURL(
                    URI.create("https://ord.servers.api.rackspacecloud.com/v2/123123"))
              .v1Default(false).build());
      serviceCatalogBuilder.put("cloudDNS",
    	        Endpoint
    	              .builder()
    	              .publicURL(
    	                    URI.create("https://dns.api.rackspacecloud.com/v1.0/123123"))
    	              .v1Default(true).build());
      serviceCatalogBuilder.put("cloudFilesCDN",
    	        Endpoint
    	              .builder()
    	              .region("DFW")
    	              .publicURL(
    	                    URI.create("https://cdn1.clouddrive.com/v1/MossoCloudFS_xxx"))
    	              .v1Default(true).build());
      serviceCatalogBuilder.put("cloudFilesCDN",
    	        Endpoint
    	              .builder()
    	              .region("ORD")
    	              .publicURL(
    	                    URI.create("https://cdn2.clouddrive.com/v1/MossoCloudFS_xxx"))
    	              .v1Default(false).build());
      serviceCatalogBuilder.put("cloudFiles",
  	        Endpoint
  	              .builder()
  	              .region("DFW")
  	              .publicURL(
  	                    URI.create("https://storage101.dfw1.clouddrive.com/v1/MossoCloudFS_xxx"))
  	              .v1Default(true)
  	              .internalURL(
  	            		URI.create("https://snet-storage101.dfw1.clouddrive.com/v1/MossoCloudFS_xxx")).build());
      serviceCatalogBuilder.put("cloudFiles",
  	        Endpoint
  	  	              .builder()
  	  	              .region("ORD")
  	  	              .publicURL(
  	  	                    URI.create("https://storage101.ord1.clouddrive.com/v1/MossoCloudFS_xxx"))
  	  	              .v1Default(false)
  	  	              .internalURL(
  	  	            		URI.create("https://snet-storage101.ord1.clouddrive.com/v1/MossoCloudFS_xxx")).build());
      serviceCatalogBuilder.put("cloudMonitoring",
  	        Endpoint
  	              .builder()
  	              .publicURL(
  	                    URI.create("https://monitoring.api.rackspacecloud.com/v1.0/123123"))
  	              .v1Default(true).build());
      serviceCatalogBuilder.put("cloudLoadBalancers",
  	        Endpoint
  	              .builder()
  	              .region("ORD")
  	              .publicURL(
  	                    URI.create("https://ord.loadbalancers.api.rackspacecloud.com/v1.0/123123"))
  	              .v1Default(true).build());
      serviceCatalogBuilder.put("cloudLoadBalancers",
  	        Endpoint
  	              .builder()
  	              .region("DFW")
  	              .publicURL(
  	                    URI.create("https://dfw.loadbalancers.api.rackspacecloud.com/v1.0/123123"))
  	              .v1Default(false).build());
      serviceCatalogBuilder.put("cloudDatabases",
    	        Endpoint
    	              .builder()
    	              .region("DFW")
    	              .publicURL(
    	                    URI.create("https://dfw.databases.api.rackspacecloud.com/v1.0/123123"))
    	              .v1Default(true).build());
      serviceCatalogBuilder.put("cloudDatabases",
    	        Endpoint
    	              .builder()
    	              .region("ORD")
    	              .publicURL(
    	                    URI.create("https://ord.databases.api.rackspacecloud.com/v1.0/123123"))
    	              .v1Default(false).build());
      serviceCatalogBuilder.put("cloudServers",
    	        Endpoint
    	              .builder()
    	              .publicURL(
    	                    URI.create("https://servers.api.rackspacecloud.com/v1.0/123123"))
    	              .v1Default(true).build());
      
      return Auth
            .builder()
            .token(
                  Token.builder()
                        .expires(new SimpleDateFormatDateService().iso8601DateParse("2012-08-30T15:34:41.000-05:00"))
                        .id("118fb907-0786-4799-88f0-9a5b7963d1ab").build())
            .serviceCatalog(serviceCatalogBuilder.build()).build();

   }
}
