import { stringify } from "../util"


export default async function(statement, onError) {
    try {
        let pointer = statement.pointer
        let times = await this.executeStatement(stringify(pointer.times))
        times = parseInt(times)
        let value = await this.executeStatement(stringify(pointer.value))
        let output = []

        for (let i = 0; i < times; i++) {
            output.push(value)
        }

        return output
    } catch (e) {
        onError(e)
    }
    
    return null
}
