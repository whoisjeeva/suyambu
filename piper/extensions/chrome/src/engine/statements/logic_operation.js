import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let op = pointer.op
        let value1 = await this.executeStatement(stringify(pointer.value1)) || null
        let value2 = await this.executeStatement(stringify(pointer.value2)) || null

        if (op === "&&") {
            return value1 && value2
        } else if (op === "||") {
            return value1 || value2
        }
    } catch (e) {
        onError(e)
    }
    return false
}
