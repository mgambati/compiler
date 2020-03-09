package dev.mgambati.parser.exception

import dev.mgambati.lexer.TokenType

fun buildExpectedTypeMessage(vararg expected: TokenType): String {
    if (expected.size == 1) {
        return expected.first().name
    }

    return expected.joinToString(separator = "|", prefix = "(", postfix = ")") { it.name }
}