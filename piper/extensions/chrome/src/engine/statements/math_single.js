import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let op = stringify(pointer.op)
        let value = await this.executeStatement(stringify(pointer.value))

        switch(op) {
            case "ROOT":
                return Math.sqrt(value)
            case "ABS":
                return Math.abs(value)
            case "NEG":
                return -value
            case "LN":
                return Math.log(value)
            case "LOG10":
                return Math.log10(value)
            case "EXP":
                return Math.exp(value)
            case "POW10":
                return Math.pow(10, value)
        }
    } catch (e) {
        onError(e)
    }

    return null
}
