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
 * Test Body class.
 *
 * @author Dev Team
 */
public class BodyTest{

    // ------------------------------------
	// default constructor
    // ------------------------------------
	public BodyTest() {
		subscriber = new BodySubscriber();
	}

	// test methods
	@Test
	/*
	 * Initiate BodyTest.
	 */
	public void startTest() throws Throwable {
		try {
			LOGGER.info("**********************************************************");
			LOGGER.info("Beginning test on Body...");
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
	 * jumpstart the process by instantiating2 Body
	 */
	protected void jumpStart() throws Throwable {
		LOGGER.info( "\n======== create instances to get the ball rolling  ======== ");

		bodyId = BodyBusinessDelegate.getBodyInstance()
		.createBody( generateNewCommand() )
		.get();

		// ---------------------------------------------
		// set up query subscriptions after the 1st create
		// ---------------------------------------------
		testingStep = "create";
		setUpQuerySubscriptions();

		BodyBusinessDelegate.getBodyInstance()
				.createBody( generateNewCommand() )
				.get();

	}

	/** 
	 * Set up query subscriptions
	 */
	protected void setUpQuerySubscriptions() throws Throwable {
		LOGGER.info( "\n======== Setting Up Query Subscriptions ======== ");
			
		try {            
			subscriber.bodySubscribe().updates().subscribe(
					  successValue -> {
						  LOGGER.info(successValue.toString());
						  try {
							  LOGGER.info("GetAll update received for Body : " + successValue.getBodyId());
							  if (successValue.getBodyId().equals(bodyId)) {
								  if (testingStep.equals("create")) {
									  testingStep = "update";
									  update();
								  } else if (testingStep.equals("delete")) {
									  testingStep = "complete";
									  state( getAll().size() == sizeOfBodyList - 1 , "value not deleted from list");
									  LOGGER.info("**********************************************************");
									  LOGGER.info("Body test completed successfully...");
									  LOGGER.info("**********************************************************\n");
								  }
							  }
						  } catch( Throwable exc ) {
							  LOGGER.warning( exc.getMessage() );
						  }
					  },
					  error -> LOGGER.warning(error.getMessage()),
					  () -> LOGGER.info("Subscription on body consumed")
					);
			subscriber.bodySubscribe( bodyId ).updates().subscribe(
					  successValue -> {
						  LOGGER.info(successValue.toString());
						  try {
							  LOGGER.info("GetOne update received for Body : " + successValue.getBodyId() + " in step " + testingStep);
							  testingStep = "delete";
							  sizeOfBodyList = getAll().size();
							  delete();
						  } catch( Throwable exc ) {
							  LOGGER.warning( exc.getMessage() );
						  }
					  },
					  error -> LOGGER.warning(error.getMessage()),
					  () -> LOGGER.info("Subscription on body for bodyId consumed")

					);
			

			}
			catch (Exception e) {
				LOGGER.warning( e.getMessage() );
				throw e;
			}
		}
		
		/** 
	 * read a Body. 
	 */
	protected Body read() throws Throwable {
		LOGGER.info( "\n======== READ ======== ");
		LOGGER.info( "-- Reading a previously created Body" );

		Body entity = null;
		StringBuilder msg = new StringBuilder( "-- Failed to read Body with primary key" );
		msg.append( bodyId );
		
		BodyFetchOneSummary fetchOneSummary = new BodyFetchOneSummary( bodyId );

		try {
			entity = BodyBusinessDelegate.getBodyInstance().getBody( fetchOneSummary );

			assertNotNull( entity,msg.toString() );

			LOGGER.info( "-- Successfully found Body " + entity.toString() );
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( msg.toString() + " : " + e );

			throw e;
		}
		
		return entity;
	}

	/** 
	 * updating a Body.
	 */
	protected void update() throws Throwable {
		LOGGER.info( "\n======== UPDATE ======== ");
		LOGGER.info( "-- Attempting to update a Body." );

		StringBuilder msg = new StringBuilder( "Failed to update a Body : " );        
		Body entity = read();
		RefreshBodyCommand command = generateUpdateCommand();
		command.setBodyId(entity.getBodyId());

		try {            
			assertNotNull( entity, msg.toString() );

			LOGGER.info( "-- Now updating the created Body." );

			// for use later on...
			bodyId = entity.getBodyId();

			BodyBusinessDelegate proxy = BodyBusinessDelegate.getBodyInstance();  

			proxy.updateBody( command ).get();

			LOGGER.info( "-- Successfully saved Body - " + entity.toString() );
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( msg.toString() + " : primarykey = " + bodyId + " : command -" +  command + " : " + e );

			throw e;
		}
	}

	/** 
	 * delete a Body.
	 */
	protected void delete() throws Throwable {
		LOGGER.info( "\n======== DELETE ======== ");
		LOGGER.info( "-- Deleting a previously created Body." );

		Body entity = null;
		
		try{
		    entity = read(); 
			LOGGER.info( "-- Successfully read Body with id " + bodyId );            
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( "-- Failed to read Body with id " + bodyId );

			throw e;
		}

		try{
			BodyBusinessDelegate.getBodyInstance().delete( new CloseBodyCommand( entity.getBodyId() ) ).get();
			LOGGER.info( "-- Successfully deleted Body with id " + bodyId );            
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( "-- Failed to delete Body with id " + bodyId );

			throw e;
		}
	}

	/**
	 * get all Bodys.
	 */
	protected List<Body> getAll() throws Throwable {    
		LOGGER.info( "======== GETALL ======== ");
		LOGGER.info( "-- Retrieving Collection of Bodys:" );

		StringBuilder msg = new StringBuilder( "-- Failed to get all Body : " );        
		List<Body> collection  = new ArrayList<>();

		try {
			// call the static get method on the BodyBusinessDelegate
			collection = BodyBusinessDelegate.getBodyInstance().getAllBody();
			assertNotNull( collection, "An Empty collection of Body was incorrectly returned.");
			
			// Now print out the values
			Body entity = null;            
			Iterator<Body> iter = collection.iterator();
			int index = 1;

			while( iter.hasNext() ) {
				// Retrieve the entity   
				entity = iter.next();

				assertNotNull( entity,"-- null entity in Collection." );
				assertNotNull( entity.getBodyId(), "-- entity in Collection has a null primary key" );        

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
	 * @return		BodyTest
	 */
	protected BodyTest setHandler(Handler handler) {
		if ( handler != null )
			LOGGER.addHandler(handler); 
		return this;
	}

	/**
	 * Returns a new populated Body
	 * 
	 * @return CreateBodyCommand alias
	 */
	protected CreateBodyCommand generateNewCommand() {
        CreateBodyCommand command = new CreateBodyCommand( null,  org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(16),  new Plant() );
		
		return( command );
	}

		/**
		 * Returns a new populated Body
		 * 
		 * @return RefreshBodyCommand alias
		 */
	protected RefreshBodyCommand generateUpdateCommand() {
	        RefreshBodyCommand command = new RefreshBodyCommand( null,  org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(16),  new Plant() );
			
			return( command );
		}
	//-----------------------------------------------------
	// attributes 
	//-----------------------------------------------------
	protected UUID bodyId = null;
	protected BodySubscriber subscriber = null;
	private final String unexpectedErrorMsg = ":::::::::::::: Unexpected Error :::::::::::::::::";
	private final Logger LOGGER = Logger.getLogger(BodyTest.class.getName());
	private String testingStep = "";
	private Integer sizeOfBodyList = 0;
}
