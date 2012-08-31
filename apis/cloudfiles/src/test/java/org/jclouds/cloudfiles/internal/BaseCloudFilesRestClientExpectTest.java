package org.jclouds.cloudfiles.internal;

import static org.jclouds.location.reference.LocationConstants.PROPERTY_REGIONS;

import java.util.Properties;

import org.jclouds.apis.ApiMetadata;
import org.jclouds.cloudfiles.CloudFilesApiMetadata;
import org.jclouds.cloudfiles.CloudFilesClient;
import org.jclouds.openstack.filters.AddTimestampQuery;
import org.jclouds.openstack.keystone.v1_1.config.AuthenticationServiceModule;
import org.jclouds.openstack.keystone.v1_1.internal.BaseKeystoneRestClientExpectTest;

public class BaseCloudFilesRestClientExpectTest extends BaseKeystoneRestClientExpectTest<CloudFilesClient> {

	   public BaseCloudFilesRestClientExpectTest() {
		      provider = "cloudfiles";
		   }
		   
		   @Override
		   protected ApiMetadata createApiMetadata() {
		      return new CloudFilesApiMetadata();
		   }


		   @Override
		   protected Properties setupProperties() {
		      Properties overrides = new Properties();
		      overrides.setProperty(PROPERTY_REGIONS, "US");
		      overrides.setProperty(provider + ".endpoint", endpoint);
		      return overrides;
		   }

		   protected static final String CONSTANT_DATE = "2009-11-08T15:54:08.897Z";

		   /**
		    * override so that we can control the timestamp used in
		    * {@link AddTimestampQuery}
		    */
		   public static class TestAuthenticationServiceModule extends AuthenticationServiceModule {
		      @Override
		      protected void configure() {
		         super.configure();
		      }
		   }
}
