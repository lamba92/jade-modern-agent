package it.lamba.main

import it.lamba.addRmaAgent
import it.lamba.agents.TestAgent
import it.lamba.createNewAgent
import it.lamba.run
import jade.core.Profile
import jade.core.ProfileImpl
import jade.core.Runtime

fun main(args: Array<String>){
    val myRuntime = Runtime.instance()

    // prepare the settings for the platform that we're going to connect to
    val myProfile = ProfileImpl()
    myProfile.setParameter(Profile.MAIN_PORT, "1099")

    // create the agent container
    val mainContainer = myRuntime.createMainContainer(myProfile)
    val rmaController = mainContainer.addRmaAgent()
    val myAgentController = mainContainer.createNewAgent(TestAgent::class, "test").run()
}