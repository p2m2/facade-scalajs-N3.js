package com.github.p2m2.facade

import utest._

import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.language.implicitConversions

object N3ParserTest extends TestSuite {
  val tests = Tests {

    test("Creating triples/quads") {
      val myQuad =  DataFactory.quad(
        DataFactory.namedNode("https://ruben.verborgh.org/profile/#me"),
        DataFactory.namedNode("http://xmlns.com/foaf/0.1/givenName"),
        DataFactory.literal("Ruben", "en"),
        DataFactory.defaultGraph(),
      );

      println(myQuad.termType) // Quad
      println(myQuad.value) // ''
      println(myQuad.subject.value) // https://ruben.verborgh.org/profile/#me
      println(myQuad.`object`.value) // Ruben
      println(myQuad.`object`.asInstanceOf[Literal].datatype.value) // http://www.w3.org/1999/02/22-rdf-syntax-ns#langString
      println(myQuad.`object`.asInstanceOf[Literal].language) // en

    }

    def fun(error : String , quad : js.UndefOr[Quad] , prefixes : js.UndefOr[js.Object] ) = {
      quad.get match {
        case null => {
          println("# That's all, folks!")
          prefixes match {
            case prefs => println(JSON.stringify(prefs))
          }
        }
        case q => println(JSON.stringify(q))
      }
    }

    test("Parsing - From an RDF document to quads") {
      val parser : N3Parser = new N3.Parser();
      val data = """
               PREFIX c: <http://example.org/cartoons#>
               c:Tom a c:Cat.
               c:Jerry a c:Mouse;
               c:smarterThan c:Tom."""

      parser.parse(data);

      parser.parse(data,fun);
      //throw new Exception("test1")
    }


    test("Parsing - N-Triples") {
      new N3.Parser(js.Dynamic.literal(baseIRI="http://example.org/",format="N-Triples"))
            .parse(
              """
                _:a <http://ex.org/b> "c" .
                """.stripMargin)
    }

    test("Parsing - Turtle") {
      new N3.Parser(js.Dynamic.literal(baseIRI="http://example.org/",format="Turtle"))
        .parse(
          """
@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix dc: <http://purl.org/dc/elements/1.1/> .
@prefix ex: <http://example.org/stuff/1.0/> .
@prefix :<http://test#> .

<http://www.w3.org/TR/rdf-syntax-grammar>
  dc:title "RDF/XML Syntax Specification (Revised)" ;
  ex:editor [
    ex:fullname "Dave Beckett";
    ex:homePage <http://purl.org/net/dajobe/>
   ] .
:hello1 :hello2 :hello3 .
            """.stripMargin)
    }

    test("Parsing - TriG") {
      new N3.Parser(js.Dynamic.literal(baseIRI="http://example.org/",format="TriG"))
        .parse("<a> <b> <c>.")
    }

    test("Parsing - application/trig") {
      new N3.Parser(js.Dynamic.literal(baseIRI="http://example.org/",format="application/trig"))
        .parse("<a> <b> <c>.")
    }

    test("Parsing - N-Quads") {
      new N3.Parser(js.Dynamic.literal(baseIRI="http://example.org/",format="N-Quads"))
        .parse("_:a <http://ex.org/b> \"c\" <http://ex.org/g>.")
    }

    test("Parsing - N3") {
      new N3.Parser(js.Dynamic.literal(baseIRI="http://example.org/",format="N3"))
        .parse("<#Patrick> <#connaît> <#Joël> .")
    }

    test("From an RDF stream to quads") {
      // todo
    }
  }
}
