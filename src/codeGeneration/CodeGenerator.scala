package codeGeneration

import java.util.UUID
import scala.collection.mutable

/*
*
* Three address codes:
* NOP: does nothing
* PUSH: adds number to stack
* ADD: pops two numbers from stack, adds them and pushes output
* NEG: pops number from stack, gets the additive negate operation result and pushes it
*
* Implement later
* PRINT (easy for strings, hard for number convert to string)
* (maybe consider linking printf?)
*
*/

/*
syscalls, look up number here: https://chromium.googlesource.com/chromiumos/docs/+/master/constants/syscalls.md

rax for call number
args: rdi, rsi, rdx (more not needed for 3-address code)
*/
object Main extends App {
  val cdg = CodeGenerator()
  cdg.addString("Hello testing World!")
  println(cdg.generateASM())
}


class CodeGenerator:
  // strings stuff
  private def generateStringID = "str_" + UUID.randomUUID.toString.replaceAll("-", "")

  def addString(str: String) =
    val uuid = generateStringID
    this.strings.put(str, uuid)
    uuid

  private val strings = mutable.HashMap[String, String]()

  def generateASM() =
    // initializing some stuff
    val builder = new StringBuilder()
    builder.append("global _start\n")

    // put code here
    builder.append("section .text\n")
    builder.append("_start:\n") // entry point

    // TODO Add code here

    // this bit of code prints out the first string to be added.
    val f = this.strings.iterator.next()
    builder.append(s"  mov rax, 1\n") // write
    builder.append(s"  mov rdi, 1\n") // stdout
    builder.append(s"  mov rsi, ${f._2}\n") // string pointer
    builder.append(s"  mov rdx, ${f._2}_length\n") // string length
    builder.append(s"  syscall\n")

    // Stop program with exit code 3.
    builder.append("  mov rax, 60\n")
    builder.append("  mov rdi, 3\n")
    builder.append("  syscall\n")
    builder.append("  ret\n")

    // initialized data (strings and stuff) here
    builder.append("section .data\n")

    // make new names for all the strings.
    strings.foreach(f => {
      builder.append(s"  ${f._2}: db \"${f._1}\"\n")
      builder.append(s"  ${f._2}_length: equ $$ - ${f._2}\n")
    })

    // uninitialized data here
    builder.append("section .bss\n")

    builder.toString
end CodeGenerator