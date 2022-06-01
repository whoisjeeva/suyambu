import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let output = []
        for (let item of pointer) {
            let value = await this.executeStatement(stringify(item))
            output.push(value)
        }
        return output
    } catch (e) {
        onError(e)
        return []
    }
}
