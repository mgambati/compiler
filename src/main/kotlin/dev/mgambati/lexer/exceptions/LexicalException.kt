package dev.mgambati.lexer.exceptions

class LexicalException(
    val value: String,
    val line: Int,
    val start: Int)
    : Exception("Erro léxico na linha $line e posição $start, \"$value\" não é uma cadeia válida.")