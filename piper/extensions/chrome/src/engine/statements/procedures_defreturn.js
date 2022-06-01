import { stringify } from "../util"

export default async function (statement, onError) {
    try {
        let pointer = statement["pointer"]
        let name = stringify(pointer["name"])
        let args = pointer["args"]
        let functionBlock = stringify(pointer["functionBlock"])
        let ret = stringify(pointer["ret"])

        for (let arg of args) {
            this.VARIABLE_STACK[arg] = null
        }

        this.FUNCTION_STACK.push({
            name: name,
            functionBlock: functionBlock,
            ret: ret
        })
    } catch (e) {
        onError(e)
    }
}