package com.github.p2m2.facade
import utest._

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
      store.addQuad(
        DataFactory.namedNode(s1),
        DataFactory.namedNode(p1),
        DataFactory.namedNode("http://ex.org/Person")
      );

     // println(JSON.stringify(store))
      val mickey = store.getQuads(DataFactory.namedNode("http://ex.org/Mickey"), null, null)

      assert( mickey.length == 2)
      assert( mickey(0).subject.value == s1)
      assert( mickey(0).predicate.value == p1)
      assert( mickey(0).`object`.value == o1)

      val count = store.countQuads(DataFactory.namedNode("http://ex.org/Mickey"), null, null)

      assert( count == 2)

      store.forEach((quad: Quad) => {
        println("forEach -> "+quad)
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
          println(src)
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

    test("Storing 2") {
      val q1 =  (DataFactory.namedNode("http://ex.org/Pluto"),
          DataFactory.namedNode("http://ex.org/type"),
          DataFactory.namedNode("http://ex.org/Dog")
        )

      val q2 =  (DataFactory.namedNode("http://ex.org/Mickey"),
        DataFactory.namedNode("http://ex.org/type"),
        DataFactory.namedNode("http://ex.org/Mouse")
      )

      val q3 =  (DataFactory.namedNode("http://ex.org/Donald"),
        DataFactory.namedNode("http://ex.org/type"),
        DataFactory.namedNode("http://ex.org/Duck")
      )

      val store = new N3.Store();
      store.addQuad(q1)
      store.addQuad(q2)
      store.addQuad(q3)

      assert( store.countQuads(null, null, null) == 3 )
    }
  }
}
