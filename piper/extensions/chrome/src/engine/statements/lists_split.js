import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let value = await this.executeStatement(stringify(pointer.value))
        let delim = await this.executeStatement(stringify(pointer.delim))
        let mode = stringify(pointer.mode)

        if (mode === "SPLIT") {
            return value.split(delim)
        } else if (mode === "JOIN") {
            return value.join(delim)
        }
    } catch (e) {
        onError(e)
    }

    return null
}
