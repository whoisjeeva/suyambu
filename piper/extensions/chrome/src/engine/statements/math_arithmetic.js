import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let op = stringify(pointer.op)
        let value1 = await this.executeStatement(stringify(pointer.value1))
        let value2 = await this.executeStatement(stringify(pointer.value2))

        switch(op) {
            case "ADD":
                return value1 + value2
            case "MINUS":
                return value1 - value2
            case "MULTIPLY":
                return value1 * value2
            case "DIVIDE":
                return value1 / value2
            case "POWER":
                return Math.pow(value1, value2)
        }
    } catch (e) {
        onError(e)
    }

    return null
}
