# SQLite Viewer

A desktop application to view and edit SQLite databases. It is written in Java and uses the SQLite JDBC driver.

[![Java 11](https://img.shields.io/badge/Java-11-blue.svg)](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
[![SQLite 3](https://img.shields.io/badge/SQLite-3-blue.svg)](https://www.sqlite.org/index.html)
[![MIT license](https://img.shields.io/github/license/dan-koller/sqlite-viewer.svg)](LICENSE)

<img src="https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/solar.png">

![Screenshot of the text editor](./app/res/screenshot-viewer.png)

<img src="https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/solar.png">

## Requirements

- JRE 11 or higher to run the application
- JDK 11 or higher to compile the application

<img src="https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/solar.png">

## Install

- [Download](https://github.com/dan-koller/sqlite-viewer/releases/tag/v1.1.0) the .jar file from the release section
  _or_
- [Clone](https://github.com/dan-koller/sqlite-viewer) this repository and compile it yourself using `./gradlew build`

<img src="https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/solar.png">

## Usage

- Load a Sqlite `*.db` file from disk from the menu
- Open it by clicking `Connect`
- Select the table you want to edit or view
- Enter a SQL query in the text area and click `Execute` to run it
- See the result in the area below

_To get started you can download a sample file from
sqlitetutorial.net [here](https://www.sqlitetutorial.net/sqlite-sample-database/)._

<img src="https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/solar.png">

## Known issues

- On **Unix-like** systems, double-clicking the jar file does not work in certain directory structures. In this case, you
  can run the jar file from the command line using `java -jar sqlite-viewer.jar` or place the jar file somewhere else.

_Unix-like systems like **Linux** and **macOS** are - unlike Windows - using the home directory as the current working directory
when double-clicking a jar file._

<img src="https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/solar.png">

## Technologies

- Java 11
- Gradle 7.4.1
- SQLite 3

<img src="https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/solar.png">

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
