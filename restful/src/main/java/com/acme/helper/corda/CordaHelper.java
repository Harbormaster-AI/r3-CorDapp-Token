/*******************************************************************************
  Oldsmobile Motor Corporation Confidential
  
  2018 Oldsmobile Motor Corporation
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Oldsmobile Motor Corporation - General Release
 ******************************************************************************/
package com.acme.helper.corda;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.corda.client.rpc.CordaRPCClient;
import net.corda.client.rpc.CordaRPCClientConfiguration;
import net.corda.client.rpc.CordaRPCConnection;
import net.corda.client.rpc.GracefulReconnect;
import net.corda.core.utilities.NetworkHostAndPort;

import com.acme.helper.corda.api.PartyEnum;

import com.acme.api.*;
import com.acme.exception.*;
import com.acme.helper.corda.api.*;

/**
 * Helper class to interact with Corda
 * 
 * @author Dev Team
 *
 */
public class CordaHelper {

	/**
	 * use static factory method
	 */
	protected CordaHelper() {
		populateCordaNodeInfos();
	}

	/**
	 * returns the user provided node configuration information
	 */
	public Map<String, CordaNodeInfo> getNodeInfos() {
		return( cordaNodeInfos );
	}
	
	/**
	 * returns the corresponding CordaNodeInfo
	 */
	public CordaNodeInfo getNodeInfo( String nodeName ) {
		return cordaNodeInfos.get(nodeName );
	}

	public static CordaHelper getInstance() {
		if ( cordaHelper == null )
			cordaHelper = new CordaHelper();
		
		return cordaHelper;
	}

	/**
	 * convenience methnod to connect and proxy to the correct node based on the provided name
	 * 
	 * @param		nodeName	String
	 * @return		CordaRPCConnection	Corda RPC Operations handler
	 */
	public CordaRPCConnection getCordaNodeConnection( String nodeName ) {
		CordaNodeInfo nodeInfo = getNodeInfo( nodeName );
		CordaRPCConnection rcpConnection = null;
		
		if ( nodeInfo != null ) {
			
			LOGGER.log( Level.INFO, "CordaNode Info found is {0} ", nodeInfo );
			
			CordaRPCClientConfiguration configuration = 
						new CordaRPCClientConfiguration(Duration.ofMinutes(3), 4, 
											java.lang.Boolean.getBoolean("net.corda.client.rpc.trackRpcCallSites"), 
											Duration.ofSeconds(1), 4, 1, Duration.ofSeconds(5), 
											new Double(1), 10, 10485760, Duration.ofDays(1));
			CordaRPCClient client 				= new CordaRPCClient(networkHostAndPort( nodeInfo.ipAddress ), 
																		configuration);
			GracefulReconnect gracefulReconnect = new GracefulReconnect(
					() -> LOGGER.info("on disconnect"), 
					() -> LOGGER.info("on reconnect"));
			
			rcpConnection = client.
					start(nodeInfo.userId, nodeInfo.password, gracefulReconnect);
							
		}
		
		return rcpConnection;
	}
	
	/**
	 * Helper method to prepopulate the corda node info the user config values
	 */
	private void populateCordaNodeInfos() {
		cordaNodeInfos.put( "Notary", new CordaNodeInfo( "Notary", 
						"London", "GB", "localhost:8600", "localhost:8650", "user1", "test", "true" ));
		LOGGER.log( Level.INFO, "Populated node map with {0}", cordaNodeInfos.get( "Notary") );
		cordaNodeInfos.put( "AutomobileCo", new CordaNodeInfo( "AutomobileCo", 
						"Detroit", "US", "localhost:8601", "localhost:8651", "user1", "test", "false" ));
		LOGGER.log( Level.INFO, "Populated node map with {0}", cordaNodeInfos.get( "AutomobileCo") );
		cordaNodeInfos.put( "LicensedDealership", new CordaNodeInfo( "LicensedDealership", 
						"New York", "US", "localhost:8602", "localhost:8652", "user1", "test", "false" ));
		LOGGER.log( Level.INFO, "Populated node map with {0}", cordaNodeInfos.get( "LicensedDealership") );
		cordaNodeInfos.put( "UsedPartsAgency", new CordaNodeInfo( "UsedPartsAgency", 
						"San Francisco", "US", "localhost:8603", "localhost:8653", "user1", "test", "false" ));
		LOGGER.log( Level.INFO, "Populated node map with {0}", cordaNodeInfos.get( "UsedPartsAgency") );
		cordaNodeInfos.put( "Buyer", new CordaNodeInfo( "Buyer", 
						"San Diego", "US", "localhost:8604", "localhost:8654", "user1", "test", "false" ));
		LOGGER.log( Level.INFO, "Populated node map with {0}", cordaNodeInfos.get( "Buyer") );
	}
	
	/**
	 * helper method to turn an ipaddress string into a Corda NetworkHostAndPort
	 */
	private NetworkHostAndPort networkHostAndPort( String ipAddress ) {
		NetworkHostAndPort hostAndPort = null;
		

		LOGGER.log( Level.INFO, "Mapping IP address {0} to Corda NetworkHostAndPort type", ipAddress );
		
		try {
			java.net.URL url = new java.net.URL( "http://" + ipAddress );
			hostAndPort = new NetworkHostAndPort( url.getHost(), url.getPort() );
		} catch( java.net.MalformedURLException exc ) {
			LOGGER.warning( exc.getMessage() );
		}
		
		return hostAndPort;
	}
	
	
	// inner class definitions 
	protected class CordaNodeInfo {
		public String name;
		public String location;
		public String country;
		public String ipAddress;
		public String adminIpAddress;
		public String userId;
		public String password;
		public Boolean isNotary = new Boolean(false);
		
		public CordaNodeInfo( String name, String location, String country, 
						String ipAddress, String adminIpAddress, String userId, String password, String isNotary ) {
			this.name = name;
			this.location = location;
			this.country = country;
			this.ipAddress = ipAddress;
			this.adminIpAddress = adminIpAddress;
			this.userId = userId;
			this.password = password;
			if ( isNotary != null )
				this.isNotary = new Boolean( isNotary );
		}	
		
		public String toString() {
			return( "Org: " + name + ", Locality: " + location + ", Country: " + country +
						"IP Address: " + ipAddress + ", Admin IP Address: " + adminIpAddress +
						", User Id: " + userId + ", Password: xxxxxxxx" + ", isNotary: " + isNotary );
		}
	}
	
    //************************************************************************    
	// Attributes
	//************************************************************************
	protected Map<String, CordaNodeInfo> cordaNodeInfos	= new HashMap<>();
	private static CordaHelper cordaHelper	= null;
	private static final Logger LOGGER 		= Logger.getLogger(CordaHelper.class.getName());

}
