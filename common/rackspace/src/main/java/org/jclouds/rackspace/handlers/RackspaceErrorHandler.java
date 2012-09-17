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

import javax.inject.Singleton;

import org.jclouds.http.HttpCommand;
import org.jclouds.http.HttpResponse;
import org.jclouds.openstack.nova.v2_0.handlers.NovaErrorHandler;
import org.jclouds.rest.InsufficientResourcesException;

/**
 * This will parse and set an appropriate exception on the command object.
 * Returns error messages more friendly to users of the Rackspace Open Cloud.
 * 
 * @author Everett Toews
 * 
 */
@Singleton
public class RackspaceErrorHandler extends NovaErrorHandler {
	
   public void handleError(HttpCommand command, HttpResponse response) {
      super.handleError(command, response);
      
      if (response.getStatusCode() == 413) {          
         String messageToUser = "You have exceeded a rate limit. For more detail on how to resolve limit errors, please see http://rax.io/limits";
         Exception exception = command.getException(); 
         
         command.setException(new InsufficientResourcesException(exception.getMessage(), exception.getCause(), messageToUser));
      }
   }
}
