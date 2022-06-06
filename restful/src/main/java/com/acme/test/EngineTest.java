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
 * Test Engine class.
 *
 * @author Dev Team
 */
public class EngineTest{

    // ------------------------------------
	// default constructor
    // ------------------------------------
	public EngineTest() {
		subscriber = new EngineSubscriber();
	}

	// test methods
	@Test
	/*
	 * Initiate EngineTest.
	 */
	public void startTest() throws Throwable {
		try {
			LOGGER.info("**********************************************************");
			LOGGER.info("Beginning test on Engine...");
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
	 * jumpstart the process by instantiating2 Engine
	 */
	protected void jumpStart() throws Throwable {
		LOGGER.info( "\n======== create instances to get the ball rolling  ======== ");

		engineId = EngineBusinessDelegate.getEngineInstance()
		.createEngine( generateNewCommand() )
		.get();

		// ---------------------------------------------
		// set up query subscriptions after the 1st create
		// ---------------------------------------------
		testingStep = "create";
		setUpQuerySubscriptions();

		EngineBusinessDelegate.getEngineInstance()
				.createEngine( generateNewCommand() )
				.get();

	}

	/** 
	 * Set up query subscriptions
	 */
	protected void setUpQuerySubscriptions() throws Throwable {
		LOGGER.info( "\n======== Setting Up Query Subscriptions ======== ");
			
		try {            
			subscriber.engineSubscribe().updates().subscribe(
					  successValue -> {
						  LOGGER.info(successValue.toString());
						  try {
							  LOGGER.info("GetAll update received for Engine : " + successValue.getEngineId());
							  if (successValue.getEngineId().equals(engineId)) {
								  if (testingStep.equals("create")) {
									  testingStep = "update";
									  update();
								  } else if (testingStep.equals("delete")) {
									  testingStep = "complete";
									  state( getAll().size() == sizeOfEngineList - 1 , "value not deleted from list");
									  LOGGER.info("**********************************************************");
									  LOGGER.info("Engine test completed successfully...");
									  LOGGER.info("**********************************************************\n");
								  }
							  }
						  } catch( Throwable exc ) {
							  LOGGER.warning( exc.getMessage() );
						  }
					  },
					  error -> LOGGER.warning(error.getMessage()),
					  () -> LOGGER.info("Subscription on engine consumed")
					);
			subscriber.engineSubscribe( engineId ).updates().subscribe(
					  successValue -> {
						  LOGGER.info(successValue.toString());
						  try {
							  LOGGER.info("GetOne update received for Engine : " + successValue.getEngineId() + " in step " + testingStep);
							  testingStep = "delete";
							  sizeOfEngineList = getAll().size();
							  delete();
						  } catch( Throwable exc ) {
							  LOGGER.warning( exc.getMessage() );
						  }
					  },
					  error -> LOGGER.warning(error.getMessage()),
					  () -> LOGGER.info("Subscription on engine for engineId consumed")

					);
			

			}
			catch (Exception e) {
				LOGGER.warning( e.getMessage() );
				throw e;
			}
		}
		
		/** 
	 * read a Engine. 
	 */
	protected Engine read() throws Throwable {
		LOGGER.info( "\n======== READ ======== ");
		LOGGER.info( "-- Reading a previously created Engine" );

		Engine entity = null;
		StringBuilder msg = new StringBuilder( "-- Failed to read Engine with primary key" );
		msg.append( engineId );
		
		EngineFetchOneSummary fetchOneSummary = new EngineFetchOneSummary( engineId );

		try {
			entity = EngineBusinessDelegate.getEngineInstance().getEngine( fetchOneSummary );

			assertNotNull( entity,msg.toString() );

			LOGGER.info( "-- Successfully found Engine " + entity.toString() );
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( msg.toString() + " : " + e );

			throw e;
		}
		
		return entity;
	}

	/** 
	 * updating a Engine.
	 */
	protected void update() throws Throwable {
		LOGGER.info( "\n======== UPDATE ======== ");
		LOGGER.info( "-- Attempting to update a Engine." );

		StringBuilder msg = new StringBuilder( "Failed to update a Engine : " );        
		Engine entity = read();
		RefreshEngineCommand command = generateUpdateCommand();
		command.setEngineId(entity.getEngineId());

		try {            
			assertNotNull( entity, msg.toString() );

			LOGGER.info( "-- Now updating the created Engine." );

			// for use later on...
			engineId = entity.getEngineId();

			EngineBusinessDelegate proxy = EngineBusinessDelegate.getEngineInstance();  

			proxy.updateEngine( command ).get();

			LOGGER.info( "-- Successfully saved Engine - " + entity.toString() );
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( msg.toString() + " : primarykey = " + engineId + " : command -" +  command + " : " + e );

			throw e;
		}
	}

	/** 
	 * delete a Engine.
	 */
	protected void delete() throws Throwable {
		LOGGER.info( "\n======== DELETE ======== ");
		LOGGER.info( "-- Deleting a previously created Engine." );

		Engine entity = null;
		
		try{
		    entity = read(); 
			LOGGER.info( "-- Successfully read Engine with id " + engineId );            
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( "-- Failed to read Engine with id " + engineId );

			throw e;
		}

		try{
			EngineBusinessDelegate.getEngineInstance().delete( new CloseEngineCommand( entity.getEngineId() ) ).get();
			LOGGER.info( "-- Successfully deleted Engine with id " + engineId );            
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( "-- Failed to delete Engine with id " + engineId );

			throw e;
		}
	}

	/**
	 * get all Engines.
	 */
	protected List<Engine> getAll() throws Throwable {    
		LOGGER.info( "======== GETALL ======== ");
		LOGGER.info( "-- Retrieving Collection of Engines:" );

		StringBuilder msg = new StringBuilder( "-- Failed to get all Engine : " );        
		List<Engine> collection  = new ArrayList<>();

		try {
			// call the static get method on the EngineBusinessDelegate
			collection = EngineBusinessDelegate.getEngineInstance().getAllEngine();
			assertNotNull( collection, "An Empty collection of Engine was incorrectly returned.");
			
			// Now print out the values
			Engine entity = null;            
			Iterator<Engine> iter = collection.iterator();
			int index = 1;

			while( iter.hasNext() ) {
				// Retrieve the entity   
				entity = iter.next();

				assertNotNull( entity,"-- null entity in Collection." );
				assertNotNull( entity.getEngineId(), "-- entity in Collection has a null primary key" );        

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
	 * @return		EngineTest
	 */
	protected EngineTest setHandler(Handler handler) {
		if ( handler != null )
			LOGGER.addHandler(handler); 
		return this;
	}

	/**
	 * Returns a new populated Engine
	 * 
	 * @return CreateEngineCommand alias
	 */
	protected CreateEngineCommand generateNewCommand() {
        CreateEngineCommand command = new CreateEngineCommand( null,  org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(16),  org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(16),  new Plant(),  EngineType.values()[0] );
		
		return( command );
	}

		/**
		 * Returns a new populated Engine
		 * 
		 * @return RefreshEngineCommand alias
		 */
	protected RefreshEngineCommand generateUpdateCommand() {
	        RefreshEngineCommand command = new RefreshEngineCommand( null,  org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(16),  org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(16),  new Plant(),  EngineType.values()[0] );
			
			return( command );
		}
	//-----------------------------------------------------
	// attributes 
	//-----------------------------------------------------
	protected UUID engineId = null;
	protected EngineSubscriber subscriber = null;
	private final String unexpectedErrorMsg = ":::::::::::::: Unexpected Error :::::::::::::::::";
	private final Logger LOGGER = Logger.getLogger(EngineTest.class.getName());
	private String testingStep = "";
	private Integer sizeOfEngineList = 0;
}
