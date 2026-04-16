package com.example.hydratrack.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    val reminderInterval by viewModel.reminderInterval.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Notifications", style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = "Receive water reminders",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { viewModel.toggleNotifications(it) }
                    )
                }

                if (notificationsEnabled) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(text = "Reminder Interval", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "Every $reminderInterval minutes",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Slider(
                        value = reminderInterval.toFloat(),
                        onValueChange = { viewModel.updateReminderInterval(it.toInt()) },
                        valueRange = 15f..240f,
                        steps = 14 // (240-15)/15 - 1 = 14 steps for 15min increments
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        // Appearance Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Appearance", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                
                val themes = listOf("System", "Light", "Dark")
                themes.forEach { mode ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (themeMode == mode),
                            onClick = { viewModel.setThemeMode(mode) }
                        )
                        Text(text = mode, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        // Account Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Account", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { viewModel.logout() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Logout / Reset Data")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        // About Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "About", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "HydraTrack v1.0", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Stay hydrated, stay healthy.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
