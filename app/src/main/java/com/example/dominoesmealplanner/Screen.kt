package com.example.dominoesmealplanner

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.platform.LocalContext


val DominoRed = Color(0xFFE31837) //Domino's Red
val DominoBlue = Color(0xFF0055A5) // Domino's Blue
val DominoWhite = Color(0xFFFFFFFF) // White
val DominoBlack = Color(0xFF000000) // Black
val DominoDarkGray = Color(0xFF333333) // Dark Gray
val LightGrey = Color(0xFFEBEBEB) // Light grey color

@Composable
fun DominoesMealPlanner() {
    var budget by remember { mutableStateOf("") }

    val combinations = remember(budget) {
        calculateCombinations(
            budget.toIntOrNull() ?: 0,
            menu = dominoMenu
        )

    }

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(LightGrey)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(28.dp))
            // Heading
            Text(
                text = "Domino's Meal Planner",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DominoBlue
            )

            Spacer(modifier = Modifier.height(18.dp))

            Image(
                painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                contentDescription = "Domino's Logo",
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = budget,
                onValueChange = { budget = it },
                label = { Text("Enter your budget", color = DominoDarkGray) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = DominoWhite,
                    unfocusedContainerColor = DominoWhite,
                    focusedTextColor = DominoBlack,
                    unfocusedTextColor = DominoBlack,
                    focusedIndicatorColor = DominoBlue,
                    unfocusedIndicatorColor = DominoBlue

                )
            )
            Spacer(modifier = Modifier.width(16.dp))

            LazyColumn {
                items(combinations) { combination ->
                    CombinationCard(
                        combination = combination,
                        context = LocalContext.current
                    )
                }
            }

        }
    }
}

@Composable
fun CombinationCard(combination: List<MenuItem>, context: Context) {
    val totalPrice = combination.sumOf { it.price }
    val totalCalories = combination.sumOf { it.calories }
    val totalCarbs = combination.sumOf { it.carbs }
    val totalProtein = combination.sumOf { it.protein }
    val totalFat = combination.sumOf { it.fat }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = DominoWhite
        ),
        border = BorderStroke(1.dp, DominoBlue)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                combination.forEach { item ->
                    Text(text = "${item.name} - ₹${item.price}", fontSize = 16.sp, color = DominoDarkGray)
                    Text(text = "Calories: ${item.calories} kcal", fontSize = 14.sp, color = DominoDarkGray)
                    Text(text = "Carbs: ${item.carbs} g", fontSize = 14.sp, color = DominoDarkGray)
                    Text(text = "Protein: ${item.protein} g", fontSize = 14.sp, color = DominoDarkGray)
                    Text(text = "Fat: ${item.fat} g", fontSize = 14.sp, color = DominoDarkGray)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Total: ₹$totalPrice", fontSize = 18.sp, color = DominoBlue)
                Text(text = "Total Calories: $totalCalories kcal", fontSize = 14.sp, color = DominoDarkGray)
                Text(text = "Total Carbs: $totalCarbs g", fontSize = 14.sp, color = DominoDarkGray)
                Text(text = "Total Protein: $totalProtein g", fontSize = 14.sp, color = DominoDarkGray)
                Text(text = "Total Fat: $totalFat g", fontSize = 14.sp, color = DominoDarkGray)
            }

            // Domino's Logo on the Right Side of the Card
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher_foreground), // Add your logo to res/drawable
                contentDescription = "Domino's Logo",
                modifier = Modifier.size(50.dp), // Adjust size as needed
                contentScale = ContentScale.Fit
            )
        }

        // Share Button
        IconButton(
            onClick = { shareMealCombo(context, combination) },
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.End)
        ) {
            Icon(
                imageVector = Icons.Filled.Share,  // Use the built-in Share icon
                contentDescription = "Share",
                tint = Color.Black // Customize the icon color if needed
            )
        }
    }
}

fun shareMealCombo(context: Context, combination: List<MenuItem>) {
    // Create the message with the combo details
    val message = buildString {
        append("Check out this Domino's Meal Combo:\n\n")
        combination.forEach {
            append("${it.name} - ₹${it.price}\n")
        }
        append("\nTotal: ₹${combination.sumOf { it.price }}")
    }

    // Create an intent to share the message
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, message)
        type = "text/plain"
    }

    try {
        // Start the share activity
        context.startActivity(Intent.createChooser(shareIntent, "Share Meal Combo"))
    } catch (e: Exception) {
        // Handle exceptions (e.g., no apps that support sharing)
        Toast.makeText(context, "Error sharing combo", Toast.LENGTH_SHORT).show()
    }
}


fun calculateCombinations(budget: Int, menu: List<MenuItem>): List<List<MenuItem>> {
    val validCombinations = mutableListOf<List<MenuItem>>() // List to store valid combinations

    // Loop through all menu items
    for (i in menu.indices) {
        val combination = mutableListOf<MenuItem>() // Temporary list for a single combination
        var totalPrice = 0
        var totalCalories = 0
        var totalCarbs = 0
        var totalProtein = 0
        var totalFat = 0

        // Add items one by one to the combination
        for (j in i until menu.size) {
            val item = menu[j]
            if (totalPrice + item.price <= budget + 10) {
                combination.add(item) // Add item to the combination
                totalPrice += item.price // Update the total price
                totalCalories += item.calories
                totalCarbs += item.carbs
                totalProtein += item.protein
                totalFat += item.fat

                // Check if this combination fits the budget range
                if (totalPrice in (budget - 10)..(budget + 10)) {
                    validCombinations.add(combination.toList()) // Add a copy of the combination
                }
            }
        }
    }

    return validCombinations // Return all valid combinations
}

