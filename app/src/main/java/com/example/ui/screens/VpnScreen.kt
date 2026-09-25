package com.example.ui.screens

import android.app.Activity
import android.net.VpnService
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.models.VpnState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VpnScreen(
    vpnState: VpnState,
    onToggleVpn: (server: String, protocol: String) -> Unit
) {
    val context = LocalContext.current
    var selectedServer by remember { mutableStateOf("India - Mumbai Secure Node") }
    var selectedProtocol by remember { mutableStateOf("WireGuard (ChaCha20-Poly1305)") }

    val servers = listOf(
        "India - Mumbai Secure Node",
        "US East - Virginia Tunnel",
        "Germany - Frankfurt Node",
        "Singapore - Fast Cloud"
    )

    val protocols = listOf(
        "WireGuard (ChaCha20-Poly1305)",
        "OpenVPN (AES-256-GCM)"
    )

    // Android VpnService Permission Launcher
    val vpnPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            onToggleVpn(selectedServer, selectedProtocol)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // VPN Status Hero Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("vpn_dashboard_card"),
            colors = CardDefaults.cardColors(
                containerColor = if (vpnState.isConnected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    color = if (vpnState.isConnected) MaterialTheme.colorScheme.primary else Color.Gray,
                    shape = CircleShape,
                    modifier = Modifier.size(88.dp)
                ) {
                    Icon(
                        imageVector = if (vpnState.isConnected) Icons.Default.VpnKey else Icons.Outlined.Shield,
                        contentDescription = "VPN Lock",
                        tint = Color.White,
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (vpnState.isConnected) "VPN CONNECTED 🔐" else "VPN DISCONNECTED",
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = vpnState.statusText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Big Connect/Disconnect Button
                Button(
                    onClick = {
                        val intent = VpnService.prepare(context)
                        if (intent != null) {
                            vpnPermissionLauncher.launch(intent)
                        } else {
                            onToggleVpn(selectedServer, selectedProtocol)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("vpn_connect_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (vpnState.isConnected) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = if (vpnState.isConnected) Icons.Default.PowerSettingsNew else Icons.Default.Lock,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (vpnState.isConnected) "Disconnect VPN" else "Connect Secure VPN")
                }
            }
        }

        // Stats Box
        if (vpnState.isConnected) {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("185.220.101.4", style = MaterialTheme.typography.titleMedium)
                        Text("Encrypted IP", style = MaterialTheme.typography.labelSmall)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("1.4 MB", style = MaterialTheme.typography.titleMedium)
                        Text("Sent ↑", style = MaterialTheme.typography.labelSmall)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("8.7 MB", style = MaterialTheme.typography.titleMedium)
                        Text("Received ↓", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }

        // Server Node Selector
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Select Secure VPN Server Location", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                servers.forEach { server ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .noRippleClickable { selectedServer = server },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedServer == server,
                            onClick = { selectedServer = server }
                        )
                        Text(text = server, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        // Protocol Selector
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Encryption Protocol", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                protocols.forEach { proto ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .noRippleClickable { selectedProtocol = proto },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedProtocol == proto,
                            onClick = { selectedProtocol = proto }
                        )
                        Text(text = proto, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        // Hindi Guide Card explaining genuine VPN VPS Server setup requirements
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Real VPN Server Setup Guide (हिंदी जानकारी) 🇮🇳",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "• Real VPN चलाने के लिए एक उपयुक्त VPS Server (जैसे AWS EC2, DigitalOcean ya Linode) की आवश्यकता होती है।\n" +
                            "• Server पर WireGuard/OpenVPN backend daemon इंस्टॉल करके encryption certificates generate किए जाते हैं।\n" +
                            "• Android App 'VpnService' API के ज़रिए आपके phone के सभी internet traffic को उस secure tunnel से रूट करता है।\n" +
                            "• Privacy Policy Guarantee: LoveNest उपयोगकर्ताओं की browsing activity या logs कभी भी सेव या ट्रैक नहीं करता।",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}
