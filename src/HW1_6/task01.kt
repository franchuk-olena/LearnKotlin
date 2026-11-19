package HW1_6

data class Address(
    val street: String,
    val city: String?,
    val postalCode: String?
)

data class Client(
    val id: Int,
    val name: String,
    val email: String?,
    val address: Address?,
    val extraData: Any? // Може містити рядок, число або null
)

val clientList = listOf(
    Client(
        id = 1,
        name = "Олена",
        email = "olena@example.com",
        address = Address("вул. Саксаганського, 10", "Київ", "01033"),
        extraData = "VIP-клієнт"
    ),
    Client(
        id = 2,
        name = "Богдан",
        email = null,
        address = Address("вул. Городоцька, 45", "Львів", null),
        extraData = 42
    ),
    Client(
        id = 3,
        name = "Марія",
        email = "maria@example.com",
        address = null,
        extraData = null
    ),
    Client(
        id = 4,
        name = "Дмитро",
        email = null,
        address = Address("вул. Соборна, 1", null, null),
        extraData = "Очікує дзвінка"
    )
)

// Ланцюжок безпечних викликів та Elvis-оператор (?., ?:)
fun getShippingLabel(client: Client): String {
    val addr = client.address ?: return "Самовивіз: Клієнт ${client.name} не надав адреси"
    val city = addr.city ?: "Місто не вказано"
    val postalCode = addr.postalCode ?: "Індекс невідомий"
    return "${addr.street}, $city, $postalCode"
}

// Безпечне приведення типів (as?) та Smart Cast
fun printClientNote(client: Client) {
    val note = (client.extraData as? String) ?: "Додаткові примітки відсутні"
    println("Клієнт ${client.name} -> Примітка: $note")
}

//Валідація з викиданням винятку (?: throw ...)
fun getClientEmailOrThrow(client: Client): String {
    return client.email ?: throw IllegalArgumentException("Клієнт з ID ${client.id} не має електронної пошти!")
}

//Дослідження оператора !!
fun forceGetPostalCode(client: Client): String {
    // Використання оператора !! ризиковане і викине NPE, якщо address або postalCode дорівнює null
    return client.address!!.postalCode!!
}

fun main() {
    println("--- Завдання 1 ---")
    // Перевірка для Олени, Богдана та Марії
    println("Адреса Олена: ${getShippingLabel(clientList[0])}")
    println("Адреса Богдан: ${getShippingLabel(clientList[1])}")
    println("Адреса Марія: ${getShippingLabel(clientList[2])}")

    println("\n--- Завдання 2 ---")
    // Перевірка для всіх 4-х клієнтів
    for (client in clientList) {
        printClientNote(client)
    }

    println("\n--- Завдання 3 ---")
    // Отримання списку email-адрес та фільтрація null
    val emails: List<String> = clientList
        .map { it.email }
        .filterNotNull()

    // Знаходження довжини найкоротшого email
    val shortestEmailLength = emails.minOfOrNull { it.length } ?: 0

    println("База email для розсилки: $emails")
    println("Довжина найкоротшого email: $shortestEmailLength")

    println("\n--- Завдання 4 ---")
    // Перевірка виклику для клієнта з id = 2 (у якого email == null) з обробкою винятку
    try {
        val email = getClientEmailOrThrow(clientList[1])
        println("Email: $email")
    } catch (e: IllegalArgumentException) {
        println("Перехоплено виняток: ${e.message}")
    }

    println("\n--- Завдання 5 ---")
    // Успішний випадок для клієнта з id = 1
    val postalCodeOlena = forceGetPostalCode(clientList[0])
    println("Індекс клієнта Олена: $postalCodeOlena")

    // Виклик для клієнта з id = 2 всередині блоку try-catch (у Богдана postalCode == null)
    try {
        forceGetPostalCode(clientList[1])
    } catch (e: NullPointerException) {
        println("Перехоплено очікуваний NPE: ${e.message}")
    }
}

/*
 * Пояснення до Завдання 5:
 * Оператор !! припустимо використовувати в реальних проєктах лише в ситуаціях абсолютної впевненості
 * (наприклад, у тестах або при взаємодії із зовнішніми/Android API), коли виняток NullPointerException
 * є бажаним і сигналізує про критичну помилку конфігурації чи порушення інваріанту програми, яку неможливо ігнорувати.
 */