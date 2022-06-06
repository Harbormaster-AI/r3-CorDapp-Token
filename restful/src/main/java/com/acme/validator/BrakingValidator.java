/*******************************************************************************
  Oldsmobile Motor Corporation Confidential
  
  2018 Oldsmobile Motor Corporation
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Oldsmobile Motor Corporation - General Release
 ******************************************************************************/
package com.acme.validator;

import org.springframework.util.Assert;

import com.acme.api.*;

public class BrakingValidator {
		
	/**
	 * default constructor
	 */
	protected BrakingValidator() {	
	}
	
	/**
	 * factory method
	 */
	static public BrakingValidator getInstance() {
		return new BrakingValidator();
	}
		
	/**
	 * handles creation validation for a Braking
	 */
	public void validate( CreateBrakingCommand braking )throws Exception {
		Assert.notNull( braking, "CreateBrakingCommand should not be null" );
//		Assert.isNull( braking.getBrakingId(), "CreateBrakingCommand identifier should be null" );
		Assert.notNull( braking.getSerialNum(), "Field CreateBrakingCommand.serialNum should not be null" );
		Assert.notNull( braking.getName(), "Field CreateBrakingCommand.name should not be null" );
	}

	/**
	 * handles update validation for a Braking
	 */
	public void validate( RefreshBrakingCommand braking ) throws Exception {
		Assert.notNull( braking, "RefreshBrakingCommand should not be null" );
		Assert.notNull( braking.getBrakingId(), "RefreshBrakingCommand identifier should not be null" );
		Assert.notNull( braking.getSerialNum(), "Field RefreshBrakingCommand.serialNum should not be null" );
		Assert.notNull( braking.getName(), "Field RefreshBrakingCommand.name should not be null" );
    }

	/**
	 * handles delete validation for a Braking
	 */
    public void validate( CloseBrakingCommand braking ) throws Exception {
		Assert.notNull( braking, "{commandAlias} should not be null" );
		Assert.notNull( braking.getBrakingId(), "CloseBrakingCommand identifier should not be null" );
	}
	
	/**
	 * handles fetchOne validation for a Braking
	 */
	public void validate( BrakingFetchOneSummary summary ) throws Exception {
		Assert.notNull( summary, "BrakingFetchOneSummary should not be null" );
		Assert.notNull( summary.getBrakingId(), "BrakingFetchOneSummary identifier should not be null" );
	}



}
