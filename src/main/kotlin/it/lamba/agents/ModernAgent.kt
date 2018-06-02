package it.lamba.agents

import it.lamba.behaviours.MessageReceiverBehaviour
import jade.core.AID
import jade.core.Agent
import jade.domain.DFService
import jade.domain.FIPAAgentManagement.DFAgentDescription
import jade.lang.acl.ACLMessage


/**
 * An extension of the Jade's [Agent] including state of the art commodity methods.
 */
abstract class ModernAgent: Agent() {
    val STD_OUT = 1
    val STD_ERR = 2

    private val behavioursMap = HashMap<Int, MessageReceiverBehaviour>().apply {
        for(i in -1 until 22 step 1)
            this[i] = MessageReceiverBehaviour(this@ModernAgent, i)
        }

    override fun setup() {
        log("Created")
        @Suppress("UNCHECKED_CAST")
        onCreate(if(arguments != null && arguments.isNotEmpty()) arguments as Array<String> else emptyArray())
    }

    override fun takeDown() {
        onDispose()
    }

    override fun doDelete() {
        stopListeningMessages()
        behavioursMap.clear()
        onDestroy()
        log("Deleted")
    }

    /**
     * Allows to log a message with a header that identifies the agent. By default [log] will print in [System.out].
     * @param message The message to be printed.
     * @param stream The output stream where the message will be printed. [STD_OUT] prints in [System.out], [STD_ERR] prints in [System.err]. Default is [STD_OUT].
     */
    fun log(message: String, stream: Int = 1){
        when(stream){
            2 -> System.err.println("[${this::class.simpleName}] ${aid.name} | $message")
            else -> System.out.println("[${this::class.simpleName}] ${aid.name} | $message")
        }
    }

    /**
     * Start listening to messages, calling [onMessageReceived] every new message. By default, every message received is processed, if you need to listen only specified message use the parameter [messageType] using values from class [ACLMessage].
     * @param messageType Message types are specified in [ACLMessage] class as public [Int]s. Default is -2 which means all messages, including unknown ones.
     */
    fun startListeningMessages(messageType: Int = -2){
        if(messageType == -2)
            for(behaviour in behavioursMap.values)
                addBehaviour(behaviour)
        else addBehaviour(behavioursMap[messageType])
    }

    /**
     * Spot listening to messages. By default, every message received will stop being processed, if you need to stop listening only specified message use the parameter [messageType] using values from class [ACLMessage].
     * @param messageType Message types are specified in [ACLMessage] class as public [Int]s. Default is -2 which means all messages, including unknown ones.
     */
    fun stopListeningMessages(messageType: Int = -2){
        if(messageType == -2)
            for(behaviour in behavioursMap.values)
                removeBehaviour(behaviour)
        else removeBehaviour(behavioursMap[messageType])
    }

    /**
     * Check if listening to messages. By default, checks if listening to EVERY message, if you need to check if listening only specified message use the parameter [messageType] using values from class [ACLMessage].
     * @param messageType Message types are specified in [ACLMessage] class as public [Int]s. Default is -2 which means all messages, including unknown ones.
     */
    fun isListeningMessages(messageType: Int = -2): Boolean{
        if(messageType == -2){
            for(behaviour in behavioursMap.values){
                if(!behaviour.isActive) return false
            }
            return true
        }
        return behavioursMap[messageType]?.isActive ?: false
    }

    /**
     * Search for agent's [AID]s through the [DFService]. This method is asynchronous using a thread.
     * @param description The [DFAgentDescription] used for the search.
     * @param onResult The callback invoked containing an [Array]<[AID]> of the agents, the array may be empty.
     */
    fun searchAgents(description: DFAgentDescription, onResult: (agentIDs: Array<AID>) -> Unit){
        Thread{
            val res = ArrayList<AID>()
            DFService.search(this, description).forEach {
                res.add(it.name)
            }
            onResult(res.toTypedArray())
        }.start()
    }

    /**
     * This method is invoked every time there is a message in queue and the agent is listening to that type of [ACLMessage]. See [startListeningMessages] for more information.
     * @param message The message received.
     */
    abstract fun onMessageReceived(message: ACLMessage)

    /**
     * This method is invoked at the creation of the agent.
     * @param args The arguments of the agent
     */
    abstract fun onCreate(args: Array<String>)

    /**
     * This method is invoked when the agent is destroyed.
     */
    abstract fun onDestroy()

    /**
     * This method is invoked when the agent is about to be destroyed. Clear any active logic here.
     */
    abstract fun onDispose()
}