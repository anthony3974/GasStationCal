package com.tony3974.gasstationcal.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tony3974.gasstationcal.model.CalculationResult
import com.tony3974.gasstationcal.ui.theme.GasStationCalTheme
import com.tony3974.gasstationcal.viewmodel.CalculatorUiState
import com.tony3974.gasstationcal.viewmodel.GasCalculatorViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GasCalculatorScreen(
    viewModel: GasCalculatorViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gas Station Calculator") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Vehicle & Fill Details Card
            VehicleDetailsCard(
                uiState = uiState,
                onEfficiencyChanged = viewModel::onFuelEfficiencyChanged,
                onLitersChanged = viewModel::onLitersToFillChanged,
                onRoundTripToggled = viewModel::onRoundTripToggled
            )

            // Gas Station 1 Card
            StationInputCard(
                title = "Station 1 (Closer / Baseline)",
                distanceValue = uiState.station1DistanceInput,
                priceValue = uiState.station1PriceInput,
                onDistanceChanged = viewModel::onStation1DistanceChanged,
                onPriceChanged = viewModel::onStation1PriceChanged
            )

            // Gas Station 2 Card
            StationInputCard(
                title = "Station 2 (Farther / Alternative)",
                distanceValue = uiState.station2DistanceInput,
                priceValue = uiState.station2PriceInput,
                onDistanceChanged = viewModel::onStation2DistanceChanged,
                onPriceChanged = viewModel::onStation2PriceChanged
            )

            // Results Card
            ResultCard(result = uiState.result)
        }
    }
}

@Composable
fun VehicleDetailsCard(
    uiState: CalculatorUiState,
    onEfficiencyChanged: (String) -> Unit,
    onLitersChanged: (String) -> Unit,
    onRoundTripToggled: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Vehicle & Trip Settings",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = uiState.fuelEfficiencyInput,
                    onValueChange = onEfficiencyChanged,
                    label = { Text("Efficiency (L/100km)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = uiState.litersToFillInput,
                    onValueChange = onLitersChanged,
                    label = { Text("Fill Amount (Liters)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Include Round-Trip Drive",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Calculates gas for driving there and back",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = uiState.isRoundTrip,
                    onCheckedChange = onRoundTripToggled
                )
            }
        }
    }
}

@Composable
fun StationInputCard(
    title: String,
    distanceValue: String,
    priceValue: String,
    onDistanceChanged: (String) -> Unit,
    onPriceChanged: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = distanceValue,
                    onValueChange = onDistanceChanged,
                    label = { Text("Distance (km)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = priceValue,
                    onValueChange = onPriceChanged,
                    label = { Text("Gas Price ($/L)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }
        }
    }
}

@Composable
fun ResultCard(result: CalculationResult) {
    val isStation2Better = result.isStation2CheaperOverall
    val containerColor = if (isStation2Better) {
        Color(0xFFE8F5E9) // Light green
    } else {
        Color(0xFFFFF3E0) // Light orange / warning
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Recommendation Header
            Text(
                text = if (isStation2Better) {
                    "🎉 Station 2 Saves You Money!"
                } else {
                    "⚠️ Stick with Station 1"
                },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = if (isStation2Better) Color(0xFF1B5E20) else Color(0xFFE65100)
            )

            Text(
                text = if (isStation2Better) {
                    "Driving to Station 2 saves you ${result.formattedNetSavings()} net after factoring in extra gas used."
                } else {
                    "Driving farther to Station 2 costs ${result.formattedNetSavings()} MORE in fuel than you save on gas!"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            // Breakdown
            ResultRow(
                label = "Gross Pump Savings:",
                value = String.format(Locale.US, "$%.2f", result.grossPumpSavings)
            )
            ResultRow(
                label = "Extra Fuel Travel Cost:",
                value = String.format(Locale.US, "$%.2f", result.extraTravelCost)
            )
            ResultRow(
                label = "Net Savings (Station 2 vs 1):",
                value = String.format(Locale.US, "$%.2f", result.netSavings),
                isBold = true
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            ResultRow(
                label = "Station 1 Total Expense:",
                value = String.format(Locale.US, "$%.2f (Gas) + $%.2f (Drive) = $%.2f",
                    result.station1FillCost, result.station1TripCost, result.station1TotalCost)
            )
            ResultRow(
                label = "Station 2 Total Expense:",
                value = String.format(Locale.US, "$%.2f (Gas) + $%.2f (Drive) = $%.2f",
                    result.station2FillCost, result.station2TripCost, result.station2TotalCost)
            )
        }
    }
}

@Composable
fun ResultRow(
    label: String,
    value: String,
    isBold: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GasCalculatorScreenPreview() {
    GasStationCalTheme {
        GasCalculatorScreen()
    }
}
