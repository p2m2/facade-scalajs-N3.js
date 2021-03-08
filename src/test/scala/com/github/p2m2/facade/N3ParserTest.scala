package com.github.p2m2.facade

import io.scalajs.nodejs.fs.Fs
import utest._

import scala.language.implicitConversions
import scala.scalajs.js
import scala.scalajs.js.JSON

object N3ParserTest extends TestSuite {
  val tests = Tests {

    test("Creating triples/quads") {
      val myQuad =  DataFactory.quad(
        DataFactory.namedNode("https://ruben.verborgh.org/profile/#me"),
        DataFactory.namedNode("http://xmlns.com/foaf/0.1/givenName"),
        DataFactory.literal("Ruben", "en"),
        DataFactory.defaultGraph(),
      )

      println(myQuad.subject.value) // https://ruben.verborgh.org/profile/#me
      println(myQuad.`object`.value) // Ruben
      println(myQuad.`object`.asInstanceOf[Literal].datatype.value) // http://www.w3.org/1999/02/22-rdf-syntax-ns#langString
      println(myQuad.`object`.asInstanceOf[Literal].language) // en

    }

    def fun(error : String , quad : js.UndefOr[Quad] , prefixes : js.UndefOr[js.Object] ) : Unit = {
      quad.get match {
        case null => {
          println("# That's all, folks!")
          prefixes match {
            case prefs => println(JSON.stringify(prefs))
          }
        }
        case q => println(q)
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

    }


    test("Parsing - N-Triples") {
      //js.Dynamic.literal(baseIRI="http://example.org/",format="N-Triples"
      new N3.Parser(N3Options(baseIRI="http://example.org/",format=N3FormatOption.`N-Triples`))
            .parse(
              """
                _:a <http://ex.org/b> "c" .
                """.stripMargin)
    }

    test("Parsing - Turtle") {
      new N3.Parser(N3Options(baseIRI="http://example.org/",format=N3FormatOption.Turtle))
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
      new N3.Parser(N3Options(baseIRI="http://example.org/",format=N3FormatOption.TriG))
        .parse("<a> <b> <c>.")
    }

    test("Parsing - application/trig") {
      new N3.Parser(N3Options(baseIRI="http://example.org/",format=N3FormatOption.`application/trig`))
        .parse("<a> <b> <c>.")
    }

    test("Parsing - N-Quads") {
      new N3.Parser(N3Options(baseIRI="http://example.org/",format=N3FormatOption.`N-Quads`))
        .parse("_:a <http://ex.org/b> \"c\" <http://ex.org/g>.")
    }

    test("Parsing - N3") {
      new N3.Parser(N3Options(baseIRI="http://example.org/",format=N3FormatOption.N3))
        .parse("<#Patrick> <#connaît> <#Joël> .")
    }

    test("From an RDF stream to quads") {
      val parser = new N3.Parser()
      val rdfStream = Fs.createReadStream("src/test/resources/example.ttl")
      parser.parse(rdfStream, fun)
    }
  }
}
