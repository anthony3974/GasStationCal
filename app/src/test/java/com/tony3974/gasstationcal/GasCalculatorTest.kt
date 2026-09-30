package com.tony3974.gasstationcal

import com.tony3974.gasstationcal.model.GasCalculator
import com.tony3974.gasstationcal.model.GasCalculatorInput
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GasCalculatorTest {

    @Test
    fun testGasCalculator_Station2IsCheaper() {
        // Given: Buying 50L. Efficiency 10 L/100km.
        // Station 1: 5 km away, $1.60 / L
        // Station 2: 15 km away, $1.40 / L (20 cents cheaper)
        // Round trip:
        // Station 1: 10 km total drive -> 1.0 L burned -> $1.60 trip cost. 50L fill = $80.00. Total = $81.60.
        // Station 2: 30 km total drive -> 3.0 L burned -> $4.20 trip cost. 50L fill = $70.00. Total = $74.20.
        // Net savings = $81.60 - $74.20 = $7.40.
        val input = GasCalculatorInput(
            fuelEfficiencyLPer100Km = 10.0,
            litersToFill = 50.0,
            station1DistanceKm = 5.0,
            station1PricePerLiter = 1.60,
            station2DistanceKm = 15.0,
            station2PricePerLiter = 1.40,
            isRoundTrip = true
        )

        val result = GasCalculator.calculate(input)

        assertEquals(80.00, result.station1FillCost, 0.01)
        assertEquals(1.60, result.station1TripCost, 0.01)
        assertEquals(81.60, result.station1TotalCost, 0.01)

        assertEquals(70.00, result.station2FillCost, 0.01)
        assertEquals(4.20, result.station2TripCost, 0.01)
        assertEquals(74.20, result.station2TotalCost, 0.01)

        assertEquals(10.00, result.grossPumpSavings, 0.01)
        assertEquals(2.60, result.extraTravelCost, 0.01)
        assertEquals(7.40, result.netSavings, 0.01)
        assertTrue(result.isStation2CheaperOverall)
    }

    @Test
    fun testGasCalculator_Station2IsNotWorthItSmallFillUp() {
        // Given: Buying only 10L.
        // Station 1: 2 km away, $1.60 / L
        // Station 2: 25 km away, $1.55 / L (5 cents cheaper)
        // Round trip efficiency 10 L/100km:
        // Station 1 drive: 4 km -> 0.4 L * $1.60 = $0.64. Fill: 10L * $1.60 = $16.00. Total = $16.64.
        // Station 2 drive: 50 km -> 5.0 L * $1.55 = $7.75. Fill: 10L * $1.55 = $15.50. Total = $23.25.
        // Driving to Station 2 costs WAY more!
        val input = GasCalculatorInput(
            fuelEfficiencyLPer100Km = 10.0,
            litersToFill = 10.0,
            station1DistanceKm = 2.0,
            station1PricePerLiter = 1.60,
            station2DistanceKm = 25.0,
            station2PricePerLiter = 1.55,
            isRoundTrip = true
        )

        val result = GasCalculator.calculate(input)

        assertFalse(result.isStation2CheaperOverall)
        assertTrue(result.netSavings < 0)
    }
}
