import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let text = await this.executeStatement(stringify(statement.pointer))
        return Number(text)
    } catch (e) {
        onError(e)
    }
}
