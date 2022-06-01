import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = stringify(statement.pointer)

        if (pointer in this.VARIABLE_STACK) {
            return this.VARIABLE_STACK[pointer]
        }
    } catch (e) {
        onError(e)
    }
    return null
}
