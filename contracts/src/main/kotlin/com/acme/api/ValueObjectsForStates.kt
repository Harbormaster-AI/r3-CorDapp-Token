/*******************************************************************************
  Oldsmobile Motor Corporation Confidential
  
  2018 Oldsmobile Motor Corporation
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Oldsmobile Motor Corporation - General Release
 ******************************************************************************/
package com.acme.api

import java.util.*
import net.corda.core.serialization.CordaSerializable


// --------------------------------------------
// Value Object Definitions
// --------------------------------------------

@CordaSerializable
data class Plant(
    val plantNo: String? = null, val street: String? = null, val city: String? = null, val state: String? = null, val zipCode: String? = null)




