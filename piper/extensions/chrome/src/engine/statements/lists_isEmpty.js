import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = stringify(statement.pointer)
        let list = await this.executeStatement(pointer)
        return list.length === 0
    } catch (e) {
        onError(e)
    }
}
