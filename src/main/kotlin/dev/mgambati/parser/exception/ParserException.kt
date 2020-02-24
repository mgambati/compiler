package dev.mgambati.parser.exception

import dev.mgambati.lexer.Token

open class ParserException(
    private val level: ExceptionLevel,
    private val description: String,
    private val token: Token? = null
) : Exception() {
    override val message: String =
        """
            ${level.readableName}
            $description
            ${buildPosition()}
        """.trimIndent()

    private fun buildPosition(): String {
        if (token == null) return ""

        return """
            Linha: ${token.line}
            Posição: ${token.start}:${token.end}
        """.trim()
    }
}