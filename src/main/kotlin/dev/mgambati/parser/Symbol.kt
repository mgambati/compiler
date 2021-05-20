package dev.mgambati.parser

import dev.mgambati.lexer.Token


sealed class Symbol(val token: Token, val scope: String)

enum class VariableType {
    INTEGER,
    REAL
}

class Variable(val type: VariableType, token: Token, scope: String) : Symbol(token, scope)
class Parameter(val type: VariableType, token: Token, scope: String) : Symbol(token, scope)
class Program(token: Token) : Symbol(token, "root")
class Procedure(token: Token, scope: String) : Symbol(token, scope)