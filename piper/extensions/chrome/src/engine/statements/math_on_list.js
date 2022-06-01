import { stringify } from "../util"


function mathModes(values) {
  var modes = [];
  var counts = [];
  var maxCount = 0;
  for (var i = 0; i < values.length; i++) {
    var value = values[i];
    var found = false;
    var thisCount;
    for (var j = 0; j < counts.length; j++) {
      if (counts[j][0] === value) {
        thisCount = ++counts[j][1];
        found = true;
        break;
      }
    }
    if (!found) {
      counts.push([value, 1]);
      thisCount = 1;
    }
    maxCount = Math.max(thisCount, maxCount);
  }
  for (var j = 0; j < counts.length; j++) {
    if (counts[j][1] === maxCount) {
        modes.push(counts[j][0]);
    }
  }
  return modes;
}


function mathStandardDeviation(numbers) {
    var n = numbers.length;
    if (!n) return null;
    var mean = numbers.reduce(function(x, y) {return x + y;}) / n;
    var variance = 0;
    for (var j = 0; j < n; j++) {
      variance += Math.pow(numbers[j] - mean, 2);
    }
    variance = variance / n;
    return Math.sqrt(variance);
}


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let op = stringify(pointer.op)
        let values = await this.executeStatement(stringify(pointer.list))

        switch(op) {
            case "SUM":
                return values.reduce((a, b) => a + b, 0)
            case "MIN":
                return Math.min(...values)
            case "MAX":
                return Math.max(...values)
            case "AVERAGE":
                return values.reduce((a, b) => a + b, 0) / values.length
            case "MEDIAN":
                values.sort((a, b) => a - b)
                let half = Math.floor(values.length / 2)
                if (values.length % 2) {
                    return values[half]
                }
                return (values[half - 1] + values[half]) / 2.0
            case "MODE":
                return mathModes(values)
            case "STD_DEV":
                return mathStandardDeviation(values)
            case "RANDOM":
                return values[Math.floor(Math.random() * values.length)]
        }
    } catch (e) {
        onError(e)
    }

    return null
}
