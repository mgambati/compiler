package dev.mgambati.utils

import de.vandermeer.asciitable.AsciiTable
import de.vandermeer.asciitable.CWC_LongestLine
import dev.mgambati.parser.*

fun printSymbolsTable(symbols: Map<String, Symbol>) {
    println("Tabela de símbolos")

    val at = AsciiTable()
    val cwc = CWC_LongestLine()
    at.renderer.cwc = cwc

    at.addRule()
    at.addRow("Escopo", "Nome", "Tipo")
    at.addRule()

    symbols.forEach {
        at.addRow(it.value.scope, it.value.token.value, getReadableType(it.value))
        at.addRule();
    }
    println(at.render())
}


private fun getReadableType(symbol: Symbol): String {
    return when (symbol) {
        is Procedure -> "Procedure"
        is Variable -> if (symbol.type == VariableType.INTEGER) "Integer Variable" else "Real Variable"
        is Parameter -> if (symbol.type == VariableType.INTEGER) "Integer Parameter" else "Real Parameter"
        is Program -> "Program"
    }
}