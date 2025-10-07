package org.example

import dev.langchain4j.data.message.SystemMessage
import dev.langchain4j.data.message.UserMessage
import dev.langchain4j.model.chat.request.ChatRequest
import dev.langchain4j.model.gpullama3.GPULlama3ChatModel
import java.nio.file.Paths

fun main() {
    val prompt = "What is the capital of France?"
    val request =
        ChatRequest
            .builder()
            .messages(
                UserMessage.from(prompt),
                SystemMessage.from("reply with extensive sarcasm"),
            ).build()

    val modelPath = Paths.get("beehive-llama-3.2-1b-instruct-fp16.gguf")

    val model =
        GPULlama3ChatModel
            .builder()
            .modelPath(modelPath)
            .onGPU(false) // if false, runs on CPU though a lightweight implementation of llama3.java
            .build()
    val response = model.chat(request)
    println("\n" + response.aiMessage().text())
}
