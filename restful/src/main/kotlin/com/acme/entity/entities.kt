/*******************************************************************************
  Oldsmobile Motor Corporation Confidential
  
  2018 Oldsmobile Motor Corporation
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Oldsmobile Motor Corporation - General Release
 ******************************************************************************/
package com.acme.entity;

import java.util.*

import javax.persistence.*
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery

import com.acme.api.*;

// --------------------------------------------
// Entity Definitions
// --------------------------------------------
@Entity
@NamedQueries(
        NamedQuery(
                name = "Chassis.findByNameLike",
            query = "SELECT c FROM Chassis c WHERE c.name = (:name) ORDER BY c.name"
        ),
        NamedQuery(
                name = "Chassis.findBySerialNum",
            query = "SELECT c FROM Chassis c WHERE c.serialNum = (:serialNum) ORDER BY c.serialNum"
        ),
        NamedQuery(
                name = "Chassis.findByType",
            query = "SELECT c FROM Chassis c WHERE c.type = (:type) ORDER BY c.type"
        )
)
data class Chassis(
    @Id var chassisId: UUID? = null,
    var name: String? = null,
    var serialNum: String? = null,
     @Embedded     @AttributeOverrides(
      AttributeOverride( name = "plantNo", column = Column(name = "plant_plantNo")),
      AttributeOverride( name = "street", column = Column(name = "plant_street")),
      AttributeOverride( name = "city", column = Column(name = "plant_city")),
      AttributeOverride( name = "state", column = Column(name = "plant_state")),
      AttributeOverride( name = "zipCode", column = Column(name = "plant_zipCode"))
    )
    var plant: Plant? = null,
    @Enumerated(EnumType.STRING) var type: ChassisType? = null
)

@Entity
data class Body(
    @Id var bodyId: UUID? = null,
    var name: String? = null,
     @Embedded     @AttributeOverrides(
      AttributeOverride( name = "plantNo", column = Column(name = "plant_plantNo")),
      AttributeOverride( name = "street", column = Column(name = "plant_street")),
      AttributeOverride( name = "city", column = Column(name = "plant_city")),
      AttributeOverride( name = "state", column = Column(name = "plant_state")),
      AttributeOverride( name = "zipCode", column = Column(name = "plant_zipCode"))
    )
    var plant: Plant? = null
)

@Entity
data class Engine(
    @Id var engineId: UUID? = null,
    var name: String? = null,
    var serialNum: String? = null,
     @Embedded     @AttributeOverrides(
      AttributeOverride( name = "plantNo", column = Column(name = "plant_plantNo")),
      AttributeOverride( name = "street", column = Column(name = "plant_street")),
      AttributeOverride( name = "city", column = Column(name = "plant_city")),
      AttributeOverride( name = "state", column = Column(name = "plant_state")),
      AttributeOverride( name = "zipCode", column = Column(name = "plant_zipCode"))
    )
    var plant: Plant? = null,
    @Enumerated(EnumType.STRING) var type: EngineType? = null
)

@Entity
data class Transmission(
    @Id var transmissionId: UUID? = null,
    var name: String? = null,
    var serialNum: String? = null,
     @Embedded     @AttributeOverrides(
      AttributeOverride( name = "plantNo", column = Column(name = "plant_plantNo")),
      AttributeOverride( name = "street", column = Column(name = "plant_street")),
      AttributeOverride( name = "city", column = Column(name = "plant_city")),
      AttributeOverride( name = "state", column = Column(name = "plant_state")),
      AttributeOverride( name = "zipCode", column = Column(name = "plant_zipCode"))
    )
    var plant: Plant? = null,
    @Enumerated(EnumType.STRING) var type: TransmissionType? = null
)

@Entity
data class Braking(
    @Id var brakingId: UUID? = null,
    var serialNum: String? = null,
    var name: String? = null,
     @Embedded     @AttributeOverrides(
      AttributeOverride( name = "plantNo", column = Column(name = "plant_plantNo")),
      AttributeOverride( name = "street", column = Column(name = "plant_street")),
      AttributeOverride( name = "city", column = Column(name = "plant_city")),
      AttributeOverride( name = "state", column = Column(name = "plant_state")),
      AttributeOverride( name = "zipCode", column = Column(name = "plant_zipCode"))
    )
    var plant: Plant? = null,
    @Enumerated(EnumType.STRING) var type: BrakingType? = null
)

@Entity
data class Interior(
    @Id var interiorId: UUID? = null,
    var serialNum: String? = null,
    var name: String? = null,
     @Embedded     @AttributeOverrides(
      AttributeOverride( name = "plantNo", column = Column(name = "plant_plantNo")),
      AttributeOverride( name = "street", column = Column(name = "plant_street")),
      AttributeOverride( name = "city", column = Column(name = "plant_city")),
      AttributeOverride( name = "state", column = Column(name = "plant_state")),
      AttributeOverride( name = "zipCode", column = Column(name = "plant_zipCode"))
    )
    var plant: Plant? = null
)

