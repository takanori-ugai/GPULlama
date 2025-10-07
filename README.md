# GPULlama-LangChain4j Example

This project demonstrates how to integrate [LangChain4j](https://docs.langchain4j.dev/integrations/language-models/gpullama3-java) with [GPULlama3.java](https://github.com/beehive-lab/GPULlama3.java) in a Kotlin application.

## About

[GPULlama3.java](https://github.com/beehive-lab/GPULlama3.java) utilizes [TornadoVM](https://www.tornadovm.org/) to accelerate Large Language Model (LLM) inference on heterogeneous hardware, including NVIDIA and AMD GPUs, as well as Apple Silicon, by leveraging PTX and OpenCL backends.

This example project showcases a simple command-line chat application that uses `GPULlama3.java` for high-performance inference.

## Prerequisites

Before you begin, ensure you have the following installed and configured:

*   **Java Development Kit (JDK)**
*   **TornadoVM**: Follow the [setup and configuration instructions](https://github.com/beehive-lab/GPULlama3.java?tab=readme-ov-file#prerequisites) to properly configure TornadoVM.

## How to Run

1.  **Setup TornadoVM Environment**

    Before running the application, you need to set up the TornadoVM environment variables.

    ```bash
    cd /path/to/your/TornadoVM/installation
    source setvars.sh
    ```

2.  **Build the Project**

    Build the project using the Gradle wrapper.

    ```bash
    ./gradlew build
    ```

3.  **Download the Language Model**

    Download the required Llama 3.2 1B Instruct GGUF model.

    ```bash
    wget https://huggingface.co/beehive-lab/Llama-3.2-1B-Instruct-GGUF-FP16/resolve/main/beehive-llama-3.2-1b-instruct-fp16.gguf
    ```

4.  **Run the Application**

    You can run the application in two ways:

    *   **Using the `tornado` command:**

        ```bash
        tornado -jar build/libs/GPULlama-1.0-SNAPSHOT-all.jar
        ```

    *   **Using a custom script with `java`:**

        This approach gives you more control over the Java flags.

        ```bash
        # Generate the execution script
        tornado --printJavaFlags > execute.sh

        # Make the script executable
        chmod +x execute.sh

        # (Optional) Edit execute.sh to pass arguments if your application needs them.
        # For this example, no arguments are needed.

        # Run the application
        ./execute.sh -jar build/libs/GPULlama-1.0-SNAPSHOT-all.jar
        ```

## How it Works

The application loads the GGUF model and uses the `ChatLanguageModel` from LangChain4j to interact with the user. The `GPULlama3ChatLanguageModel` is a custom implementation that bridges LangChain4j with the GPULlama3.java library for accelerated inference.
