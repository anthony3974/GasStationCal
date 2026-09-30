package com.tony3974.gasstationcal.model

import java.util.Locale
import kotlin.math.abs

/**
 * User inputs for comparing two gas stations.
 */
data class GasCalculatorInput(
    val fuelEfficiencyLPer100Km: Double = 8.5, // Liters per 100 km
    val litersToFill: Double = 50.0,            // Liters to fill up
    val station1DistanceKm: Double = 3.0,       // Distance to Station 1 (km)
    val station1PricePerLiter: Double = 1.65,   // Price at Station 1 ($/L)
    val station2DistanceKm: Double = 12.0,      // Distance to Station 2 (km)
    val station2PricePerLiter: Double = 1.55    // Price at Station 2 ($/L)
)

/**
 * Comparison result between Station 1 and Station 2.
 */
data class CalculationResult(
    val station1FillCost: Double,
    val station1TripCost: Double,
    val station1TotalCost: Double,
    
    val station2FillCost: Double,
    val station2TripCost: Double,
    val station2TotalCost: Double,
    
    val grossPumpSavings: Double,
    val extraTravelCost: Double,
    val netSavings: Double,
    val isStation2CheaperOverall: Boolean
) {
    fun formattedNetSavings(): String {
        val absSavings = abs(netSavings)
        return String.format(Locale.US, "$%.2f", absSavings)
    }
}

/**
 * User inputs for calculating gas cost for any single trip distance.
 */
data class TripCalculatorInput(
    val tripDistanceKm: Double = 50.0,         // Distance (km)
    val gasPricePerLiter: Double = 1.65,       // Gas price ($/L)
    val fuelEfficiencyLPer100Km: Double = 8.5  // Fuel efficiency (L/100km)
)

/**
 * Result for single trip gas cost calculation.
 */
data class TripCalculationResult(
    val fuelUsedLiters: Double,
    val totalTripCost: Double,
    val costPerKm: Double
) {
    fun formattedTotalCost(): String = String.format(Locale.US, "$%.2f", totalTripCost)
    fun formattedFuelUsed(): String = String.format(Locale.US, "%.2f L", fuelUsedLiters)
    fun formattedCostPerKm(): String = String.format(Locale.US, "$%.3f / km", costPerKm)
}

/**
 * Pure calculation logic for station comparison and single trip calculations.
 */
object GasCalculator {

    fun calculateComparison(input: GasCalculatorInput): CalculationResult {
        // Station 1
        val fuelUsed1Liters = (input.station1DistanceKm / 100.0) * input.fuelEfficiencyLPer100Km
        val station1TripCost = fuelUsed1Liters * input.station1PricePerLiter
        val station1FillCost = input.litersToFill * input.station1PricePerLiter
        val station1TotalCost = station1FillCost + station1TripCost

        // Station 2
        val fuelUsed2Liters = (input.station2DistanceKm / 100.0) * input.fuelEfficiencyLPer100Km
        val station2TripCost = fuelUsed2Liters * input.station2PricePerLiter
        val station2FillCost = input.litersToFill * input.station2PricePerLiter
        val station2TotalCost = station2FillCost + station2TripCost

        // Comparison
        val grossPumpSavings = station1FillCost - station2FillCost
        val extraTravelCost = station2TripCost - station1TripCost
        val netSavings = station1TotalCost - station2TotalCost
        val isStation2CheaperOverall = netSavings > 0.0

        return CalculationResult(
            station1FillCost = station1FillCost,
            station1TripCost = station1TripCost,
            station1TotalCost = station1TotalCost,
            station2FillCost = station2FillCost,
            station2TripCost = station2TripCost,
            station2TotalCost = station2TotalCost,
            grossPumpSavings = grossPumpSavings,
            extraTravelCost = extraTravelCost,
            netSavings = netSavings,
            isStation2CheaperOverall = isStation2CheaperOverall
        )
    }

    fun calculateTrip(input: TripCalculatorInput): TripCalculationResult {
        val fuelUsedLiters = (input.tripDistanceKm / 100.0) * input.fuelEfficiencyLPer100Km
        val totalTripCost = fuelUsedLiters * input.gasPricePerLiter
        val costPerKm = if (input.tripDistanceKm > 0.0) totalTripCost / input.tripDistanceKm else 0.0

        return TripCalculationResult(
            fuelUsedLiters = fuelUsedLiters,
            totalTripCost = totalTripCost,
            costPerKm = costPerKm
        )
    }
}
