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
 * Test Interior class.
 *
 * @author Dev Team
 */
public class InteriorTest{

    // ------------------------------------
	// default constructor
    // ------------------------------------
	public InteriorTest() {
		subscriber = new InteriorSubscriber();
	}

	// test methods
	@Test
	/*
	 * Initiate InteriorTest.
	 */
	public void startTest() throws Throwable {
		try {
			LOGGER.info("**********************************************************");
			LOGGER.info("Beginning test on Interior...");
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
	 * jumpstart the process by instantiating2 Interior
	 */
	protected void jumpStart() throws Throwable {
		LOGGER.info( "\n======== create instances to get the ball rolling  ======== ");

		interiorId = InteriorBusinessDelegate.getInteriorInstance()
		.createInterior( generateNewCommand() )
		.get();

		// ---------------------------------------------
		// set up query subscriptions after the 1st create
		// ---------------------------------------------
		testingStep = "create";
		setUpQuerySubscriptions();

		InteriorBusinessDelegate.getInteriorInstance()
				.createInterior( generateNewCommand() )
				.get();

	}

	/** 
	 * Set up query subscriptions
	 */
	protected void setUpQuerySubscriptions() throws Throwable {
		LOGGER.info( "\n======== Setting Up Query Subscriptions ======== ");
			
		try {            
			subscriber.interiorSubscribe().updates().subscribe(
					  successValue -> {
						  LOGGER.info(successValue.toString());
						  try {
							  LOGGER.info("GetAll update received for Interior : " + successValue.getInteriorId());
							  if (successValue.getInteriorId().equals(interiorId)) {
								  if (testingStep.equals("create")) {
									  testingStep = "update";
									  update();
								  } else if (testingStep.equals("delete")) {
									  testingStep = "complete";
									  state( getAll().size() == sizeOfInteriorList - 1 , "value not deleted from list");
									  LOGGER.info("**********************************************************");
									  LOGGER.info("Interior test completed successfully...");
									  LOGGER.info("**********************************************************\n");
								  }
							  }
						  } catch( Throwable exc ) {
							  LOGGER.warning( exc.getMessage() );
						  }
					  },
					  error -> LOGGER.warning(error.getMessage()),
					  () -> LOGGER.info("Subscription on interior consumed")
					);
			subscriber.interiorSubscribe( interiorId ).updates().subscribe(
					  successValue -> {
						  LOGGER.info(successValue.toString());
						  try {
							  LOGGER.info("GetOne update received for Interior : " + successValue.getInteriorId() + " in step " + testingStep);
							  testingStep = "delete";
							  sizeOfInteriorList = getAll().size();
							  delete();
						  } catch( Throwable exc ) {
							  LOGGER.warning( exc.getMessage() );
						  }
					  },
					  error -> LOGGER.warning(error.getMessage()),
					  () -> LOGGER.info("Subscription on interior for interiorId consumed")

					);
			

			}
			catch (Exception e) {
				LOGGER.warning( e.getMessage() );
				throw e;
			}
		}
		
		/** 
	 * read a Interior. 
	 */
	protected Interior read() throws Throwable {
		LOGGER.info( "\n======== READ ======== ");
		LOGGER.info( "-- Reading a previously created Interior" );

		Interior entity = null;
		StringBuilder msg = new StringBuilder( "-- Failed to read Interior with primary key" );
		msg.append( interiorId );
		
		InteriorFetchOneSummary fetchOneSummary = new InteriorFetchOneSummary( interiorId );

		try {
			entity = InteriorBusinessDelegate.getInteriorInstance().getInterior( fetchOneSummary );

			assertNotNull( entity,msg.toString() );

			LOGGER.info( "-- Successfully found Interior " + entity.toString() );
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( msg.toString() + " : " + e );

			throw e;
		}
		
		return entity;
	}

	/** 
	 * updating a Interior.
	 */
	protected void update() throws Throwable {
		LOGGER.info( "\n======== UPDATE ======== ");
		LOGGER.info( "-- Attempting to update a Interior." );

		StringBuilder msg = new StringBuilder( "Failed to update a Interior : " );        
		Interior entity = read();
		RefreshInteriorCommand command = generateUpdateCommand();
		command.setInteriorId(entity.getInteriorId());

		try {            
			assertNotNull( entity, msg.toString() );

			LOGGER.info( "-- Now updating the created Interior." );

			// for use later on...
			interiorId = entity.getInteriorId();

			InteriorBusinessDelegate proxy = InteriorBusinessDelegate.getInteriorInstance();  

			proxy.updateInterior( command ).get();

			LOGGER.info( "-- Successfully saved Interior - " + entity.toString() );
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( msg.toString() + " : primarykey = " + interiorId + " : command -" +  command + " : " + e );

			throw e;
		}
	}

	/** 
	 * delete a Interior.
	 */
	protected void delete() throws Throwable {
		LOGGER.info( "\n======== DELETE ======== ");
		LOGGER.info( "-- Deleting a previously created Interior." );

		Interior entity = null;
		
		try{
		    entity = read(); 
			LOGGER.info( "-- Successfully read Interior with id " + interiorId );            
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( "-- Failed to read Interior with id " + interiorId );

			throw e;
		}

		try{
			InteriorBusinessDelegate.getInteriorInstance().delete( new CloseInteriorCommand( entity.getInteriorId() ) ).get();
			LOGGER.info( "-- Successfully deleted Interior with id " + interiorId );            
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( "-- Failed to delete Interior with id " + interiorId );

			throw e;
		}
	}

	/**
	 * get all Interiors.
	 */
	protected List<Interior> getAll() throws Throwable {    
		LOGGER.info( "======== GETALL ======== ");
		LOGGER.info( "-- Retrieving Collection of Interiors:" );

		StringBuilder msg = new StringBuilder( "-- Failed to get all Interior : " );        
		List<Interior> collection  = new ArrayList<>();

		try {
			// call the static get method on the InteriorBusinessDelegate
			collection = InteriorBusinessDelegate.getInteriorInstance().getAllInterior();
			assertNotNull( collection, "An Empty collection of Interior was incorrectly returned.");
			
			// Now print out the values
			Interior entity = null;            
			Iterator<Interior> iter = collection.iterator();
			int index = 1;

			while( iter.hasNext() ) {
				// Retrieve the entity   
				entity = iter.next();

				assertNotNull( entity,"-- null entity in Collection." );
				assertNotNull( entity.getInteriorId(), "-- entity in Collection has a null primary key" );        

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
	 * @return		InteriorTest
	 */
	protected InteriorTest setHandler(Handler handler) {
		if ( handler != null )
			LOGGER.addHandler(handler); 
		return this;
	}

	/**
	 * Returns a new populated Interior
	 * 
	 * @return CreateInteriorCommand alias
	 */
	protected CreateInteriorCommand generateNewCommand() {
        CreateInteriorCommand command = new CreateInteriorCommand( null,  org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(16),  org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(16),  new Plant() );
		
		return( command );
	}

		/**
		 * Returns a new populated Interior
		 * 
		 * @return RefreshInteriorCommand alias
		 */
	protected RefreshInteriorCommand generateUpdateCommand() {
	        RefreshInteriorCommand command = new RefreshInteriorCommand( null,  org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(16),  org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(16),  new Plant() );
			
			return( command );
		}
	//-----------------------------------------------------
	// attributes 
	//-----------------------------------------------------
	protected UUID interiorId = null;
	protected InteriorSubscriber subscriber = null;
	private final String unexpectedErrorMsg = ":::::::::::::: Unexpected Error :::::::::::::::::";
	private final Logger LOGGER = Logger.getLogger(InteriorTest.class.getName());
	private String testingStep = "";
	private Integer sizeOfInteriorList = 0;
}
