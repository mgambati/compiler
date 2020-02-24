package dev.mgambati.lexer

class Token(
    val value: String,
    val type: TokenType,
    val line: Int,
    val start: Int,
    val end: Int
) {
    infix fun of(value: TokenType) = type === value
    infix fun notOf(value: TokenType) = type !== value
}