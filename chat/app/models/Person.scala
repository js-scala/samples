package models

import scala.virtualization.lms.common.Base
import forest.lms._
import js._

case class Person(children: List[String])

trait PersonOps extends Base with Fields {
  class PersonOps()(implicit person: Rep[Person]) extends Fields[Person] {
  	def children = field[List[String]]("children")
  }
  implicit def toPersonOps(p: Rep[Person]): PersonOps = new PersonOps()(p)
}