import java.util.*

class Output(private val grammar: Grammar) {
    private val nrChildren: MutableMap<String, Int> = HashMap()
    private val values: MutableList<String> = ArrayList()
    private val father: MutableList<Int> = ArrayList()
    private val leftChild: MutableList<Int> = ArrayList()
    private val rightSibling: MutableList<Int> = ArrayList()

    init {
        for (nonterminal in grammar.nonTerminals) {
            val productions = grammar.productions[nonterminal]!!
            for (i in productions.indices) {
                nrChildren[nonterminal + "#" + (i + 1)] = productions[i].size
            }
        }
        for (terminal in grammar.terminals) {
            nrChildren[terminal] = 0
        }
        nrChildren["epsilon"] = 1
    }

    fun addProductionString(productionString: List<String>) {
        val stack: Stack<Int> = Stack()
        stack.push(-1)
        for (p in productionString) {
            val parent = stack.pop()
            val idx = add(p, parent)
            for (i in 0 until nrChildren[p]!!) {
                stack.push(idx)
            }
        }
    }

    fun add(node: String, parent: Int): Int {
        values.add(node)
        val index = values.size - 1
        rightSibling.add(-1)
        leftChild.add(-1)
        if (parent == -1) {
            return index
        }

        if (leftChild[parent] == -1) {
            leftChild[parent] = index
        } else {
            var current = leftChild[parent]
            var prev = -1
            while (current != -1) {
                prev = current
                current = rightSibling[current]
            }
            rightSibling[prev] = index
        }
        father.add(parent)
        return index
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("Parser output:\n\n")
        sb.append("Values: ").append(values).append("\n")
        sb.append("Father: ").append(father).append("\n")
        sb.append("Left child: ").append(leftChild).append("\n")
        sb.append("Right sibling: ").append(rightSibling).append("\n\n")
        if (values.isEmpty()) {
            return sb.toString()
        }
        sb.append(subtree(0))
        return sb.toString()
    }

    private fun subtree(node: Int): String {
        val sb = StringBuilder()
        var value = values[node]
        if (value.contains("#")) {
            value = value.split("#")[0]
        }
        sb.append("$value\n")
        val subtrees: MutableList<String> = ArrayList()
        var child = leftChild[node]
        while (child != -1) {
            val subtreeString = subtree(child)
            val shiftedSubtree = subtreeString.split("\n").joinToString("\n") { "\t$it" }
            val linkedSubtree = " -- ${shiftedSubtree.substring(1)}"
            subtrees.add(linkedSubtree)
            child = rightSibling[child]
        }
        val B: MutableList<String> = ArrayList()
        if (subtrees.size > 0) {
            for (a in subtrees.subList(0, subtrees.size - 1)) {
                val b = a.split("\n").joinToString("\n") { " \t|$it".substring(1) }
                B.add(b)
            }
            B.add(" \t\\${subtrees[subtrees.size - 1].substring(1)}")
        }
        B.forEach { sb.append(it) }
        return sb.toString()
    }
}
