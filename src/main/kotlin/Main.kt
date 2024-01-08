import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.util.*

fun main() {
    val grammar1 = Grammar("src/main/kotlin/g1.txt")
    val grammar2 = Grammar("src/main/kotlin/g2.txt")
    var grammar = grammar1
    val algorithm1 = RecursiveDescendant(grammar1)
    val algorithm2 = RecursiveDescendant(grammar2)
    var algorithm = algorithm1
    val seq1 = sequenceFromPIF("src/main/kotlin/PIF.out")
    val seq2 = sequenceFromPIF("src/main/kotlin/PIF2.out")
    var seq = seq1
    val output1 = Output(grammar1)
    val output2 = Output(grammar2)
    var output = output1

    val menu = mapOf(
        "1" to "Load file G1",
        "2" to "Load file G2",
        "3" to "CFG check",
        "4" to "Get productions",
        "5" to "Get terminals",
        "6" to "Get non-terminals",
        "7" to "Get productions for given non-terminal",
        "8" to "PARSE",
        "9" to "Exit"
    )

    while (true) {
        println("Menu:")
        menu.forEach { (key, value) -> println("$key. $value") }
        print("Enter your choice: ")
        val choice = readlnOrNull() ?: ""

        when (choice) {
            "1" -> {
                grammar = grammar1
                algorithm = algorithm1
                seq = seq1
                output = output1
            }
            "2" -> {
                grammar = grammar2
                algorithm = algorithm2
                seq = seq2
                output = output2
            }
            "3" -> println(grammar.checkIfCFG())
            "4" -> println(grammar.getProductions())
            "5" -> println(grammar.getTerminals())
            "6" -> println(grammar.getNonTerminals())
            "7" -> {
                print("Enter non-terminal: ")
                val nonTerminal = readlnOrNull() ?: ""
                println(grammar.getProductionsForNonTerminal(nonTerminal))
            }
            "8" -> {
                try {
                    println("Sequence: $seq")

                    val productionString = algorithm.run(seq)
                    println(productionString)
                    output.addProductionString(productionString)
                    println(output)
                    BufferedWriter(FileWriter("out2.txt")).use { bufferedWriter ->
                        bufferedWriter.write(output.toString())
                    }
                } catch (e: Exception){
                    println(e.message)
                }

            }
            "9" -> return
            else -> println("Invalid choice")
        }
    }
}

fun sequenceFromPIF(pifFileName: String): List<String> {
    Scanner(BufferedReader(FileReader(pifFileName))).use { scanner ->
        val w = mutableListOf<String>()
        while (scanner.hasNext()) {
            val line = scanner.nextLine()
            w.add(line.substring(line.indexOf('(') + 1, line.indexOf(',')))
        }
        return w
    }
}
