# facade-scalajs-N3.js
[![Scala.js](https://www.scala-js.org/assets/badges/scalajs-1.13.0.svg)](https://www.scala-js.org)
[![Coverage Status](https://coveralls.io/repos/github/p2m2/facade-scalajs-N3.js/badge.svg?version=v1.17.2)](https://coveralls.io/github/p2m2/facade-scalajs-N3.js)
![CI](https://github.com/p2m2/facade-scalajs-N3.js/actions/workflows/ci.yml/badge.svg)
![Release](https://github.com/p2m2/facade-scalajs-N3.js/actions/workflows/release.yml/badge.svg)
[![Maven](https://badgen.net/badge/icon/maven?icon=maven&label)](https://search.maven.org/search?q=g:com.github.p2m2)

Scala.js Facade of the [N3.js library](https://github.com/rdfjs/N3.js)

## Install

```
libraryDependencies += "com.github.p2m2" %%% "n3js" % "v1.17.2",
```

## N3.Parser
```scalajs
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
```

## N3.Writer

```scalajs=
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
```
## N3.Store

```scalajs=
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

      val mickey = store.getQuads(DataFactory.namedNode("http://ex.org/Mickey"), null, null)
```

Note :
 - N3.js Examples are implemented in the [test directory](./src/test)
