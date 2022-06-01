import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let value = await this.executeStatement(stringify(statement.pointer))
        return stringify(value)
    } catch (e) {
        onError(e)
    }
    return null
}
