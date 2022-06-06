/*******************************************************************************
  Oldsmobile Motor Corporation Confidential
  
  2018 Oldsmobile Motor Corporation
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Oldsmobile Motor Corporation - General Release
 ******************************************************************************/
package com.acme.test;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.util.Assert.state;

import com.acme.delegate.*;
import com.acme.entity.*;
import com.acme.api.*;
import com.acme.subscriber.*;

/**
 * Test Chassis class.
 *
 * @author Dev Team
 */
public class ChassisTest{

    // ------------------------------------
	// default constructor
    // ------------------------------------
	public ChassisTest() {
		subscriber = new ChassisSubscriber();
	}

	// test methods
	@Test
	/*
	 * Initiate ChassisTest.
	 */
	public void startTest() throws Throwable {
		try {
			LOGGER.info("**********************************************************");
			LOGGER.info("Beginning test on Chassis...");
			LOGGER.info("**********************************************************\n");
			
			// ---------------------------------------------
			// jumpstart process
			// ---------------------------------------------
			jumpStart();
			
		} catch (Throwable e) {
			throw e;
		} finally {
		}
	}

	/** 
	 * jumpstart the process by instantiating2 Chassis
	 */
	protected void jumpStart() throws Throwable {
		LOGGER.info( "\n======== create instances to get the ball rolling  ======== ");

		chassisId = ChassisBusinessDelegate.getChassisInstance()
		.createChassis( generateNewCommand() )
		.get();

		// ---------------------------------------------
		// set up query subscriptions after the 1st create
		// ---------------------------------------------
		testingStep = "create";
		setUpQuerySubscriptions();

		ChassisBusinessDelegate.getChassisInstance()
				.createChassis( generateNewCommand() )
				.get();

	}

	/** 
	 * Set up query subscriptions
	 */
	protected void setUpQuerySubscriptions() throws Throwable {
		LOGGER.info( "\n======== Setting Up Query Subscriptions ======== ");
			
		try {            
			subscriber.chassisSubscribe().updates().subscribe(
					  successValue -> {
						  LOGGER.info(successValue.toString());
						  try {
							  LOGGER.info("GetAll update received for Chassis : " + successValue.getChassisId());
							  if (successValue.getChassisId().equals(chassisId)) {
								  if (testingStep.equals("create")) {
									  testingStep = "update";
									  update();
								  } else if (testingStep.equals("delete")) {
									  testingStep = "complete";
									  state( getAll().size() == sizeOfChassisList - 1 , "value not deleted from list");
									  LOGGER.info("**********************************************************");
									  LOGGER.info("Chassis test completed successfully...");
									  LOGGER.info("**********************************************************\n");
								  }
							  }
						  } catch( Throwable exc ) {
							  LOGGER.warning( exc.getMessage() );
						  }
					  },
					  error -> LOGGER.warning(error.getMessage()),
					  () -> LOGGER.info("Subscription on chassis consumed")
					);
			subscriber.chassisSubscribe( chassisId ).updates().subscribe(
					  successValue -> {
						  LOGGER.info(successValue.toString());
						  try {
							  LOGGER.info("GetOne update received for Chassis : " + successValue.getChassisId() + " in step " + testingStep);
							  testingStep = "delete";
							  sizeOfChassisList = getAll().size();
							  delete();
						  } catch( Throwable exc ) {
							  LOGGER.warning( exc.getMessage() );
						  }
					  },
					  error -> LOGGER.warning(error.getMessage()),
					  () -> LOGGER.info("Subscription on chassis for chassisId consumed")

					);
			

			}
			catch (Exception e) {
				LOGGER.warning( e.getMessage() );
				throw e;
			}
		}
		
		/** 
	 * read a Chassis. 
	 */
	protected Chassis read() throws Throwable {
		LOGGER.info( "\n======== READ ======== ");
		LOGGER.info( "-- Reading a previously created Chassis" );

		Chassis entity = null;
		StringBuilder msg = new StringBuilder( "-- Failed to read Chassis with primary key" );
		msg.append( chassisId );
		
		ChassisFetchOneSummary fetchOneSummary = new ChassisFetchOneSummary( chassisId );

		try {
			entity = ChassisBusinessDelegate.getChassisInstance().getChassis( fetchOneSummary );

			assertNotNull( entity,msg.toString() );

			LOGGER.info( "-- Successfully found Chassis " + entity.toString() );
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( msg.toString() + " : " + e );

			throw e;
		}
		
		return entity;
	}

