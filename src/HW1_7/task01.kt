package HW1_7

// Оголошення інтерфейсів
interface Refundable {
    fun refund(amount: Double): Boolean
}

interface EReceiptable {
    val receiptEmail: String

    fun sendReceipt() {
        println(" [E-Receipt] Електронний чек надіслано на пошту: $receiptEmail")
    }
}

// Базовий абстрактний клас PaymentMethod
abstract class PaymentMethod(
    val transactionId: String,
    val amount: Double,
    val currency: String = "UAH"
) {
    init {
        require(amount > 0.0) { "Сума платежу повинна бути більшою за нуль" }
    }

    abstract fun processPayment(): Boolean

    open fun printDetails() {
        println("Транзакція #$transactionId | Сума: $amount $currency")
    }
}

//Реалізація конкретних класів платежів

// 1. CreditCardPayment
class CreditCardPayment(
    transactionId: String,
    amount: Double,
    currency: String = "UAH",
    val cardNumber: String,
    override val receiptEmail: String
) : PaymentMethod(transactionId, amount, currency), Refundable, EReceiptable {

    override fun processPayment(): Boolean {
        println("Списання $amount $currency з картки (закінчується на ${cardNumber.takeLast(4)})... Успішно!")
        return true
    }

    override fun refund(amount: Double): Boolean {
        println("Повернення $amount $currency на картку ${cardNumber.takeLast(4)} виконано.")
        return true
    }

    override fun printDetails() {
        super.printDetails()
        println("  └ Метод: Банківська картка (**** ${cardNumber.takeLast(4)})")
    }
}

// 2. CryptoPayment
class CryptoPayment(
    transactionId: String,
    amount: Double,
    currency: String = "UAH",
    val cryptoAddress: String,
    val networkFee: Double
) : PaymentMethod(transactionId, amount, currency), Refundable {

    override fun processPayment(): Boolean {
        println("Відправка $amount $currency (+ комісія $networkFee) на гаманець ${cryptoAddress.take(6)}...${cryptoAddress.takeLast(4)}... Підтверджено!")
        return true
    }

    override fun refund(amount: Double): Boolean {
        if (amount > networkFee) {
            val refundAmount = amount - networkFee
            println("Крипто-повернення $refundAmount$currency (з урахуванням комісії мережі) надіслано.")
            return true
        } else {
            println("Помилка повернення: сума менша або дорівнює комісії мережі.")
            return false
        }
    }

    override fun printDetails() {
        super.printDetails()
        println("  └ Метод: Криптовалюта (Адреса: ${cryptoAddress.take(6)}...)")
    }
}

// 3. CashOnDeliveryPayment
class CashOnDeliveryPayment(
    transactionId: String,
    amount: Double,
    currency: String = "UAH",
    val deliveryAddress: String,
    override val receiptEmail: String
) : PaymentMethod(transactionId, amount, currency), EReceiptable {

    override fun processPayment(): Boolean {
        println("Замовлення зареєстровано. Оплата $amount $currency буде здійснена кур'єру за адресою: $deliveryAddress.")
        return true
    }

    override fun printDetails() {
        super.printDetails()
        println("  └ Метод: Післяплата (Адреса: $deliveryAddress)")
    }

    override fun sendReceipt() {
        println(" [SMS & E-Receipt] Фіскальний чек зареєстровано та продубльовано на $receiptEmail")
    }
}

//  Спеціалізована пакетна операція (Повернення коштів)
fun processMassRefund(items: List<PaymentMethod>, refundPercentage: Double) {
    println("==================================================")
    println("       МАСОВЕ ПОВЕРНЕННЯ КОШТІВ (${refundPercentage.toInt()}%)")
    println("==================================================")

    var successCount = 0
    val totalEligible = items.count { it is Refundable }

    for (payment in items) {
        // Безпечна перевірка та Smart Cast до інтерфейсу Refundable
        if (payment is Refundable) {
            val refundAmount = payment.amount * (refundPercentage / 100.0)
            val isSuccess = payment.refund(refundAmount)
            if (isSuccess) {
                successCount++
            }
        }
    }
    println("Успішно оброблено повернень: $successCount з ${items.size} транзакцій.")
}

fun main() {
    // 1. Поліморфний список транзакцій
    val payments: List<PaymentMethod> = listOf(
        CreditCardPayment(
            transactionId = "TXN-001",
            amount = 2500.0,
            cardNumber = "4149499988884321",
            receiptEmail = "client@gmail.com"
        ),
        CryptoPayment(
            transactionId = "TXN-002",
            amount = 10000.0,
            cryptoAddress = "0x71C89F3A2B9C",
            networkFee = 50.0
        ),
        CashOnDeliveryPayment(
            transactionId = "TXN-003",
            amount = 1200.0,
            deliveryAddress = "м. Львів, Відділення №5",
            receiptEmail = "receiver@lviv.ua"
        )
    )

    // 2. Загальна обробка платежів (Поліморфізм)
    println("==================================================")
    println("        ОБРОБКА ПОТОЧНИХ ТРАНЗАКЦІЙ")
    println("==================================================")
    for (payment in payments) {
        payment.printDetails()
        payment.processPayment()

        // Використання оператора is та автоматичний Smart Cast до EReceiptable
        if (payment is EReceiptable) {
            payment.sendReceipt()
        }
        println("--------------------------------------------------")
    }
    println()

    // 3. Виклик масового повернення коштів (наприклад, 50%)
    processMassRefund(payments, 50.0)
}