package com.acme.automobilemarket.states

import com.r3.corda.lib.tokens.contracts.states.EvolvableTokenType
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.Party
import com.acme.automobilemarket.contracts.ChassisContract
import com.acme.api.*

@BelongsToContract(ChassisContract::class)
class ChassisTokenState(val maintainer: Party,
                      override val linearId: UniqueIdentifier,
                      override val fractionDigits: Int,                     
					  val name: String? = null, val serialNum: String? = null, val plant: Plant? = null, val type: ChassisType? = null,
                      override val maintainers: List<Party> = listOf(maintainer)) : EvolvableTokenType()
