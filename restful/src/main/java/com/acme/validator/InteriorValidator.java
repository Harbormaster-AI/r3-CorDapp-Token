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

public class InteriorValidator {
		
	/**
	 * default constructor
	 */
	protected InteriorValidator() {	
	}
	
	/**
	 * factory method
	 */
	static public InteriorValidator getInstance() {
		return new InteriorValidator();
	}
		
	/**
	 * handles creation validation for a Interior
	 */
	public void validate( CreateInteriorCommand interior )throws Exception {
		Assert.notNull( interior, "CreateInteriorCommand should not be null" );
//		Assert.isNull( interior.getInteriorId(), "CreateInteriorCommand identifier should be null" );
		Assert.notNull( interior.getSerialNum(), "Field CreateInteriorCommand.serialNum should not be null" );
		Assert.notNull( interior.getName(), "Field CreateInteriorCommand.name should not be null" );
	}

	/**
	 * handles update validation for a Interior
	 */
	public void validate( RefreshInteriorCommand interior ) throws Exception {
		Assert.notNull( interior, "RefreshInteriorCommand should not be null" );
		Assert.notNull( interior.getInteriorId(), "RefreshInteriorCommand identifier should not be null" );
		Assert.notNull( interior.getSerialNum(), "Field RefreshInteriorCommand.serialNum should not be null" );
		Assert.notNull( interior.getName(), "Field RefreshInteriorCommand.name should not be null" );
    }

	/**
	 * handles delete validation for a Interior
	 */
    public void validate( CloseInteriorCommand interior ) throws Exception {
		Assert.notNull( interior, "{commandAlias} should not be null" );
		Assert.notNull( interior.getInteriorId(), "CloseInteriorCommand identifier should not be null" );
	}
	
	/**
	 * handles fetchOne validation for a Interior
	 */
	public void validate( InteriorFetchOneSummary summary ) throws Exception {
		Assert.notNull( summary, "InteriorFetchOneSummary should not be null" );
		Assert.notNull( summary.getInteriorId(), "InteriorFetchOneSummary identifier should not be null" );
	}



}
