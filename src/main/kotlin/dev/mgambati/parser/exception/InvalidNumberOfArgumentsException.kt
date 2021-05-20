package dev.mgambati.parser.exception

import dev.mgambati.lexer.Token


class InvalidNumberOfArgumentsException(token: Token) :
    ParserException(
        level = ExceptionLevel.SEMANTIC,
        description = "Número de argumentos inválido para função ${token.value}",
        token = token
    )