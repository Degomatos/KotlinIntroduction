package com.example.secondactivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.secondactivity.ui.theme.KotlinIntroductionTheme
import java.text.NumberFormat
import java.util.Locale

class Main : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinIntroductionTheme {
                FinanceApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceApp() {
    var transactions by remember { mutableStateOf(listOf<Transaction>()) }
    var dreams by remember { mutableStateOf(listOf<Dream>()) }
    var currentScreen by remember { mutableStateOf("home") }

    val totalIncome = transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.value }
    val totalExpense = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.value }
    val balance = totalIncome - totalExpense

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (currentScreen) {
                            "home" -> "Meu Saldo"
                            "income" -> "Ganhos"
                            "expense" -> "Gastos"
                            "dreams" -> "Sonhos"
                            else -> "Finance App"
                        }
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Início") },
                    selected = currentScreen == "home",
                    onClick = { currentScreen = "home" }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.KeyboardArrowUp, contentDescription = null) },
                    label = { Text("Ganhos") },
                    selected = currentScreen == "income",
                    onClick = { currentScreen = "income" }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.KeyboardArrowDown, contentDescription = null) },
                    label = { Text("Gastos") },
                    selected = currentScreen == "expense",
                    onClick = { currentScreen = "expense" }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Star, contentDescription = null) },
                    label = { Text("Sonhos") },
                    selected = currentScreen == "dreams",
                    onClick = { currentScreen = "dreams" }
                )
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when (currentScreen) {
                "home" -> HomeScreen(balance, totalIncome, totalExpense)
                "income" -> TransactionScreen(TransactionType.INCOME, transactions) {
                    transactions = transactions + it
                }
                "expense" -> TransactionScreen(TransactionType.EXPENSE, transactions) {
                    transactions = transactions + it
                }
                "dreams" -> DreamsScreen(balance, dreams) {
                    dreams = dreams + it
                }
            }
        }
    }
}

@Composable
fun HomeScreen(balance: Double, income: Double, expense: Double) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Saldo Atual", fontSize = 20.sp)
        Text(
            formatCurrency(balance),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = if (balance >= 0) Color(0xFF4CAF50) else Color.Red
        )
        Spacer(modifier = Modifier.height(32.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            SummaryItem("Entradas", income, Color(0xFF4CAF50))
            SummaryItem("Saídas", expense, Color.Red)
        }
    }
}

@Composable
fun SummaryItem(label: String, value: Double, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 14.sp)
        Text(formatCurrency(value), color = color, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun TransactionScreen(type: TransactionType, list: List<Transaction>, onAdd: (Transaction) -> Unit) {
    var description by remember { mutableStateOf("") }
    var valueStr by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = valueStr,
            onValueChange = { valueStr = it },
            label = { Text("Valor") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                val value = valueStr.toDoubleOrNull() ?: 0.0
                if (description.isNotBlank() && value > 0) {
                    onAdd(Transaction(System.currentTimeMillis().toInt(), description, value, type))
                    description = ""
                    valueStr = ""
                }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Adicionar ${if (type == TransactionType.INCOME) "Ganho" else "Gasto"}")
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        LazyColumn {
            items(list.filter { it.type == type }) { item ->
                ListItem(
                    headlineContent = { Text(item.description) },
                    trailingContent = { Text(formatCurrency(item.value)) }
                )
            }
        }
    }
}

@Composable
fun DreamsScreen(balance: Double, dreams: List<Dream>, onAdd: (Dream) -> Unit) {
    var description by remember { mutableStateOf("") }
    var valueStr by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Saldo Disponível: ${formatCurrency(balance)}", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Qual o seu sonho?") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = valueStr,
            onValueChange = { valueStr = it },
            label = { Text("Valor Estimado") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                val value = valueStr.toDoubleOrNull() ?: 0.0
                if (description.isNotBlank() && value > 0) {
                    onAdd(Dream(System.currentTimeMillis().toInt(), description, value))
                    description = ""
                    valueStr = ""
                }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Registrar Sonho")
        }

        LazyColumn {
            items(dreams) { dream ->
                val progress = if (balance <= 0) 0f else (balance / dream.targetValue).toFloat().coerceIn(0f, 1f)
                val percent = (progress * 100).toInt()
                
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(dream.description, fontWeight = FontWeight.Bold)
                        Text("Meta: ${formatCurrency(dream.targetValue)}")
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxWidth(),
                            color = if (progress >= 1f) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary
                        )
                        Text("$percent% alcançado", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

fun formatCurrency(value: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR")).format(value)
}
