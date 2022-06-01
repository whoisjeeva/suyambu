import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let op = stringify(pointer.op)
        let value = await this.executeStatement(pointer.value)

        switch(op) {
            case "ROUND":
                return Math.round(value)
            case "ROUNDUP":
                return Math.ceil(value)
            case "ROUNDDOWN":
                return Math.floor(value)
        }
    } catch (e) {
        onError(e)
    }

    return null
}
