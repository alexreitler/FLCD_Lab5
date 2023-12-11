fun main() {
    val grammar1 = Grammar("src/main/kotlin/g1.txt")
    val grammar2 = Grammar("src/main/kotlin/g2.txt")
    var grammar = grammar1
    val menu = mapOf(
        "1" to "Load file G1",
        "2" to "Load file G2",
        "3" to "CFG check",
        "4" to "Get productions",
        "5" to "Get terminals",
        "6" to "Get non-terminals",
        "7" to "Get productions for given non-terminal",
        "8" to "Exit"
    )

    while (true) {
        println("Menu:")
        menu.forEach { (key, value) -> println("$key. $value") }
        print("Enter your choice: ")
        val choice = readlnOrNull() ?: ""

        when (choice) {
            "1" -> grammar = grammar1
            "2" -> grammar = grammar2
            "3" -> println(grammar.checkIfCFG())
            "4" -> println(grammar.getProductions())
            "5" -> println(grammar.getTerminals())
            "6" -> println(grammar.getNonTerminals())
            "7" -> {
                print("Enter non-terminal: ")
                val nonTerminal = readlnOrNull() ?: ""
                println(grammar.getProductionsForNonTerminal(nonTerminal))
            }
            "8" -> return
            else -> println("Invalid choice")
        }
    }
}
