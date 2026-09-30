package com.tony3974.gasstationcal.model

import java.util.Locale
import kotlin.math.abs

/**
 * User inputs for gas station calculations.
 */
data class GasCalculatorInput(
    val fuelEfficiencyLPer100Km: Double = 8.5, // Liters per 100 km
    val litersToFill: Double = 50.0,            // Liters to fill up
    val station1DistanceKm: Double = 3.0,       // Distance to Station 1 (km)
    val station1PricePerLiter: Double = 1.65,   // Price at Station 1 ($/L)
    val station2DistanceKm: Double = 12.0,      // Distance to Station 2 (km)
    val station2PricePerLiter: Double = 1.55,   // Price at Station 2 ($/L)
    val isRoundTrip: Boolean = true             // Round trip drive
)

/**
 * Detailed breakdown of comparison results between Station 1 and Station 2.
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

    fun formattedValue(value: Double): String {
        return String.format(Locale.US, "$%.2f", value)
    }
}

/**
 * Pure calculation logic for gas station comparison.
 */
object GasCalculator {

    fun calculate(input: GasCalculatorInput): CalculationResult {
        val tripMultiplier = if (input.isRoundTrip) 2.0 else 1.0
        
        // Station 1
        val totalDistance1Km = input.station1DistanceKm * tripMultiplier
        val fuelUsed1Liters = (totalDistance1Km / 100.0) * input.fuelEfficiencyLPer100Km
        val station1TripCost = fuelUsed1Liters * input.station1PricePerLiter
        val station1FillCost = input.litersToFill * input.station1PricePerLiter
        val station1TotalCost = station1FillCost + station1TripCost

        // Station 2
        val totalDistance2Km = input.station2DistanceKm * tripMultiplier
        val fuelUsed2Liters = (totalDistance2Km / 100.0) * input.fuelEfficiencyLPer100Km
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
}