	/** 
	 * updating a Chassis.
	 */
	protected void update() throws Throwable {
		LOGGER.info( "\n======== UPDATE ======== ");
		LOGGER.info( "-- Attempting to update a Chassis." );

		StringBuilder msg = new StringBuilder( "Failed to update a Chassis : " );        
		Chassis entity = read();
		RefreshChassisCommand command = generateUpdateCommand();
		command.setChassisId(entity.getChassisId());

		try {            
			assertNotNull( entity, msg.toString() );

			LOGGER.info( "-- Now updating the created Chassis." );

			// for use later on...
			chassisId = entity.getChassisId();

			ChassisBusinessDelegate proxy = ChassisBusinessDelegate.getChassisInstance();  

			proxy.updateChassis( command ).get();

			LOGGER.info( "-- Successfully saved Chassis - " + entity.toString() );
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( msg.toString() + " : primarykey = " + chassisId + " : command -" +  command + " : " + e );

			throw e;
		}
	}

	/** 
	 * delete a Chassis.
	 */
	protected void delete() throws Throwable {
		LOGGER.info( "\n======== DELETE ======== ");
		LOGGER.info( "-- Deleting a previously created Chassis." );

		Chassis entity = null;
		
		try{
		    entity = read(); 
			LOGGER.info( "-- Successfully read Chassis with id " + chassisId );            
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( "-- Failed to read Chassis with id " + chassisId );

			throw e;
		}

		try{
			ChassisBusinessDelegate.getChassisInstance().delete( new CloseChassisCommand( entity.getChassisId() ) ).get();
			LOGGER.info( "-- Successfully deleted Chassis with id " + chassisId );            
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( "-- Failed to delete Chassis with id " + chassisId );

			throw e;
		}
	}

	/**
	 * get all Chassiss.
	 */
	protected List<Chassis> getAll() throws Throwable {    
		LOGGER.info( "======== GETALL ======== ");
		LOGGER.info( "-- Retrieving Collection of Chassiss:" );

		StringBuilder msg = new StringBuilder( "-- Failed to get all Chassis : " );        
		List<Chassis> collection  = new ArrayList<>();

		try {
			// call the static get method on the ChassisBusinessDelegate
			collection = ChassisBusinessDelegate.getChassisInstance().getAllChassis();
			assertNotNull( collection, "An Empty collection of Chassis was incorrectly returned.");
			
			// Now print out the values
			Chassis entity = null;            
			Iterator<Chassis> iter = collection.iterator();
			int index = 1;

			while( iter.hasNext() ) {
				// Retrieve the entity   
				entity = iter.next();

				assertNotNull( entity,"-- null entity in Collection." );
				assertNotNull( entity.getChassisId(), "-- entity in Collection has a null primary key" );        

				LOGGER.info( " - " + String.valueOf(index) + ". " + entity.toString() );
				index++;
			}
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( msg.toString() + " : " + e );

			throw e;
		}
		
		return collection;			
	}

	/**
	 * Assigns a common log handler for each test class in the suite 
	 * in the event log output needs to go elsewhere
	 * 
	 * @param		handler	Handler
	 * @return		ChassisTest
	 */
	protected ChassisTest setHandler(Handler handler) {
		if ( handler != null )
			LOGGER.addHandler(handler); 
		return this;
	}

	/**
	 * Returns a new populated Chassis
	 * 
	 * @return CreateChassisCommand alias
	 */
	protected CreateChassisCommand generateNewCommand() {
        CreateChassisCommand command = new CreateChassisCommand( null,  org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(16),  org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(16),  new Plant(),  ChassisType.values()[0] );
		
		return( command );
	}

		/**
		 * Returns a new populated Chassis
		 * 
		 * @return RefreshChassisCommand alias
		 */
	protected RefreshChassisCommand generateUpdateCommand() {
	        RefreshChassisCommand command = new RefreshChassisCommand( null,  org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(16),  org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(16),  new Plant(),  ChassisType.values()[0] );
			
			return( command );
		}
	//-----------------------------------------------------
	// attributes 
	//-----------------------------------------------------
	protected UUID chassisId = null;
	protected ChassisSubscriber subscriber = null;
	private final String unexpectedErrorMsg = ":::::::::::::: Unexpected Error :::::::::::::::::";
	private final Logger LOGGER = Logger.getLogger(ChassisTest.class.getName());
	private String testingStep = "";
	private Integer sizeOfChassisList = 0;
}
