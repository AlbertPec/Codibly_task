# Codibly_task

## 📟 Energy Analysis Service Documentation

Energy analysis service handles user management, including account creation, profile management, user preferences management, and storing
 - Returns data for three days with calculated average shares of individual energy sources and the percentage of clean energy.
 - Based on forecast data for two consecutive days, it determines the period when the share of clean energy (biomass, nuclear, hydro, wind, solar) is highest, making charging electric cars most optimal.

 ### Endpoints

 #### 1) GET /energy/energy-mix

Returns average energy mix and average green energy share for:

- today
- tomorrow
- the day after tomorrow


Responses:

 - 200 OK:

```json
{
  "today": {
    "date": "2026-01-13",
    "avgFuelShare": [
      {
        "fuel": "hydro",
        "perc": 0
      },
      {
        "fuel": "other",
        "perc": 0
      },
      {
        "fuel": "biomass",
        "perc": 7.7125
      },
      {
        "fuel": "imports",
        "perc": 10.01875
      },
      {
        "fuel": "gas",
        "perc": 38.11875
      },
      {
        "fuel": "solar",
        "perc": 0.533333333333333
      },
      {
        "fuel": "coal",
        "perc": 0
      },
      {
        "fuel": "nuclear",
        "perc": 11.1770833333333
      },
      {
        "fuel": "wind",
        "perc": 32.4
      }
    ],
    "cleanEnergyPercentage": 51.8229166666667
  },
  "tomorrow": {...},
  "afterTomorrow": {...},
}
```

 - 500 INTERNAL_SERVER_ERROR

#### 2) GET /energy/optimal-charging-window

Finds the time window with the highest average green energy share in the next 48 hours.

Params: 
- windowHours : int

Responses:

- 200 OK:

```json
{
  "windowDateTimeStart": "2026-01-14T21:30:00",
  "windowDateTimeEnd": "2026-01-15T03:30:00",
  "avgCleanEnergyPercentage": 65.6
}
```

 - 400 BAD_REQUEST

 - 500 INTERNAL_SERVER_ERROR

 ### External API 

https://carbonintensity.github.io/api-definitions/?shell#get-generation-from-to

 The API provides data on the UK's energy mix in half-hourly intervals. This should be taken into account in the calculations – for example, for 3 hours, 6 intervals are required.