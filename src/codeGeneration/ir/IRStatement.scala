package codeGeneration.ir

class IRStatement(var next: Option[IRStatement], var cond: Option[IRStatement])

case class Nop() extends IRStatement(None, None)

case class Add(dest: Integer, src0: Integer, src1: Integer) extends IRStatement(None, None)
case class Ref(dest: Integer, src: Integer) extends IRStatement(None, None)

case class Allocate(numBytes: Integer) extends IRStatement(None, None)
case class Free(numBytes: Integer) extends IRStatement(None, None)


case class Init(dest: Integer, intValue: Integer, label: String) extends IRStatement(None, None)

/*
    dest = src
    copies src into dest
*/
case class Copy(dest: Integer, src: Integer) extends IRStatement(None, None)

/*
    *dest = src
    copies src into the memory address of dest
*/
case class Write(dest: Integer, src: Integer) extends IRStatement(None, None);
