package dev.mgambati.parser

import dev.mgambati.lexer.Token


sealed class Symbol(val token: Token)

enum class VariableType {
    INTEGER,
    REAL
}

class Variable(val type: VariableType, token: Token) : Symbol(token)