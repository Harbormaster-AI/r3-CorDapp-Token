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

import java.util.logging.*;

/**
 * Base class for application Test classes.
 *
 * @author    Dev Team
 */
public class BaseTest
{
	/**
	 * hidden
	 */
	protected BaseTest() {
	}
	
	public static void runTheTest( Handler logHandler ) 
    {
         try {
		    new ChassisTest().setHandler(logHandler).startTest();
			Thread.sleep(timeToWait);
		    new BodyTest().setHandler(logHandler).startTest();
			Thread.sleep(timeToWait);
		    new EngineTest().setHandler(logHandler).startTest();
			Thread.sleep(timeToWait);
		    new TransmissionTest().setHandler(logHandler).startTest();
			Thread.sleep(timeToWait);
		    new BrakingTest().setHandler(logHandler).startTest();
			Thread.sleep(timeToWait);
		    new InteriorTest().setHandler(logHandler).startTest();
			Thread.sleep(timeToWait);
        } catch( Throwable exc ) {
        	exc.printStackTrace();
        }
    }
	//-----------------------------------------------------
	// attributes
	//-----------------------------------------------------
	private static final Integer timeToWait = 5000; //milliseconds
}
