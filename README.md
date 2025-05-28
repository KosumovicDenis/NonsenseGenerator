# Get Started
Welcome to **NonsenseGenerator**, a playful and creative application designed to generate quirky, unexpected sentences based on random grammatical structures. Whether you're a writer looking for prompts, a developer exploring language generation, or just someone in need of a good chuckle — this tool is for you.


![NonsenseGenerator Banner](/deliverables/img/preview.png)

> *NonsenseGenerator app – Where grammar meets absurdity.*


NonsenseGenerator takes simple text input and turns it into wild, sometimes poetic nonsense using structured templates, a randomized dictionary, and optional syntax trees. It features an easy-to-use graphical interface, a customizable wordbank, and a history of all your generated gems.

Ready to dive in?


# Installation Manual

NonsenseGenerator is a desktop Java application built using JavaFX and Maven. This project uses **Java 21**, **JavaFX 21**.

## Requirements

Ensure the following tools are installed on your system:

- **Java Development Kit (JDK) 21**  
  Check version with:
  ```bash
  java -version
  ```
- **Apache Maven 3.6+**  
  Check version with:
  ```bash
  mvn -version
  ```

> ⚠️ *Make sure `JAVA_HOME` is set to the JDK 21 installation directory.*

## Clone the Repository

```bash
git clone https://github.com/KosumovicDenis/NonsenseGenerator.git
cd NonsenseGenerator
```
## Environment Variables

This project requires an API key to access external services. You must define the following environment variable:

```
API_KEY=your_api_key_here
```

To configure it:

1. Copy the `.env.example` file to a new file named `.env`:
   ```bash
   cp .env.example .env
   ```

2. Open `.env` and replace `your_api_key_here` with your actual key.

## Build the Project

To compile the project and run tests:

```bash
mvn clean install
```

## Download Dependencies

Before running the application for the first time, make sure all Maven dependencies are downloaded:

```bash
mvn dependency:resolve
```

## Run the Application

You can launch the application using the included JavaFX plugin:

```bash
mvn javafx:run
```

## OS-Specific Instructions

### 🐧 Linux

On Linux systems, make sure your environment supports JavaFX rendering and has the required native libraries:

- Dependencies
    Install required libraries via APT (for Debian/Ubuntu-based distros):
    ```bash
    sudo apt update
    sudo apt install openjfx libgl1-mesa-glx fonts-dejavu
    ```

- Notes:
    - JavaFX may not be bundled with OpenJDK on Linux — installing `openjfx` separately ensures availability of rendering components.
    - On some distributions (like Arch), package names may differ.

- Run the App:
    ```bash
    mvn javafx:run
    ```

### 🪟 Windows

- Setup:
    1. Download and install **JDK 21** from [Adoptium](https://adoptium.net) or Oracle.
    2. Set the `JAVA_HOME` environment variable:
       - Go to System Properties → Environment Variables.
       - Add `JAVA_HOME` pointing to your JDK installation folder.
       - Add `%JAVA_HOME%\bin` to your system `Path`.

- Example:
    ```cmd
    set JAVA_HOME=C:\Program Files\Java\jdk-21
    mvn javafx:run
    ```

### 🍎 macOS

- Install JDK and Maven via Homebrew:
    ```bash
    brew install openjdk@21 maven
    ```

- Set Environment Variables (add to `.zshrc` or `.bash_profile`):
    ```bash
    export JAVA_HOME=$(/usr/libexec/java_home -v21)
    export PATH=$JAVA_HOME/bin:$PATH
    ```

    If using Homebrew-managed OpenJDK:
    ```bash
    export JAVA_HOME=/opt/homebrew/opt/openjdk@21
    export PATH="$JAVA_HOME/bin:$PATH"
    ```

- Run the App:
    ```bash
    mvn javafx:run
    ```

- Notes:
    - On macOS with Apple Silicon (M1/M2), make sure to use the correct architecture build of the JDK (ARM64).


## Run Tests

To run unit tests:

```bash
mvn test
```

## Project Structure

- **Main class:**
    - `unipd.ddkk.core`
    - `unipd.ddkk.gui`
- **Key dependencies:**
  - `javafx-controls`
  - `jackson-databind`
  - `atlantafx-base`
  - `junit-jupiter` for testing

## License

This project is licensed under the MIT License. See `LICENSE` for details.


## Maven-generated Website

**[Website Link](https://nonsensegeneratorsite.netlify.app/site/index.html)**

[![Maven Website Link](/deliverables/img/maven-website.png)](https://nonsensegeneratorsite.netlify.app/site/index.html)

## JUnit Tests Website

[Surefire Report Link](https://nonsensegeneratorsite.netlify.app/reports/surefire.html)

[![Surefire Report Link](/deliverables/img/surefire-report.png)](https://nonsensegeneratorsite.netlify.app/reports/surefire.html)
