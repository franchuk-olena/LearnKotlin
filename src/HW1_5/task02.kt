package HW1_5

data class ArraySummary(
    val maxElement: Int,
    val elementsOverLimit: Int,
    val totalSum: Int
)

fun generateRandomArray(size: Int, maxValue: Int): IntArray {
    require(size > 0 && maxValue > 0) { "Розмір та максимальне значення мають бути більшими за нуль" }
    return IntArray(size) { kotlin.random.Random.nextInt(1, maxValue + 1) }
}

fun main() {
    generateRandomArray(size = 8, maxValue = 40)
        .also {
            //Логування початкового стану
            println("Початковий масив: ${it.contentToString()}")
        }
        .apply {
            // Модифікація елементів "на місці"
            for (i in indices) {
                if (this[i] % 2 != 0) {
                    this[i] *= 2
                } else {
                    this[i] /= 2
                }
            }
        }
        .also {
            //Логування модифікованого стану
            println("Модифікований масив: ${it.contentToString()}")
        }
        .run {
            //Обчислення аналітики та агрегація
            ArraySummary(
                maxElement = reduce { max, x -> maxOf(max, x) },
                elementsOverLimit = count { it > 20 },
                totalSum = sum()
            )
        }
        .let {
            // Трансформація статистики у звіт
            """
            ----------------------------------------
            ЗВІТ ОБРОБКИ МАСИВУ:
            - Найбільше значення: ${it.maxElement}
            - Кількість елементів > 20: ${it.elementsOverLimit}
            - Сумарне значення: ${it.totalSum}
            ----------------------------------------
            """.trimIndent()
        }
        .also {
            println(it)
        }
}