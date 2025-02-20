package sttp.tapir.server.pekkohttp

import org.apache.pekko.http.scaladsl.model.HttpHeader
import org.apache.pekko.http.scaladsl.model.HttpHeader.ParsingResult
import sttp.model.{HasHeaders, Header, HeaderNames}

import scala.collection.immutable.Seq

private[pekkohttp] object PekkoModel {
  private val ctHeaderNameLowerCase = HeaderNames.ContentType.toLowerCase
  private val clHeaderNameLowerCase = HeaderNames.ContentLength.toLowerCase
  private val teHeaderNameLowerCase = HeaderNames.TransferEncoding.toLowerCase

  def parseHeadersOrThrowWithoutContentHeaders(hs: HasHeaders): Seq[HttpHeader] =
    hs.headers
      .map(parseHeaderOrThrow)
      .filterNot(h => h.is(ctHeaderNameLowerCase) || h.is(clHeaderNameLowerCase) || h.is(teHeaderNameLowerCase))

  def parseHeaderOrThrow(h: Header): HttpHeader =
    HttpHeader.parse(h.name, h.value) match {
      case ParsingResult.Ok(h, _)     => h
      case ParsingResult.Error(error) => throw new IllegalArgumentException(s"Cannot parse header (${h.name}, ${h.value}): $error")
    }
}
