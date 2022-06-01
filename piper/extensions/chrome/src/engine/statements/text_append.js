import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let variableName = stringify(pointer.variable)
        let text = await this.executeStatement(stringify(pointer.text))
        text = stringify(text)

        if (variableName in this.VARIABLE_STACK) {
            this.VARIABLE_STACK[variableName] += text
        } else {
            this.VARIABLE_STACK[variableName] = text
        }
    } catch (e) {
        onError(e)
    }
}
