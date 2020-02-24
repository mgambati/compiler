package dev.mgambati.parser

import dev.mgambati.lexer.Token
import dev.mgambati.parser.exception.IncompatibleVariableTypes
import dev.mgambati.parser.exception.VariableAlreadyDeclaredException
import java.lang.Exception


class SymbolTable {
    private val table = mutableMapOf<String, Symbol>()
    private val staging = mutableListOf<Token>()


    fun addToStaging(token: Token) {
        staging.add(token)
    }

    fun addStagingToTable(type: VariableType) {
        staging.forEach {
            if (table.containsKey(it.value)) throw VariableAlreadyDeclaredException(it)
            table[it.value] = Variable(type, token = it)
        }

        staging.clear()
    }

    fun lookup(name: String): Symbol? = table[name]

    fun assertStagingTypes() {
        val distinct = staging.map { lookup(it.value) as Variable }.map { it.type }.distinct()
        val isCompatible = distinct.size == 1
        if (!isCompatible) throw IncompatibleVariableTypes(staging.first())
        staging.clear()
    }
}