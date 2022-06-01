import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let op = pointer.op
        let value1 = await this.executeStatement(stringify(pointer.value1)) || null
        let value2 = await this.executeStatement(stringify(pointer.value2)) || null

        switch (op) {
            case "==":
                return value1 === value2
            case "!=":
                return value1 !== value2
            case ">":
                return value1 > value2
            case "<":
                return value1 < value2
            case ">=":
                return value1 >= value2
            case "<=":
                return value1 <= value2
            default:
                return false
        }
    } catch (e) {
        onError(e)
    }
}
