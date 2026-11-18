package HW1_5

// Модель даних з мілким початковим станом або var-властивостями для налаштування
data class Vehicle(
    var brand: String,
    var model: String,
    var year: Int,
    var licensePlate: String
)

fun main() {
    // Створення, налаштування та логування (apply + also)
    val vehicle = Vehicle("", "", 0, "").apply {
        brand = "Honda"
        model = "Civic"
        year = 2020
        licensePlate = "КА3344ВІ"
    }.also {
        println("Створено новий транспортний засіб: $it")
    }

    println("\n---")

    with(vehicle) {
        println("Марка = $brand")
        println("Модель = $model")
        println("Рік випуску: $year")
        println("Номерний знак: $licensePlate")
    }
}