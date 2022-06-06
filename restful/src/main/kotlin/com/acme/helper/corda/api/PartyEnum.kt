/*******************************************************************************
  Oldsmobile Motor Corporation Confidential
  
  2018 Oldsmobile Motor Corporation
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Oldsmobile Motor Corporation - General Release
 ******************************************************************************/
package com.acme.helper.corda.api

/**
 * Helper to represent the different types of discovered Parties -- Holders of tokens
 **/
 
// --------------------------------------------
// enum declarations
// --------------------------------------------
enum class PartyEnum {
    Notary, AutomobileCo, LicensedDealership, UsedPartsAgency, Buyer
}

