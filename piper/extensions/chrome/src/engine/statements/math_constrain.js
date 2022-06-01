import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let value = await this.executeStatement(stringify(pointer.value))
        let minValue = await this.executeStatement(stringify(pointer.min))
        let maxValue = await this.executeStatement(stringify(pointer.max))

        return Math.min(Math.max(value, minValue), maxValue)
    } catch (e) {
        onError(e)
    }
    return null
}
