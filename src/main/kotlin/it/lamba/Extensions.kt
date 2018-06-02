package it.lamba

import jade.core.Agent
import jade.wrapper.ContainerController
import kotlin.reflect.KClass

/**
 * Creates a new JADE agent, running within this container.
 * @param agentClass The [KClass]<out [Agent]> to be added to the container.
 * @param nickname A platform-unique nickname for the newly created agent. The agent will be given a FIPA compliant agent identifier using the nickname and the ID of the platform it is running on.
 * @param args A [String] [Array], containing initialization parameters to pass to the new agent.
 */
fun ContainerController.createNewAgent(agentClass: KClass<out Agent>, nickname: String = agentClass::class.qualifiedName!!, args: Array<String>? = null){
    createNewAgent(agentClass.java, nickname, args)
}

/**
 * Creates a new JADE agent, running within this container.
 * @param agentClass The [Class]<out [Agent]> to be added to the container.
 * @param nickname A platform-unique nickname for the newly created agent. The agent will be given a FIPA compliant agent identifier using the nickname and the ID of the platform it is running on.
 * @param args A [String] [Array], containing initialization parameters to pass to the new agent.
 */
fun ContainerController.createNewAgent(agentClass: Class<out Agent>, nickname: String = agentClass::class.qualifiedName!!, args: Array<String>? = null){
    createNewAgent(nickname, agentClass::class.qualifiedName!!, args)
}