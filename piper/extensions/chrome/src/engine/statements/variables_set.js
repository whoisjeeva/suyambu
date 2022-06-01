import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let variableName = stringify(pointer.variable)
        let value = await this.executeStatement(stringify(pointer.value))

        this.VARIABLE_STACK[variableName] = value
    } catch (e) {
        onError(e)
    }
}
