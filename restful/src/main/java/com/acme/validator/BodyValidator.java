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

public class BodyValidator {
		
	/**
	 * default constructor
	 */
	protected BodyValidator() {	
	}
	
	/**
	 * factory method
	 */
	static public BodyValidator getInstance() {
		return new BodyValidator();
	}
		
	/**
	 * handles creation validation for a Body
	 */
	public void validate( CreateBodyCommand body )throws Exception {
		Assert.notNull( body, "CreateBodyCommand should not be null" );
//		Assert.isNull( body.getBodyId(), "CreateBodyCommand identifier should be null" );
		Assert.notNull( body.getName(), "Field CreateBodyCommand.name should not be null" );
	}

	/**
	 * handles update validation for a Body
	 */
	public void validate( RefreshBodyCommand body ) throws Exception {
		Assert.notNull( body, "RefreshBodyCommand should not be null" );
		Assert.notNull( body.getBodyId(), "RefreshBodyCommand identifier should not be null" );
		Assert.notNull( body.getName(), "Field RefreshBodyCommand.name should not be null" );
    }

	/**
	 * handles delete validation for a Body
	 */
    public void validate( CloseBodyCommand body ) throws Exception {
		Assert.notNull( body, "{commandAlias} should not be null" );
		Assert.notNull( body.getBodyId(), "CloseBodyCommand identifier should not be null" );
	}
	
	/**
	 * handles fetchOne validation for a Body
	 */
	public void validate( BodyFetchOneSummary summary ) throws Exception {
		Assert.notNull( summary, "BodyFetchOneSummary should not be null" );
		Assert.notNull( summary.getBodyId(), "BodyFetchOneSummary identifier should not be null" );
	}



}
