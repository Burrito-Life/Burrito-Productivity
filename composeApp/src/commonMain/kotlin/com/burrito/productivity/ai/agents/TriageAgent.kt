package com.burrito.productivity.ai.agents

/**
 * Agent responsible for analyzing the user's input and deciding which capability/model is best suited for the job.
 * Now returns a dynamic workflow plan.
 */
class TriageAgent {
    data class TriagedRequest(
        val originalInput: String,
        val steps: List<WorkflowStep>,
        val estimatedComplexity: String = "medium" // TODO: standardize with an enum or similar
    )

    /*
    * TODO: Write a description of this method
    * TODO: Use SemanticContext here
    */
    suspend fun triage(userInput: String): TriagedRequest {
        // In a real implementation, this logic would be driven by an LLM or rules engine
        // to detect intents like "draw a diagram" vs "explain this code".
        
        val steps = mutableListOf<WorkflowStep>()
        
        // Default "Smart Chat" Flow
        // TODO: Rather than "hard code" building the list of WorkflowSteps, call a 1B parameter AI model (locally run on the device's NPU or similar) to generate the list of WorkflowSteps (and `estimatedComplexity` value), with smart prompting to not only get a usable list of pre-defined step names but also to tell the model what options it has as far as tool use and AI call types and more (To Be Developed) -- one thing the AI's response must include is `searchTermsForRAGContext` as an array of strings to input into vector similarity search
        steps.add(WorkflowStep.Reason())
        steps.add(WorkflowStep.Critique())
        steps.add(WorkflowStep.Revise)
        steps.add(WorkflowStep.GenerateUI("chat_response"))
        
        return TriagedRequest(
            originalInput = userInput,
            steps = steps
        )
    }
}
