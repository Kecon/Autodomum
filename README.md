# Autodomum
Lightweight Java based home automation system, which easily may be extended using Java or Javascript.

## Introduction
Autodomum is built on Spring Boot and is running as a standalone web application. It requires that you have some Java knowledge and it is made for enthusiasts that want to be able to tweak their own system and host their service at home.

## GUI
Autodomum's user interface is via a very simple web page where you set your own house blueprint and place all the lamps in the interface that you want to be able to change. The easiest way to get going is to make a copy of the example application and change house.png, ai.js and update style.css to match your needs.

## Events
Autodomum has support for scheduling events depending on daylight, which is calculated depending on longitude, latitude and day of year. It's also possible to use fixed or random time events. Events may also be triggered from external sources such as an REST API by sending events to the EventComponent (requires coding).

Events may be written in Java or using Javascript via Java's Nashorn script engine. You find examples of this in ai.js in the example project.

The project also contains classes to test if a specific day is a holiday or not (covers Swedish and some west European countries). 

## Providers
The current provider to use for changing lamp states or power switches is Telldus with their product line of USB-radio controllers. More available at http://www.telldus.se

It's however easy to add other type of providers via command line or via other protocols if needed.

## Modular design
The system is designed with various artifacts that you may include in your final application.

## Requirements
The system may run on any platform that is available for Java 1.8, and it's possible to run it on a Rasperry PI.
