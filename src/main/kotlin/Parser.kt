import Grammar
import java.util.*
import kotlin.collections.ArrayList

class RecursiveDescendant(private val grammar: Grammar) {

    private var configuration: Configuration = Configuration("q", 2, Stack(), Stack())

    init {
        configuration.workingStack.push("epsilon")
        configuration.inputStack.push("epsilon")
        configuration.inputStack.push(grammar.startSymbol)
    }

    fun run(w: List<String>): List<String> {
        while (configuration.state != "f" && configuration.state != "e") {
            //println(configuration)
            val inputTop = configuration.inputStack.peek()
            val workingTop = configuration.workingStack.peek()


            if (configuration.position == w.size +1 && configuration.inputStack.peek() == "epsilon") {
                //println("success")
                success()
            }

            if (configuration.state == "q") {
                when {
                    grammar.nonTerminals.contains(inputTop) -> {
                        //println("expand")
                        expand()
                    }
                    w.size >= configuration.position && w[configuration.position - 1] == inputTop -> {
                        //println("advance")
                        advance()
                    }
                    w.size < configuration.position || w[configuration.position - 1] != inputTop -> {
                        //println("momentary insuccess")
                        momentaryInsuccess()}
                }
            } else if (configuration.state == "b") {
                if (!grammar.terminals.contains(workingTop)) {
                    //println("another try")
                    anotherTry()
                } else {
                    //println("back")
                    back()
                }
            }

        }

        return if (configuration.state == "f") {
            println("Sequence accepted\n")
            configuration.workingStack
        } else {
            throw Exception("Syntax Error")
        }
    }

    private fun expand() {
        val nonterminal = configuration.inputStack.pop()
        val result = ArrayList(grammar.productions[nonterminal]!![0])
        result.reverse()
        result.forEach { s -> configuration.inputStack.push(s) }
        configuration.workingStack.add("$nonterminal#1")
    }

    private fun advance() {
        configuration.position++
        val terminal = configuration.inputStack.pop()
        configuration.workingStack.push(terminal)
    }

    private fun momentaryInsuccess() {
        configuration.state = "b"
    }

    private fun back() {
        configuration.position--
        configuration.inputStack.push(configuration.workingStack.pop())
    }

    private fun anotherTry() {
        val topWorking = configuration.workingStack.peek()
        val gamma = grammar.productions[topWorking.split("#")[0]]!![Integer.parseInt(topWorking.split("#")[1]) - 1]
        println("Gamma: $gamma")

        if (Integer.parseInt(topWorking.split("#")[1]) != grammar.productions[topWorking.split("#")[0]]!!.size) {
            configuration.state = "q"
            gamma.forEach { _ -> configuration.inputStack.pop() }
            configuration.workingStack.pop()
            configuration.workingStack.push("${topWorking.split("#")[0]}#${Integer.parseInt(topWorking.split("#")[1]) + 1}")
            val gamma2 = ArrayList(grammar.productions[topWorking.split("#")[0]]!![Integer.parseInt(topWorking.split("#")[1])])
            println("Gamma2: $gamma2")
            gamma2.reverse()
            gamma2.forEach { s -> configuration.inputStack.push(s) }

        } else if (configuration.position == 1 && topWorking.split("#")[0] == grammar.startSymbol) {
            configuration.state = "e"
        } else {
            configuration.workingStack.pop()
            gamma.forEach { _ -> configuration.inputStack.pop() }
            configuration.inputStack.push(topWorking.split("#")[0])
        }
    }

    private fun success() {
        configuration.state = "f"
    }
}
