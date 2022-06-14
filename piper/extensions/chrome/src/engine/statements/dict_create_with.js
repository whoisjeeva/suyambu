import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let output = {}
        for (let item of pointer) {
            console.log(item)
            let pair = await this.executeStatement(stringify(item))
            let keys = Object.keys(pair)
            if (keys.length !== 1) {
                throw new Error("Dict pair must have exactly one key")
            }
            let key = keys[0]
            output[key] = pair[key]
        }
        return output
    } catch (e) {
        onError(e)
    }
    return {}
}
