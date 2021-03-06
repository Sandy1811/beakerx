/*
 *  Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.twosigma.beakerx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AutocompleteNodeStatic extends AutocompleteNode {

  public AutocompleteNodeStatic(String name, List<AutocompleteNode> children) {
    super(name, children);
  }

  public List<String> matchToTheWord(LinkedList<String> parts, String last) {
    if (parts.isEmpty()) {
      return findMatches(getChildren(), last);
    } else {
      Optional<AutocompleteNode> node = findNode(parts);
      if (node.isPresent()) {
        return node.get().matchToTheWord(parts, last);
      }
      return new ArrayList<>();
    }
  }

  public List<String> findNextWord(LinkedList<String> parts) {
    if (parts.isEmpty()) {
      return getChildren().stream().map(x->x.getName()).collect(Collectors.toList());
    } else {
      Optional<AutocompleteNode> node = findNode(parts);
      if (node.isPresent()) {
        return node.get().findNextWord(parts);
      }
      return new ArrayList<>();
    }
  }

  private List<String> findMatches(Collection<AutocompleteNode> nodes, String txt) {
    return nodes.stream()
            .filter(x -> x.getName().startsWith(txt))
            .filter(x -> !x.getName().equals(txt))
            .map(x -> x.getName())
            .collect(Collectors.toList());
  }



  private Optional<AutocompleteNode> findNode(LinkedList<String> parts) {
    String first = parts.removeFirst();
    return getChildren().stream().filter(x -> x.getName().equals(first)).findFirst();
  }
}
