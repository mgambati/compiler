package dev.mgambati.parser.exception

import dev.mgambati.lexer.TokenType


class EarlyExitSyntaxException(vararg expected: TokenType) :
    Exception("Final do arquivo encontrado, porém, era esperado ${expected.map { it.name }}")