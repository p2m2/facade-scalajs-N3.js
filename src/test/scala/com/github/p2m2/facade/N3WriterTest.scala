package com.github.p2m2.facade

import utest._

import scala.scalajs.js
import scala.scalajs.js.JSConverters._

object N3WriterTest extends TestSuite {
  val tests = Tests {

    test("From quads to a string") {
      val writer = new N3.Writer(
        js.Dynamic.literal(
          prefixes=js.Dynamic.literal(
            c="http://example.org/cartoons#"
          )))

      writer.addQuad(DataFactory.namedNode("http: //example.org/cartoons#Tom"),
          DataFactory.namedNode("http: //www.w3.org/1999/02/22-rdf-syntax-ns#type"),
          DataFactory.namedNode("http: //example.org/cartoons#Cat"))

      writer.addQuad(DataFactory.quad(
        DataFactory.namedNode("http://example.org/cartoons#Tom"),
          DataFactory.namedNode("http://example.org/cartoons#name"),
          DataFactory.literal("Tom")))

      writer.end((error, result) => println(result));
    }

    test("Blank nodes and lists") {
      val writer = new N3.Writer(
        js.Dynamic.literal(
          prefixes=js.Dynamic.literal(
            c="http://example.org/cartoons#",
            foaf="http://xmlns.com/foaf/0.1/"
          )))

      writer.addQuad(
        writer.blank(
          DataFactory.namedNode("http://xmlns.com/foaf/0.1/givenName"),
            DataFactory.literal("Tom", "en")),
          DataFactory.namedNode("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),
            DataFactory.namedNode("http://example.org/cartoons#Cat")
      )

      writer.addQuad(DataFactory.quad(
        DataFactory.namedNode("http://example.org/cartoons#Jerry"),
          DataFactory.namedNode("http://xmlns.com/foaf/0.1/knows"),
          writer.blank(Seq(js.Dynamic.literal(
          predicate=DataFactory.namedNode("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),
            `object`= DataFactory.namedNode("http://example.org/cartoons#Cat"),
          ),js.Dynamic.literal(
          predicate= DataFactory.namedNode("http://xmlns.com/foaf/0.1/givenName"),
            `object`=    DataFactory.literal("Tom", "en"),
        )).toJSArray)
      ))

      writer.addQuad(
        DataFactory.namedNode("http://example.org/cartoons#Mammy"),
          DataFactory.namedNode("http://example.org/cartoons#hasPets"),
          writer.list(Seq(
          DataFactory.namedNode("http://example.org/cartoons#Tom"),
          DataFactory.namedNode("http://example.org/cartoons#Jerry"),
          ).toJSArray)
      );
      writer.end((error, result) => println(result));
    }

  }
}
