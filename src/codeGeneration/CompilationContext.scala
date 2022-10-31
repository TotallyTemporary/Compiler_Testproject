package codeGeneration

import ast.{FuncDeclNode, Node}
import semanticAnalysis.SymbolTable
import semanticAnalysis.SymbolType
import semanticAnalysis.Symbol
import ir.*

import scala.collection.mutable

class CompilationContext(val func: FuncDeclNode) {
  // begin registers
  private var registerCounter = 0
  private var varMap = mutable.Map[Symbol, Integer]()
  private var currentScope: SymbolTable = null
  enterScope(func.body)

  def moveVar(variable: Symbol, register: Integer): Unit = varMap.put(variable, register)
  def moveVar(variable: String, register: Integer): Unit = varMap.put(currentScope.inHierarchy(variable).get, register)

  def getVar(variable: Symbol): Integer = varMap(variable)
  def getVar(variable: String): Integer = varMap(currentScope.inHierarchy(variable).get)

  def getRegister =
    registerCounter += 1
    registerCounter-1



  // begin memory management
  private var allocated = mutable.Stack[Integer]()

  def enterScope(node: Node): Unit = {
    currentScope = SymbolTable.startTable(node)
    var localVariables = currentScope.symbolMap.values.filter(_.sType == SymbolType.Variable)
    var params = currentScope.symbolMap.values.filter(_.sType == SymbolType.Param)

    // allocate params
    for (param <- params) {
      val reg: Integer = getRegister
      moveVar(param, reg)
      println(s"defined param $param as $reg")
    }

    // local variables will be set to registers when their time comes.

    // allocate memory for BOTH params and locals here.
  }

  def exitScope(): Unit = {
    // freeMemory()
    currentScope = currentScope.parent.get
  }

  private def allocateMemory(allocateBytes: Integer): Unit = {
    allocated.append(allocateBytes)
    appendStatement(Allocate(allocateBytes))
  }

  private def freeMemory(): Unit = {
    val freeBytes = allocated.pop()
    appendStatement(Free(freeBytes))
  }

  // begin ir statement list
  var lastStatement: IRStatement = Nop()
  val firstStatement: IRStatement = lastStatement

  def appendStatement(statement: IRStatement) =
    lastStatement.next = Some(statement)
    lastStatement = statement
}
