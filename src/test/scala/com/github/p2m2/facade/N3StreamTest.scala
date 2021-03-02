package com.github.p2m2.facade

import io.scalajs.nodejs.fs.Fs
import utest._

import scala.language.implicitConversions

object N3StreamTest extends TestSuite {
  val tests = Tests {

    test("stream parser") {
      val streamParser = new N3.StreamParser()
      val inputStream = Fs.createReadStream("src/test/resources/example.ttl")
//      val streamWriter = new N3.StreamWriter({ prefixes: { c: 'http://example.org/cartoons#' } });
    }
  }
}