data class MenuItem(
    val name: String,
    val price: Int,
    val calories: Int,
    val carbs: Int,
    val protein: Int,
    val fat: Int
)


val dominoMenu = listOf(
    // Pizzas
    MenuItem("Margherita", 200, 250, 30, 10, 9),
    MenuItem("Pepperoni", 250, 300, 35, 15, 12),
    MenuItem("Veggie Paradise", 220, 270, 40, 8, 7),
    MenuItem("Cheese Burst", 300, 350, 40, 18, 20),
    MenuItem("Farmhouse", 280, 300, 35, 12, 10),
    MenuItem("Chicken Dominator", 350, 400, 45, 25, 15),
    MenuItem("Paneer Makhani", 270, 320, 35, 15, 10),
    MenuItem("Spicy Chicken", 320, 360, 40, 18, 20),
    MenuItem("Tandoori Veg", 240, 270, 35, 7, 5),
    MenuItem("Tandoori Chicken", 330, 380, 42, 28, 18),
    MenuItem("Double Cheese Margherita", 310, 360, 38, 20, 16),
    MenuItem("Mexican Green Wave", 290, 340, 36, 18, 14),
    MenuItem("Chicken Golden Delight", 249, 350, 40, 25, 22),
    MenuItem("Non-Veg Supreme", 319, 390, 42, 30, 18),
    MenuItem("Veg Extravaganza", 260, 320, 35, 12, 9),
    MenuItem("Pepper Barbecue Chicken & Onion", 229, 330, 37, 22, 15),
    MenuItem("Chicken Sausage", 189, 270, 25, 12, 14),
    MenuItem("Chicken Pepperoni", 319, 380, 43, 30, 20),
    MenuItem("Chicken Fiesta", 249, 330, 35, 20, 16),
    MenuItem("Indi Chicken Tikka", 319, 360, 40, 28, 18),
    MenuItem("Keema Do Pyaza", 189, 240, 30, 18, 10),

    // Sides
    MenuItem("Garlic Breadsticks", 100, 160, 35, 6, 7),
    MenuItem("Stuffed Garlic Bread", 150, 220, 40, 10, 12),
    MenuItem("Paneer Zingy Parcel", 120, 200, 28, 8, 10),
    MenuItem("Chicken Wings", 180, 300, 20, 25, 22),
    MenuItem("Potato Wedges", 90, 150, 30, 2, 5),
    MenuItem("Chicken Pepperoni Stuffed Garlic Bread", 200, 250, 35, 12, 16),
    MenuItem("Veg Pasta Italiano White", 130, 200, 38, 6, 7),
    MenuItem("Non-Veg Pasta Italiano White", 160, 240, 40, 18, 10),
    MenuItem("Veg Pasta Italiano Red", 130, 220, 37, 7, 8),
    MenuItem("Non-Veg Pasta Italiano Red", 160, 250, 40, 19, 11),

    // Desserts
    MenuItem("Choco Lava Cake", 110, 200, 30, 2, 10),
    MenuItem("Butterscotch Mousse Cake", 140, 250, 35, 3, 12),
    MenuItem("New York Cheesecake", 170, 280, 38, 4, 14),
    MenuItem("Dark Fantasy", 120, 180, 28, 3, 9),
    MenuItem("Chocolate Brownie", 100, 230, 32, 5, 11),
    MenuItem("Vanilla Ice Cream", 80, 120, 18, 4, 5),
    MenuItem("Strawberry Ice Cream", 80, 120, 18, 4, 5),
    MenuItem("Chocolate Ice Cream", 80, 140, 22, 4, 6),

    // Beverages
    MenuItem("Pepsi 500ml", 60, 150, 40, 0, 0),
    MenuItem("Mirinda 500ml", 60, 150, 40, 0, 0),
    MenuItem("7Up 500ml", 60, 140, 37, 0, 0),
    MenuItem("Mountain Dew 500ml", 60, 150, 40, 0, 0),
    MenuItem("Water Bottle 1L", 40, 0, 0, 0, 0),
    MenuItem("Iced Tea", 70, 120, 30, 0, 0),
    MenuItem("Cold Coffee", 90, 180, 40, 2, 4),
    MenuItem("Orange Juice", 80, 150, 35, 1, 0),
    MenuItem("Mango Juice", 80, 160, 38, 1, 0),

    // Combos
    MenuItem("Meal for 2: 2 Medium Pizzas + Garlic Bread + Pepsi", 800, 1000, 130, 25, 40),
    MenuItem("Meal for 4: 4 Medium Pizzas + Stuffed Garlic Bread + Pepsi", 1500, 1800, 240, 50, 70),
    MenuItem("Snack Combo: Garlic Bread + Potato Wedges + Pepsi", 300, 500, 70, 12, 18),
    MenuItem("Dessert Combo: Choco Lava Cake + Brownie + Ice Cream", 250, 550, 70, 9, 30),
    MenuItem("Family Combo: 1 Large Pizza + 1 Medium Pizza + Garlic Bread + Pepsi", 1200, 1500, 190, 45, 55)
)


