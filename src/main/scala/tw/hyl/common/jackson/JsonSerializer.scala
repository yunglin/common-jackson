/*
 * Copyright (c) 2013. Blue Tang Studio LLC. All rights reserved.
 */
package tw.hyl.common.jackson

import com.fasterxml.jackson.databind.{Module, ObjectMapper}

import java.io.StringWriter
import java.io.InputStream


/**
 * serializer that serialize (and de-serialize) objects to json strings.
 *
 */
object JsonSerializer {

    val mapper = {
        val m = new ObjectMapper(); // can reuse, share globally
        try {
            val scalaModule = Class.forName("com.fasterxml.jackson.module.scala.DefaultScalaModule")
            m.registerModule(scalaModule.newInstance().asInstanceOf[Module])
            val paranamerModule = Class.forName("com.fasterxml.jackson.module.paranamer.ParanamerModule")
            m.registerModule(paranamerModule.newInstance().asInstanceOf[Module])
        } catch {
            case e: ClassNotFoundException => {}
        }
      try {
          val jodaModule = Class.forName("com.fasterxml.jackson.datatype.joda.JodaModule")
          m.registerModule(jodaModule.newInstance().asInstanceOf[Module])
      } catch {
          case e: ClassNotFoundException => {}
      }
        m
    }

    def toJson(obj: Any): String = {
        val writer = new StringWriter()
        mapper.writeValue(writer, obj)
        return writer.toString
    }

    def toJsonBytes(obj: Any): Array[Byte] = {
        return toJson(obj).getBytes("UTF-8");
    }

    /**
     * don't use this one unless you know what you are doing.
     */
    def fromJson[T <: AnyRef](jsonString: Array[Byte])(implicit m: Manifest[T]): T = {
        mapper.readValue(new String(jsonString), m.erasure).asInstanceOf[T];
    }

    /**
     * don't use this one unless you know what you are doing.
     */
    def fromJson[T <: AnyRef](istream: InputStream)(implicit m: Manifest[T]): T = {
        mapper.readValue(istream, m.erasure).asInstanceOf[T];
    }

    /**
     * don't use this one unless you know what you are doing.
     */
    def fromJson[T <: AnyRef](istream: InputStream, clazz: Class[T]): T = {
        mapper.readValue(istream, clazz);
    }

    def fromJson[T <: AnyRef](jsonString: String)(implicit m: Manifest[T]): T = {
        mapper.readValue(jsonString, m.erasure).asInstanceOf[T];
    }

    def fromJson[T](jsonString: String, clazz: Class[T]): T = {
        return mapper.readValue(jsonString, clazz);
    }

    def fromJson[T](jsonString: Array[Byte], clazz: Class[T]): T = {
        return fromJson[T](new String(jsonString), clazz);
    }
}
