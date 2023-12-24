import java.util.*

data class Configuration(
    var state: String,
    var position: Int,
    var workingStack: Stack<String>,
    var inputStack: Stack<String>
) {
    override fun toString(): String {
        return "Configuration{" +
                "state='$state'" +
                ", pos=$position" +
                ", working=$workingStack" +
                ", input=$inputStack" +
                '}'
    }
}
