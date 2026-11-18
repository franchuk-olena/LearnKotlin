package HW1_4

data class Product(
    val id: Int,
    val name: String,
    val category: String,
    val price: Double,
    val rating: Double,
    val inStock: Boolean
)

val products = listOf(
    Product(1, "Google Pixel 9", "Smartphones", 899.0, 4.8, true),
    Product(2, "iPhone 16 Pro", "Smartphones", 1199.0, 4.9, true),
    Product(3, "Galaxy A55", "Smartphones", 399.0, 4.3, false),
    Product(4, "MacBook Air M3", "Laptops", 1299.0, 4.9, true),
    Product(5, "ThinkPad X1 Carbon", "Laptops", 1499.0, 4.6, false),
    Product(6, "Dell XPS 13", "Laptops", 1150.0, 4.4, true),
    Product(7, "Sony WH-1000XM5", "Audio", 349.0, 4.7, true),
    Product(8, "AirPods Pro 2", "Audio", 249.0, 4.8, false),
    Product(9, "Pixel Buds Pro 2", "Audio", 229.0, 4.5, true)
)

//Визначення власної функції вищого порядку як функції-розширення
fun List<Product>.filterAndTransform(
    predicate: (Product) -> Boolean,
    transform: (Product) -> String
): List<String> {
    val result = mutableListOf<String>()
    for (product in this) {
        if (predicate(product)) {
            result.add(transform(product))
        }
    }
    return result
}

fun main() {
    println("=== Ланцюжки трансформації даних ===")
    products
        .filter { it.inStock && it.rating >= 4.7 && it.price < 1000.0 }
        .sortedByDescending { it.rating }
        .map { "Назва: ${it.name} | Рейтинг: ${it.rating} | Ціна: $${it.price}" }
        .forEach { println(it) }

    println("\n=== Пошук та предикатні перевірки ===")

    // 1. Точковий пошук
    val expensiveLaptop = products.find { it.category == "Laptops" && it.price > 1200.0 }
    println("Знайдений ноутбук: ${expensiveLaptop?.name ?: "Товар не знайдено"}")

    // 2. Перевірка наявності хоча б одного елемента (any)
    val hasExpensiveAudio = products.any { it.category == "Audio" && it.price > 300.0 }
    println("Чи є аудіопристрій дорожчий за 300.0: $hasExpensiveAudio")

    // 3. Перевірка відповідності всіх елементів (all)
    val allSmartphonesHighRating = products
        .filter { it.category == "Smartphones" }
        .all { it.rating > 4.0 }
    println("Чи у всіх смартфонів рейтинг вище за 4.0: $allSmartphonesHighRating")

    println("\n=== Розділення та групування вибірки ===")

    // 1. Розділення списку за умовою
    val (available, outOfStock) = products.partition { it.inStock }
    println("Кількість товарів у наявності (available): ${available.size}")
    println("Кількість товарів поза складом (outOfStock): ${outOfStock.size}")

    // 2 & 3. Групування за категорією та пошук лідера
    val groupedByCategory = products.groupBy { it.category }
    groupedByCategory.forEach { (category, groupProducts) ->
        val leader = groupProducts.maxByOrNull { it.price }
        if (leader != null) {
            println("Категорія $category -> Найдорожчий: ${leader.name} (\$${leader.price})")
        }
    }

    println("\n=== Власна функція вищого порядку (Trailing Lambda) ===")

    // Виклик власної функції з використанням синтаксису Trailing Lambda
    val promoMessages = products.filterAndTransform({ it.price < 300.0 }) { product ->
        "Акційна ціна на ${product.name}: лише \$${product.price}!"
    }
    promoMessages.forEach { println(it) }

    println("\n=== Додаткове завдання (*) (fold) ===")

    // Сумарна та середня вартість товарів у наявності через fold
    val inStockProducts = products.filter { it.inStock }
    val totalPrice = inStockProducts.fold(0.0) { acc, product -> acc + product.price }
    val averagePrice = if (inStockProducts.isNotEmpty()) totalPrice / inStockProducts.size else 0.0

    println("Сумарна вартість товарів у наявності: \$$totalPrice")
    println("Середня вартість товарів у наявності: \$${String.format("%.2f", averagePrice)}")
}