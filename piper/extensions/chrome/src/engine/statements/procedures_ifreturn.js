import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let condition = await this.executeStatement(stringify(pointer.condition))
        let value = await this.executeStatement(stringify(pointer.value))

        if (condition) {
            this.functionReturnValue = value
            this.isFunctionReturn = true
        }
    } catch (e) {
        onError(e)
    }
}
