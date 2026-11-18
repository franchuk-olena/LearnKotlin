package HW1_4

class Employee(
    val firstName: String,
    val lastName: String,
    val position: String
) {
    // Поле в тілі класу
    var bonus: Int = 0


    override fun toString(): String {
        return "Employee(firstName=$firstName, lastName=$lastName, position=$position)"
    }

    // реалізація структурної рівності equals()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Employee) return false

        return firstName == other.firstName &&
                lastName == other.lastName &&
                position == other.position
    }

    // Обов'язкова пара hashCode() до equals()
    override fun hashCode(): Int {
        var result = firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + position.hashCode()
        return result
    }

    // Операторні функції для деструктуризації
    operator fun component1(): String = firstName
    operator fun component2(): String = lastName
    operator fun component3(): String = position

    // Ручна реалізація методу copy()
    fun copy(
        firstName: String = this.firstName,
        lastName: String = this.lastName,
        position: String = this.position
    ): Employee {
        return Employee(firstName, lastName, position)
    }
}

fun main() {
    // 2. Створення екземплярів
    val emp1 = Employee("Олена", "Коваленко", "Junior Developer")
    val emp2 = Employee("Олена", "Коваленко", "Junior Developer")
    val emp3 = emp1


    println("emp1: $emp1")


    println("emp1 === emp2 (різні об'єкти в пам'яті): ${emp1 === emp2}") // false
    println("emp1 === emp3 (одне посилання): ${emp1 === emp3}")           // true
    println("emp1 == emp2 (однакові дані): ${emp1 == emp2}")             // true


    val (name, surname, pos) = emp1
    println("Деструктуризація -> Ім'я: $name, Прізвище: $surname, Посада: $pos")


    val promotedEmp = emp1.copy(position = "Middle Developer")
    println("Клонований із новою посадою: $promotedEmp")

    emp1.bonus = 500
    emp2.bonus = 1000
    println("emp1 (bonus=${emp1.bonus}) == emp2 (bonus=${emp2.bonus}): ${emp1 == emp2}") // true
}