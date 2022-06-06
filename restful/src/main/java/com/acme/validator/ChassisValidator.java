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

public class ChassisValidator {
		
	/**
	 * default constructor
	 */
	protected ChassisValidator() {	
	}
	
	/**
	 * factory method
	 */
	static public ChassisValidator getInstance() {
		return new ChassisValidator();
	}
		
	/**
	 * handles creation validation for a Chassis
	 */
	public void validate( CreateChassisCommand chassis )throws Exception {
		Assert.notNull( chassis, "CreateChassisCommand should not be null" );
//		Assert.isNull( chassis.getChassisId(), "CreateChassisCommand identifier should be null" );
		Assert.notNull( chassis.getName(), "Field CreateChassisCommand.name should not be null" );
		Assert.notNull( chassis.getSerialNum(), "Field CreateChassisCommand.serialNum should not be null" );
	}

	/**
	 * handles update validation for a Chassis
	 */
	public void validate( RefreshChassisCommand chassis ) throws Exception {
		Assert.notNull( chassis, "RefreshChassisCommand should not be null" );
		Assert.notNull( chassis.getChassisId(), "RefreshChassisCommand identifier should not be null" );
		Assert.notNull( chassis.getName(), "Field RefreshChassisCommand.name should not be null" );
		Assert.notNull( chassis.getSerialNum(), "Field RefreshChassisCommand.serialNum should not be null" );
    }

	/**
	 * handles delete validation for a Chassis
	 */
    public void validate( CloseChassisCommand chassis ) throws Exception {
		Assert.notNull( chassis, "{commandAlias} should not be null" );
		Assert.notNull( chassis.getChassisId(), "CloseChassisCommand identifier should not be null" );
	}
	
	/**
	 * handles fetchOne validation for a Chassis
	 */
	public void validate( ChassisFetchOneSummary summary ) throws Exception {
		Assert.notNull( summary, "ChassisFetchOneSummary should not be null" );
		Assert.notNull( summary.getChassisId(), "ChassisFetchOneSummary identifier should not be null" );
	}



}
