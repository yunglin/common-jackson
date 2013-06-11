/*
 * Copyright (c) 2013. Blue Tang Studio LLC. All rights reserved.
 */

package tw.hyl.common.jackson

import org.scalatest.testng.TestNGSuite
import org.testng.annotations.Test
import com.fasterxml.jackson.annotation.{JsonProperty, JsonCreator}


class JsonSerializerTest extends TestNGSuite {

  import JsonSerializerTest._

  @Test
  def testSerializeIntArray() {
    assert(JsonSerializer.toJson(Array(1, 2)) === "[1,2]")
    assert(JsonSerializer.fromJson[Array[Int]]("[1,2]") === Array(1, 2))
  }

  @Test
  def testSerializeScalaOption() {
    assert(JsonSerializer.toJson(Option(null)) === "null")
    assert(JsonSerializer.fromJson[Option[_]]("null") === None)
  }

  @Test
  def testSerializeScalaSeq() {
    assert(JsonSerializer.toJson(Seq(1, 2)) === "[1,2]")
    assert(JsonSerializer.fromJson[Seq[Int]]("[1,2]") === Seq(1, 2))
  }

  @Test
  def testSerializeScalaCaseClassWithLazyVal() {
    val testee = CaseClassWithLazyVal("str", 1)
    val json = """{"str":"str","integer":1}"""
    assert(JsonSerializer.toJson(testee) === json)
    assert(JsonSerializer.fromJson[CaseClassWithLazyVal](json) === testee)

  }

}

object JsonSerializerTest {

  case class CaseClassWithLazyVal @JsonCreator()(
       @JsonProperty("str") str: String,
       @JsonProperty("integer") intt: Int) {
  }

}