package com.tony3974.gasstationcal.viewmodel

import androidx.lifecycle.ViewModel
import com.tony3974.gasstationcal.model.CalculationResult
import com.tony3974.gasstationcal.model.GasCalculator
import com.tony3974.gasstationcal.model.GasCalculatorInput
import com.tony3974.gasstationcal.model.TripCalculationResult
import com.tony3974.gasstationcal.model.TripCalculatorInput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * State representing user inputs for both Station Comparison and Trip Distance Calculator.
 */
data class CalculatorUiState(
    val selectedTabIndex: Int = 0,
    
    // Station Comparison Inputs
    val fuelEfficiencyInput: String = "8.5",
    val litersToFillInput: String = "50",
    val station1DistanceInput: String = "3.0",
    val station1PriceInput: String = "1.65",
    val station2DistanceInput: String = "12.0",
    val station2PriceInput: String = "1.55",
    val comparisonResult: CalculationResult = GasCalculator.calculateComparison(GasCalculatorInput()),

    // Trip Distance Calculator Inputs
    val tripDistanceInput: String = "50.0",
    val tripPriceInput: String = "1.65",
    val tripEfficiencyInput: String = "8.5",
    val tripResult: TripCalculationResult = GasCalculator.calculateTrip(TripCalculatorInput())
)

class GasCalculatorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
    }

    // Station Comparison Actions
    fun onFuelEfficiencyChanged(value: String) {
        _uiState.update { 
            it.copy(
                fuelEfficiencyInput = value,
                tripEfficiencyInput = value // keep fuel efficiency synced for convenience
            ) 
        }
        recalculateComparison()
        recalculateTrip()
    }

    fun onLitersToFillChanged(value: String) {
        _uiState.update { it.copy(litersToFillInput = value) }
        recalculateComparison()
    }

    fun onStation1DistanceChanged(value: String) {
        _uiState.update { it.copy(station1DistanceInput = value) }
        recalculateComparison()
    }

    fun onStation1PriceChanged(value: String) {
        _uiState.update { it.copy(station1PriceInput = value) }
        recalculateComparison()
    }

    fun onStation2DistanceChanged(value: String) {
        _uiState.update { it.copy(station2DistanceInput = value) }
        recalculateComparison()
    }

    fun onStation2PriceChanged(value: String) {
        _uiState.update { it.copy(station2PriceInput = value) }
        recalculateComparison()
    }

    // Trip Distance Actions
    fun onTripDistanceChanged(value: String) {
        _uiState.update { it.copy(tripDistanceInput = value) }
        recalculateTrip()
    }

    fun onTripPriceChanged(value: String) {
        _uiState.update { it.copy(tripPriceInput = value) }
        recalculateTrip()
    }

    fun onTripEfficiencyChanged(value: String) {
        _uiState.update { 
            it.copy(
                tripEfficiencyInput = value,
                fuelEfficiencyInput = value
            ) 
        }
        recalculateTrip()
        recalculateComparison()
    }

    private fun recalculateComparison() {
        val currentState = _uiState.value
        val input = GasCalculatorInput(
            fuelEfficiencyLPer100Km = currentState.fuelEfficiencyInput.toDoubleOrNull() ?: 0.0,
            litersToFill = currentState.litersToFillInput.toDoubleOrNull() ?: 0.0,
            station1DistanceKm = currentState.station1DistanceInput.toDoubleOrNull() ?: 0.0,
            station1PricePerLiter = currentState.station1PriceInput.toDoubleOrNull() ?: 0.0,
            station2DistanceKm = currentState.station2DistanceInput.toDoubleOrNull() ?: 0.0,
            station2PricePerLiter = currentState.station2PriceInput.toDoubleOrNull() ?: 0.0
        )
        val calculated = GasCalculator.calculateComparison(input)
        _uiState.update { it.copy(comparisonResult = calculated) }
    }

    private fun recalculateTrip() {
        val currentState = _uiState.value
        val input = TripCalculatorInput(
            tripDistanceKm = currentState.tripDistanceInput.toDoubleOrNull() ?: 0.0,
            gasPricePerLiter = currentState.tripPriceInput.toDoubleOrNull() ?: 0.0,
            fuelEfficiencyLPer100Km = currentState.tripEfficiencyInput.toDoubleOrNull() ?: 0.0
        )
        val calculated = GasCalculator.calculateTrip(input)
        _uiState.update { it.copy(tripResult = calculated) }
    }
}
