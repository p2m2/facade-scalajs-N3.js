package com.github.p2m2.facade

import utest._

import scala.scalajs.js.JSON

object N3StoreTest extends TestSuite {
  val tests = Tests {

    val s1 = "http://ex.org/Mickey"
    val p1 = "http://ex.org/type"
    val o1 = "http://ex.org/Mouse"

    test("Storing") {
      val store = new N3.Store();
      store.addQuad(
        (DataFactory.namedNode("http://ex.org/Pluto"),
          DataFactory.namedNode("http://ex.org/type"),
          DataFactory.namedNode("http://ex.org/Dog")
        ))
      store.addQuad(
        DataFactory.namedNode(s1),
          DataFactory.namedNode(p1),
          DataFactory.namedNode(o1)
        );

     // println(JSON.stringify(store))
      val mickey = store.getQuads(DataFactory.namedNode("http://ex.org/Mickey"), null, null)

      assert( mickey.length == 1)
      assert( mickey(0).subject.value == s1)
      assert( mickey(0).predicate.value == p1)
      assert( mickey(0).`object`.value == o1)

      val count = store.countQuads(DataFactory.namedNode("http://ex.org/Mickey"), null, null)

      assert( count == 1)

      store.forEach((quad: Quad) => {
        println("forEach -> "+JSON.stringify(quad))
      } , DataFactory.namedNode("http://ex.org/Mickey"), null, null)

      assert(store.every((quad: Quad) => {
        true
      }, DataFactory.namedNode("http://ex.org/Mickey"), null, null))

      assert(!store.every((quad: Quad) => {
        false
      }, DataFactory.namedNode("http://ex.org/Mickey"), null, null))

      assert(store.some((quad: Quad) => {
        true
      }, DataFactory.namedNode("http://ex.org/Mickey"), null, null))

      assert(!store.some((quad: Quad) => {
        false
      }, DataFactory.namedNode("http://ex.org/Mickey"), null, null))

      store.`match`(DataFactory.namedNode("http://ex.org/Mickey"), null, null)
        .on("data",(src : Quad) => {
          println(" ====== on =========")
          println(JSON.stringify(src))
        })
        .on("end", () => {
          println("All writes are now complete.");
        })
        .on("error", (message : String) => {
          println("an error occurs : " + message);
        })
        .on("prefix", (prefix : String ,iri : String) => {
          println(s"prefix $prefix = $iri ");
        })
    }
  }
}
