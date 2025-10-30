
STUDENT NAME = "Per Eric Trapnes"  
STUDENT ID = "107013"

## Project description

In this project we create a digital diary. A diary is often depicted as the one place you can write all your secrets. Secret crushes, passwords or maybe recipes. Thats why the diary needs to be encrypted, password protected and at least have some safety measures implimented. So you can store your secrets like any other diary.

## Project structure

All source code is under src/main/java, with tests in src/test/java. The project is organized into clear packages:
1. Core logic (e.g., PageManager, FileManager, EncryptionManager).
2. User Interface UI classes (HomePage, LoginPage, MainPage, RegisterPage).
3. Data classes (Author, DiaryPage, Time).
4. Handles persistence of user data in JSON.
5. Stores encrypted diary entries (.txt files) and temporary draft.txt.
Design rationale:
Separation of concerns keeps UI, logic, and data models independent. All diary content is encrypted and stored with safe filenames, ensuring both usability and privacy


## Link to repository

https://github.com/NTNU-IDI/mappe-2025-Per-Eric-Trapnes

## How to run the project

What is the input and output of the program? What is the expected behaviour of the program?

## How to run the tests

Run via your IDE or mvn compile exec:java

## References

All code is writen by me with Chat-GPT explaining consepts that are outside my scope.