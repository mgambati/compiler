package dev.mgambati.parser.exception

import dev.mgambati.lexer.Token
import dev.mgambati.lexer.TokenType

fun buildExpected(vararg expected: TokenType): String {
    if (expected.size == 1) {
        return expected.first().name
    }

    return expected.joinToString(separator = "|", prefix = "(", postfix = ")") { it.name }
}

class SyntaxException(
    private vararg val expected: TokenType,
    val found: Token
) : ParserException(
    level = ExceptionLevel.SYNTAX,
    description = """
        Token inesperado ${found.type.name} encontrado, era esperado ${buildExpected(*expected)}.
    """.trimIndent(),
    token = found
)