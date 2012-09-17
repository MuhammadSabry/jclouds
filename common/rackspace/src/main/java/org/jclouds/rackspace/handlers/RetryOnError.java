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

import static org.jclouds.http.HttpUtils.closeClientButKeepContentStream;

import java.util.Map;

import org.jclouds.date.DateService;
import org.jclouds.domain.Credentials;
import org.jclouds.http.HttpCommand;
import org.jclouds.http.HttpResponse;
import org.jclouds.json.Json;
import org.jclouds.openstack.keystone.v2_0.domain.Access;
import org.jclouds.openstack.keystone.v2_0.handlers.RetryOnRenew;
import org.jclouds.providers.ProviderMetadata;
import org.jclouds.rest.Utils;

import com.google.common.cache.LoadingCache;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * This will retry on errors it makes sense to retry on.
 * 
 * @author Everett Toews
 * 
 */
@Singleton
public class RetryOnError extends RetryOnRenew {
	private static final String RETRY_AFTER = "retryAfter";
	
	private Utils utils;
	private ProviderMetadata providerMetadata;

	@Inject
	protected RetryOnError(LoadingCache<Credentials, Access> authenticationResponseCache, Utils utils, ProviderMetadata providerMetadata) {
		super(authenticationResponseCache);
		this.utils = utils;
		this.providerMetadata = providerMetadata;
	}

	@Override
	public boolean shouldRetryRequest(HttpCommand command, HttpResponse response) {
		boolean retry = super.shouldRetryRequest(command, response);
		
		System.err.println("retry: " + retry);
		
		if (retry) {
			return retry;
		}
		else {
			String content = new String(closeClientButKeepContentStream(response));
			
			if (response.getStatusCode() == 413 && content.contains(RETRY_AFTER)) {
				Json json = utils.getJson();
				Map<String, Map<String, String>> map = json.fromJson(content, 
					new TypeToken<Map<String, Map<String, String>>>() {}.getType());
				
				// TODO: fix these after the retryAfter/retryAt and the UTC/Z bugs are resolved
				String retryDateTimeStr = map.get("overLimit").get(RETRY_AFTER);
				retryDateTimeStr = retryDateTimeStr.replace("UTC", "Z");
				
				System.err.println("retryDateTimeStr = " + retryDateTimeStr);
				
				DateService dateService = utils.getDateService();
				int secondsBetween = dateService.secondsBetween(dateService.iso8601SecondsDateParse(retryDateTimeStr));
				
				// TODO: use secondsBetween and a property to find out whether to wait and retry or throw an exception
				System.err.println("secondsBetween = " + secondsBetween);
				System.err.println(providerMetadata.getDefaultProperties().get("foo"));
			}

			return retry;
		}
	}
}
