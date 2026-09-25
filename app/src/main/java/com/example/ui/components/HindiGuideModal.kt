package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HindiGuideModal(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "LoveNest ❤️ Beginner Hindi Guide",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "नमस्ते! LoveNest ❤️ App की नई सुविधायों और सेटिंग्स की पूरी जानकारी यहाँ देखें:",
                    style = MaterialTheme.typography.bodyMedium
                )

                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("🎬 1. एक साथ Movie / Video देखें (Watch Together)", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "• 'Watch Movie' पेज पर दोनों पार्टनर एक ही समय पर sync होकर वीडियो/मूवी देख सकते हैं।\n" +
                            "• Real-time Reaction Emojis (❤️, 💋, 🍿, 🌹) और लाइव साइड-चैट से वीडियो देखते समय बातें करें।",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("📁 2. Secure Love Storage & Vault", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "• 'Love Storage' में 20 GB तक Photos, Videos, Love Letters और Secret Voice Notes सुरक्षित रखें।\n" +
                            "• AES-256 Encryption और 4-Digit Passcode PIN लॉक से आपकी मीडिया पूरी तरह प्राइवेट रहेगी।",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("✒️ 3. Font Styles & Attractive Love DP", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "• Settings से Romantic Script, Serif Elegance, Modern Sans या Playful fonts बदलें।\n" +
                            "• Profile पेज से Neon Heart, Rose Garland और Golden Ring जैसे आकर्षक DP Frames चुनें।",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("🔑 4. API Keys & Backend Connectivity", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "• Settings -> Configure API Keys में अपनी Firebase API Key और Gemini AI Key दर्ज करें।\n" +
                            "• App का डाटा रूम डेटाबेस और क्लाउड सिंक से हमेशा सुरक्षित और एक्टिव रहता है।",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("📱 5. Android Phone पर टेस्ट और APK डाउनलोड", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "• AI Studio UI से Preview देखें या Export APK चुनकर सीधे अपने फ़ोन में `.apk` इनस्टॉल करें।",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("समझ गया (Okay)")
            }
        }
    )
}
