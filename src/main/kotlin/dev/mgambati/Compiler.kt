package dev.mgambati

import dev.mgambati.lexer.Lexer
import dev.mgambati.parser.Parser
import dev.mgambati.utils.printTokensTable


fun main() {
    val sampleSource = """
        var foo, bar: integer;
            foobar: real
        if foo + bar then foo := bar
    """.trimIndent()

    try {
        val lexer = Lexer()
        val tokens = lexer.tokenize(sampleSource)

        printTokensTable(tokens)

        val parser = Parser(tokens)
        parser.parse()

        println("Nenhum erro sintático detectado.")
    } catch (e: Exception) {
        val red = "\u001B[31m"
        val reset = "\u001B[0m"
        println(red + e.message + reset)
    }

}

