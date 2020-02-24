package dev.mgambati.parser.exception

import dev.mgambati.lexer.Token

class UndefinedVariableException(
    token: Token
) : ParserException(
    level = ExceptionLevel.SEMANTIC,
    description = "Variável ${token.value} não foi previamente definida",
    token = token
)