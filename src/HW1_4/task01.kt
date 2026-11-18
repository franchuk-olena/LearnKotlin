package HW1_4

import kotlin.math.sqrt
import kotlin.random.Random

// моделювання результату через sealed interface
sealed interface Roots {
    data class TwoRoots(val x1: Double, val x2: Double) : Roots
    data class OneRoot(val x: Double) : Roots
    data object NoRealRoots : Roots
}

// первинний конструктор із властивостями
class QuadraticEquation(
    a: Double,
    var b: Double,
    var c: Double
) {
    //Кастомний сетер та backing field для 'a'
    // Оголошуємо в тілі класу, щоб додати власний сеттер, ініціалізуючи параметром конструктора
    var a: Double = a
        set(value) {
            require(value != 0.0) { "Коефіцієнт 'a' не може бути нулем!" }
            field = value
        }

    //Валідація в блоці init при створенні об'єкта
    init {
        require(a != 0.0) { "Коефіцієнт 'a' не може бути нулем!" }
    }

    // Вторинний конструктор для цілих чисел
    constructor(a: Int, b: Int, c: Int) : this(a.toDouble(), b.toDouble(), c.toDouble())

    //Вторинний конструктор для зведеного рівняння (a = 1.0)
    constructor(b: Double, c: Double) : this(1.0, b, c)

    //Обчислювана властивість через custom getter
    val discriminant: Double
        get() = b * b - 4 * a * c

    //(базовий варіант із List<Double>)
    fun solveAsList(): List<Double> {
        val d = discriminant
        return when {
            d > 0 -> {
                val sqrtD = sqrt(d)
                listOf((-b - sqrtD) / (2 * a), (-b + sqrtD) / (2 * a))
            }
            d == 0.0 -> listOf(-b / (2 * a))
            else -> emptyList()
        }
    }

    // метод solve(), що повертає Roots
    fun solve(): Roots {
        val d = discriminant
        return when {
            d > 0 -> {
                val sqrtD = sqrt(d)
                Roots.TwoRoots(
                    x1 = (-b - sqrtD) / (2 * a),
                    x2 = (-b + sqrtD) / (2 * a)
                )
            }
            d == 0.0 -> Roots.OneRoot(-b / (2 * a))
            else -> Roots.NoRealRoots
        }
    }

    // Перевизначення toString() для читабельного формату
    override fun toString(): String {
        val aPart = if (a == 1.0) "x^2" else "${a}x^2"
        val bPart = if (b >= 0) "+ ${b}x" else "- ${-b}x"
        val cPart = if (c >= 0) "+ $c" else "- ${-c}"
        return "$aPart $bPart $cPart = 0"
    }
}

fun main() {
    println("=== 1. Тестування конструкторів та мутабельності ===")
    val eq1 = QuadraticEquation(1, -4, 4) // Виклик Int-конструктора
    println("Рівняння 1: $eq1 | D = ${eq1.discriminant}")

    // Змінюємо коефіцієнти на льоту
    eq1.c = 3.0
    println("Після зміни c = 3.0: $eq1 | Новий D = ${eq1.discriminant}")

    // Перевірка захисту від a = 0 у сетері
    try {
        eq1.a = 0.0
    } catch (e: IllegalArgumentException) {
        println("Перехоплено помилку валідації: ${e.message}")
    }

    val reducedEq = QuadraticEquation(-5.0, 6.0) // Зведене рівняння (a = 1.0)
    println("Зведене рівняння: $reducedEq | Розв'язки: ${reducedEq.solve()}\n")

    println("=== 2. Генерація 100 рівнянь та фільтрація (Частина 4 + ⭐️) ===")
    val equations = List(100) {
        // Генеруємо 'a' так, щоб виключити 0
        var randomA = 0
        while (randomA == 0) {
            randomA = Random.nextInt(-50, 51)
        }
        val randomB = Random.nextInt(-50, 51)
        val randomC = Random.nextInt(-50, 51)

        QuadraticEquation(randomA, randomB, randomC)
    }

    // Фільтруємо рівняння, які мають рівно 2 корені
    val equationsWithTwoRoots = equations.filter { it.discriminant > 0 }

    equationsWithTwoRoots.forEach { eq ->
        // Обробка sealed interface через вираз when
        when (val result = eq.solve()) {
            is Roots.TwoRoots -> {
                println("$eq  -->  x1 = %.2f, x2 = %.2f".format(result.x1, result.x2))
            }
            is Roots.OneRoot -> println("$eq  -->  x = %.2f".format(result.x))
            is Roots.NoRealRoots -> println("$eq  -->  Дійсних коренів немає")
        }
    }

    println("\nЗагальна кількість рівнянь із 2 дійсними коренями: ${equationsWithTwoRoots.size} із 100")
}