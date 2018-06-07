package it.lamba

import it.lamba.agents.ModernAgent
import jade.core.Agent
import jade.wrapper.AgentController
import jade.wrapper.ContainerController
import kotlin.reflect.KClass

/**
 * Creates a new JADE agent, running within this container.
 * @param agentClass The [KClass]<out [Agent]> to be added to the container.
 * @param nickname A platform-unique nickname for the newly created agent. The agent will be given a FIPA compliant agent identifier using the nickname and the ID of the platform it is running on.
 * @param args A [String] [Array], containing initialization parameters to pass to the new agent.
 * @return A proxy object, allowing to call state-transition forcing methods on the real agent instance.
 */
fun ContainerController.createNewAgent(agentClass: KClass<out ModernAgent>, nickname: String = agentClass.qualifiedName!!, args: Array<String>? = null) =
    createNewAgent(agentClass.java, nickname, args)

/**
 * Adds and run the RMA agent on given container.
 * @return The [AgentController] of the RMA instance.
 */
fun ContainerController.addRmaAgent(): AgentController  =
        createNewAgent("rma", jade.tools.rma.rma::class.qualifiedName, null).apply { start() }

/**
 * Creates a new JADE agent, running within this container.
 * @param agentClass The [Class]<out [Agent]> to be added to the container.
 * @param nickname A platform-unique nickname for the newly created agent. The agent will be given a FIPA compliant agent identifier using the nickname and the ID of the platform it is running on.
 * @param args A [String] [Array], containing initialization parameters to pass to the new agent.
 * @return A proxy object, allowing to call state-transition forcing methods on the real agent instance.
 */
fun ContainerController.createNewAgent(agentClass: Class<out ModernAgent>, nickname: String = agentClass.canonicalName, args: Array<String>? = null) =
        createNewAgent(nickname, agentClass.canonicalName, args)

/**
 * Commodity method to create and run an [Agent]. Example:
 *
 * ```
 * val controller = createNewAgent(...).run()
 * ```
 * See [createNewAgent].
 *
 * @return The [AgentController] itself.
 */
fun AgentController.run(): AgentController {
    start()
    return this
}
