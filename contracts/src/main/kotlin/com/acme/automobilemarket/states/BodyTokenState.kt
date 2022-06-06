package com.acme.automobilemarket.states

import com.r3.corda.lib.tokens.contracts.states.EvolvableTokenType
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.Party
import com.acme.automobilemarket.contracts.BodyContract
import com.acme.api.*

@BelongsToContract(BodyContract::class)
class BodyTokenState(val maintainer: Party,
                      override val linearId: UniqueIdentifier,
                      override val fractionDigits: Int,                     
					  val name: String? = null, val plant: Plant? = null,
                      override val maintainers: List<Party> = listOf(maintainer)) : EvolvableTokenType()
