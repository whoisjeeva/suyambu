import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let values = []

        for (let value of pointer) {
            let valueResult = await this.executeStatement(stringify(value))
            values.push(valueResult)
        }

        return values.join('')
    } catch (e) {
        onError(e)
    }
}
