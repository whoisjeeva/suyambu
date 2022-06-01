import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let cond = await this.executeStatement(stringify(pointer.cond)) || false
        if (cond) {
            return await this.executeStatement(stringify(pointer.value1))
        }
        return await this.executeStatement(stringify(pointer.value2))
    } catch (e) {
        return onError(e)
    }
}
