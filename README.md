# IoT Application - SmartHome

SmartHome is a Internet Of Thing (IoT) application project with MVVM Design Structure by using RXJava2 with LiveData and Repository Patterns. It contains the application list which remotely controls all fixtures.

From this example, we use the APIs provide by [JSON Placeholder](http://private-1e863-house4591.apiary-mock.com/), which will return the following result: (We take fetching room facilities as example)
```
{
  "rooms": {
        "Bedroom": {
            "fixtures": [
                "Light1",
                "Light2",
                "AC"
            ]
        },
        "Living Room": {
            "fixtures": [
                "Light",
                "TV"
            ]
        },
        "Kitchen": {
            "fixtures": [
                "Light",
                "Music",
                "Slowcooker"
            ]
        }
    }
}
```

# Project Architechure
In the project, we used MVVM design pattern with Google Android Architecture Components (AAC). The application used RXJava2 to perform data binding. With Android Architecture Lifecycle, it observes and monitor the network data fetched from Fuel Library.

The following diagram shows the flow how it works in the project:

<p float="left">
  <img src="https://github.com/sunnytse0326/SmartHome/blob/master/screenshot/structure.png" width="350" height="550">
</p>


# Implementation
We use an activity which shows all room fixtures information and switches can control all facilities directly
<p float="left">
  <img src="https://github.com/sunnytse0326/SociNet/blob/master/screenshot/screenshot1.png" width="250" height="400">
  <img src="https://github.com/sunnytse0326/SociNet/blob/master/screenshot/screenshot2.png" width="250" height="400">
</p>

For the air conditioning, we use [Job Scheduler](https://github.com/evernote/android-job) to monitor the weather API periodially by 15 minutes.

# Library used:
Anko (Layout Design)
Fuel (Network Library)
RxJava2 (Data binding)



