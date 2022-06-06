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

public class EngineValidator {
		
	/**
	 * default constructor
	 */
	protected EngineValidator() {	
	}
	
	/**
	 * factory method
	 */
	static public EngineValidator getInstance() {
		return new EngineValidator();
	}
		
	/**
	 * handles creation validation for a Engine
	 */
	public void validate( CreateEngineCommand engine )throws Exception {
		Assert.notNull( engine, "CreateEngineCommand should not be null" );
//		Assert.isNull( engine.getEngineId(), "CreateEngineCommand identifier should be null" );
		Assert.notNull( engine.getName(), "Field CreateEngineCommand.name should not be null" );
		Assert.notNull( engine.getSerialNum(), "Field CreateEngineCommand.serialNum should not be null" );
	}

	/**
	 * handles update validation for a Engine
	 */
	public void validate( RefreshEngineCommand engine ) throws Exception {
		Assert.notNull( engine, "RefreshEngineCommand should not be null" );
		Assert.notNull( engine.getEngineId(), "RefreshEngineCommand identifier should not be null" );
		Assert.notNull( engine.getName(), "Field RefreshEngineCommand.name should not be null" );
		Assert.notNull( engine.getSerialNum(), "Field RefreshEngineCommand.serialNum should not be null" );
    }

	/**
	 * handles delete validation for a Engine
	 */
    public void validate( CloseEngineCommand engine ) throws Exception {
		Assert.notNull( engine, "{commandAlias} should not be null" );
		Assert.notNull( engine.getEngineId(), "CloseEngineCommand identifier should not be null" );
	}
	
	/**
	 * handles fetchOne validation for a Engine
	 */
	public void validate( EngineFetchOneSummary summary ) throws Exception {
		Assert.notNull( summary, "EngineFetchOneSummary should not be null" );
		Assert.notNull( summary.getEngineId(), "EngineFetchOneSummary identifier should not be null" );
	}



}
