package dev.mgambati.parser

import dev.mgambati.lexer.Token
import dev.mgambati.parser.exception.*
import dev.mgambati.utils.printSymbolsTable


class SymbolTable {
    private val table = mutableMapOf<String, Symbol>()
    private val staging = mutableListOf<Token>()

    private var rootScope = "root"
    private var currentScope = "root"

    fun setRootScope(scope: String) {
        rootScope = scope
        currentScope = scope
    }

    fun resetToRootScope() {
        currentScope = rootScope
    }

    fun setScope(scope: String) = run { currentScope = scope }

    fun addToStaging(token: Token) {
        staging.add(token)
    }

    fun addProgramToTable(token: Token) {
        val key = "$currentScope:${token.value}"
        table[key] = Program(token)
    }

    fun addProcedureToTable(token: Token) {
        val key = "$currentScope:${token.value}"
        table[key] = Procedure(token, currentScope)
    }

    fun addStagingToTableAsVariable(type: VariableType) {
        staging.forEach {
            val key = "$currentScope:${it.value}"
            if (table.containsKey(key)) throw VariableAlreadyDeclaredException(it)

            table[key] = Variable(type, token = it, scope = currentScope)
        }
        staging.clear()
    }

    fun addStagingToTableAsParameter(type: VariableType) {
        staging.forEach {
            val key = "$currentScope:${it.value}"
            if (table.containsKey(key)) throw ParameterAlreadyDeclaredException(it)

            table[key] = Parameter(type, token = it, scope = currentScope)
        }
        staging.clear()
    }

    fun lookupOnCurrentScopeOrRoot(token: Token)  {
        lookup(token.value, currentScope) ?: throw UndefinedVariableException(token)
    }

    private fun lookup(name: String, scope: String, canFallbackOnRoot:Boolean = true): Symbol?  {
        val symbolOnScope = table["$scope:$name"]
        if (symbolOnScope != null) return symbolOnScope


        return if (canFallbackOnRoot) table["$rootScope:$name"] else null
    }

    fun clearStaging() {
        staging.clear()
    }

    fun lookupStaging() {
        staging.forEach {
            lookup(it.value, currentScope) ?: throw UndefinedVariableException(it)
        }
    }

    private fun lookupForParamsOfProcedure(scope: String): List<Symbol> {
        return table.filter {
            it.value.scope == scope && it.value is Parameter
        }.values.toList()
    }


    fun assertStagingTypesForProcedureCall(procedureToken: Token) {
        val procedureName = procedureToken.value
        val paramsOfProcedure = lookupForParamsOfProcedure(procedureName)
        val arguments = staging.map { lookup(it.value, currentScope) }.filterNotNull()

        if (paramsOfProcedure.size != arguments.size)
            throw InvalidNumberOfArgumentsException(procedureToken)

        paramsOfProcedure.forEachIndexed { index, param ->
            if ((param as Parameter).type !== (arguments[index] as Variable).type)
                throw IncompatibleVariableTypes(arguments[index].token)
        }

        staging.clear()
    }
    fun assertStagingTypes() {
        val distinct = staging.map { lookup(it.value, currentScope) as Variable }.map { it.type }.distinct()
        val isCompatible = distinct.size == 1
        if (!isCompatible) throw IncompatibleVariableTypes(staging.first())
        staging.clear()
    }

    fun print() {
        printSymbolsTable(symbols = table)
    }
}