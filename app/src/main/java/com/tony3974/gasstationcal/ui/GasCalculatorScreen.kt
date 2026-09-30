package com.tony3974.gasstationcal.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tony3974.gasstationcal.model.CalculationResult
import com.tony3974.gasstationcal.model.TripCalculationResult
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
            Column {
                TopAppBar(
                    title = { Text("Gas Station Calculator") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
                PrimaryTabRow(selectedTabIndex = uiState.selectedTabIndex) {
                    Tab(
                        selected = uiState.selectedTabIndex == 0,
                        onClick = { viewModel.onTabSelected(0) },
                        text = { Text("Station Comparison") }
                    )
                    Tab(
                        selected = uiState.selectedTabIndex == 1,
                        onClick = { viewModel.onTabSelected(1) },
                        text = { Text("Trip Calculator") }
                    )
                }
            }
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
            if (uiState.selectedTabIndex == 0) {
                StationComparisonTab(
                    uiState = uiState,
                    viewModel = viewModel
                )
            } else {
                TripCalculatorTab(
                    uiState = uiState,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun StationComparisonTab(
    uiState: CalculatorUiState,
    viewModel: GasCalculatorViewModel
) {
    // Vehicle & Fill Details Card
    VehicleDetailsCard(
        fuelEfficiencyInput = uiState.fuelEfficiencyInput,
        litersToFillInput = uiState.litersToFillInput,
        onEfficiencyChanged = viewModel::onFuelEfficiencyChanged,
        onLitersChanged = viewModel::onLitersToFillChanged
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
    ComparisonResultCard(result = uiState.comparisonResult)
}

@Composable
fun TripCalculatorTab(
    uiState: CalculatorUiState,
    viewModel: GasCalculatorViewModel
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
                text = "Trip Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = uiState.tripDistanceInput,
                onValueChange = viewModel::onTripDistanceChanged,
                label = { Text("Trip Distance (km)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = uiState.tripPriceInput,
                    onValueChange = viewModel::onTripPriceChanged,
                    label = { Text("Gas Price ($/L)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = uiState.tripEfficiencyInput,
                    onValueChange = viewModel::onTripEfficiencyChanged,
                    label = { Text("Efficiency (L/100km)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }
        }
    }

    TripResultCard(result = uiState.tripResult)
}

@Composable
fun VehicleDetailsCard(
    fuelEfficiencyInput: String,
    litersToFillInput: String,
    onEfficiencyChanged: (String) -> Unit,
    onLitersChanged: (String) -> Unit
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
                text = "Vehicle & Fill Settings",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = fuelEfficiencyInput,
                    onValueChange = onEfficiencyChanged,
                    label = { Text("Efficiency (L/100km)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = litersToFillInput,
                    onValueChange = onLitersChanged,
                    label = { Text("Fill Amount (L)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f),
                    singleLine = true
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
fun ComparisonResultCard(result: CalculationResult) {
    val isDark = isSystemInDarkTheme()
    val isStation2Better = result.isStation2CheaperOverall

    // Theme-safe dark & light mode colors for summary card
    val (containerColor, titleColor, textColor) = when {
        isStation2Better && isDark -> Triple(
            Color(0xFF142B18), // Dark forest green
            Color(0xFF81C784), // Soft green title
            Color(0xFFE0E0E0)  // Crisp readable text
        )
        isStation2Better && !isDark -> Triple(
            Color(0xFFE8F5E9), // Light green
            Color(0xFF1B5E20), // Dark green title
            Color(0xFF212121)  // Dark text
        )
        !isStation2Better && isDark -> Triple(
            Color(0xFF382312), // Dark brown/orange
            Color(0xFFFFB74D), // Soft orange title
            Color(0xFFE0E0E0)  // Crisp readable text
        )
        else -> Triple(
            Color(0xFFFFF3E0), // Light orange
            Color(0xFFE65100), // Dark orange title
            Color(0xFF212121)  // Dark text
        )
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
            Text(
                text = if (isStation2Better) {
                    "🎉 Station 2 Saves You Money!"
                } else {
                    "⚠️ Stick with Station 1"
                },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = titleColor
            )

            Text(
                text = if (isStation2Better) {
                    "Driving to Station 2 saves you ${result.formattedNetSavings()} net after factoring in extra gas used."
                } else {
                    "Driving farther to Station 2 costs ${result.formattedNetSavings()} MORE in fuel than you save on gas!"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )

            HorizontalDivider(color = textColor.copy(alpha = 0.25f), modifier = Modifier.padding(vertical = 4.dp))

            ResultRow(
                label = "Gross Pump Savings:",
                value = String.format(Locale.US, "$%.2f", result.grossPumpSavings),
                textColor = textColor
            )
            ResultRow(
                label = "Extra Fuel Travel Cost:",
                value = String.format(Locale.US, "$%.2f", result.extraTravelCost),
                textColor = textColor
            )
            ResultRow(
                label = "Net Savings (Station 2 vs 1):",
                value = String.format(Locale.US, "$%.2f", result.netSavings),
                isBold = true,
                textColor = textColor
            )

            HorizontalDivider(color = textColor.copy(alpha = 0.25f), modifier = Modifier.padding(vertical = 4.dp))

            ResultRow(
                label = "Station 1 Total Expense:",
                value = String.format(Locale.US, "$%.2f (Gas) + $%.2f (Drive) = $%.2f",
                    result.station1FillCost, result.station1TripCost, result.station1TotalCost),
                textColor = textColor
            )
            ResultRow(
                label = "Station 2 Total Expense:",
                value = String.format(Locale.US, "$%.2f (Gas) + $%.2f (Drive) = $%.2f",
                    result.station2FillCost, result.station2TripCost, result.station2TotalCost),
                textColor = textColor
            )
        }
    }
}

@Composable
fun TripResultCard(result: TripCalculationResult) {
    val isDark = isSystemInDarkTheme()
    val containerColor = if (isDark) Color(0xFF1E293B) else Color(0xFFE3F2FD)
    val titleColor = if (isDark) Color(0xFF90CAF9) else Color(0xFF1565C0)
    val textColor = if (isDark) Color(0xFFE0E0E0) else Color(0xFF212121)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "🚗 Trip Gas Estimate",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = titleColor
            )

            HorizontalDivider(color = textColor.copy(alpha = 0.25f), modifier = Modifier.padding(vertical = 4.dp))

            ResultRow(
                label = "Estimated Fuel Used:",
                value = result.formattedFuelUsed(),
                textColor = textColor
            )

            ResultRow(
                label = "Cost Per Kilometer:",
                value = result.formattedCostPerKm(),
                textColor = textColor
            )

            ResultRow(
                label = "Total Trip Gas Cost:",
                value = result.formattedTotalCost(),
                isBold = true,
                textColor = textColor
            )
        }
    }
}

@Composable
fun ResultRow(
    label: String,
    value: String,
    textColor: Color,
    isBold: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = textColor
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = textColor
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
