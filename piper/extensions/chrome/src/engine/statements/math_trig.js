import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let op = stringify(pointer.op)
        let value = await this.executeStatement(stringify(pointer.value))

        switch(op) {
            case "SIN":
                return Math.sin(value / 180 * Math.PI)
            case "COS":
                return Math.cos(value / 180 * Math.PI)
            case "TAN":
                return Math.tan(value / 180 * Math.PI)
            case "ASIN":
                return Math.asin(value) / Math.PI * 180
            case "ACOS":
                return Math.acos(value) / Math.PI * 180
            case "ATAN":
                return Math.atan(value) / Math.PI * 180
        }
    } catch (e) {
        onError(e)
    }

    return null
}
