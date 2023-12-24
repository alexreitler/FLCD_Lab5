import java.io.File
import java.lang.StringBuilder

class Grammar(filePath: String) {
    val productions: HashMap<String, ArrayList<ArrayList<String>>> = HashMap()
    val terminals: MutableList<String> = mutableListOf()
    val nonTerminals: MutableList<String> = mutableListOf()
    var startSymbol: String = ""

    init {
        readFile(filePath)
    }

    private fun readFile(filePath: String) {
        val delimiter = " "
        val lines = File(filePath).readLines()

        lines[0].split(delimiter).forEach { t -> if (t.trim().isNotEmpty()) nonTerminals.add(t.trim()) }
        lines[1].split(delimiter).forEach { nt -> if (nt.trim().isNotEmpty()) terminals.add(nt.trim()) }
        startSymbol = lines[2].trim()
        lines.drop(3).forEach { p ->
            val production = p.split("->")
            val left = production[0].trim()
            val right = production[1].trim().split(delimiter).toCollection(ArrayList())
            productions.computeIfAbsent(left) { ArrayList() }.add(right)
        }
    }

    fun checkIfCFG(): Boolean {
        var containsStartingSymbol = false
        productions.forEach { (left, _) ->
            if (left !in nonTerminals) {
                println("Symbol $left is not in the non-terminals")
                return false
            }

            if (left == startSymbol) {
                containsStartingSymbol = true
            }
        }

        if (!containsStartingSymbol) {
            println("Starting symbol $startSymbol is not in the non-terminals")
            return false
        }

        productions.values.forEach { p ->
            p.forEach { t ->
                t.forEach { s ->
                    if (s !in terminals && s !in nonTerminals && s != "epsilon") {
                        println("Symbol $s is not in the terminals or non-terminals")
                        return false
                    }
                }
            }
        }

        return true
    }

    fun getProductions(): String {
        val result = StringBuilder()
        productions.forEach { (left, right) ->
            right.forEach { t ->
                result.append("$left -> ${t.joinToString(" ")}\n")
            }
        }
        return result.toString()
    }

    fun getTerminals(): String {
        return terminals.joinToString(" ")
    }

    fun getNonTerminals(): String {
        return nonTerminals.joinToString(" ")
    }

    fun getProductionsForNonTerminal(nonTerminal: String): String {
        val result = StringBuilder()

        if (!productions.containsKey(nonTerminal)) {
            result.append("No productions for $nonTerminal")
            return result.toString()
        }

        productions[nonTerminal]?.forEach { t ->
            result.append("$nonTerminal -> ${t.joinToString(" ")}\n")
        }

        return result.toString()
    }

    /*fun getStartSymbol(): String {
        return startSymbol;
    }*/
}
