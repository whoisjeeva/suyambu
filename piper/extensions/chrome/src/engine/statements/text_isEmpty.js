import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = await this.executeStatement(stringify(statement.pointer))
        let text = stringify(pointer)
        return text.length === 0
    } catch (e) {
        onError(e)
    }
}
