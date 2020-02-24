package dev.mgambati.utils

import de.vandermeer.asciitable.AsciiTable
import de.vandermeer.asciitable.CWC_LongestLine
import dev.mgambati.lexer.Token

fun printTokensTable(tokens: List<Token>) {
    val at = AsciiTable()
    val cwc = CWC_LongestLine()
    at.renderer.cwc = cwc

    at.addRule()
    at.addRow("Token", "Valor")
    at.addRule()

    tokens.forEach { token ->
        at.addRow(token.value, token.type)
        at.addRule();
    }
    println(at.render())
}