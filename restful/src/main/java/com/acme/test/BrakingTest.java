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
 * Test Braking class.
 *
 * @author Dev Team
 */
public class BrakingTest{

    // ------------------------------------
	// default constructor
    // ------------------------------------
	public BrakingTest() {
		subscriber = new BrakingSubscriber();
	}

	// test methods
	@Test
	/*
	 * Initiate BrakingTest.
	 */
	public void startTest() throws Throwable {
		try {
			LOGGER.info("**********************************************************");
			LOGGER.info("Beginning test on Braking...");
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
	 * jumpstart the process by instantiating2 Braking
	 */
	protected void jumpStart() throws Throwable {
		LOGGER.info( "\n======== create instances to get the ball rolling  ======== ");

		brakingId = BrakingBusinessDelegate.getBrakingInstance()
		.createBraking( generateNewCommand() )
		.get();

		// ---------------------------------------------
		// set up query subscriptions after the 1st create
		// ---------------------------------------------
		testingStep = "create";
		setUpQuerySubscriptions();

		BrakingBusinessDelegate.getBrakingInstance()
				.createBraking( generateNewCommand() )
				.get();

	}

	/** 
	 * Set up query subscriptions
	 */
	protected void setUpQuerySubscriptions() throws Throwable {
		LOGGER.info( "\n======== Setting Up Query Subscriptions ======== ");
			
		try {            
			subscriber.brakingSubscribe().updates().subscribe(
					  successValue -> {
						  LOGGER.info(successValue.toString());
						  try {
							  LOGGER.info("GetAll update received for Braking : " + successValue.getBrakingId());
							  if (successValue.getBrakingId().equals(brakingId)) {
								  if (testingStep.equals("create")) {
									  testingStep = "update";
									  update();
								  } else if (testingStep.equals("delete")) {
									  testingStep = "complete";
									  state( getAll().size() == sizeOfBrakingList - 1 , "value not deleted from list");
									  LOGGER.info("**********************************************************");
									  LOGGER.info("Braking test completed successfully...");
									  LOGGER.info("**********************************************************\n");
								  }
							  }
						  } catch( Throwable exc ) {
							  LOGGER.warning( exc.getMessage() );
						  }
					  },
					  error -> LOGGER.warning(error.getMessage()),
					  () -> LOGGER.info("Subscription on braking consumed")
					);
			subscriber.brakingSubscribe( brakingId ).updates().subscribe(
					  successValue -> {
						  LOGGER.info(successValue.toString());
						  try {
							  LOGGER.info("GetOne update received for Braking : " + successValue.getBrakingId() + " in step " + testingStep);
							  testingStep = "delete";
							  sizeOfBrakingList = getAll().size();
							  delete();
						  } catch( Throwable exc ) {
							  LOGGER.warning( exc.getMessage() );
						  }
					  },
					  error -> LOGGER.warning(error.getMessage()),
					  () -> LOGGER.info("Subscription on braking for brakingId consumed")

					);
			

			}
			catch (Exception e) {
				LOGGER.warning( e.getMessage() );
				throw e;
			}
		}
		
		/** 
	 * read a Braking. 
	 */
	protected Braking read() throws Throwable {
		LOGGER.info( "\n======== READ ======== ");
		LOGGER.info( "-- Reading a previously created Braking" );

		Braking entity = null;
		StringBuilder msg = new StringBuilder( "-- Failed to read Braking with primary key" );
		msg.append( brakingId );
		
		BrakingFetchOneSummary fetchOneSummary = new BrakingFetchOneSummary( brakingId );

		try {
			entity = BrakingBusinessDelegate.getBrakingInstance().getBraking( fetchOneSummary );

			assertNotNull( entity,msg.toString() );

			LOGGER.info( "-- Successfully found Braking " + entity.toString() );
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( msg.toString() + " : " + e );

			throw e;
		}
		
		return entity;
	}

	/** 
	 * updating a Braking.
	 */
	protected void update() throws Throwable {
		LOGGER.info( "\n======== UPDATE ======== ");
		LOGGER.info( "-- Attempting to update a Braking." );

		StringBuilder msg = new StringBuilder( "Failed to update a Braking : " );        
		Braking entity = read();
		RefreshBrakingCommand command = generateUpdateCommand();
		command.setBrakingId(entity.getBrakingId());

		try {            
			assertNotNull( entity, msg.toString() );

			LOGGER.info( "-- Now updating the created Braking." );

			// for use later on...
			brakingId = entity.getBrakingId();

			BrakingBusinessDelegate proxy = BrakingBusinessDelegate.getBrakingInstance();  

			proxy.updateBraking( command ).get();

			LOGGER.info( "-- Successfully saved Braking - " + entity.toString() );
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( msg.toString() + " : primarykey = " + brakingId + " : command -" +  command + " : " + e );

			throw e;
		}
	}

	/** 
	 * delete a Braking.
	 */
	protected void delete() throws Throwable {
		LOGGER.info( "\n======== DELETE ======== ");
		LOGGER.info( "-- Deleting a previously created Braking." );

		Braking entity = null;
		
		try{
		    entity = read(); 
			LOGGER.info( "-- Successfully read Braking with id " + brakingId );            
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( "-- Failed to read Braking with id " + brakingId );

			throw e;
		}

		try{
			BrakingBusinessDelegate.getBrakingInstance().delete( new CloseBrakingCommand( entity.getBrakingId() ) ).get();
			LOGGER.info( "-- Successfully deleted Braking with id " + brakingId );            
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( "-- Failed to delete Braking with id " + brakingId );

			throw e;
		}
	}

	/**
	 * get all Brakings.
	 */
	protected List<Braking> getAll() throws Throwable {    
		LOGGER.info( "======== GETALL ======== ");
		LOGGER.info( "-- Retrieving Collection of Brakings:" );

		StringBuilder msg = new StringBuilder( "-- Failed to get all Braking : " );        
		List<Braking> collection  = new ArrayList<>();

		try {
			// call the static get method on the BrakingBusinessDelegate
			collection = BrakingBusinessDelegate.getBrakingInstance().getAllBraking();
			assertNotNull( collection, "An Empty collection of Braking was incorrectly returned.");
			
			// Now print out the values
			Braking entity = null;            
			Iterator<Braking> iter = collection.iterator();
			int index = 1;

			while( iter.hasNext() ) {
				// Retrieve the entity   
				entity = iter.next();

				assertNotNull( entity,"-- null entity in Collection." );
				assertNotNull( entity.getBrakingId(), "-- entity in Collection has a null primary key" );        

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
	 * @return		BrakingTest
	 */
	protected BrakingTest setHandler(Handler handler) {
		if ( handler != null )
			LOGGER.addHandler(handler); 
		return this;
	}

	/**
	 * Returns a new populated Braking
	 * 
	 * @return CreateBrakingCommand alias
	 */
	protected CreateBrakingCommand generateNewCommand() {
        CreateBrakingCommand command = new CreateBrakingCommand( null,  org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(16),  org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(16),  new Plant(),  BrakingType.values()[0] );
		
		return( command );
	}

		/**
		 * Returns a new populated Braking
		 * 
		 * @return RefreshBrakingCommand alias
		 */
	protected RefreshBrakingCommand generateUpdateCommand() {
	        RefreshBrakingCommand command = new RefreshBrakingCommand( null,  org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(16),  org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(16),  new Plant(),  BrakingType.values()[0] );
			
			return( command );
		}
	//-----------------------------------------------------
	// attributes 
	//-----------------------------------------------------
	protected UUID brakingId = null;
	protected BrakingSubscriber subscriber = null;
	private final String unexpectedErrorMsg = ":::::::::::::: Unexpected Error :::::::::::::::::";
	private final Logger LOGGER = Logger.getLogger(BrakingTest.class.getName());
	private String testingStep = "";
	private Integer sizeOfBrakingList = 0;
}
