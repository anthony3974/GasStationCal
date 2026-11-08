# GasStationCal

An Android app that helps you decide whether driving farther for cheaper gas is actually worth it. It also works out the fuel cost of any single trip.

## Features

### Station Comparison
Compare a closer station (baseline) against a farther one (alternative). Enter:

- Fuel efficiency (L/100 km)
- Fill amount (L)
- Distance (km) and gas price ($/L) for each station

The app shows:

- Gross pump savings
- Extra fuel cost of driving to the farther station
- Net savings, with a plain-language verdict on which station is cheaper overall
- Total expense at each station (fill cost plus trip cost)

### Trip Calculator
Enter a distance, gas price, and fuel efficiency to get:

- Estimated fuel used
- Cost per kilometer
- Total trip gas cost

Fuel efficiency is shared between both tabs, so changing it on one updates the other. Results update as you type, and the UI supports dark mode.

## How it calculates

```
fuel used   = (distance / 100) * efficiency
trip cost   = fuel used * price per liter
fill cost   = liters to fill * price per liter
total cost  = fill cost + trip cost
net savings = station 1 total - station 2 total
```

Trip cost is based on the distance you enter, so enter the round-trip distance if you want a round trip counted.

## Tech stack

- Kotlin
- Jetpack Compose with Material 3
- MVVM (`ViewModel` + `StateFlow`)
- Min SDK 24, target SDK 37

## Project structure

```
app/src/main/java/com/tony3974/gasstationcal/
├── MainActivity.kt
├── model/GasStationModels.kt          # Input/result models and calculation logic
├── viewmodel/GasCalculatorViewModel.kt
└── ui/
    ├── GasCalculatorScreen.kt
    └── theme/
```

## Building

Open the project in Android Studio and run the `app` configuration, or from the command line:

```
./gradlew assembleDebug
```

Run the unit tests with:

```
./gradlew test
```
