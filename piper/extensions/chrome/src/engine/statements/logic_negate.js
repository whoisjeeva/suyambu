import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let value = await this.executeStatement(stringify(statement.pointer))
        if (typeof value === "boolean") {
            return !value
        }
    } catch (e) {
        onError(e)
    }
    return false
}
