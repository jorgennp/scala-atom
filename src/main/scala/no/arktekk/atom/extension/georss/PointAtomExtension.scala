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

package no.arktekk.atom.extension.georss

import no.arktekk.atom.extension.{SimpleTextElementWrapper, OptionSelectableElementWrapperAtomExtension}
import no.arktekk.atom._

/**
 * @author Erlend Hamnaberg<erlend@hamnaberg.net>
 */
class PointAtomExtension(format: String = "###.#####") extends OptionSelectableElementWrapperAtomExtension[EntryLike, Point] {
  protected def selector = namespaceSelector(GeorssConstants.ns, "point")

  protected def function = (e) => Point(new SimpleTextElementWrapper(e).value.get).get

  def toChildren(a: Option[Point], parent: ElementWrapper) = a.map( p =>
    SimpleTextElementWrapper(NamespacedName(GeorssConstants.ns, GeorssConstants.prefix, "point"), p.toValue(format))
  ).toSeq
}

object PointAtomExtension {
  def apply() = new PointAtomExtension()
}
