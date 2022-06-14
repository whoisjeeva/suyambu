import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let dict = await this.executeStatement(stringify(pointer))
        return Object.keys(dict)
    } catch (e) {
        onError(e)
    }
    return null
}
