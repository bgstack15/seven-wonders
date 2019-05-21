package org.luxons.sevenwonders.game.resources

import org.luxons.sevenwonders.game.api.resources.Provider
import org.luxons.sevenwonders.game.api.resources.ResourceTransaction
import org.luxons.sevenwonders.game.api.resources.ResourceTransactions
import org.luxons.sevenwonders.game.api.resources.ResourceType

class TradingRules internal constructor(private val defaultCost: Int) {

    private val costs: MutableMap<ResourceType, MutableMap<Provider, Int>> = mutableMapOf()

    fun getCosts(): Map<ResourceType, Map<Provider, Int>> {
        return costs
    }

    internal fun getCost(type: ResourceType, provider: Provider): Int =
        costs.computeIfAbsent(type) { mutableMapOf() }.getOrDefault(provider, defaultCost)

    internal fun setCost(type: ResourceType, provider: Provider, cost: Int) {
        costs.computeIfAbsent(type) { mutableMapOf() }[provider] = cost
    }

    internal fun computeCost(transactions: ResourceTransactions): Int = transactions.map { computeCost(it) }.sum()

    internal fun computeCost(transact: ResourceTransaction) = computeCost(transact.asResources(), transact.provider)

    private fun computeCost(resources: Resources, provider: Provider): Int =
        resources.quantities.map { (type, qty) -> getCost(type, provider) * qty }.sum()
}
