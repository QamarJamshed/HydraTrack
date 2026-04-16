package com.example.hydratrack.ui.dashboard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hydratrack.domain.model.WaterIntake
import com.example.hydratrack.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val progress by viewModel.dailyProgress.collectAsState()
    val totalIntake by viewModel.totalDailyIntake.collectAsState()
    val goal by viewModel.userProfile.collectAsState()
    val adjustedGoal by viewModel.adjustedGoal.collectAsState()
    val isHotDay by viewModel.isHotDay.collectAsState()
    val intakes by viewModel.dailyIntake.collectAsState()
    val drinkTypes by viewModel.drinkTypes.collectAsState()

    var showAddWaterDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddWaterDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Water")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                val userName = goal?.name ?: "User"
                Text(
                    text = "Hello, $userName!",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    textAlign = TextAlign.Start
                )
                
                Text(
                    text = "Stay hydrated today",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    textAlign = TextAlign.Start
                )

                if (isHotDay) {
                    Text(
                        text = "☀️ It's a hot day! Goal increased by 500ml.",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.height(24.dp))
                }

                HydrationCircle(progress = progress, totalIntake = totalIntake, goal = adjustedGoal)

                if (totalIntake >= (goal?.dailyGoalMl ?: 2000)) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Warning: You've reached or exceeded your daily goal. Be careful not to overhydrate!",
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    }
                } else if (totalIntake >= (goal?.dailyGoalMl ?: 2000) * 0.9) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Almost there! You're very close to your hydration goal.",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Today's Logs",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold
                )
            }

            items(intakes) { intake ->
                IntakeItem(intake = intake, onDelete = { viewModel.deleteWaterIntake(intake) })
            }
        }
    }

    if (showAddWaterDialog) {
        AddWaterDialog(
            drinkTypes = drinkTypes,
            onDismiss = { showAddWaterDialog = false },
            onConfirm = { amount, drinkTypeId ->
                viewModel.addWaterIntake(amount, drinkTypeId)
                showAddWaterDialog = false
            }
        )
    }
}

@Composable
fun HydrationCircle(progress: Float, totalIntake: Int, goal: Int) {
    val animatedProgress by animateFloatAsState(targetValue = progress.coerceIn(0f, 1f))
    
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(240.dp)) {
        Canvas(modifier = Modifier.size(200.dp)) {
            drawArc(
                color = Color.LightGray.copy(alpha = 0.3f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = Color(0xFF2196F3),
                startAngle = -90f,
                sweepAngle = animatedProgress * 360f,
                useCenter = false,
                style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$totalIntake",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2196F3)
            )
            Text(
                text = "/ $goal ml",
                fontSize = 18.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun IntakeItem(intake: WaterIntake, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "${intake.amountMl} ml", fontWeight = FontWeight.Bold)
                Text(text = intake.drinkTypeName, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWaterDialog(
    drinkTypes: List<com.example.hydratrack.domain.model.DrinkType>,
    onDismiss: () -> Unit,
    onConfirm: (Int, Long) -> Unit
) {
    var quantity by remember { mutableStateOf("1") }
    var selectedUnit by remember { mutableStateOf("Glass (250ml)") }
    var customAmount by remember { mutableStateOf("250") }
    var selectedDrinkType by remember { mutableStateOf(drinkTypes.firstOrNull()) }
    var unitExpanded by remember { mutableStateOf(false) }
    var drinkExpanded by remember { mutableStateOf(false) }

    val units = listOf("Glass (250ml)", "Bottle (500ml)", "Large Bottle (750ml)", "Custom (ml)")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Water Intake") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Unit Selection
                ExposedDropdownMenuBox(
                    expanded = unitExpanded,
                    onExpandedChange = { unitExpanded = !unitExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedUnit,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Vessel") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = unitExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = unitExpanded,
                        onDismissRequest = { unitExpanded = false }
                    ) {
                        units.forEach { unit ->
                            DropdownMenuItem(
                                text = { Text(unit) },
                                onClick = {
                                    selectedUnit = unit
                                    unitExpanded = false
                                }
                            )
                        }
                    }
                }

                if (selectedUnit == "Custom (ml)") {
                    OutlinedTextField(
                        value = customAmount,
                        onValueChange = { customAmount = it },
                        label = { Text("Amount per unit (ml)") },
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Quantity Selection
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("How many?") },
                    placeholder = { Text("e.g. 1, 2, 3...") },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                // Drink Type Selection
                ExposedDropdownMenuBox(
                    expanded = drinkExpanded,
                    onExpandedChange = { drinkExpanded = !drinkExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedDrinkType?.name ?: "Select Drink",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Drink Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = drinkExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = drinkExpanded,
                        onDismissRequest = { drinkExpanded = false }
                    ) {
                        drinkTypes.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.name) },
                                onClick = {
                                    selectedDrinkType = type
                                    drinkExpanded = false
                                }
                            )
                        }
                    }
                }

                // Live Calculation Preview
                val qty = quantity.toIntOrNull() ?: 0
                val unitVol = when (selectedUnit) {
                    "Glass (250ml)" -> 250
                    "Bottle (500ml)" -> 500
                    "Large Bottle (750ml)" -> 750
                    else -> customAmount.toIntOrNull() ?: 0
                }
                val total = qty * unitVol
                if (total > 0) {
                    Text(
                        text = "Total to log: $total ml",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val qty = quantity.toIntOrNull() ?: 1
                val unitVol = when (selectedUnit) {
                    "Glass (250ml)" -> 250
                    "Bottle (500ml)" -> 500
                    "Large Bottle (750ml)" -> 750
                    else -> customAmount.toIntOrNull() ?: 250
                }
                onConfirm(
                    qty * unitVol,
                    selectedDrinkType?.id ?: 1L
                )
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
