package com.tony3974.gasstationcal.viewmodel

import androidx.lifecycle.ViewModel
import com.tony3974.gasstationcal.model.CalculationResult
import com.tony3974.gasstationcal.model.GasCalculator
import com.tony3974.gasstationcal.model.GasCalculatorInput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * State representing all user input fields and current calculation results.
 */
data class CalculatorUiState(
    val fuelEfficiencyInput: String = "8.5",
    val litersToFillInput: String = "50",
    val station1DistanceInput: String = "3.0",
    val station1PriceInput: String = "1.65",
    val station2DistanceInput: String = "12.0",
    val station2PriceInput: String = "1.55",
    val isRoundTrip: Boolean = true,
    val result: CalculationResult = GasCalculator.calculate(GasCalculatorInput())
)

class GasCalculatorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    fun onFuelEfficiencyChanged(value: String) {
        _uiState.update { it.copy(fuelEfficiencyInput = value) }
        recalculate()
    }

    fun onLitersToFillChanged(value: String) {
        _uiState.update { it.copy(litersToFillInput = value) }
        recalculate()
    }

    fun onStation1DistanceChanged(value: String) {
        _uiState.update { it.copy(station1DistanceInput = value) }
        recalculate()
    }

    fun onStation1PriceChanged(value: String) {
        _uiState.update { it.copy(station1PriceInput = value) }
        recalculate()
    }

    fun onStation2DistanceChanged(value: String) {
        _uiState.update { it.copy(station2DistanceInput = value) }
        recalculate()
    }

    fun onStation2PriceChanged(value: String) {
        _uiState.update { it.copy(station2PriceInput = value) }
        recalculate()
    }

    fun onRoundTripToggled(isRoundTrip: Boolean) {
        _uiState.update { it.copy(isRoundTrip = isRoundTrip) }
        recalculate()
    }

    private fun recalculate() {
        val currentState = _uiState.value
        val input = GasCalculatorInput(
            fuelEfficiencyLPer100Km = currentState.fuelEfficiencyInput.toDoubleOrNull() ?: 0.0,
            litersToFill = currentState.litersToFillInput.toDoubleOrNull() ?: 0.0,
            station1DistanceKm = currentState.station1DistanceInput.toDoubleOrNull() ?: 0.0,
            station1PricePerLiter = currentState.station1PriceInput.toDoubleOrNull() ?: 0.0,
            station2DistanceKm = currentState.station2DistanceInput.toDoubleOrNull() ?: 0.0,
            station2PricePerLiter = currentState.station2PriceInput.toDoubleOrNull() ?: 0.0,
            isRoundTrip = currentState.isRoundTrip
        )
        val calculated = GasCalculator.calculate(input)
        _uiState.update { it.copy(result = calculated) }
    }
}
