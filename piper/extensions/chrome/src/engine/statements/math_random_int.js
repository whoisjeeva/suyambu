import { stringify } from "../util"


function mathRandomInt(a, b) {
    if (a > b) {
      var c = a;
      a = b;
      b = c;
    }
    return Math.floor(Math.random() * (b - a + 1) + a);
}

export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let minValue = await this.executeStatement(stringify(pointer.min))
        let maxValue = await this.executeStatement(stringify(pointer.max))

        return mathRandomInt(minValue, maxValue)
    } catch (e) {
        onError(e)
    }
}
