package dev.mgambati.parser.exception

import dev.mgambati.lexer.Token


class IncompatibleVariableTypes (token: Token): ParserException(
    level = ExceptionLevel.SEMANTIC,
    description = "Expressão com variáveis com tipos incompatíveis.",
    token = token
)