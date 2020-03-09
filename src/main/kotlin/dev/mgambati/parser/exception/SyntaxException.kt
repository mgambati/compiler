package dev.mgambati.parser.exception

import dev.mgambati.lexer.Token
import dev.mgambati.lexer.TokenType

class SyntaxException(
    private vararg val expected: TokenType,
    val found: Token
) : ParserException(
    level = ExceptionLevel.SYNTAX,
    description = """
        Token inesperado ${found.type.name} encontrado, era esperado ${buildExpectedTypeMessage(*expected)}.
    """.trimIndent(),
    token = found
)