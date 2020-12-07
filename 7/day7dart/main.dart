import 'dart:io';

class ColorMap {
  final String color;
  final List<Rule> rules;

  const ColorMap(this.color, this.rules);
}

class Rule {
  final String color;
  final int weight;

  const Rule(this.color, this.weight);
}

var rules = Map<String, List<String>>();
var forwardRules = Map<String, List<Rule>>();

main() {
  new File('input.txt').readAsString().then((String contents) {
    final colorLists = contents.split("\n").map((e) => parse(e));
    print("${colorLists}");

    for (var colorList in colorLists) {
      var bagColor = colorList.first;
      for (var color in colorList.getRange(1, colorList.length)) {
        rules.putIfAbsent(color, () => []);
        rules[color].add(bagColor);
      }
    }
    for (var rule in rules.entries) {
      print(rule);
    }
    var seen = findContainingBags("shiny gold", Set());
    seen.remove("shiny gold");
    print(seen.length);

    var colorMaps = contents.split("\n").map((e) => parseColorMap(e));
    for (var m in colorMaps) {
      forwardRules[m.color] = m.rules;
    }

    var numBags = findBagsInBag("shiny gold");
    print(numBags);
  });
}

int findBagsInBag(String color) {
  var num = 0;
  if (forwardRules.containsKey(color)) {
    for (var rules in forwardRules[color]) {
      var weight = rules.weight;
      num += weight;
      num += weight * findBagsInBag(rules.color);
    }
  }
  return num;
}

Set<String> findContainingBags(String color, Set<String> seen) {
  seen.add(color);
  if (rules.containsKey(color)) {
    for (var containingColor in rules[color]) {
      seen.addAll(findContainingBags(containingColor, seen));
    }
  }
  return seen;
}

List<String> parse(String s) {
  var matches = RegExp(r'(.+) bags contain (.*)').matchAsPrefix(s);
  var bagColor = matches.group(1);
  var containStatement = matches.group(2);
  var containsColors =
      containStatement.split(", ").expand((e) => parseContainStatement(e));
  return [bagColor, ...containsColors];
}

parseContainStatement(String s) {
  if ("no other bags." == s) {
    return [];
  } else {
    return [RegExp(r"[0-9]+ (.*) bag.*").matchAsPrefix(s).group(1)];
  }
}

ColorMap parseColorMap(String s) {
  var matches = RegExp(r'(.+) bags contain (.*)').matchAsPrefix(s);
  var bagColor = matches.group(1);
  var containStatement = matches.group(2);
  var containsColors = containStatement.split(", ").expand((e) => parseRule(e));
  return ColorMap(bagColor, [...containsColors]);
}

List<Rule> parseRule(String s) {
  if ("no other bags." == s) {
    return [];
  } else {
    var matches = RegExp(r"([0-9]+) (.*) bag.*").matchAsPrefix(s);
    return [Rule(matches.group(2), int.parse(matches.group(1)))];
  }
}
