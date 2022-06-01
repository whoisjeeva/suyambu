import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let text = await this.executeStatement(stringify(pointer))
        return stringify(text).length
    } catch (e) {
        onError(e)
    }
    return null
}
