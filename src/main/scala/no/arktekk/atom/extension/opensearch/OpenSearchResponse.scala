/*
 * Copyright 2012 Arktekk AS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package no.arktekk.atom.extension.opensearch

import no.arktekk.atom.extension.AtomExtension
import no.arktekk.atom._
import com.codecommit.antixml.Elem
import no.arktekk.atom.extension.opensearch.OpensearchConstants._

/**
 * @author Erlend Hamnaberg<erlend@hamnaberg.net>
 */
case class OpenSearchResponse(queries: Seq[Query] = Nil, totalResults: Option[Int] = None, itemsPerPage: Option[Int] = None, startIndex: Option[Int] = None) {
  def filter(f: (Query) => Boolean) = queries.filter(f)

  def correctionQueries = filter((q) => q.role == Role.CORRECTION)

  def exampleQueries = filter((q) => q.role == Role.EXAMPLE)

  def requestQueries = filter((q) => q.role == Role.REQUEST)

  def relatedQueries = filter((q) => q.role == Role.RELATED)

  def subsetQueries = filter((q) => q.role == Role.SUBSET)

  def supersetQueries = filter((q) => q.role == Role.SUPERSET)

  def addQuery(q: Query) = copy(queries = queries ++ Seq(q))

  def withQueries(seq: Seq[Query]) = copy(queries = seq)
  
  def withTotalResults(n: Int) = copy(totalResults = Some(n))

  def withItemsPerPage(n: Int) = copy(itemsPerPage = Some(n))

  def withStartIndex(n: Int) = copy(startIndex = Some(n))
}

object OpenSearchResponse {
  def apply(): OpenSearchResponse = apply(Nil, None, None, None)
}

object OpenSearchResponseAtomExtension extends AtomExtension[FeedLike, OpenSearchResponse] {
  val numberExt = AtomExtension(new IntAtomExtension("totalResults"), new IntAtomExtension("itemsPerPage"), new IntAtomExtension("startIndex"))

  def fromLike(like: FeedLike) = {
    val numbers = numberExt.fromLike(like)
    val queries = (like.wrapped \ namespaceSelector(ns, "query")).map(_.asInstanceOf[Elem]).map(Query(_))
    OpenSearchResponse(queries, numbers._1, numbers._2, numbers._3)
  }

  def toChildren(a: OpenSearchResponse, wrapper: ElementWrapper) = {
    a.queries.map(_.toElem).map(ElementWrapper(_)) ++ numberExt.toChildren((a.totalResults, a.itemsPerPage, a.startIndex), wrapper)
  }
}
