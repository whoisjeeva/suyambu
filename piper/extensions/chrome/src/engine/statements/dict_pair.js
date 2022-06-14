import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let key = await this.executeStatement(stringify(pointer.key))
        let value = await this.executeStatement(stringify(pointer.value))
        let output = {}
        output[key] = value
        return output
    } catch (e) {
        onError(e)
    }
    return {}
}
