package HW1_6

import kotlin.random.Random

// Функція генерації масиву, що може повернути null при невалідних параметрах
fun generateRandomArray(size: Int, maxValue: Int): IntArray? {
    if (size <= 0 || maxValue <= 0) return null
    return IntArray(size) { Random.nextInt(0, maxValue + 1) }
}

fun main() {
    // Єдиний неперервний ланцюжок викликів без проміжних змінних
    generateRandomArray(size = 10, maxValue = 50)
        ?.let {
            // Безпечний вхід (?.let), якщо масив успішно створено
            // Модифікуємо елементи "на місці" через apply
            it.apply {
                for (i in indices) {
                    if (this[i] % 2 != 0) {
                        this[i] *= 2
                    } else {
                        this[i] /= 2
                    }
                }
            }
        }
        ?.also {
            // Логування модифікованого масиву через also
            println("Модифікований масив: ${it.contentToString()}")
        }
        ?.let {
            // Пошук максимального елемента (maxOrNull з перевіркою на null або maxOf)
            it.maxOrNull() ?: 0
        }
        ?.also {
            println("Максимальне значення масиву: $it")
        }
        ?: println("Помилка вхідних даних або згенерація повернула null")
}