package test.scala

import org.scalatest.FunSuite

class InVar[T]     { override def toString = "InVar" }
class CoVar[+T]     { override def toString = "CoVar" }
class ContraVar[-T] { override def toString = "ContraVar"}

/**
 Exercise to understand type variance from
 http://oldfashionedsoftware.com/2008/08/26/variance-basics-in-java-and-scala/

 My goal is to understand this scala type:

 trait CanBuildFrom [-From, -Elem, +To] extends AnyRef
*/
class VarianceTest extends FunSuite{

    test("assignment works when param types are equal"){
        val test1: InVar[String] = new InVar[String]
        val test2: CoVar[String] = new CoVar[String]
        val test3: ContraVar[String] = new ContraVar[String]
    }

    test("invariant type assignment won't work if param types are different"){
        // these wont compile
        //val test1: InVar[AnyRef] = new InVar[String]
        //val test2: InVar[String] = new InVar[AnyRef]
    }

    test("covariant type assignment works only if assigned param is a subtype"){
        val test1: CoVar[AnyRef] = new CoVar[String]
        // but this won't compile
        //val test2: CoVar[String] = new CoVar[AnyRef]

        val strings = List("a","b","c")
        val objects: List[Object] = strings
        assert(objects(0) === "a")

        val mixed = strings ++ List(1)
        assert(mixed(0) === "a")
    }

    // when would this case be useful?
    test("contraviariant type assignment works only if assigned param is supertype"){
        // this won't compile
        //val test1:  ContraVar[AnyRef] = new ContraVar[String]
        // but this will
        val test2:  ContraVar[String] = new ContraVar[AnyRef]
    }

    test("covariant types are restricted so it's not possible to break the type by adding a supertype to the list for example"){
        class LinkedList[+A] {
            // the following lines won't compile because it is a way you could break the type ..?
            //private var next: LinkedList[A] = null
            //def add1(item: A): Unit = {  }

            // but why doesn't this work - it works for List
            //def +: (elem: A): List[A] = { List.empty}


            // but this method declaration is fine because it would return the more general type
            def add1[B >: A](x : B) : List[B] ={ List.empty
            }

            // this doesn't work because it is equivalent maybe to add1?
            //def add2[B <: A](x : B) : List[A] ={ List.empty}

            // this would compile but there seems no way i can implement it
            //def get(index: Int): A = {  }
        }
    }

}
